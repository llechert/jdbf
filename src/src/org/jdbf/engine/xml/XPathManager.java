
/*
* 03/14/2002 - 23:15:57
* $RCSfile: XPathManager.java,v $ - JDBF Object Relational mapping system
* Copyright (C) 2002 Jdbf Development Team
* 
* http://jdbf.sourceforge.net
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
*/
package org.jdbf.engine.xml;


import java.io.*;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.ArrayList;

import javax.xml.transform.TransformerException;
import javax.xml.parsers.*;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.*;
import org.xml.sax.*;

/* Project's packages and classes */
import org.jdbf.castor.Messages;
import org.jdbf.engine.mapping.*;
import org.jdbf.engine.repository.*;


/**
 *
 * <code>XPathManager</code> is the class that parses the informations on the
 * repository and finally it creates an RepositoryView.
 * To create a RepositoryView, XPathManager uses the XPath that specifies the
 * path the repositoryView on the repository file.
 * XPath is express as /repository/repositoryView/[name='repositoryName'], where
 * repositoryName is the name of repository which needs to return the informations.
 * These informations are: <br><br>
 *
 * <li>BeanDescriptor</li><br>
 * <li>ItemDescriptor</li><br>
 *
 * @see <a href=http://www.w3.org/TR/xpath>XPath Specification</a>
 * @see org.jdbf.engine.repository.RepositoryView RepositoryView
 *
 */
public class XPathManager implements GenericXPath {

    /** RepositoryFactory object */
    private RepositoryFactory repFactory;

    /** Document object */
    private Document document;

    /** name of repository file */
    private String fileName;

    /** XPath */
    private String xPath;

    /** Logger object */
    Logger logger;



    /**
     *
     * Creates the XPathManager object and it parses the repository file
     * specified in fileName
     *
     * @param  fileName name of repository file
     * @throws Exception
     *
     */
    public XPathManager(String fileName) throws Exception{
        logger = Logger.getLogger("org.jdbf.engine.xml");
		this.fileName = fileName;
		logger.log(Level.INFO,Messages.format("XPathManager.parsing",fileName));
		repFactory = new RepositoryFactory();
		document = parse(this.fileName);
		loadRepositoryFactory();
    }

    
    /**
     *
     * Creates the repository given a current element.
     * 
     * @see Repository
     *
     * @param  e - Xml element
     * @return Repository
     * @throws MappingException
     *
     */
    public Repository createRepositoryView(Element e)throws MappingException {

	    
		RepositoryView repositoryView = new RepositoryView() ;
		BeanDescriptor beanDescriptor = getBeanDescriptor(e);
		xPath = "/repository/repositoryView[@name='" + 
		/*
		 * Bug fixing (Gmartone) : 953273 
		 */
		beanDescriptor.getRepositoryViewName() + "']/item";
		/*
		 * End Bug fixing (Gmartone)
		 */
		
		ArrayList itemDescriptors = getItemDescriptor(xPath,beanDescriptor);
	
	    logger.log(Level.INFO,Messages.format("XPathManager.itemDesc",
					beanDescriptor.getRepositoryViewName()));
		
		for(int i = 0 ; i < itemDescriptors.size(); i++){
	
		    ItemDescriptor item = (ItemDescriptor) itemDescriptors.get(i);
		    item.setClassName(beanDescriptor.getClassName());
		    item.setTableName(beanDescriptor.getTableName());
		    item.setRepositoryViewName(beanDescriptor.getRepositoryViewName());
		    logger.log(Level.FINEST,itemDescriptors.toString());
		    itemDescriptors.set(i,item);
	
		}
		
		beanDescriptor.setItemDescriptors(itemDescriptors);
		logger.log(Level.INFO,Messages.format("XPathManager.itemAdd",
					beanDescriptor.getRepositoryViewName()));
		xPath = "/repository/repositoryView[@name='"
		        + beanDescriptor.getRepositoryViewName() + "']/key-generator";
		PrimaryKeyMap pkMap = beanDescriptor.getPrimaryKeyMap();
		
		//if primary key is not composite
		//KeyGeneratorMap must be loaded
		if(!pkMap.isComposite()){
		
			GeneratorMap genMap = createGeneratorMap(xPath);
		
			if(genMap == null){
				logger.throwing(getClass().getName(),"createReposioryView()",
							new MappingException(Messages.format(
                                    "mapping.missingKeyGen",
									beanDescriptor.getRepositoryViewName())));
				throw new MappingException(Messages.format("mapping.missingKeyGen",
									   beanDescriptor.getRepositoryViewName()));
			}
		
			beanDescriptor.setGeneratorMap(genMap);
		}
		repositoryView.setBeanDescriptor(beanDescriptor);
	    logger.log(Level.INFO,Messages.message("XPathManager.beanAdd"));
		return repositoryView;
    }


    /**
     *
     * Creates the GeneratorMap object. GeneratorMap object is needed to load a correct 
     * KeyGenerator class
     * 
     * If type is highlow a HighLowMap object is created, if type is equal to max
     * or identity a default GeneratorMap is created, if type is equal to sequence
     * a SequenceMap is created.
     *
     * @see GeneratorMap
     * @param     xPath  path the repositoryView on the repository
     * @return    GeneratorMap
     * @exception MappingException
     *
     */
    public GeneratorMap createGeneratorMap(String xPath)throws MappingException{

        GeneratorMap generatorMap = null;
		try{

		    Element e =(Element)XPathAPI.selectSingleNode(document, xPath);
		    String type = e.getAttribute("type");
	
		    if(type.equals("highlow")){
	
		        String tableName   = e.getAttribute("table");
			String keyColumn   = e.getAttribute("keyColumn");
			String nextColumn  = e.getAttribute("nextColumn");
			String tableColumn = e.getAttribute("tableColumn");
	
			generatorMap       = new HighLowMap(tableName,keyColumn,nextColumn,tableColumn);
			logger.log(Level.INFO,Messages.format("XPathManager.genCreate",type));
			
		    }
	
		    if(type.equals("max") || type.equals("identity")){
				generatorMap = new GeneratorMap(type);
		        logger.log(Level.INFO,Messages.format("XPathManager.genCreate",type));
		    
	            if(type.equals("sequence")){	
				    String sequenceName = e.getAttribute("sequenceName");
				    generatorMap = new SequenceMap(type,sequenceName);
				    logger.log(Level.INFO,Messages.format("XPathManager.genCreate",type));		    
				}
	
		    }
        }
		catch(TransformerException e){

	    	logger.throwing(getClass().getName(),"createGeneratorMap()",
			    new MappingException(e));
	    	throw new MappingException(e);
		}
		finally{
	    	logger.log(Level.FINEST,generatorMap.toString());
	    	return generatorMap;
		}
    }


	/**
	 *
	 * Return value of attirbute specified in attribute
	 *
	 * @param xPath
	 * @param attribute
	 * @param value
	 * @return Object
	 * @throws MappingException
	 * @deprecated Method deprecated
	 *
	 */
	public Object getValueOfAttribute(String xPath, String attribute, String value) throws MappingException {

	    
	    String name = "";

	    try{

	        this.xPath = xPath + "='" + value + "']" ;
	    	NodeList node = XPathAPI.selectNodeList(document,xPath);

	        Element e = (Element)node.item(0);
			name = e.getAttribute(attribute);

		    if(name == null || name.equals("")){
				logger.throwing(getClass().getName(),"getValueOfAttribute()",
								new MappingException("mapping.repositoryViewNameMissing")
							   );		    
			   throw new MappingException("mapping.repositoryViewNameMissing");
		    }
	     }
	     catch(TransformerException e){
		 	logger.throwing(getClass().getName(),"getValueOfAttribute()",
			    new MappingException(e));
	        throw new MappingException(e);
	     }
	     finally{

			return name;
	     }
	}


	/**
	 *
	 * Return the BeanDescriptor object
	 *
	 * @see BeanDescriptor
	 * @param  e -Element  xml node in the repository
	 * @return BeanDescriptor
	 * @throws MappingException
	 *
	 */
	protected BeanDescriptor getBeanDescriptor(Element e) throws MappingException{

	    BeanDescriptor beanDescriptor = new BeanDescriptor();
		String attribute = e.getAttribute("name");
		logger.log(Level.INFO,Messages.format("XPathManager.beanDesc",attribute));

	    if(attribute == null || attribute.equals("")){			
			logger.throwing(getClass().getName(), "getBeanDescriptor", 
				            new MappingException("mapping.repositoryViewNameMissing"));
			throw new MappingException("mapping.repositoryViewNameMissing");
		}
		else
			beanDescriptor.setRepositoryViewName(attribute);

		attribute = e.getAttribute("table-name");

		if(attribute == null || attribute.equals("")){
		
			logger.throwing(getClass().getName(), "getBeanDescriptor", 
				            new MappingException("mapping.noTableName",xPath));
			throw new MappingException("mapping.noTableName",xPath);
		}
		else
			beanDescriptor.setTableName(attribute);

		attribute = e.getAttribute("object-name");

		if(attribute == null || attribute.equals("")){
		
			logger.throwing(getClass().getName(), "getBeanDescriptor", 
				            new MappingException("mapping.classMapNotFound",xPath));
			throw new MappingException("mapping.classMapNotFound",xPath);
		}
		else
			beanDescriptor.setClassName(attribute);

		attribute = e.getAttribute("database-name");

		if(attribute == null || attribute.equals("")){
		
			logger.throwing(getClass().getName(), "getBeanDescriptor", 
				            new MappingException("mapping.databaseNameMissing"));	
			throw new MappingException("mapping.databaseNameMissing");
		}
		else
			beanDescriptor.setDatabaseName(attribute);

		logger.log(Level.FINEST,beanDescriptor.toString());
		return beanDescriptor;
	}


	/**
	 *
	 * Return the ItemDescriptor object
	 * 
	 * If the xPath is incorrect MappingException is throwed
	 *
	 * @see ItemDescriptor
	 * @param  xPath  path the repositoryView on the repository
	 * @return Collection of ItemDescriptor
	 * @throws MappingException
	 *
	 */
	protected ArrayList getItemDescriptor(String xPath,
										  BeanDescriptor beanDescriptor)
		throws MappingException{

	    ArrayList itemDescriptors = new ArrayList();
		PrimaryKeyMap pk = new PrimaryKeyMap();	
	    try{
				
			NodeList nl = XPathAPI.selectNodeList(document, xPath);

         	for (int i = 0 ; i < nl.getLength(); i++) {

			    ItemDescriptor itemDescriptor = new ItemDescriptor();
			    Element e =(Element)nl.item(i);
	
			    String primaryKey = e.getAttribute("primary-key");
	
			    if(primaryKey == null || primaryKey.equals("")){
			    
			       logger.throwing(getClass().getName(), "getItemDescriptor()", 
					       new MappingException("mapping.primaryKeyMissing"));
			       throw new MappingException("mapping.primaryKeyMissing");
			    }
			    else{
	
				 //if item is unique key
				 if(primaryKey.equalsIgnoreCase("yes"))
			        itemDescriptor.setIsPrimaryKey(true);
				 else
				    itemDescriptor.setIsPrimaryKey(false);
			    }
	
			    String attribute = e.getAttribute("property-name");
	
			    if(attribute == null || attribute.equals("")){
			       logger.throwing(getClass().getName(), "getItemDescriptor()", 
					       new MappingException("mapping.propertyNameMissing"));
			       throw new MappingException("mapping.propertyNameMissing");
	         	}
			    else
				   itemDescriptor.setPropertyName(attribute);
	
			    attribute = e.getAttribute("data-type");
	
			    if(attribute == null || attribute.equals("")){
	    		    	logger.throwing(getClass().getName(), "getItemDescriptor()", 
					       new MappingException("mapping.sqlTypeMissing"));
	    		    	throw new MappingException("mapping.sqlTypeMissing");
			    }
			    else
	    		   itemDescriptor.setDataType(attribute);
	
			    attribute = e.getAttribute("column-name");
	
			    if(attribute == null || attribute.equals("")){
	     			logger.throwing(getClass().getName(), "getItemDescriptor()", 
					       new MappingException("mapping.columnNameMissing"));
	     			throw new MappingException("mapping.columnNameMissing");
			    }
	 		    else
	    			itemDescriptor.setColumnTableName(attribute);
				
				if(itemDescriptor.isPrimaryKey()){				
					pk.addKey(itemDescriptor);
					beanDescriptor.setPrimaryKeyMap(pk);
				}
				else
	    			itemDescriptors.add(itemDescriptor);
         	}
	     }
	     catch(TransformerException e){
			logger.throwing(getClass().getName(), "getItemDescriptor()", 
				       new MappingException(e));
	       throw new MappingException(e);
	     }
	     finally{
		   
	       return itemDescriptors;
	     }
	}


	/**
	 *
	 * Load all repositoryView object in RepositoryFactory.
	 *
	 * @throws MappingException if error occurs
	 *
	 */
	protected void loadRepositoryFactory() throws MappingException {

	    try{
			logger.log(Level.INFO,Messages.message("XPathManager.loadingRep"));
			xPath = "/repository/*";
			NodeList nl = XPathAPI.selectNodeList(document, xPath);

			for(int i = 0 ; i < nl.getLength() ; i++) {

				Element node = (Element) nl.item(i);
				RepositoryView repository = (RepositoryView)
											createRepositoryView(node);
				String repositoryViewName = repository.getBeanDescriptor()
													  .getRepositoryViewName();
				repFactory.addRepository(repositoryViewName,repository);				
				logger.log(Level.INFO,Messages.format("XPathManager.repAdd",
					       repositoryViewName));
			}

	    }
	    catch(TransformerException e){
			
			logger.throwing(getClass().getName(), "loadRepositoryFactory()", 
				       new MappingException(e));
			throw new MappingException(e);
	    }
	}


	/**
	 *
	 * Return  RepositoryFactory
	 *
	 * @return RepositoryFactory
	 *
	 */
	public RepositoryFactory getRepositoryFactory() {

		return repFactory;
	}

	/**
	 *
	 * Parses the repository file specified in fileName
	 *
	 * @param  fileName  name of repository file
	 * @return Document
	 * @throws MappingException
	 * @throws SAXException
	 * @throws ParserConfigurationExcpetion 
	 *
	 */
	protected Document parse(String fileName) throws ParserConfigurationException,SAXException,MappingException {

		DocumentBuilderFactory dfactory = null;
		InputSource in = null;
		Document doc = null;
		try{

			//Set up a DOM tree to query
			in = new InputSource( new FileInputStream(fileName) );
			dfactory = DocumentBuilderFactory.newInstance();
			dfactory.setNamespaceAware(true);
			
			doc = dfactory.newDocumentBuilder().parse(in);
			
		}
		catch(FileNotFoundException e){
			logger.throwing(getClass().getName(), "parse()", 
				       new MappingException("mapping.missingRepositoryConf",fileName));
			throw new MappingException("mapping.missingRepositoryConf",fileName);
			//return null;
		}
		catch(IOException e){
			logger.throwing(getClass().getName(), "parse()", 
				       new MappingException("mapping.missingRepositoryConf",fileName));
    		throw new MappingException("mapping.missingRepositoryConf",fileName);
    		//return null;
		}
		finally{
			return doc;
		}
	}

}
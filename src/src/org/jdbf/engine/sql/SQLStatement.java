/*

 * 06/05/2002 - 23:35:27

 *

 * $RCSfile: SQLStatement.java,v $ - JDBF Object Relational mapping system

 * Copyright (C) 2002 JDBF Development Team

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
package org.jdbf.engine.sql;



import java.util.List;
import java.util.ArrayList;

import java.util.logging.Logger;

import java.util.logging.Level;



import org.jdbf.castor.Types;

import org.jdbf.castor.TypeConvertor;

import org.jdbf.castor.Messages;

import org.jdbf.engine.basic.ObjectMapped;


import org.jdbf.engine.criteria.Criteria;

import org.jdbf.engine.mapping.ItemDescriptor;

import org.jdbf.engine.mapping.MappingException;
import org.jdbf.engine.mapping.PrimaryKeyMap;

import org.jdbf.engine.reflection.*;

import org.jdbf.engine.repository.RepositoryView;



/**
 * <code>SQLStatement</code> is an abstract class that represents the sql
 * statement.
 * SQLStatement handles the creation of sql statement with the informations
 * specified in a RepositoryView object. 
 */
public abstract class SQLStatement{

    /** class name */
    protected String className = this.getClass().getName();
    /** sql statement */
    protected String statement;
    /** Criteria object */
    protected Criteria criteria;
    /** identifies WHERE clause */
    protected final static String WHERE = SqlInterface.WHERE;
    /** identifies ( */
    protected static final String OPEN_BRACE = "( ";

        

    /** identifies ) */

    protected static final String CLOSE_BRACE = ") ";



    /** Logger object */

    protected Logger logger;

	

	

	/**

	 * Creates a object that represents a SQLStatement

	 *

	 */

    public SQLStatement(){

        criteria = null;

        statement = "";

		logger = Logger.getLogger(className);

    }

	

	

    /**

     * Creates the object that represents a SQLStatements

     *

     * SQLStatement is created with repositoryView information, 

     * the name of properties,with criteria and with a specific SQLInterface

     * 

     * @param repository

     * @param propertiesNames

     * @param criteria

     * @param sqlInterface

     *

     */

    public SQLStatement(RepositoryView repository,String[] propertiesNames,
    				    Criteria criteria,SqlInterface sqlInterface){

		logger = Logger.getLogger(className);

        this.criteria = criteria;

	

		if(criteria != null){

  	    	buildStatementForCriteria(repository,propertiesNames,sqlInterface);

		}
		else

 	    	buildStatement(repository,propertiesNames,sqlInterface);

    	}

	

	
	/**
	 * Creates a statement
	 * @param repository
	 * @param propertiesNames
	 * @param sqlInterface
	 */
    public abstract void buildStatement(RepositoryView repository,

                                        String[] propertiesNames,

					SqlInterface sqlInterface);

    
	/**
	 * Creates a statement with particular condition specified in Criteria object
	 * @param repository
	 * @param propertiesNames
	 * @param sqlInterface
	 */
    public abstract void buildStatementForCriteria(RepositoryView repository,

                                                   String[] propertiesNames,

						   SqlInterface sqlInterface);

	

    

    /**

     * Convertes the class of propertyValue as Class specified in toClass

     *

     * @param propertyName

     * @param propertyValue

     * @param object

     * @param item

     * @param fromClass

     * @param toClass

     * @return Object

     * @throws MappingException

     *

     */

    protected Object convertValue(String propertyName,Object propertyValue,

								  ObjectMapped object,ItemDescriptor item,

								  Class fromClass,Class toClass)

	throws MappingException{

		

	Object obj = null;

			    

	if(ReflectionManager.isPrimitive(object,propertyName)){

 	    obj = ReflectionManager.createNotPrimitiveObject(toClass,"0");

	    toClass = obj.getClass();

	}

		        

	if(fromClass != toClass){

			

	    String[] params = { propertyName,toClass.getName(),fromClass.getName()};

	    logger.log(Level.FINEST,Messages.format("SQLStatement.convert",params));

	    //System.out.println("propertyName " + propertyName);

	    //System.out.println("toClass " + toClass.getName() + " from Class " + fromClass.getName());    

		

	    if(fromClass.getName().equals("java.sql.Date")){

	        try{

		    toClass = Class.forName("java.util.Date");

		    

		    //first convert from java.sql.Date to java.util.Date

		    TypeConvertor convertor = Types.getConvertor(fromClass,toClass);						

		    propertyValue = convertor.convert(propertyValue,null);						

		    fromClass = toClass;

		    toClass = ReflectionManager.getPropertyType(object,item.getPropertyName());

		    

		    //then convert from java.util.Date in specific class

		    convertor = Types.getConvertor(fromClass,toClass);

		    String formatPattern = item.getFormatPattern();

		    propertyValue = convertor.convert(propertyValue,formatPattern);				

		}

		catch(ClassNotFoundException e){

		   logger.throwing(className,"convertValue()",

				   new MappingException(e));

		   //throw new MappingException(e);

		}

	    }

	    else{

	        TypeConvertor convertor = Types.getConvertor(fromClass,toClass);						

		propertyValue = convertor.convert(propertyValue,null);			

	    }

	}

	return propertyValue;

    }





    /** 

     * Return sqlstatement as string

     *

     * @return String     

     */

    public String getStatement(){

        return statement;	

    }



    

    /**

     * Return a String composed from fields separated by ","

     *

     * @param items - List of items to parse 

     * @return String

     *

     */

    protected String parseFields(List items){

	logger.log(Level.FINEST,Messages.message("SQLStatement.fields"));

        StringBuffer fields = new StringBuffer();

	for(int i = 0; i < items.size(); i++){

    	    ItemDescriptor item = (ItemDescriptor)items.get(i);

	    fields.append(item.getColumnTableName());

	    if(i < items.size() - 1)

	        fields.append(",");

        }

        return fields.toString();

    }

	

	

    /**

     * Return a String composed from fields separated by ","

     *

     * Creates the string where values must be inserted. This string is composed 

     * as ?,?,?

     *

     * @param items - List of value of items to parse

     * @return String

     *

     */

    protected String parseValues(List items){

	 

	logger.log(Level.FINEST,Messages.message("SQLStatement.values"));

        StringBuffer buff = new StringBuffer();	     

	int size = items.size();

	

	//build list of values

	for(int i = 0 ; i < size; i++){

 	    buff.append("?");

	    if(i < size - 1) 

	        buff.append(",");

	}

	return buff.toString();

    }

	

	
	/**
	 * Return a Stirng representation of this object
	 */
    public String toString(){

	StringBuffer buff = new StringBuffer();

	buff.append(className).append("[ ")

	    .append("statement= ").append(statement)

	    .append("]");

	return buff.toString();

    }


	/**
	 * Parse parameters specified in params
	 * @param params
	 * @return
	 */
    protected String parseParameters(Object[] params){

		String paramsStr = "";

		for (int i = 1; i < params.length ; i++) {

			paramsStr +=  params[i];

			if( i < (params.length - 1))

				paramsStr +=  ",";

		}

		return paramsStr;

    }
    
    /**
     * Return a ArrayList that contains all ItemDescriptors
     * @param pks
     * @param itemDesc
     * @return ArrayList
     */
    protected ArrayList unionItemDescriptors(ArrayList pks,ArrayList itemDesc){

		ArrayList items = new ArrayList(pks);	       	       
		items.addAll(itemDesc);
		return items;
    }
    
	/**
	 * Create WHERE clause on primaryKey and returns an Criteria 
	 * object that represents this clause
	 * 
	 * @param repositoryName
	 * @param view
	 * @return Criteria 
	 */
    protected Criteria createClauseOnPk(String repositoryName,RepositoryView view){
		
		PrimaryKeyMap pk = view.getBeanDescriptor().getPrimaryKeyMap();		  
		ArrayList keys = (ArrayList)pk.getPrimaryKey();  
		Criteria criteria = new Criteria(repositoryName);
  
		for(int i = 0; i < keys.size(); i++){
	  
			Criteria internal = null;
			if(i > 0)
				internal = new Criteria(repositoryName);	      
      
			ItemDescriptor item = (ItemDescriptor)keys.get(i);
      
			String columnName = item.getPropertyName();
	  
			if(i == 0)
				criteria.addSelectEqualTo(columnName,new Character('?'));
			else			     
				internal.addSelectEqualTo(columnName,new Character('?'));
	  
			if(i > 0)
				criteria.addAndCriteria(internal);			  			  		 
		}
		
		return criteria;
		  
    }

}
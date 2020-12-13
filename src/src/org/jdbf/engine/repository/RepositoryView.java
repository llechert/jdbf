/*
 * 03/12/2002 - 14:08:11
 *
 * $RCSfile: RepositoryView.java,v $ - JDBF Object Relational mapping system
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
package org.jdbf.engine.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.jdbf.castor.Messages;

import org.jdbf.engine.basic.ObjectMapped;
import org.jdbf.engine.mapping.*;
import org.jdbf.engine.reflection.ReflectionManager;

/**
 * <code>RepositoryView</code> is the class that represents
 * the repositoryView specified in repository.
 * His purpose is that to map the informations of Java object
 * to RDBMS.
 * In this way RepositoryView can fills and returns the Java object after 
 * the queries are executed.
 * 
 */
public class RepositoryView implements Repository{

	/** Class name */
	private static final String CLASS_NAME = "org.jdbf.engine.repository.RepositoryView";
	
	/** ClassDescriptor */
	private BeanDescriptor beanDescriptor;
	
	/** Logger object */
    private Logger logger;
	
		
	/**
	 * Create the object
	 */
	public RepositoryView(){
	    /** Logger object */
        logger = Logger.getLogger("org.jdbf.engine.repository");
	    beanDescriptor = null;
	}


	/**
     * Create a ObjectMapped by a CLASS_NAME and a hashmap that contains
	 * a list with the names of the properties and relative values.
     * If props is null the object created is null.
     *
     * @param name  CLASS_NAME
     * @param props list property/value
     * @return      ObjectMapped
     * @throws MappingException
     */
     public ObjectMapped createObject(String name,HashMap props)
     	throws MappingException{
		
		return ReflectionManager.createBean(name,props);        
     }
    
    
    /**
	 * Return BeanDescriptor object.
	 * @return BeanDescriptor
	 */
	public BeanDescriptor getBeanDescriptor(){
		return beanDescriptor;
	}
	
				
	/**
	 * Return the current value of a property specified in propertyName.
	 * If the property isn't specified in repository, 
	 * MappingException throws.
	 * @param object ObjectMapped object,propertyName name of property
	 * @return Object current value of property
	 * @throws MappingExceptionExcpetion
	 */
	public Object getPropertyValue(ObjectMapped object, String propertyName) 
		throws MappingException{
		
		logger.log(Level.INFO,Messages.format("RepositoryView.getPropertyValue", propertyName));
		
		//return the array that ocntains the itemDescriptor
		ArrayList items = beanDescriptor.getItemDescriptors();
		
		ItemDescriptor item = beanDescriptor.getItemDescriptorFromName(propertyName);
		if(item == null){
			throw new MappingException("mapping.invalidPropertyName", propertyName);
		}
		Object propertyValue = ReflectionManager.getValueByName(object,propertyName);
		return propertyValue;
	}
	
				
	/**
	 * Set the BeanDescriptor object
	 * @param beanDescriptor
	 */
	public void setBeanDescriptor(BeanDescriptor beanDescriptor){
		this.beanDescriptor = beanDescriptor;
	}
	
	
	/**
	 * Set the value of a property specified in propertyName.
	 * If the property isn't specified in repository, 
	 * MappingException throws.      
	 * @param object ObjectMapped object 
	 * @param propertyName name of property
	 * @param propertyValue value to set	 
	 * @throws MappingExcpetion
	 */
	public ObjectMapped setPropertyValue(ObjectMapped object,String propertyName, Object propertyValue)
		throws MappingException{
		
		logger.log(Level.INFO,Messages.format("RepositoryView.setPropertyValue", propertyName));
		
		//return the array that ocntains the itemDescriptor
		ArrayList items = beanDescriptor.getItemDescriptors();
		boolean found = false;
		
		ItemDescriptor item = beanDescriptor.getItemDescriptorFromName(propertyName);
		if(item == null){
			throw new MappingException("mapping.invalidPropertyName", propertyName);
		}
		object = ReflectionManager.setValueByName(object,propertyName,propertyValue);
		
		return object;
	}
        

	/**
	 * Return a String representation of this object
	 * @return String
	 */
	public String toString(){
	    
	    StringBuffer buff = new StringBuffer();
	    buff.append(CLASS_NAME)
			.append("[ ").append("\n")
			.append(beanDescriptor).append("\n")
			.append("]");
	    
	    return buff.toString();	    
	}
	
}

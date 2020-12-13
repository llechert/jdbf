/*
* May 27, 2004 - 2:48:54 PM
*
* $RCSfile: PrimaryKeyMap.java,v $ - JDBF Object Relational mapping system
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
package org.jdbf.engine.mapping;

import java.util.List;
import java.util.ArrayList;

/**
 * <code>PrimaryKeyMap</code> represents all primary keys that have been defined
 * in repository for particular repositoryView.
 * Primary key are all ItemDescriptor object that have setted true in isPrimaryKey
 * flag.
 * If a table contains one primary key, the method PrimaryKeyMap.isComposite()
 * returns false, returns true otherwise. 
 * 
 */
public class PrimaryKeyMap {

    /**
     * Collection of ItemDescriptor
     */
    private List primaryKeys;
    
    private String className;
    
    
    
    /**
     * Creates an empty object
     */
    public PrimaryKeyMap() {
    	className = this.getClass().getName();
    	primaryKeys = new ArrayList();
    }
    
    /**
     * Creates a object with a colection of primary keys
     * @param keys
     */
    public PrimaryKeyMap(List keys){
		className = this.getClass().getName();
    	primaryKeys = keys;
    }
    
    /**
     * Add a ItemDescriptor object as primary key
     * @param item
     */
    public void addKey(ItemDescriptor item){
    	primaryKeys.add(item);
    }
    
    /**
     * Return primary key, specified in keyName param.
     * Key name must contains the name of property obtained invoking
     * @see ItemDescriptor#getPropertyName()
     * @param keyName
     * @return ItemDescriptor
     */
    public ItemDescriptor getKey(String keyName){
    	ItemDescriptor item = null;
    	for(int i = 0; i < primaryKeys.size(); i++){
    		item = (ItemDescriptor)primaryKeys.get(i);
    		if(item.getPropertyName().equals(keyName))
    			break;
    	}
    	return item;
    }
    
    
    /**
     * Return all primary keys as collection 
     * @return List
     */
    public List getPrimaryKey(){
    	return primaryKeys;
    }
    
    
    /**
     * Return true if it's a composite PrimaryKey,false otherwise 
     * @return boolean
     */
    public boolean isComposite(){
    	if(primaryKeys.size() > 1)
    		return true;
    	else
    		return false;
    }
    
	/**
	 * Return the object as String
	 * @return String
	 */
	public String toString(){
		StringBuffer buff = new StringBuffer();
		buff.append(className).append("[").append("\n")    	    
			.append("primary keys ").append(primaryKeys).append("\n")	
			.append("]");	
		return buff.toString();
	 }
}
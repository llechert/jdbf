/*
* May 27, 2004 - 4:01:07 PM
*
* $RCSfile: PrimaryKey.java,v $ - JDBF Object Relational mapping system
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

package org.jdbf.engine.basic;

import java.util.HashMap;
/**
 * <code>PrimaryKey</code> is that class that manages PrimaryKey.<br>
 * To add a primaryKey you should invoke setValueKey specifing the 
 * property name and its value.<br>
 * getValueKey is not important method becuase it is needed for jdbf only,
 * however you can invoke it.
 * 
 */
public class PrimaryKey {

	/**
	 * Primary key
	 */
	private HashMap keys;
	
	/**
	 * Class name
	 */
	private String className;
	
	
    /**
     * Creates object
     */
    public PrimaryKey() {
    	className = this.getClass().getName();
    	keys = new HashMap();
    }
    
    /**
     * Return value of primary key specified in key param.
     *
     * Key shoud be the property name of ItemDescriptor
     * @param key
     * @return Object
     * @see org.jdbf.engine.mapping.ItemDescriptor#getPropertyName()
     */
    public Object getValueKey(String key){
    	return keys.get(key);
    }
    
    
    /**
     * Set a primary key specifing its name and its value.
     * 
     * Key name is property name of ItemDescriptor
     * @param key
     * @param value
     * @see org.jdbf.engine.mapping.ItemDescriptor#getPropertyName()
     */
    public void setValueKey(String key,Object value){
    	keys.put(key,value);
    }

}
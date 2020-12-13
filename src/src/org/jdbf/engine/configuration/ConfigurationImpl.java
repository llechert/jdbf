/*
* 04/04/2002 - 23:12:27
*
* $RCSfile: ConfigurationImpl.java,v $ - JDBF Object Relational mapping system
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

package org.jdbf.engine.configuration;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * <code>ConfigurationImpl</code> is the class that represents 
 * the file configuration.<br>
 * 
 */
public class ConfigurationImpl extends AbstractConfiguration{
    
	private Map children;
    
    private static Iterator EMPTY_ITERATOR = new Iterator() {
	    
		public boolean hasNext() {return false;}

	    public Object next() throws java.util.NoSuchElementException {
			throw new java.util.NoSuchElementException();
	    }

	    public void remove ()  {}
	};

    
	/**
	 * Creates object given a name of tag
	 * @param name
	 */
	public ConfigurationImpl(String name){
		
		super(name);
    }
    
    
    /**
     * Append a new value to current children
     * @param value
     */
	public void appendValueData(String value){
		
		if (this.value == null)
			this.value = value;
		else
			this.value += value;
    }
    
    
	/**
     * Return an iterator for configuration subnodes for the given name 
     *
     * @param name a of the list of child nodes to return
     * @return an <code>Iterator</code> for child nodes for the given name
     */
    public Iterator getConfigurations(String name){
		if (children != null) {
			List values = (List)children.get(name);	    
			if (values != null) 
				return values.iterator();
		}
		return EMPTY_ITERATOR;
    }


    /**
     * Return the first subnode for the given name
     *
     * @param name of the <code>Configuration</code> to return.
     * @return a <code>Configuration</code> value for the given name
     */
    public Configuration getConfiguration(String name){
		if (children != null) {
			List values = (List)children.get(name);
			if (values != null) 
				return (Configuration)values.get(0);
		}
		return null;
    }

    
	/**
     * Return the first subnode for the given name
     *
     * @param defaultValue value to use for new 
     * <code>Configuration</code> if not present
     * @param name of the <code>Configuration</code> to return.
     * @return a <code>Configuration</code> value for the given name
     */
    public Configuration getConfiguration(String name, String defaultValue){
	
		Configuration conf = getConfiguration(name);
		if (conf == null) {
			conf = new ConfigurationImpl(name);
				   ((ConfigurationImpl)conf).appendValueData(defaultValue);
			addConfiguration(conf);
		}
		
		return conf;
    }

    
	/** 
	 * Add a configuration subnode 
	 * 
	 * @param config
	 *
	 */
    public void addConfiguration(Configuration config){
		
		if (children == null) {
			children = java.util.Collections.synchronizedMap(new HashMap());
		}
	
		List values = (List)children.get(config.getName());
		if (values == null) {
			values = new java.util.ArrayList();
			values.add(config);
			children.put(config.getName(), values);
		}
		else {
			values.add(config);
		}
    }
}


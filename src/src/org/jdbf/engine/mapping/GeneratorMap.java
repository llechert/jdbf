/*
 * 11/12/2002 - 14:02:45
 *
 * $RCSfile: GeneratorMap.java,v $ - JdbF Object Relational mapping system
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


/**
 * <code>GeneratorMap </code> represents a mapping of 
 * key generator
 *
 */
public class GeneratorMap{    
    
    /** name of class */
    protected String className;

        
    /** type of keyGenerator */
    protected String type;
                      
    /** 
     * Creates the GeneratorMap object.	 	 	
     */
    public GeneratorMap(){
        className = getClass().getName();
    }

    /** 
     * Creates the GeneratorMap object.
     *
     * @param type type of generator.
     */
    public GeneratorMap(String type){
		className = getClass().getName();
		this.type = type;
    }
	
  
    /**
     * Return type of generator
     * @return String type of generator
     */
    public String getType(){
        return type;
    }
    
                  
    /**
     * Set type of generator
     * @param type of generator
     */
    public void setType(String type){
        this.type = type;
    }
    
          
    /**
     * Return the object as String
     * @return String
     */
    public String toString(){
        StringBuffer buff = new StringBuffer();
    	buff.append(className).append("[").append("\n")    	    
	    	.append("type generator ").append(type).append("\n");		
		return buff.toString();
     }
}

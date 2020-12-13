/*
* 04/04/2002 - 23:12:27
*
* $RCSfile: AbstractConfiguration.java,v $ - JDBF Object Relational mapping system
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
/**
 * <code>AbstractConfiguration</code> is a abstract configuration that represents 
 * a node in a XML file.<br>
 * 
 */ 
public abstract class AbstractConfiguration implements Configuration {
    /** value of this configuration */
    protected String value;
    
    /** name of this configuration */
    protected String name;

    
    /**
     * Creates the AbstractConfiguration object.
     *
     * @param name
     */
    protected AbstractConfiguration(String name){
        this.name = name;
    }

  
    /**
     * Return the value of this configuraion as a <code>float</code>.
     *
     * @return a <code>float</code> for this configuration value
     */
    public float getFloatValue() throws NumberFormatException {
        try {
            return(Float.parseFloat(value.trim()));
        }
        catch (NumberFormatException except) {
            throw new NumberFormatException(except.getMessage());
        }
    }
    
    
    /**
     * Return the value of this configuraion as a <code>int</code>
     *
     * @return a <code>int</code> for this configuration value
     * @throws NumberFormatException
     */
    public int getIntValue() throws NumberFormatException{
        try {
            return(Integer.parseInt(value.trim()));
        }
        catch (NumberFormatException except) {
            throw new NumberFormatException(except.getMessage());
        }
    }
    
    /**
     * Return the name of this configuration
     *
     * @return name of this configuration
     */
    public String getName(){
        return name;
    }

    /**
     * Return the value of this configuration
     *
     * @return value of this configuration
     */
    public String getValue(){
        return value;
    }

    /**
     * Return the value of this configuration. Return the defaultValue is value
     * is null
     *
     * @param defaultValue default value of node
     * @return value of node,if value is null return the defaultValue specified
     * in defaultValue
     */
    public String getValue(String defaultValue){
        if (value == null) 
            return defaultValue;
        return value;
    }

    
    /**
     * Return a iterator of configuration specified in name
     *
     * @return iterator of configuration specified in name
     */
    public abstract java.util.Iterator getConfigurations(String name);

    
    /**
     * Return a  configuration specified in name
     *
     * @return Configuration configuration specified in name
     */
    public abstract Configuration getConfiguration(String name);    
}


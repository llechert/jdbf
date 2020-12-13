/*
* 04/04/2002 - 23:12:27
*
* $RCSfile: Configuration.java,v $ - JDBF Object Relational mapping system
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

import java.util.Iterator;\
/**
 * <code>Configuration</code> is a configuration represented in 
 * the XML format. <br> 
 * 
 */
 public interface Configuration{
    
	/** Return the name of this configuration */
    String getName();

    /** Return child node of this configuration with the given name */
    Configuration getConfiguration(String name);

    /** Return child node of this configuration with the given name */
    Configuration getConfiguration(String name, String defaultValue);

    /** Return child nodes of this configuration with the given name */
    Iterator getConfigurations(String name);

    /** Return the value of this configuration as int */
    int getIntValue() throws NumberFormatException;

    /** Return the value of this configuration as float */
    float getFloatValue() throws NumberFormatException;

    /** Return the value of this configuration as String */
    String getValue();

    /** Return the value of this configuration as String */
    String getValue(String defaultValue);
}



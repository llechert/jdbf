/*
 * 04/04/2002 - 23:12:27
 *
 * $RCSfile: ConfigurationException.java,v $ - JDBF Object Relational mapping system
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

import org.jdbf.engine.JDBFException;
import org.jdbf.castor.Messages;

/**
 * <code>ConfigurationException</code> handles exception that occur
 * during parsing configuration process.<br>
 * 
 */
public class ConfigurationException extends JDBFException
{
    /**
     * Nested Exception
     */
    private Exception except;

	/**
	 * Creates exception with message
	 * 
	 * @param message
	 */
    public ConfigurationException (String message){
        super(Messages.message(message));
    }

	/**
	 * Creates exception with message and Integer parameter
	 * 
	 * @param message
	 * @param type
	 */
    public ConfigurationException (String message, Integer type){
        super(Messages.format(message, type));
    }

	/**
	 * Creates exception with message and int param
	 * 
	 * @param message
	 * @param type
	 */
	public ConfigurationException (String message, int type){
        this(message, new Integer(type));
    }

	/**
	 * Creates exception with message and String parameter
	 * 
	 * @param message
	 * @param type
	 */
	public ConfigurationException (String message, String type){
        super(Messages.format(message, type));
    }

	/**
	 * Creates exception with message and two String parameters
	 * 
	 * Message is parsed with two parameter to create error message
	 * 
	 * @param message
	 * @param type1
	 * @param type2
	 */
	public ConfigurationException (String message, String type1, String type2){
        super(Messages.format(message, type1, type2));
    }

	/**
	 * Creates exception with a nested exception
	 * 
	 * @param except
	 */
	public ConfigurationException(Exception except){
        super(Messages.format("config.nested", except.toString()));
        this.except = except;
    }

    /**
     * Return nested exception 
     * 
     * @return Exception
     * 
     */
	public Exception getException() {
		return except;
	}	
}


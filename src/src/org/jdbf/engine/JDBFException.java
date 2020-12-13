/*
* 04/04/2002 - 23:12:27
*
* $RCSfile: JDBFException.java,v $ - JDBF Object Relational mapping system
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
package org.jdbf.engine;

import org.jdbf.castor.Messages;

/**
 * 
 * <code>JDBFException</code> is the generic exception. All excpetions
 * extends from it.
 * 
 */
public abstract class JDBFException extends Exception{
    
    /**
     * Creates exception with a message
     * @param message
     */
    public JDBFException (String message){
        super(Messages.message(message));
    }

	/**
	 * Creates exception with a particular message.
	 * 
	 * This message is composed from a string 
	 * (that is internazionalized) and by a parameter.
	 *  
	 * @param message
	 * @param type
	 */
    public JDBFException (String message, Object type){
        super(Messages.format(message, type));
    }


	/**
	 * Creates exception with a particular message.
	 * 
	 * This message is composed from static sring 
	 * (internazionalized) and by custom part that is
	 * represents by args. 
	 * @param message
	 * @param args
	 */
    public JDBFException (String message, Object [] args){
        super(Messages.format(message, args));
    }
}
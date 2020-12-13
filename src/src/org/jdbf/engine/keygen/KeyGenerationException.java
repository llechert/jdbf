/*
* 20/04/2004 - 01:24:27
*
* $RCSfile: KeyGenerationException.java,v $ - JDBF Object Relational mapping system
* Copyright (C) 2004 JDBF Development Team
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
package org.jdbf.engine.keygen;


/**
 * <code>KeyGenerationException</code> handles error if the 
 * key operation operation fails.
 * 
 */
public class KeyGenerationException extends org.jdbf.engine.JDBFException{
	
	/**
	 * Embedded exception
	 */
	private Exception except;

	/**
	 * Creates exception with a specific message
	 * @param message
	 */
	public KeyGenerationException (String message){
		super(message);
	}

	/**
	 * Creates exception with a message parsed with param type
	 * @param message
	 * @param type
	 */
	public KeyGenerationException (String message, int type){
		super(message, new Integer(type));
	}

	/**
	 * Creates exception with a message parsed with param type
	 * @param message
	 * @param type
	 */
	public KeyGenerationException (String message, Object type){
		super(message, type);
	}

	/**
	 * Creates exception with a message parsed with param type1
	 * and param type2
	 * @param message
	 * @param type1
	 * @param type2
	 */
	public KeyGenerationException (String message, String type1, String type2){
		super(message, new Object [] {type1, type2});
	}

	/**
	 * Creates exception from an Exception
	 * @param except
	 * 
	 */
	public KeyGenerationException(Exception except){
		this("mapping.nested", except.toString());
		this.except = except;
	}

	/**
	 * Return embedded exception
	 * @return Exception
	 */
	public Exception getException() {
		return except;
	}
}

/*
* 04/04/2002 - 23:12:27
*
* $RCSfile: MappingException.java,v $ - JdbF Object Relational mapping system
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

import org.xml.sax.SAXParseException;


/**
 * <code>MappingException</code> handles message errors 
 * if information mapped in repository are incorrect.
 * 
 */
public class MappingException extends org.jdbf.engine.JDBFException
{
    /**
     * Embedded exception
     */
    private Exception except;

	/**
	 * Creates exception with message
	 * @param message
	 */
    public MappingException (String message)
    {
        super(message);
    }

	/**
	 * Creates excpetion with message parsed with type param
	 * @param message
	 * @param type
	 */
    public MappingException (String message, int type)
    {
        super(message, new Integer(type));
    }

	/**
	 * Creates exception with message parsed with type param
	 * @param message
	 * @param type
	 */
    public MappingException (String message, Object type)
    {
        super(message, type);
    }

	/**
	 * Creates exception with message parsed with type1 param 
	 * and type2 param
	 * @param message
	 * @param type1
	 * @param type2
	 */
    public MappingException (String message, String type1, String type2)
    {
        super(message, new Object [] {type1, type2});
    }

	/**
	 * Creates exception by SaxPArseException
	 * @param sx
	 */
    public MappingException(SAXParseException sx)
    {
        super("mapping.SAXParseError", new Object[] {
            sx.getSystemId(),
            sx.getPublicId(),
            new Integer(sx.getLineNumber()),
            new Integer(sx.getColumnNumber()),
            sx.getMessage()
        });
        this.except = sx;
    }

	/**
	 * Creates exception by Exception
	 * @param except
	 */
    public MappingException(Exception except)
    {
        this("mapping.nested", except.toString());
        this.except = except;
    }

	/**
	 * Return embedded exception
	 * @return Exception
	 */
    public Exception getException() {return except;}
}


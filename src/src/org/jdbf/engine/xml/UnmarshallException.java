/*
* 22/01/2004 - 12:43:29
*
* $RCSfile: UnmarshallException.java,v $ - JDBF Object Relational mapping system
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
package org.jdbf.engine.xml;

import org.xml.sax.SAXParseException;


/**
 * <code>UnmarshallException</code> is thrown if the unmarshalling process fails.
 *
 */
public class UnmarshallException extends org.jdbf.engine.JDBFException{
    
    /** Exception object */
    private Exception except;

    /**
     * Creates UnmarshallException with a message
     *
     * @param message
     *
     */
    public UnmarshallException (String message){
        super(message);
    }


    /**
     * Creates UnmarshallException matching a message with
     * an integer argument
     *
     * @param message
     * @param type
     *
     */
    public UnmarshallException (String message, int type){
        super(message, new Integer(type));
    }


    /**
     * Creates UnmarshallException matching a message with
     * an object argument
     *
     * @param message
     * @param type
     *
     */
    public UnmarshallException (String message, Object type){
        super(message, type);
    }


    /**
     * Creates UnmarshallException matching a message with
     * two string arguments
     *
     * @param message
     * @param type1
     * @param type2
     *
     */
    public UnmarshallException (String message, String type1, String type2){
        super(message, new Object [] {type1, type2});
    }


    /**
     * Creates UnmarshallException from a SAXParseException
     *
     * @param sx SAXParseException
     *
     */
    public UnmarshallException(SAXParseException sx){
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
     * Creates UnmarshallException from a Exception
     *
     * @param except Exception
     *
     */
    public UnmarshallException(Exception except){
        this("mapping.nested", except.toString());
        this.except = except;
    }


    /**
     * Return exception
     *
     * @return Exception
     *
     */
    public Exception getException() {
        return except;
    }
}
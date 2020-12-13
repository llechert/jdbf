/*
* 04/04/2002 - 23:12:27
*
* $RCSfile: TransactionException.java,v $ - JdbF Object Relational mapping system
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
package org.jdbf.engine.transaction;

import org.jdbf.engine.JDBFException;

/**
 *
 * <code>TransactionException</code> informs that the user transaction 
 * has been explicitly aborted by the database due to some failure and 
 * the reason for that failure.
 *
 */
public class TransactionException extends JDBFException{

    /**
     * Creates TransactionException matching a message with
     * an exception
     *
     * @param message
     * @param except
     *
     */
    public TransactionException(String message, Exception except){
        super(message, except);
    }

    /**
     * Creates TransactionException with a message
     *
     * @param message
     *
     */
    public TransactionException(String message){
        super(message);
    }
}
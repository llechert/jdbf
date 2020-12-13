/*
* 14/05/2004 - 12:35:27
*
* $RCSfile: QueryException.java,v $ - JDBF Object Relational mapping system
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
package org.jdbf.engine.sql;

import org.jdbf.engine.JDBFException;


/**
 * <code>QueryException</code> handles all exceptions that occur during the 
 * execution of sql statement.
 *
 */

public class QueryException extends JDBFException {

    /**
     *
     * Creates a QueryException and appends message with sql
     * 
     * @param sql
     * @param message
     * 
     */
    public QueryException(String sql,String message) {
       
       super(sql + ":" + message);
       
    }
}
/*
 * 24/01/2003 - 11:43:45
 *
 * $RCSfile: MySQLInterface.java,v $ - JDBF Object Relational mapping system
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

/**
 * <code>MySQLInterface</code>overrides SqlInterface's methods for constructing sql statements
 * where Interbase's implementaion differs from ANSI or is not specified by ANSI.
 *
 */
public class MySQLInterface extends SqlInterface{

    /**
     * Creates an empty object
     *
     */
    public MySQLInterface(){}


	/**
	 * Forms an sql statement the drop the table given tableName
	 *
	 * @param tableName name fo table to drop
	 * @return String sql statement
	 */
    public String getDropTableStatement(String tableName){
        return new StringBuffer(DROP).
            append(' ').
            append(TABLE).
            append(' ').
            append("IF EXISTS ").
            append(tableName).toString();
    }


	/**
     * Forms an sql insert id statement
	 *
	 * @return String Sql statement for selecting a insert id key.
	 * @throws MappingException
	 * @see SqlInterface#getSelectInsertIdStatement
	 *
     */
	public String getSelectInsertIdStatement() {
		return "SELECT LAST_INSERT_ID()";
    }
}


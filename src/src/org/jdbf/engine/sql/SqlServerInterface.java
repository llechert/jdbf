/*
 * 20/01/2003 - 10:07:11
 *
 * $RCSfile: SqlServerInterface.java,v $ - JDBF Object Relational mapping system
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
 * <code>SqlServerInterface</code> overrides SqlInterface's methods for constructing sql statements
 * where MS SQL Server implementation differs from ANSI or is not specified by ANSI.
 *
 */
public class SqlServerInterface extends SqlInterface {

    /**
     * Creates an empty object
     *
     */
    public SqlServerInterface() {}


	/**
	 * Return the cluase of current timeStamp
	 *
	 * @return current timeStamp
	 * @throws MappingExpcetion if feature not supported
	 */
	public String getClauseStringCurrentTimeStamp() {
		return "GETDATE()";
    }

	
	/**
	 * Return the current timeStamp statement
	 *
	 * @return String current timestamp statement
	 */
    public String getCurrentTimeStampStatement() {
		return "SELECT GETDATE()";
    }


	/**
     * Forms an sql insert id statement
	 *
	 * NEWID() returns a 16-byte binary value (GUID)
     * for example: 6F9619FF-8B86-D011-D42D-00C04FC964FF
	 * 
	 * @return String Sql statement for selecting a insert id key.
	 *
     */
    public String getSelectInsertIdStatement() {
		return "SELECT NEWID()";
    }
}

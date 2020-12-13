/*
 * 20/01/2003 - 10:17:56
 *
 * $RCSfile: OracleInterface.java,v $ - JDBF Object Relational mapping system
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
 * <code>OracleInterface</code> overrides SqlInterface's methods for constructing sql statements
 * where Oracle's implementation differs from ANSI or is not specified by ANSI.
 *
 */
public class OracleInterface extends SqlInterface{

    /**
     * Creates an empty object 
     */
    public OracleInterface(){}


	/**
	 * Return the relative clause for "FOR UPDATE" statement
	 *
	 * @return FOR update statement
	 */
	public String getClauseStringForUpdate() {
        return "FOR UPDATE";
    }


    /**
	 * Return the clause of current timeStamp
	 *
	 * @return current timeStamp
	 */
	public String getClauseStringCurrentTimeStamp(){
        return 	"SYSDATE";
    }

	

	/**
	 * Return the current timeStamp statement
	 *
	 * @return current timestamp statement
	 */
	public String getCurrentTimeStampStatement(){
        return 	"SELECT SYSDATE FROM DUAL";
    }


	/**
	 * Forms an sql statement the drop the table given tableName
	 *
	 * @param tableName name of table to drop
	 * @return sql statement
	 */
    public String getDropTableStatement(String tableName){
		return super.getDropTableStatement(tableName)
            + " CASCADE CONSTRAINTS";
    }


	/**
     * Forms an sql sequence statement given name
	 *
	 * This method throws an MappingExcpetion because for a generic sql interface
	 * the sequence feature is not supported.
	 * @see OracleInterface#getSelectSequenceStatement
	 *
	 * @param name
	 * @return Sql statement for selecting a sequence key
	 *
     */
	public String getSelectSequenceStatement(String name) {
		return new StringBuffer(SELECT).append(" ").append(name).
	    append(".nextval ").append(FROM).append(" dual").toString();
    }
}
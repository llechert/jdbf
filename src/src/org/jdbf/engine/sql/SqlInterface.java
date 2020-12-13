/*
 * 28/10/2002 - 10:29:11
 *
 * $RCSfile: SqlInterface.java,v $ - JDBF Object Relational mapping system
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

import java.util.logging.Logger;


import org.jdbf.engine.mapping.MappingException;

/**
 * <code>SQLInterface</code> implements a part of RelationalDatabase
 * It provides methods to build sql strings and statements
 * and to convert data values into sql strings.
 * It supports the ANSI standard SQL clauses.
 * Subclasses will override the appropriate method to support extensions
 * to ANSI SQL.
 *
 */
public class SqlInterface implements java.io.Serializable{


	public static String INSERT = "INSERT INTO ";
	public static String SELECT_ALL = "SELECT * ";
	public static String SELECT = "SELECT ";
	/*
	 * Bug fixing (Gmartone) 955130
	 */
	public static String DELETE = "DELETE ";
	/*
	 * End bug fixing (Gmartone)
	 */
	public static String UPDATE = "UPDATE ";
	public static String FROM = "FROM ";
	public static String WHERE = "WHERE ";
	public static String ORDER_BY = "ORDER BY ";
	public static String SET = "SET ";
	public static String VALUES = "VALUES ";
	public static String AND = "AND ";
	public static String OR = "OR ";
	public static String EQUAL =  "= ";
	public static String NOT_EQUAL =  "<> ";
	public static String GREATER_THAN = "> ";
	public static String GREATER_OR_EQUAL = ">= ";
	public static String LESS_THAN = "< ";
	public static String LESS_OR_EQUAL = "<= ";
	public static String LIKE = "LIKE ";
	public static String BETWEEN = "BETWEEN ";
	public static String GROUP_BY = "GROUP BY ";
	public static String HAVING = "HAVING ";
	public static String UNION = "UNION ";
	public static String DISTINCT = "DISTINCT ";
	public static String NOT_EXISTS = "NOT EXISTS ";
	public static String UNIQUE = "UNIQUE ";
	public static String NULL = "NULL ";
	public static String PARAMETER = "? ";
	public static String MAX = "MAX";
	public static String NEXTVAL = "NEXTVAL ";
	public static String ASSIGNMENT = "= ";
	public static String ASC = "ASC ";
	public static String DESC = "DESC ";
	public static String TABLE = "TABLE ";
	public static String CREATE = "CREATE ";
	public static String DROP = "DROP ";

	protected String className;

	private Logger logger;


	/**
	 *
	 * Creates an empty object
	 *
	 */
	public SqlInterface() {
		className = getClass().getName();
		logger = Logger.getLogger(className);
	}


	/**
	 * Return the claase of current timeStamp
	 *
	 * @return current timeStamp
	 * @throws MappingExpcetion if feature not supported
	 */
	public String getClauseStringCurrentTimeStamp() throws MappingException{
		return 	"CURRENT_TIMESTAMP";
	}


	/**
	 * Returns the definition of column given name, given type,given isNullable
	 *
	 * @param name
	 * @param type
	 * @param isNullable
	 * @return column definition
	 */
	public String getColumnDefinition(String name, String type, boolean isNullable){
		return name + " " + type + (isNullable ? "" : " NOT NULL");
	}


	/**
	 * Forms an sql statement which counts records in the given table
	 * which statisfy the given search condition.
	 *
	 * @param tableName name of table
	 * @return count statement as String
	 */
	String getCountStatement(String tableName){
		return getCountStatement(tableName, "");
	}


	/**
	 * Forms an sql statement which counts records in the given table
	 * which statisfy the given search condition.
	 *
	 * @param tableName name of table
	 * @param condition
	 * @return String count statement as String
	 */
	String getCountStatement(String tableName, String condition){
		return getSelectStatement(tableName, getSelectCountAll() , condition);
	}


	/**
	 * Forms an sql statement the return a current timestamp
	 *
	 * @return String sql statement
	 * @throws MappingException
	 */
	public String getCurrentTimeStampStatement() throws MappingException{
		return 	"SELECT" + " " +   getClauseStringCurrentTimeStamp();
	}


	/**
	 * Forms an sql statement the create the table given tableName,column
	 *
	 * @param tableName name fo table to create
	 * @param columns
	 * @return String sql statement
	 */
	public String getCreateTableStatement(String tableName, String columns){
		return new StringBuffer(CREATE).
				append(' ').
				append(TABLE).
				append(' ').
				append(tableName).
				append(" (").
				append(columns).
				append(')').toString();
	}


	/**
	 * Froms an sql statement the delete the table given tableName
	 *
	 * @param tableName
	 * @param condition
	 * @return String
	 *
	 */
	public String getDeleteStatement(String tableName,String condition){
		StringBuffer buff = new StringBuffer();
		buff.append(DELETE).append(FROM).append(tableName).append(" ");
	
		if(condition.length() > 0)
			buff.append(condition);

		return buff.toString();
	}


	/**
	 * Forms an sql statement the drop the table given tableName
	 *
	 * @param tableName name fo table to drop
	 * @return String sql statement
	 */
	public String getDropTableStatement(String tableName){
		return new StringBuffer(DROP)
			.append(' ')
			.append(TABLE)
			.append(' ')
			.append(tableName).toString();
	}


	/**
	 * Forms an sql insert statement for a given table, given columns, and
	 * given values.
	 *
	 * @param tableName name of table
	 * @param columns to insert
	 * @return String insert statement as String
	 */
	public String getInsertStatement(String tableName, String columns, String values){
		return new StringBuffer(INSERT)
			.append(tableName).append(" (")
			.append(columns)
			.append(") ")
			.append(VALUES)
			.append(" (")
			.append(values)
			.append(")").toString();
	}


	/**
	 * Return the COUNT(*) statement
	 *
	 * @return String count all statement
	 */
	public String getSelectCountAll(){
		return "SELECT COUNT(*)";
	}


	/**
	 * Forms an sql insert id statement
	 *
	 * This method throws an MappingExcpetion because for a generic sql interface
	 * the inset id feature is not supported
	 *
	 * @return Sql statement for selecting a insert id key
	 * @throws MappingException
	 *
	 */
	public String getSelectInsertIdStatement()throws MappingException{
		logger.throwing(className,"getSelectInsertIdStatement",
			        new MappingException("mapping.autoIncrementNotSupported"));
		throw new MappingException("mapping.autoIncrementNotSupported");
		//return null;
		//throw new MappingException("mapping.autoIncrementNotSupported");
	}


	/**
	 *  Forms an sql select statement for a given table, given fields, and
	 *  given criteria.
	 *
	 * @param tableName name of table
	 * @param fields to select
	 * @param condition
	 * @return String select statement as String
	 */
	public String getSelectStatement(String tableName, String fields,
                                     String condition){
    		StringBuffer sb = new StringBuffer(200);
			sb.append(SELECT)
		  	  .append(fields).append(" ")
		      .append(FROM).append(" ")
		      .append(tableName).append(" ");

			// WHERE should be included in condition
			if (condition != null && condition.length() > 0) {
				sb.append(WHERE).append(condition);
			}

			return sb.toString();
	}


	/**
	 * Forms an sql select statement for a given table,given fileds
	 *
	 * @param tableName name of table
	 * @param fields to select
	 * @return select statement as String
	 */
	public String getSelectStatement(String tableName, String fields){
		return getSelectStatement(tableName, fields, null);
	}


	/**
	 * Forms an sql sequence statement given name
	 *
	 * This method throws an MappingExcpetion because for a generic sql interface
	 * the sequence feature is not supported.
	 *
	 * @param name
	 * @return Sql statement for selecting a sequence key.
	 * @throws MappingException
	 *
	 */
	public String getSelectSequenceStatement(String name)throws MappingException{
		logger.throwing(className,"getSelectInsertIdStatement",
			        new MappingException("mapping.sequenceNotSupported"));
			        
		throw new MappingException("mapping.sequenceNotSupported");
		//return null;
		
	}


	/**
	 *  Forms an sql update statement for a given table, given columns, and
	 *  given values.
	 *
	 * @param tableName name of table
	 * @param columnsEqual
	 * @param condition
	 * @return String update statement as String
	 */
	public String getUpdateStatement(String tableName, String columnsEqual,
                                     String condition){
		StringBuffer sb = new StringBuffer(200);
		sb.append(UPDATE)
		  .append(tableName).append(" ")
		  .append(SET)
		  .append(columnsEqual).append(" ");

		if (condition.length() > 0){
			sb.append(condition);
		}
		return sb.toString();
	}
}
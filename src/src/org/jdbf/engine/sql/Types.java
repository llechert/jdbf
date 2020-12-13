/*
* 05/18/2002 - 13:01:11
*
*
* $RCSfile: Types.java,v $ - JDBF Object Relational mapping system
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

import java.util.HashMap;
import java.util.logging.Logger;

import org.jdbf.engine.mapping.MappingException;

/**
 * <code> Types </code> is the class that maps the type of the property
 * with SQLType (e.g. int with java.sql.Types.INTEGER).
 * 
 * <pre>
 *
 *  |    type      |        SQL Type          | 
 *  |------------- |--------------------------|
 *  |   int        |  java.sql.Type.INTEGER   |
 *  |   long       |  java.sql.Type.BIGINT    |
 *  |   binary     |  java.sql.Type.BINARY    |
 *  |   boolean    |  java.sql.Type.BIT       |
 *  |   date       |  java.sql.Type.DATE      |
 *  |   decimal    |  java.sql.Type.DECIMAL   |
 *  |   double     |  java.sql.Type.DOUBLE    |
 *  |   float      |  java.sql.Type.FLOAT     |
 *  |   real       |  java.sql.Type.REAL      |
 *  |   smallint   |  java.sql.Type.INTEGER   |
 *  |   time       |  java.sql.Type.TIME      |
 *  |   timestamp  |  java.sql.Type.TIMESTAMP |
 *  |   tinyint    |  java.sql.Type.TINYINT   |
 *  |   varbinary  |  java.sql.Type.VARBINARY |
 *  |   char       |  java.sql.Type.VARCHAR   |
 *  |   string     |  java.sql.Type.VARCHAR   |
 *  |   numeric    |  java.sql.Type.NUMERIC   |
 *  |   longstring |  java.sql.Type.VARCHAR   |
 *  |   lognbinary |  java.sql.Type.BINARY    |
 *
 * </pre>
 */
public class Types{

	/**
	 * Class name
	 */
	private static String className = "org.jdbf.engine.sql.Types"; 

	/**
	 * Logger object
	 */
	private static Logger logger  = Logger.getLogger(className);

	/** 
	 * Map type of property - sql type 
	 */
	protected static final HashMap TYPES = loadTypes();


	/** 
	 * Load the table that maps the type of property with SQL type.
	 * 
	 * @return HashMap map loaded.
	 */
	private static HashMap loadTypes(){
		HashMap types = new HashMap();
		types.put("string",new Integer(java.sql.Types.VARCHAR));
		types.put("long",new Integer(java.sql.Types.BIGINT));
		types.put("binary",new Integer(java.sql.Types.BINARY));
		types.put("boolean",new Integer(java.sql.Types.BIT));
		types.put("date",new Integer(java.sql.Types.DATE));
		types.put("decimal",new Integer(java.sql.Types.DECIMAL));
		types.put("double",new Integer(java.sql.Types.DOUBLE));
		types.put("float",new Integer(java.sql.Types.FLOAT));
		types.put("int",new Integer(java.sql.Types.INTEGER));
		types.put("real",new Integer(java.sql.Types.REAL));
		types.put("smallint",new Integer(java.sql.Types.INTEGER));
		types.put("time",new Integer(java.sql.Types.TIME));
		types.put("timestamp",new Integer(java.sql.Types.TIMESTAMP));
		types.put("tinyint",new Integer(java.sql.Types.TINYINT));
		types.put("varbinary",new Integer(java.sql.Types.VARBINARY));
		types.put("char",new Integer(java.sql.Types.CHAR));
		types.put("numeric", new Integer(java.sql.Types.NUMERIC));
		types.put("longstring", new Integer(java.sql.Types.VARCHAR));
		types.put("longbinary", new Integer(java.sql.Types.BINARY));
		return types;
	}


	/**
	 * Return the sql type for type of property specified in type.
	 * If type is invalid type MappigException if thrown
	 *
	 * @param type of property
	 * @return int sql type
	 * @throws MappingException if type is invalid
	 */
	public static int getSQLType(String type) throws MappingException{
		Integer sqlType = (Integer) TYPES.get(type);	
		if( sqlType == null){
			logger.throwing(className,"getSQLType",
			    new MappingException("mapping.invalidType",type));
		        
			//return -1;
			throw new MappingException("mapping.invalidType",type);
		}
		else
			return sqlType.intValue();
	}
}

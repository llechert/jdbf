/*
 * 23/01/2003 - 11:56:11
 *
 * $RCSfile: SqlInterfaceFactory.java,v $ - JDBF Object Relational mapping system
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

import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.jdbf.engine.mapping.MappingException;
import org.jdbf.castor.Messages;

/**
 * <code>SqlIntrerfaceFactory</code> associates a name with an instance of SqlInterface.
 */
public class SqlInterfaceFactory{

	/**
	 * Class name
	 */
	private static final String CLASS_NAME = "org.jdbf.engine.sql.SqlInterfaceFactory";

	/**
	 *
	 * Logger object
	 */
	private static Logger logger = Logger.getLogger(CLASS_NAME);


	/**
	 *
	 * Creates an empty object
	 *
	 */
	private SqlInterfaceFactory() {
		logger = Logger.getLogger(CLASS_NAME);
	}


	/**
	 * List of <code>SqlInterfaceEntry</code>
	 * holding the instantiated interfaces
	 */
	static ArrayList sqlIfaces = new ArrayList();

	/**
	 * Single entry of SQLInterface
	 */
	static class SqlInterfaceEntry {

		/**
		 * Logic name of SQLInterface
		 */
		String name;

		/**
		 * SQLInterface object
		 */
		SqlInterface sqlInterface;

		/**
		 * Creates a SQLInterfaceEntry
		 */
		SqlInterfaceEntry(String name, SqlInterface sqlInterface) {
	    		this.name = name;
	    		this.sqlInterface = sqlInterface;
		}
	}


	/**
	 * Interface that creates the SQLInterface object
	 */
	static interface SqlInterfaceMaker {
		SqlInterface create();
	}

	/** the list of sql interfaces */
	private static SqlInterfaceMaker[] sqlInterfaces =
		new SqlInterfaceMaker[] {
	    		new SqlInterfaceMaker() {

				public SqlInterface create() {
					return new SqlInterface();
		    		}

				public String toString() {
					return "generic";
		    		}
		},
	    	new SqlInterfaceMaker() {

				public SqlInterface create() {
					return new HsqlInterface();
		    		}

				public String toString() {
					return "hsql";
		    		}
		},
	    	new SqlInterfaceMaker() {

				public SqlInterface create() {
					return new PostgreSQLInterface();
		    		}

				public String toString() {
					return "postgres";
		    		}
		},
	    	new SqlInterfaceMaker() {

				public SqlInterface create() {
					return new SqlServerInterface();
		    		}

				public String toString() {
					return "sqlserver";
		    		}
		},
	    	new SqlInterfaceMaker() {

				public SqlInterface create() {
					return new OracleInterface();
		    		}

				public String toString() {
					return "oracle";
		    		}
		},
	    	new SqlInterfaceMaker() {
		    		public SqlInterface create() {
					return new MySQLInterface();
		    		}

				public String toString() {
					return "mysql";
		    		}
		},
	    	new SqlInterfaceMaker() {

				public SqlInterface create() {
					return new SybaseInterface();
		    		}

				public String toString() {
					return "sybase";
		    		}
		},
	    	new SqlInterfaceMaker() {

				public SqlInterface create() {
					return new InterbaseInterface();
		    		}

				public String toString() {
					return "interbase";
		    		}
		},
	    	new SqlInterfaceMaker() {

				public SqlInterface create() {
					return new InformixInterface();
		    		}

				public String toString() {
					return "informix";
		    		}
		}
	};


	/**
	 * Adds a <code>SqlInterface</code> to the list of
	 * instantiated interfaces.
	 *
	 * @param the short name of the <code>SqlInterface</code>
	 * @return SqlInterface added to the list
	 * @throws MappingException
	 */
	private static SqlInterface addSqlInterface(String name)throws MappingException{
		logger.log(Level.FINEST,Messages.format(
				"SQLInterfaceFactory.addInterface",name));
		SqlInterface sqlIface = null;
		for (int i=0; i<sqlInterfaces.length; i++) {
	    		if (name.equals(sqlInterfaces[i].toString())) {
				sqlIface = sqlInterfaces[i].create();
				sqlIfaces.add(new SqlInterfaceEntry(
						sqlInterfaces[i].toString(),
						sqlIface));
				logger.log(Level.FINEST,Messages.format(
					"SQLInterfaceFactory.interfaceAdded",
					name));
				return sqlIface;
	    		}
		}
		return null;
	}


	/**
	 * Return  an <code>SqlInterface</code> for the given
	 * class name.
	 *
	 * @param the short name of the <code>SqlInterface</code>
	 * @return SqlInterface for the given
	 * class name.
	 * @throws MappingException
	 */
	private static SqlInterface getSqlInterfaceIntern(String name)throws MappingException{
		logger.log(Level.FINEST,Messages.format(
				"SQLInterfaceFactory.getInterface",name));
		SqlInterfaceEntry entry = null;
		int size = sqlIfaces.size();
		for (int i=0; i<size; i++) {
	    		entry = (SqlInterfaceEntry)sqlIfaces.get(i);
	    		if (entry.name.equals(name))
				return entry.sqlInterface;
		}
		logger.log(Level.FINEST,Messages.format(
				"SQLInterfaceFactory.notExistInterface",name));
		return null;
	}


	/**
	 * Return the SqlInterface specified in name
	 *
	 * @param name of the sql interface
	 * @return SqlInterface for the given name
	 * @throws MappingException
	 */
	public synchronized static SqlInterface getSqlInterface(String name)
		throws MappingException{
		
		String low = name.toLowerCase();
		SqlInterface sqlIface = getSqlInterfaceIntern(low);
		if (sqlIface != null)
	    		return sqlIface;

		sqlIface = addSqlInterface(low);
		if (sqlIface != null)
	    		return sqlIface;

		logger.throwing(CLASS_NAME,"getSqlInterface", 
			new MappingException("mapping.sqlIfaceNotFound", name));
		//return null;
		throw new MappingException("mapping.sqlIfaceNotFound", name);

    }
}
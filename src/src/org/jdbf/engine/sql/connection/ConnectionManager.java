/*
* 10/09/2002 - 22:56:11
*
* $RCSfile: ConnectionManager.java,v $ - JDBF Object Relational mapping system
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
package org.jdbf.engine.sql.connection;

 
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.logging.Level;
 
import org.jdbf.engine.configuration.Configuration;
import org.jdbf.engine.configuration.ConfigurationImpl;
import org.jdbf.engine.Configurable;
import org.jdbf.castor.Messages;
import org.jdbf.engine.mapping.DatabaseMap;
import org.jdbf.engine.mapping.MappingException;

/**
 * <code>ConnectionManager</code> handles the database connection 
 * for many vendors. SetConfiguration method, read information about
 * database connection from configuration file and creates database
 * connection.<br>
 * 
 * Te database connection is created with flag autoCommit setted to 
 * false value.
 */

public class ConnectionManager implements Configurable {
    
    /**
     * Class name
     */
    private static final String CLASS_NAME = "org.jdbf.engine.sql.connection.ConnectionManager";

    /**
     * Logger object
     */
    private Logger logger;
    
    
    /** A list of the availabe connections. */
    private Map conns = Collections.synchronizedMap(
                                           new HashMap());


    /**
     *
     * Creates an empty object
     *
     */
    public ConnectionManager() {
        logger = Logger.getLogger(CLASS_NAME);
    }


    /**
     * Add a new ConnectionSource with a specific name
     *
     * @param name name of connection
     * @param connection - ConnectionSource object
     *
     */
    public void addConnection(String name,ConnectionSource connection){
	
		conns.put(name, connection);
    }

    
    /**
     * Create a collection of database connection
     * 
     * Connection is setted with autoCommit property
     * to false value.
     *
     * @param dbs
     * @exception MappingException if an error occurs
     */
    public void createConnection(List dbs)
        throws MappingException{
		
		DatabaseMap dbMap = null;
        logger.log(Level.INFO,Messages.message("ConnectionManager.createConnection"));
		try{
			
	    	for(int i = 0; i < dbs.size(); i++){
				dbMap = (DatabaseMap) dbs.get(i);
					
                Class.forName(dbMap.getDbDriver());
				Connection conn = DriverManager.getConnection(dbMap.getDbServer(),
		                                dbMap.getDbLogin(),dbMap.getDbPassword());
                Object params[] = {dbMap.getName(),
                                   dbMap.getDbServer(),
                                   dbMap.getDbLogin(),
                                   dbMap.getDbPassword()
                                  };

				conn.setAutoCommit(false);
                
                logger.log(Level.FINEST,Messages.format(
                                "ConnectionManager.connection",params));
		
                addConnection(dbMap.getName(),new ConnectionSource(conn,
		                              dbMap.getVendor()));
                
                logger.log(Level.FINEST,Messages.format(
                                "Connection.connectionAdded",dbMap.getName()));
	    	}
		}
		catch(ClassNotFoundException e){
	    	logger.throwing(CLASS_NAME,"createConnection()",
                            new MappingException("class.noClassDefFound",dbMap.getDbDriver())
                           );
            throw new MappingException("class.noClassDefFound",dbMap.getDbDriver());
		}
		catch(SQLException e){
	    	logger.throwing(CLASS_NAME,"createConnection()",
                            new MappingException("mapping.connNotFound",dbMap.getDbServer())
                           );
            throw new MappingException("mapping.connNotFound",dbMap.getDbServer());
		}    
    }


    /**
     * Closes the connection specified in name.
     * 
     *
     * @param name - name of connection to close
     * @throws MappingException if error occurred
     */
    protected void closeConnection(String name) throws MappingException,SQLException{
        
        logger.log(Level.INFO,Messages.format("ConnectionManager.closeConn",name));
		ConnectionSource conn = (ConnectionSource)conns.get(name);
		
		if(conn == null){
		
	    	logger.throwing(CLASS_NAME,"closeConnection()",
                            new MappingException("mapping.connNotFound",name)
                           );        
            throw new MappingException("mapping.connNotFound",name);
		}
		else
	    	conn.closeConnection();
    }

    

    /**
     * Closes all the connections
     *
     * @throws MappingException if error occurred
     */
    protected void closeAllConnections() throws MappingException,SQLException{
        	
		Iterator iter = conns.keySet().iterator();
		while(iter.hasNext()){		
	    	String nameConn = (String)iter.next();
	    	closeConnection(nameConn);
		}	
    }

    
    /**
     * Destroy object.
     * 
     * This method releases all database connection
     * 
     * @throws Exception
     */
    public synchronized void destroy()throws Exception{        
		closeAllConnections();
    }


    /**
     * Finalize object
     * 
     * @throws Throwable
     */
    protected void finalize() throws Throwable{
        super.finalize();
    }
 

    /**
     * Returns the connection specified in nameConn
     *
     * @param nameConn name of connection
     * @return java.sql.Connection object
     * @throws MappingExpcetion if connection not exits
     */
    public Connection getConnection(String nameConn) throws MappingException{
        
        logger.log(Level.INFO,Messages.format("ConnectionManager.getConn",nameConn));
		try{
            ConnectionSource conn = getConnectionSource(nameConn);					
	    	return conn.getConnection();
		}
		catch(SQLException e){
	    	logger.throwing(CLASS_NAME,"createConnection()",
                            new MappingException(e)
                           );
            throw new MappingException(e);
            
		}
    }

    
    /**
     * Return the ConnectionSource object specified in nameConn
     *
     * @param nameConn name of connection
     * @return ConnectionSource
     * @throws MappingExcpetion if connection not exits
     *
     */
    public ConnectionSource getConnectionSource(String nameConn)
    	throws MappingException{
        
        ConnectionSource conn = (ConnectionSource)conns.get(nameConn);

		if(conn == null){
	        logger.throwing(CLASS_NAME,"createConnection()",
                            new MappingException("mapping.connNotFound",nameConn)
                           );
            throw new MappingException("mapping.connNotFound",nameConn);
        }
		else
	    	return conn;
    }

     
    /**
     * Releases  connection specified in conn
     *
     * @param name
     * @throws MappingException if error occured
     */
    public void releaseConnection(String name)
		throws MappingException{	
	
		logger.log(Level.INFO,Messages.format("ConnectionManager.releaseConn",name));
        ConnectionSource conn = (ConnectionSource)conns.get(name);
		if(conn == null){		
	        logger.throwing(CLASS_NAME,"createConnection()",
                            new MappingException("mapping.connNotFound",name)
                           );
            throw new MappingException("mapping.connNotFound",name);
		}
		else
	    	conn.releaseConnection();
    }
    

    /**
     * Loads and creates database configurations from maps.
     * 
     * This method read form configuration file the information
     * about databases from root specified in conf.
     *
     * @param conf holds configuration for loading the database maps
     * @exception MappingException if an error occurs
     */
    public void setConfiguration(Configuration conf) 
    	throws MappingException{
        
		Iterator iter = conf.getConfigurations("database");
		ArrayList dbs = new ArrayList();
		while(iter.hasNext()){
	    	conf = (ConfigurationImpl)iter.next();
	    	DatabaseMap dbMap = new DatabaseMap();
	    	ConfigurationImpl child = (ConfigurationImpl)conf.getConfiguration("name");
            logger.log(Level.FINEST,"name " + child.getValue());
	    	dbMap.setName(child.getValue());
	    	child = (ConfigurationImpl)conf.getConfiguration("vendor");
            logger.log(Level.FINEST,"vendor " + child.getValue());
	    	dbMap.setVendor(child.getValue());
	    	child = (ConfigurationImpl)conf.getConfiguration("dbDriver");
            logger.log(Level.FINEST,"dbDriver " + child.getValue());
	    	String dbDriver = child.getValue();
	    	if(dbDriver == null){	    	
            	logger.throwing(CLASS_NAME,"createConnection()",
                            new MappingException("mapping.missingDriver")
                           );
	        	throw new MappingException(Messages.message("mapping.missingDriver"));
	    	}
	    	else
				dbMap.setDbDriver(dbDriver);
			
	    	child = (ConfigurationImpl)conf.getConfiguration("dbServer");
            logger.log(Level.FINEST,"dbServer " + child.getValue());
	    	dbMap.setDbServer(child.getValue());
	    	child = (ConfigurationImpl)conf.getConfiguration("dbLogin");
            logger.log(Level.FINEST,"dbLogin " + child.getValue());
	    	dbMap.setDbLogin(child.getValue());
	    	child = (ConfigurationImpl)conf.getConfiguration("dbPassword");
            logger.log(Level.FINEST,"dbPassword " + child.getValue());
	    	dbMap.setDbPassword(child.getValue());
			
	    	dbs.add(dbMap);
		}
		createConnection(dbs);
    }
}
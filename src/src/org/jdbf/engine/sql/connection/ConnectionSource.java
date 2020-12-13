/*
* 07/10/2002 - 20:28:34
*
* $RCSfile: ConnectionSource.java,v $ - JDBF Object Relational mapping system
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

import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <code>ConnectionSource</code>wrappes a object Connection.
 * 
 * ConnecitonSource contains the state of connection.
 * When a connection is loaded by ConnectionManager, it is marked 
 * as in use and it is locked until releaseConnection method 
 * is invoked.
 * 
 */
public class ConnectionSource{
    
    /**
     * Class name
     */
    private static final String CLASS_NAME = "org.jdbf.engine.sql.connection.ConnectionSource";

    /** Connection object */
    private Connection connection;

    /** flag that indicates if connection is in use */
    private boolean isUsed;

    /** Database's vendor */
    private String vendor;

    /**
     * Logger object
     */
    private Logger logger;
     
    
    /**
     * Creates an empty object
     */
    public ConnectionSource() {
        
		connection = null;
		isUsed = false;
		vendor = null;
        logger = Logger.getLogger(CLASS_NAME);
    }


    /**
     * Creates a ConnectionSource object.
     * Default it is created as not in use 
     *
     * @param vendor
     * @param connection new Connection object
     */
    public ConnectionSource(Connection connection,String vendor){
        
		setConnection(connection);
		isUsed = false;
		this.vendor = vendor;
    }

    
    /**
     * Close  the connection
     *
     * @throws SQLException if error occured
     */
    public void closeConnection() throws SQLException{	
	
        if(!connection.isClosed()){
	    	connection.close();
	    	isUsed = false;
        }
    }


    /** 
     * Returns the Connection object.
     *
     * @return Connection object
     * @throws SQLException if connection is in use
     */
    public Connection getConnection() throws SQLException{
		if(!isUsed()){
	    	isUsed = true;
	    	return connection;
		}
		else{
	    	logger.throwing(CLASS_NAME,"getConnection()",
                    new SQLException(org.jdbf.castor.Messages.message("conn.inUse")));
            throw new SQLException(org.jdbf.castor.Messages.message("conn.inUse"));
            
        }
    }


    /**
     * Return the state of ConnectionSource
     *
     * @return true if connection is used, false otherwise
     */
     public boolean isUsed(){
         return isUsed;
     }


     /**
      * Return name of database vendor
      * 
      * @return name of database's vnedor
      *
      */
      public String getVendor(){
          return vendor;
      }


     /**
      * Sets the Connection object
      *
      * @param connection - Connection object
      */
      public void setConnection(Connection connection){
          this.connection = connection;
      }

    
    /**
     * Set the name of database vendor
     *
     * @param vendor - name of database vendor
     *
     */
     public void setVendor(String vendor){
         this.vendor = vendor;
     }


    /**
     * Release the connection.      
     *
     */
    public void releaseConnection(){
        isUsed = false;
    }
}
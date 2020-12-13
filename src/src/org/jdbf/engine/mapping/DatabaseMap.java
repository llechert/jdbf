/*
* 18/04/2002 - 21:06:27
*
* $RCSfile: DatabaseMap.java,v $ - JdbF Object Relational mapping system
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
package org.jdbf.engine.mapping;

/**
 * <code>DatabaseMap </code> represents a mapping of database
 * 
 */
public class DatabaseMap{    
    
    /** name of class */
    private static final String CLASS_NAME = "org.jdbf.engine.mapping.DatabaseMap";

    /** name of database */
    private String name;
        
    /** vendor of database */
    private String vendor;
    
    /** JDBC driver */
    private String dbDriver;
    
    /** Server of database */
    private String dbServer;
    
    /** Login of database */
    private String dbLogin;
    
    /** Password of database*/
    private String dbPassword;
    
    
    
    
    /** 
     * Creates the DatabaseMap object.	 	 	
     */
    public DatabaseMap(){}
	
  
    /**
      * Return vendor of database
      * @return String vendor of database
      */        
    public String getVendor(){
        return vendor;
    }
    
    
    /**
     * Return name of database
     * @return String name of database
     */
    public String getName(){
        return name;
    }
    
    
    /**
     * Return JDBC driver
     * @return String name of JDBC driver
     */
    public String getDbDriver(){
        return dbDriver;
    }
    
    
    /**
     * Return name of server 
     * @return String name of server
     */
    public String getDbServer(){
  	    return dbServer;
    }
    
    
    /**
     * Return login of database
     * @return String login of database
     */
    public String getDbLogin(){
        return dbLogin;
    }
    
    
    /**
     * Return password of database
     * @return String password of database
     */
    public String getDbPassword(){
        return dbPassword;
    }
    
      
    /**
     * Set vendor name of database
     * @param vendor name of database
     */
    public void setVendor(String vendor){
        this.vendor = vendor;
    }
    
    
    /**
     * Set name of database
     * @param name of database
     */
    public void setName(String name){
        this.name = name;
    }
    
      
    /**
     * Set JDBC driver
     * @param dbDriver
     */
    public void setDbDriver(String dbDriver){
        this.dbDriver = dbDriver;
    }
    
    
    /**
     * Set server
     * @param dbServer server
     */
    public void setDbServer(String dbServer){
        this.dbServer = dbServer;
    }
    
    
    /**
     * Set login of database
     * @param dbLogin login of database
     */
    public void setDbLogin(String dbLogin){
        this.dbLogin = dbLogin;
    }
    
    
    /**
     * Set password of database
     * @param dbPassword password of database
     */
    public void setDbPassword(String dbPassword){
        this.dbPassword = dbPassword;
    }
    
          
    /**
     * Return the object as String
     * @return String
     */
    public String toString(){
        StringBuffer buff = new StringBuffer();
    	buff.append(CLASS_NAME).append("[").append("\n")
    	    .append("name ").append(name).append("\n")
	    	.append("vendor ").append(vendor).append("\n")			
	    	.append("dbDriver ").append(dbDriver).append("\n")
	    	.append("dbServer ").append(dbServer).append("\n")			
	    	.append("dbLogin ").append(dbLogin).append("\n")			
	    	.append("]").append("\n");
			
		return buff.toString();
     }
}

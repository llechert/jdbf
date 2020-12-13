/*

 * 09/01/2003 - 21:29:27

 *

 * $RCSfile: KeyKeeper.java,v $ - JdbF Object Relational mapping system

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
 package org.jdbf.engine.keygen;

   
import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.jdbf.castor.Messages;
import org.jdbf.engine.mapping.*;
import org.jdbf.engine.sql.SqlInterface;



/**

 *<code>KeyKeeper</code> provides generated keys
 *for HighLow keyGenerator

 *

 * The method is similar to the one called Key-Values by Scott Ambler

 * in <a href="http://www.Ambysoft.com/mappingObjects.pdf">Mapping

 * Objects to Relational Databases</a>.

 * With this method, there's a multi-row table which has three columns,

 * one for the identifier for the table name, one for the identifier for the key table

 * and another for the next key value for that table.
 */

public class KeyKeeper {

    
	/**
	 * Class name
	 */
    private static final String CLASS_NAME = "org.jdbf.engine.keygen.KeyKeeper";



    /** 

     * Represents the sql type for high value 

     */

    private static int DATA_TYPE = Types.VARCHAR; 

    

    

    /** 

     * Represents the progressive to add the high forn new insert operation

     */

    private static int PROGRESSIVE = 1;

    

    

    /** 

     * Represents the tyoe of column of high value

     */

    private int columnType = -1;



    

    /**

     * The key is generated for this table.

     */

    private String tableName;



    

    /**

     * Indicates if KeyKeeper is used

     */

    private boolean isUsed = false;

        

    

    /**

     * The high value is combined with the low value to

     * produce the key.

     */

    private HighLowMap hiloMap;





    /**

     * SqlInterface object

     */

    private SqlInterface sqlInterface;



    

    /**

     * Logger object

     */

    private Logger logger;



                    

    /**

     * Creates the object given table name,given hiloMap
     * and current sqlInterface

     *

     * @param tableName

     * @param hiloMap
     * @param sqlInterface

     */

    public KeyKeeper (String tableName,HighLowMap hiloMap,
    				  SqlInterface sqlInterface){

		logger = Logger.getLogger(CLASS_NAME);
		this.tableName = tableName;
		this.hiloMap = hiloMap;
		this.sqlInterface = sqlInterface;

    }



        

    /**

     * Obtains a block of available keys 
     * from the key-values table.

     *

     * @param conn java.sql.Connection

     * @param vendor name of database vendor

     * @return HighLowMap the value beyond the block of available keys,

     * @exception KeyGenerationException if an error occurs

     */

    private HighLowMap fetchHigh(Connection conn,String vendor)

        throws KeyGenerationException{

	

		logger.log(Level.INFO,Messages.message("KeyKeeper.fetchHigh"));

		/*
	 	 * HighLowMap holds three things:  
	 	 * 1) name of the table used to store two columns. 
	 	 * 2) column name for identifier  for table name and 
	 	 * 3) column name for the next key for that table.
	 	 */
		StringBuffer condition = new StringBuffer(32);		
		condition.append(hiloMap.getTableColumn()).append("=?");

		String sql = sqlInterface.getSelectStatement(hiloMap.getTable(),
													 "*",
													 condition.toString()
										);

		logger.log(Level.FINEST,Messages.format("KeyKeeper.highLowQuery",
		                                sql,tableName));

	
		ResultSet rs = null;
		PreparedStatement pstmt = null;

		try{			  

			// First get column type of key.

			if (columnType == -1) {
				columnType = getColumnType(conn, 
										   hiloMap.getTable(),
						   				   hiloMap.getKeyColumn());

			}

    
			if (columnType != DATA_TYPE){					
				logger.throwing(CLASS_NAME,"fetchHigh()",
			    			new KeyGenerationException(
			    				Messages.format("mapping.keyGenWrongType", 
				   				new Integer(columnType),getClass())
		           ));
		           
				throw new KeyGenerationException(
						Messages.format("mapping.keyGenWrongType", 
						new Integer(columnType),getClass()));  
			}

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,tableName);

			rs = pstmt.executeQuery();

			//Get the high and low values and creates the key
			//then updates the low values adding to last low the 
			//value of PROGRESSIVE variable and update the table 
			//that contains the highlow
			if (rs.next()) {

				hiloMap.setHigh(rs.getString(2));
				hiloMap.setLow(rs.getObject(3));
			}
			else{
				logger.throwing(CLASS_NAME,"fetchHigh()",
			    		new KeyGenerationException("mapping.MissingDataKeyGen",
							hiloMap.getTable(),tableName)

		         );
		         
		         throw new KeyGenerationException("mapping.MissingDataKeyGen",
						hiloMap.getTable(),tableName);
			}
		}
		catch (SQLException excep) {

			logger.throwing(CLASS_NAME,"fetchHigh()",
			    new KeyGenerationException(excep));
			
			throw new KeyGenerationException(excep);
		}
		finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null) {
					pstmt.close();
				}
				pstmt = null;
			}
			catch (SQLException excep) {
				logger.throwing(CLASS_NAME,"fetchHigh()",
			    	new KeyGenerationException(excep));
			    	
				throw new KeyGenerationException(excep);
			}
		}
		logger.log(Level.FINEST,hiloMap.toString());
		logger.log(Level.INFO,Messages.message("KeyKeeper.fetchedHigh"));
		return hiloMap;
    }

	
    /**

     * Return column's type fo column specified in column parameter.

     *

     * @param conn

     * @param table name of table

     * @param column name of column

     * @return type of dataType specified in int

     * @throws SQLException if error occurs

     * @throws KeyGenerationException if error occurs

     *

     */

    private final int getColumnType(Connection conn, String table, String column)

		throws SQLException, KeyGenerationException{

		

		logger.log(Level.INFO,Messages.message("KeyKeeper.getColumnType"));
	
		ResultSet rs = null;
	
		int dataType = -1;
	
		try {
	
		    // Using meta data because many DBMS can not
	
		    // pre-compile statements.
	
		    DatabaseMetaData dmd = conn.getMetaData();
	
		    rs = dmd.getColumns(null , null, table, column);
	
	
	
		    synchronized (this) {
	
		        if (rs.next()) {
	
		   	    dataType =  rs.getInt("DATA_TYPE");
	
			    logger.log(Level.FINEST,Messages.message("dataType: " + dataType));
	
			    return dataType;
	
			}
	
			logger.throwing(CLASS_NAME,"getColumnType()",
	
				    new KeyGenerationException("mapping.checkKeyGen",
	
				        getClass().getName()
	
				    ));
			throw new KeyGenerationException("mapping.checkKeyGen",
											 getClass().getName());
		    }
	
		}	
		finally{
	
		    if (rs != null) {
	
				rs.close();
	
				rs = null;
	
		    }
	
		    logger.log(Level.INFO,Messages.message("KeyKeeper.getColumnTypeFinish"));
	
		    return dataType;
	
		}

    }

	

	

    /**

     * Returns the next generated key.

     *

     * @param conn database connections for the key-values table

     * @param vendor name of database vendor

     * @return Object the generated key

     * @exception KeyGenerationException if an error occurs

     */

    public synchronized Object nextKey(Connection conn,String vendor)
		throws KeyGenerationException{



		logger.log(Level.INFO,Messages.message("KeyKeeper.nextKey"));
	
		
	
		Object key = null;
	
		
	
		if (!isUsed){		
	
		    hiloMap = fetchHigh(conn,vendor);
	
		    isUsed = true;
	
	        }	    
	
		int  low = Integer.parseInt(hiloMap.getLow().toString());
	
		int iLow = low + PROGRESSIVE;
	
		String cKey = hiloMap.getHigh() + String.valueOf(iLow);
	
	        key = (cKey);
	
		
	
		logger.log(Level.FINEST,Messages.format("KeeyKeeper.highLowKey",key));
	
		
	
		Object sLow = (String.valueOf(iLow)); 
	
		hiloMap.setLow(sLow);
	
		updateLow(conn,hiloMap);
	
	
	
		logger.log(Level.INFO,Messages.message("KeyKeeper.nextKeyFinished"));
	
		return key;

    }





    /**

     * Updates in table where keyGenerators are mapped the new low value specified

     * in table.

     *

     * @param conn, 
     * @param hiloMap HighLowMap object.

     * @return row number of rows affected.

     * @throws KeyGenerationException if occurs

     */

    private int updateLow(Connection conn, HighLowMap hiloMap) throws KeyGenerationException{

        

		logger.log(Level.INFO,Messages.message("KeyKeeper.updateLow"));
	
		int row = 0;
	
		PreparedStatement pstmt = null; 
	
		SqlInterface sqlInterface = new SqlInterface();
	
		StringBuffer condition = new StringBuffer(32);
	
		condition.append(hiloMap.getTableColumn()).append("=?");
	
		condition.append(" " + SqlInterface.AND  + hiloMap.getKeyColumn() + "=?");
	
	
	
		String setNewValue = hiloMap.getNextColumn() + "=" + hiloMap.getLow().toString();
	
	
	
		//Updates value
	
		String sql = sqlInterface.getUpdateStatement(hiloMap.getTable(),setNewValue,
	
					      condition.toString());
	
		
	
		Object[] params = {sql,tableName,hiloMap.getHigh()};
	
		logger.log(Level.FINEST,Messages.format("KeeyKeeper.updateLowQuery",params));
	
	
	
		try{
	
		    pstmt = conn.prepareStatement(sql);
	
		    pstmt.setString(1,tableName);
	
		    pstmt.setString(2,hiloMap.getHigh());
	
		    row = pstmt.executeUpdate();
	
	        }
	
		catch(SQLException excep){
	
		    logger.throwing(CLASS_NAME,"updateLow()",
		    			    new KeyGenerationException(excep));
			throw new KeyGenerationException(excep);
		}
	
		finally{
	
		    try{
	
				if(pstmt != null)
	
			    	pstmt.close();	    
	
				pstmt = null;
	
		    }
		    catch(SQLException ex){
	
				logger.throwing(CLASS_NAME,"updateLow()",
				    		    new KeyGenerationException(ex));
				throw new KeyGenerationException(ex);
		    }
	
		}
	
		logger.log(Level.INFO,Messages.message("KeyKeeper.updatedLow"));
	
		return row;

    }

}
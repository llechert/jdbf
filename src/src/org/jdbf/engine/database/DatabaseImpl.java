/*
* 04/04/2002 - 23:12:27
*
* $RCSfile: DatabaseImpl.java,v $ - JDBF Object Relational mapping system
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
package org.jdbf.engine.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

import org.jdbf.castor.Messages;
import org.jdbf.engine.basic.*;
import org.jdbf.engine.caching.CacheException;
import org.jdbf.engine.caching.CacheManager;
import org.jdbf.engine.configuration.*;
import org.jdbf.engine.criteria.*;
import org.jdbf.engine.mapping.*;
import org.jdbf.engine.keygen.*;
import org.jdbf.engine.sql.*;
import org.jdbf.engine.sql.connection.*;
import org.jdbf.engine.repository.*;
import org.jdbf.engine.transaction.*;


/**
 * <code>DatabaseImpl</code> is the class that handles the operations against 
 * database. DatabaseImpl handles the operations on transaction.
 * These operations are:<br>
 * <li>begin</li>
 * <li>close</li>
 * <li>commit</li>
 * <li>rollback</li>
 *
 */
public class DatabaseImpl extends DatabaseCore  {
    
    /** Transaction object */
    private Transaction transaction;
    
    /** ConnectionManager object */
    private ConnectionManager connectionManager;

    /** List of connections affected in operations that must be commited */
    private ArrayList dbs;
    
        
    /**
     * Creates the DatabaseImpl object, creating and loading 
     * a ConnectionManager object
     *
     * @param fileName name of configuration file
     * @param repFactory RepositoryFactory object
     * @param cacheMan CacheManager object
     * @throws Exception
     *
     */											
    public DatabaseImpl(String fileName,RepositoryFactory repFactory,
                        CacheManager cacheMan)
    	throws Exception {

		super(repFactory,cacheMan);
	
		dbs = new ArrayList();
		ConfigurationImpl conf = (ConfigurationImpl)ConfigurationBuilder.build(fileName);
		connectionManager = new ConnectionManager();
		connectionManager.setConfiguration(conf);		
    }
   
		 
    /**
     * Add a connection that has been used to list 
     * to be processed in commit or rollback operations
     *
     * @param conn to add
     *
     */
    private void addConnection(Connection conn){
        
		if(!dbs.contains(conn))
	    	dbs.add(conn);
    }

    
    /**
     * Begin transaction
     *
     * @throws TransactionException
     *
     */
    public void beginTransaction() throws TransactionException{
        
		synchronized(this){
	    	begin();
		}
    }

    
    /** 
     * Begin transaction 
     *
     * @throws TransactionException if transaction is already used
     *
     */
    public synchronized void begin() throws TransactionException{
    	
        logger.log(Level.INFO,Messages.message("Database.beginTx"));
        if(transaction != null && transaction.isOpen())
    	    logger.throwing(className,"begin()",
    			    new TransactionException(Messages.message("transaction.txInProgress")
				    ));
				        	    		
		transaction = new TransactionImpl();	
    }

        
    /**
     * Close transaction
     *
     */
    public synchronized void close(){    			
		//finalize the transaction
		transaction = null;
		logger.log(Level.INFO,Messages.message("Database.closedTx"));
    }
    
    
    /**
     * Commit transaction of all database affected 
     * in sql operations
     */
    public void commitTransaction(){
        
		try{
	    	synchronized(this){
	        	for(int i = 0; i < dbs.size(); i++){
		    		Connection conn = (Connection)dbs.get(i);
	            	commit(conn);
				}
	    	}
		}
		catch(Exception e){	    
	    	logger.log(Level.SEVERE,e.getMessage());
		}
    }
    
    
    /** 
     * Commit transaction.
     *
     * <b>Not use this method, you must use commitTransaction method!</b>
     *
     * @param connection to commit
     * @throws TransactionException
     *
     */
    public synchronized void commit(Connection connection) 
    	throws TransactionException{
    	        
		transaction.commit(connection);
		logger.log(Level.INFO,Messages.message("Database.committedTx"));
    }
	
	
    /**
     * Create OID using a keyGenerator specified in typeKeyGen.
     *
     * @param view RepositoryView object
     * @param typeKeyGen type of key generator
     * @param conn  Connection object.
     * @return Object OID created
     * @exception KeyGenerationExcpetion if error occurs
     *
     */
     private Object createOID(RepositoryView view,String typeKeyGen,Connection conn,String vendor)
        throws KeyGenerationException{
	
		logger.log(Level.INFO,Messages.message("Database.createOID"));	
		synchronized(this){
	    	KeyGenerator keyGen = KeyGeneratorFactory.getKeyGenerator(typeKeyGen);
	    	return  keyGen.generateKey(view,conn,vendor,sqlInterface);
		}
     }
     
     /**
      * Create cacheId composed by values of all primary key defined in obj.
      * @param pkMap
      * @param obj
      * @return String 
      * @throws CacheException
      */
     private String createCacheId(PrimaryKeyMap pkMap,ObjectMapped obj) 
     	throws CacheException {
     	return cacheManager.createId(pkMap,obj);
     }
	

    /**
     * Delete the object.<br> 
     * 
     * Delete operation is performed using primary key of object 
     * specified in input parameter is deleted.<br> 
     * 
     * It handles the following operations:
     * <li> get connection </li>
     * <li> create statement </li>
     * <li> execute statement </li>
     * <li> release connection </li>
     *
     * @param object object to delete
     * @return number of rows affected
     * @throws QueryException
     * @throws MappingException
     * 
     */
     public int delete(ObjectMapped object)
     	throws QueryException,MappingException,CacheException{
			 	  
        logger.log(Level.INFO,Messages.message("Database.delete"));
        
        long initTime = System.currentTimeMillis();        
		String repositoryViewName = object.getRepositoryViewName();		 
		RepositoryView view = (RepositoryView) getRepository(repositoryViewName);
		PrimaryKeyMap pkMap = view.getBeanDescriptor().getPrimaryKeyMap();
		String databaseName = view.getBeanDescriptor().getDatabaseName();
		Connection connection = connectionManager.getConnection(databaseName);
		addConnection(connection);
        sqlInterface = getCurrentSqlInterface(databaseName);
		int rows = 0;
				
		//create delete statement
		DeleteStatement deleteStat = new DeleteStatement(view,null,sqlInterface);		
		logger.log(Level.FINER,deleteStat.toString());
				
		//execute delete statement
		rows =  deleteStat.delete(object,view,connection);
			
		//release connection
		connectionManager.releaseConnection(databaseName);
	
		long endTime = System.currentTimeMillis();
		long finalTime = endTime - initTime;
	
		logger.log(Level.INFO,Messages.format("Database.rowsAffected",
	                                      String.valueOf(rows),
	                                      String.valueOf(finalTime)
	                                     )
	               );
	               
	    //create cache id 
	    String cacheId = createCacheId(pkMap,object);
	    
	    //invalidate object in cache
	    cacheManager.invalidateObject(cacheId,databaseName);				
		return rows;
     }


     /**
      * Delete the object. It handles the following operations:
      * <li> get connection </li>
      * <li> create statement </li>
      * <li> execute statement </li>
      * <li> release connection </li>
      *
      * @param criteria for delete statement
      * @return number of rows affected
      * @throws SQLException
      * @throws MappingException
      *
      */
     public int deleteForCriteria(Criteria criteria) 
         throws QueryException,MappingException{
			 	           
	 	logger.log(Level.INFO,Messages.message("Database.delete"));
	 
	 	long initTime = System.currentTimeMillis();
	 	String repositoryViewName = criteria.getRepositoryName();
	 	RepositoryView view = (RepositoryView)getRepository(repositoryViewName);
	 	String databaseName = view.getBeanDescriptor().getDatabaseName();
	 	Connection connection = connectionManager.getConnection(databaseName);
	 	addConnection(connection);
	 	sqlInterface = getCurrentSqlInterface(databaseName);
	 	int rows = 0;
				
	 	//create delete statement
	 	DeleteStatement deleteStat = new DeleteStatement(view,criteria,sqlInterface);		
	 	logger.log(Level.FINER,deleteStat.toString());
				
	 	//execute delete statement
	 	rows =  deleteStat.delete(connection);
				
	 	//release connection
	 	connectionManager.releaseConnection(databaseName);
	 
	 	long endTime = System.currentTimeMillis();
	 	long finalTime = endTime - initTime;
	
	 	logger.log(Level.INFO,Messages.format("Database.rowsAffected",
	                                          String.valueOf(rows),
	                                          String.valueOf(finalTime)
	                                         )
	               );
				
	 	return rows;
     }


     /**
      * Free DatabaseImpl object from memory
      *
      */
     public void destroy(){
		try{
	    	synchronized(this){
	        	close();
	        	connectionManager.destroy();
	    	}
		}
		catch(Exception e){
	   		logger.log(Level.SEVERE,e.getMessage());
		}
     }
	
	 
     /**
      * Finalize the object
      * 
      * @throws Throwable
      *
      */
     protected void finalize() throws Throwable{         
        super.finalize();
 		transaction = null;
 		connectionManager = null;
		dbs.clear();
		logger.log(Level.INFO,Messages.message("Database.finalize"));
     }
     

     /**
      * Return ConnectionManager object
      *
      * @return ConnectionManager object	  
      *
      */
     public ConnectionManager getConnectionManager(){
         return connectionManager;
     }

	 
     /**
      * Return Repository to RepositoryFactory specified in 
      * repositoryViewName
      *
      * @param repositoryViewName name repository
      * @return Repository
      * 
      */
     protected Repository getRepository(String repositoryViewName){
        
         RepositoryView view = (RepositoryView)
		               repFactory.getRepository(repositoryViewName);
         return view;
     }


     /**
      * Return the specific sqlInterface object for the 
      * current database name
      *
      * @param databaseName
      * @throws MappingException
      * 
      */
     protected SqlInterface getCurrentSqlInterface(String databaseName)
         throws MappingException{
         
	 	 String vendor = connectionManager.getConnectionSource(databaseName)
	                					  .getVendor();
	 	 return SqlInterfaceFactory.getSqlInterface(vendor);
     }
      
	 
     /**
      * Insert the object. It handles the following operations:
      * <li> get connection </li>
      * <li> key generator </li>
      * <li> create statement </li>
      * <li> execute statement </li>
      * <li> release connection </li>
      *
      * @param object object to delete
      * @return number of rows affected
      * @throws QueryException if errors occurs
      * @throws MappingException if error occurs
      * @throws KeyGenerationException if error occurs
      *
      */
      public int insert(ObjectMapped object) throws QueryException,
      												MappingException,
      						    					KeyGenerationException{
			           
		  logger.log(Level.INFO,Messages.message("Database.insert"));
		 
		  long initTime = System.currentTimeMillis();
		 
		  String repositoryViewName = object.getRepositoryViewName();
		  RepositoryView view = (RepositoryView)getRepository(repositoryViewName);
		  String databaseName = view.getBeanDescriptor().getDatabaseName();
		  PrimaryKeyMap pkMap = view.getBeanDescriptor().getPrimaryKeyMap();
		  ConnectionSource connSource = connectionManager.getConnectionSource(databaseName);
		  Connection connection = connectionManager.getConnection(databaseName);
		  addConnection(connection);
		  sqlInterface = getCurrentSqlInterface(databaseName);
		  int rows = 0;
		  
		  if(!pkMap.isComposite()){			
		  
		  	  String typeKeyGen = view.getBeanDescriptor().getGeneratorMap().getType();
		  	  Object oid = createOID(view,typeKeyGen,connection,connSource.getVendor());	  
		  	  object.setOID(oid);
		  }
	
		  //create insert statement
		  InsertStatement insertStat = new InsertStatement(view,sqlInterface);		
		  logger.log(Level.FINER,insertStat.toString());
						
		  //execute insert statement
		  rows = insertStat.insert(object,view,connection);
						
		  connectionManager.releaseConnection(databaseName);
		  
		  long endTime = System.currentTimeMillis();
		  long finalTime = endTime - initTime;
		
		  logger.log(Level.INFO,Messages.format("Database.rowsAffected",
		                                      String.valueOf(rows),
		                                      String.valueOf(finalTime)
		                                     )
		          );
						
		  return rows;
      }		
		

      /**
       * Rollback transaction of all databases affected 
       * in sql operations
       */
      public void rollbackTransaction(){
          try{
	          synchronized(this){
		  	      for(int i = 0; i < dbs.size(); i++){
		      		  Connection conn = (Connection)dbs.get(i);  
	              	  rollback(conn);
		  		  }
	      	  }
	  	  }
	  	  catch(Exception e){
	          logger.log(Level.SEVERE,e.getMessage());
	  	  }	      
      }

      
      /**
       * Rollback transaction.
       *
       *<b>Not use this method, you must use rollbackTransaction method!</b>
       *
       * @param connection to rollback
       * @throws TransactionException
       *
       */
      public void rollback(Connection connection) throws TransactionException{
          transaction.rollback(connection);
          logger.log(Level.INFO,Messages.message("Database.rollbackedTx"));
      }
	
	
      /**
       * Execute select statement.
       *
       * @param repositoryViewName is the name of repositoryView
       * @param criteria
       * @return QueryResults a collection of object
       * @throws SQLException
       * @throws MappingException
       *
       */
      public QueryResults select(String repositoryViewName,Criteria criteria)
          throws QueryException,MappingException,CacheException{
		    	
	 	  logger.log(Level.INFO,Messages.message("Database.select"));
	 
	  	  long initTime = System.currentTimeMillis();
	  	  
		  QueryResults results = null;
	  	  RepositoryView view = (RepositoryView) getRepository(repositoryViewName);		  
	  	  String databaseName = (view.getBeanDescriptor().getDatabaseName());
		  PrimaryKeyMap pkMap = view.getBeanDescriptor().getPrimaryKeyMap();
	  	  
  	      sqlInterface = getCurrentSqlInterface(databaseName);
  	      SelectStatement selectStat = new SelectStatement(view,null,criteria,sqlInterface); 
  	      logger.log(Level.FINER,selectStat.toString());		 
      	  	                 
          Connection connection = connectionManager.getConnection(databaseName);
          results = selectStat.select(repositoryViewName,view,connection);
          
		  //Put in cache the objects retrieved
		  while(results.next()){
		      ObjectMapped obj = results.getObject();
		      String cacheId = cacheManager.createId(pkMap,obj);
			  logger.log(Level.INFO,Messages.message("Database.putInCache"));		  	      		  	 
			  cacheManager.putInCache(cacheId,obj,databaseName,
									  repositoryViewName);
		  }
		  results.close();
          connectionManager.releaseConnection(databaseName);
		  		  	
	  	  long endTime = System.currentTimeMillis();
	  	  long finalTime = endTime - initTime;
		  
	  	  logger.log(Level.INFO,Messages.format("Database.rowsAffected",
	                                      String.valueOf(results.size()),
	                                      String.valueOf(finalTime)
	                                     )
	      );
	      
	  	  return results; 
      }
      
      
	  /**
	   * Execute select statement with primary key in where clause.
	   * 
	   * 
	   * @param repositoryViewName is the name of repositoryView
	   * @param primaryKey
	   * @return QueryResults a collection of object
	   * @throws SQLException
	   * @throws MappingException
	   * @see org.jdbf.engine.basic.PrimaryKey
	   */
	  public QueryResults selectByPrimaryKey(String repositoryViewName,PrimaryKey primaryKey)
		  throws QueryException,MappingException,CacheException{
	    	
		  logger.log(Level.INFO,Messages.message("Database.select"));
 
		  long initTime = System.currentTimeMillis();
		  
		  QueryResults results = null;
		  RepositoryView view = (RepositoryView) getRepository(repositoryViewName);
		  PrimaryKeyMap pkMap = view.getBeanDescriptor().getPrimaryKeyMap();
		  String region = view.getBeanDescriptor().getDatabaseName();
		  		  
		  //get object from cache
		  String cacheId = cacheManager.createId(primaryKey,pkMap);
		  
		  ObjectMapped object = cacheManager.getFromCache(cacheId,region,repositoryViewName);
  
		  //if object not exist in cache...
		  if(object == null){
			  
			  logger.log(Level.FINE,Messages.message("Database.cache"));  	  
		  	  //then executes select	  		 
		      SelectStatement selectStat = new SelectStatement(view,null,null,sqlInterface); 
		      //create criteria on primary keys
		      Criteria criteria = selectStat.createClauseOnPk(
		  									repositoryViewName,
		  									view,
		  									primaryKey);
		      results = select(repositoryViewName,criteria);
		  }
		  else{			  
			  ArrayList objs = new ArrayList();
			  objs.add(object);
			  results = new Cursor(objs);
			  long endTime = System.currentTimeMillis();
			  long finalTime = endTime - initTime;
		  
			  logger.log(Level.INFO,Messages.format("Database.rowsAffected",
													String.valueOf(results.size()),
													String.valueOf(finalTime)
												   )
					  );
		  }
		  	  						
		  return results; 		   
	  }
  

      /**
	   * Update the object. It handles the following operations:
	   * <li> get connection </li>
	   * <li> create statement </li>
	   * <li> execute statement </li>
	   * <li> release connection </li>
	   *
	   * @param object object to delete
	   * @param propertiesNames name of proeprties to update
	   * @return number of rows affected
	   * @throws QueryException
	   * @throws MappingException
	   *
	   */
       public int update(ObjectMapped object,String[] propertiesNames) 
           throws QueryException,MappingException,CacheException{
           
           logger.log(Level.INFO,Messages.message("Database.update"));
	 
	   	   long initTime = System.currentTimeMillis();
    	
	   	   String repositoryViewName = object.getRepositoryViewName();
	   	   RepositoryView view = (RepositoryView) getRepository(repositoryViewName);
	   	   PrimaryKeyMap pkMap = view.getBeanDescriptor().getPrimaryKeyMap();
	       String databaseName = view.getBeanDescriptor().getDatabaseName();
	       Connection connection = connectionManager.getConnection(databaseName);
	       addConnection(connection);
           sqlInterface = getCurrentSqlInterface(databaseName);
	       int rows = 0;
				
	       //create statement
	       UpdateStatement updateStat = new UpdateStatement(view,propertiesNames,null,sqlInterface);
	       logger.log(Level.FINER,updateStat.toString());
					
	       //execute statement
	       rows = updateStat.update(object,view,connection,propertiesNames);
					
	       //release connection
	       connectionManager.releaseConnection(databaseName);
	   
	       long endTime = System.currentTimeMillis();
	       long finalTime = endTime - initTime;
	
	       logger.log(Level.INFO,Messages.format("Database.rowsAffected",
	                                      String.valueOf(rows),
	                                      String.valueOf(finalTime)
	                                     )
	             );
	             
	       String cacheId = createCacheId(pkMap,object);
	       cacheManager.replaceObject(cacheId,object,
	                                  databaseName,repositoryViewName);					
	       return rows;
       }


       /**
	    * Update the object. It handles the following operations:
	    * <li> get connection </li>
	    * <li> create statement </li>
	    * <li> execute statement </li>
	    * <li> release connection </li>
	    *
	    * @param repositoryViewName name of repository
	    * @param propertiesNames name of properties to update
	    * @param values of propertiesNames
	    * @param criteria criteria for update
	    * @return number of rows affected
	    * @throws QueryException
	    * @throws MappingException
	    *
	    */
	   public int updateForCriteria(String repositoryViewName,
	                                String[] propertiesNames,Object[] values,
			    	                Criteria criteria) 
	       throws QueryException,MappingException{
    	
    	   logger.log(Level.INFO,Messages.message("Database.update"));
	 
	       long initTime = System.currentTimeMillis();
	    
           RepositoryView view = (RepositoryView) getRepository(repositoryViewName);
	       String databaseName = view.getBeanDescriptor().getDatabaseName();
	       Connection connection = connectionManager.getConnection(databaseName);
	       addConnection(connection);
           sqlInterface = getCurrentSqlInterface(databaseName);
	       int rows = 0;
					
	       //create statement
	       UpdateStatement updateStat = new UpdateStatement(view,propertiesNames,
							     criteria,sqlInterface);
	       logger.log(Level.FINER,updateStat.toString());
					
	       //execute statement
	       rows = updateStat.updateForCriteria(view,connection,propertiesNames,values);
					
	       connectionManager.releaseConnection(databaseName);
	    
	       long endTime = System.currentTimeMillis();
	       long finalTime = endTime - initTime;
	
	       logger.log(Level.INFO,Messages.format("Database.rowsAffected",
	                                      String.valueOf(rows),
	                                      String.valueOf(finalTime)
	                                     )
	              );
					
	       return rows;
        }
}
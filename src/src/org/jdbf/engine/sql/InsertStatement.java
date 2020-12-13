/*
 * 07/05/2002 - 23:46:45
 *
 * $RCSfile: InsertStatement.java,v $ - JDBF Object Relational mapping system
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

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.jdbf.engine.basic.ObjectMapped;
import org.jdbf.engine.mapping.*;
import org.jdbf.engine.repository.*;
import org.jdbf.castor.Messages;

/**
 * <code>InsertStatement</code> is that  class that represents the insert sql
 * statement.
 * InsertStatement handles the creation of sql statement with the informations
 * specified in a RepositoryView object and provides to execute the insert statement. 
 *
 */
 public class InsertStatement extends SQLStatement{

	/**
	 * Create the object and build the insert statement.
	 *
	 * @param repository
	 * @param sqlInterface
	 * 
	 */
	public InsertStatement(RepositoryView repository,
	                       SqlInterface sqlInterface){
		logger = Logger.getLogger(className);
		buildStatement(repository,null,sqlInterface);
	}

	
	/**
	 * Creates the empty object
	 */
	public InsertStatement(){
		logger = Logger.getLogger(className);
		statement = "";
	}

	
	/**
	 * Builds the insert statement with the informations 
	 * passed by repository.<br<
	 * These informations are:
	 * <li> tableName;
	 * <li> columnTableName
	 *
	 * @param repository
	 * @param propertiesNames name of properties it may be null,
	 * @param sqlInterface         	  
	 * @return String insert statement
	 *
	 */
	public void buildStatement(RepositoryView repository,
			                   String[] propertiesNames,
	                           SqlInterface sqlInterface){
		
		logger.log(Level.INFO,Messages.message("Statement.build"));
		BeanDescriptor beanDescriptor = repository.getBeanDescriptor();
		String tableName = beanDescriptor.getTableName();
		ArrayList pks = (ArrayList)beanDescriptor.getPrimaryKeyMap()
		                                         .getPrimaryKey();
		ArrayList items = beanDescriptor.getItemDescriptors();
		ArrayList itemDescriptors = this.unionItemDescriptors(pks,items);
		
		statement = sqlInterface.getInsertStatement(tableName,
	                              parseFields(itemDescriptors),
			              parseValues(itemDescriptors));
		logger.log(Level.INFO,Messages.format("Statement.statement",
			   statement));
	 }


	/**
	 * Not implemented
	 */
	public void buildStatementForCriteria(RepositoryView repository,
					      				  String[] propertiesNames,
	                                      SqlInterface sqlInterface){
	}


	/**
	 * Executes the insert statement and store in database the ObjectMapped 
	 * object specified in obj.
	 *
	 * @param obj object to store
	 * @param view contains the informations about property of object mapped
	 * @param connection AbstractConnection object.
	 * @return int number of rows affected
	 * @throws QueryException if error occurs
	 * 
	 */
	public int insert(ObjectMapped obj,RepositoryView view,
	                  Connection connection)
		throws QueryException{
	 
	    int rows = 0;
	    try{
	    
			//Build insert statement
			PreparedStatement preStat = connection.prepareStatement(statement);
		     
			//Get the info on the repository
			BeanDescriptor beanDescriptor = view.getBeanDescriptor();
			PrimaryKeyMap pks = beanDescriptor.getPrimaryKeyMap();
			
			ArrayList itemDescriptors = new ArrayList(pks.getPrimaryKey());
			
			itemDescriptors.addAll(beanDescriptor.getItemDescriptors());
			
			Object params[] = new Object[itemDescriptors.size() + 1 ];
			
			String paramsStr = "";
			params[0] = statement;
			
			
			for(int i = 0; i < itemDescriptors.size(); i++){
		 		
				ItemDescriptor item = (ItemDescriptor)itemDescriptors.get(i);
				
		 		String propertyName = item.getPropertyName();
				
		  		String dataType = item.getDataType();	
							
		  		Object propertyValue = view.getPropertyValue(obj,
								propertyName);
				
		 		int types = org.jdbf.engine.sql.Types.getSQLType(dataType);
				
				if(propertyValue == null){					
					propertyValue = org.jdbf.castor.Types.getDefault
									(item.getDataType());					
				}
				
	 	 		preStat.setObject(i + 1,propertyValue,types);
				
				params[i + 1] = propertyValue;
			}	 
			
			rows = preStat.executeUpdate();
			
			preStat.close();
			
			logger.log(Level.INFO,Messages.format("Statement.ExecuteFirstHalf",
				                               statement));
			/*loop to get value of params.
			 * It is needed because i don't know the length of paramters
			 * that are used in insert statement
			 */
			paramsStr = parseParameters(params);
			
	
			logger.log(Level.INFO,Messages.format("Statement.ExecuteSecondHalf",
				                               paramsStr));
			logger.log(Level.INFO,Messages.format("Statement.rows",
				                              new Integer(rows)));
	    }
	    catch(SQLException e){
	    	logger.throwing(className,"insert()",
	    	                new QueryException(statement,e.getMessage()));
	    	
	    	throw new QueryException(statement,e.getMessage());
	    	      
	    }
		catch(MappingException e){
			logger.throwing(className,"insert()",
							new QueryException(statement,e.getMessage()
									  )
							);
			throw new QueryException(statement,e.getMessage());
		}
		
		return rows;
	}
}
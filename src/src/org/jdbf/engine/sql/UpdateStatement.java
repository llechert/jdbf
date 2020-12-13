/*
 * 07/05/2002 - 23:46:45
 *
 * $RCSfile: UpdateStatement.java,v $ - JDBF Object Relational mapping system
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
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;

import org.jdbf.engine.basic.ObjectMapped;
import org.jdbf.engine.criteria.*;
import org.jdbf.engine.mapping.*;
import org.jdbf.engine.repository.*;
import org.jdbf.castor.Messages;
import org.jdbf.engine.sql.Types;

/**
 * <code>UpdateStatement</code> is that  class that represents the update sql
 * statement.
 * UpdateStatement handles the creation of sql statement with the informations
 * specified in a RepositoryView object and provides to execute the update statement.
 *
 */
 public class UpdateStatement extends SQLStatement{


	/**
	 * Creates the UpdateStatement object and build the statement
	 *
	 * @param view repository
	 * @param propertiesNames names of properties to update
	 * @param criteria
	 * @param sqlInterface
	 *
	 */
	public UpdateStatement(RepositoryView view,String[] propertiesNames,
			               Criteria criteria,SqlInterface sqlInterface){
		super(view,propertiesNames,criteria,sqlInterface);		
	}
	
	/**
	 * Creates the empty object
	 */
	public UpdateStatement(){
		super();
	}

	/**
	 * Builds the update statement with the informations passed by repository.
	 *
	 * These informations are:
	 * <li> tableName;
	 * <li> columnTableName
	 *
	 * @param repository
	 * @param propertiesNames name of properties
	 * @param sqlInterface
	 * @return String update statement
	 * 
	 */
	public void buildStatement(RepositoryView repository,
	                           String[] propertiesNames,
			                   SqlInterface sqlInterface){
		logger.log(Level.INFO,Messages.message("Statement.build"));
		//build update statement		
		BeanDescriptor beanDescriptor = repository.getBeanDescriptor();
		String tableName = beanDescriptor.getTableName();
		String repositoryViewName = beanDescriptor.getRepositoryViewName();
		
		StringBuffer condition = new StringBuffer();
		Criteria criteria = createClauseOnPk(repositoryViewName,
											  repository
											  );
		 condition.append(SqlInterface.WHERE)
		          .append(criteria.getCriteria());
		 List items = new ArrayList();
		 for(int i = 0; i < propertiesNames.length; i++){
			 String propertyName = propertiesNames[i];
			 String columnName = beanDescriptor
									.getItemDescriptorFromName(propertyName)
									.getColumnTableName();
			 items.add(columnName);
		 }	
		 statement = sqlInterface.getUpdateStatement(tableName,parseFields(items),
			                            condition.toString());
		 logger.log(Level.INFO,Messages.format("Statement.statement",statement));
	 }

	
	/**
	 * Builds the delete statement with criteria.
	 *
	 * @param repository
	 * @param propertiesNames
	 * @return String delete statement
	 * 
	 */
	public void buildStatementForCriteria(RepositoryView repository,
			                              String[] propertiesNames,
					                      SqlInterface sqlInterface){
		logger.log(Level.INFO,Messages.message("Statement.build"));
		//build update statement
		BeanDescriptor beanDescriptor = repository.getBeanDescriptor();
		String tableName = beanDescriptor.getTableName();
				
		StringBuffer condition = criteria.getCriteria();
		if(condition.length() > 0)
			condition.insert(0,WHERE);
		List items = new ArrayList();
		for(int i = 0; i < propertiesNames.length; i++){
			String propertyName = propertiesNames[i];
			String columnName = beanDescriptor
									.getItemDescriptorFromName(propertyName)
									.getColumnTableName();
			items.add(columnName);
		}
		statement = sqlInterface.getUpdateStatement(tableName,parseFields(items),
			                                condition.toString());
		logger.log(Level.INFO,Messages.format("Statement.statement",statement));
	}
	
	
	/**
	 * Executes the update statement and updates in database the ObjectMapped object specified in obj.
	 *
	 * @param obj object to delete
	 * @param view contains the informations about property of object mapped,
	 * @param connection Connection object
	 * @param propertiesNames names of property to update.
	 * @return rows number of rows affected
	 * @throws QueryException if error occurs
	 * 
	 */
	public int update(ObjectMapped obj,RepositoryView view,Connection connection,
		      	      String[] propertiesNames)
		throws QueryException{
	 
		int rows = 0;
		
		try{
		
			PreparedStatement preStat = connection.prepareStatement(statement);
		 
			//Get the info on the repository
			BeanDescriptor beanDescriptor = view.getBeanDescriptor();
			
			PrimaryKeyMap pkMap = beanDescriptor.getPrimaryKeyMap();
			ArrayList pks = (ArrayList)pkMap.getPrimaryKey();
			
			ArrayList items = beanDescriptor.getItemDescriptors();
			ArrayList itemDescriptors = unionItemDescriptors(pks,items);
			
			ItemDescriptor item = null;
			
			String propertyName = "";
			String dataType = "";
			Object propertyValue = null;
			int length = propertiesNames.length;
	        String paramsStr = "";
			Object params[] = new Object[itemDescriptors.size() + 1];
			params[0] = statement;
			for(int i = 0; i < length; i++){
					    
				for(int j = 0; j < itemDescriptors.size(); j++){		    			
					item = (ItemDescriptor)itemDescriptors.get(j);
					propertyName = item.getPropertyName();
						
					if(propertiesNames[i].equals(propertyName)){
						if(item.isPrimaryKey()){
							
							logger.throwing(className,"update()",
											new QueryException(statement,
															   Messages.message("sql.primaryKeyUpdate")
															   )
										   );	    	
							throw new QueryException(statement,
													 Messages.message("sql.primaryKeyUpdate")
													);	
						}
						else{					
							dataType = item.getDataType();
							propertyValue = view.getPropertyValue(obj,propertyName);										
							int types = Types.getSQLType(dataType);
	 
							if(propertyValue == null){					
								propertyValue = 
									org.jdbf.castor.Types.getDefault(item.getDataType());
							}
							preStat.setObject(i + 1,propertyValue,types);
							params[i + 1] = propertyValue;
						}
									
					}
					
					
				}
			}			
		
			//WHERE clause on primary key
			for(int i = 0; i < pks.size(); i++){			
				int columnIndex = i + 1;
				item = (ItemDescriptor)pks.get(i);
			   
				propertyName = item.getPropertyName();
				dataType = item.getDataType();
				propertyValue = view.getPropertyValue(obj,propertyName);
				int types = org.jdbf.engine.sql.Types.getSQLType(dataType);
				preStat.setObject(length + columnIndex,propertyValue,types);
				params[itemDescriptors.size()] = propertyValue;				
			}				
			rows = preStat.executeUpdate();
			preStat.close();
			logger.log(Level.INFO,Messages.format("Statement.ExecuteFirstHalf",
				                               statement));
			/*loop to get value of params.
			 * It is needed because i don't know the length of paramters
			 * that are used in update statement
			 */
			paramsStr = parseParameters(params);
	
			logger.log(Level.INFO,Messages.format("Statement.ExecuteSecondHalf",
				                               paramsStr));
			logger.log(Level.INFO,Messages.format("Statement.rows",
				                              new Integer(rows)));
		}
		catch(SQLException e){
			logger.throwing(className,"update()",
							new QueryException(statement,e.getMessage()));
			throw new QueryException(statement,e.getMessage());	    	     
		}
		catch(MappingException e){
			logger.throwing(className,"update()",
							new QueryException(statement,e.getMessage())
						   );
			throw new QueryException(statement,e.getMessage());
		}				                         
		return rows;
	}
	 

	/**
	 * Executes the update statement and updates in database with sql criteria.
	 *
	 * @param view contains the informations about property of object mapped,
	 * @param connection Connection object
	 * @param propertiesNames names of property to update
	 * @param values values of propertiesNames
	 * @return rows number of rows affected
	 * @throws QueryException
	 * 
	 */
	public int updateForCriteria(RepositoryView view,Connection connection,
		                 String[] propertiesNames,Object[] values)
		throws QueryException{
	 
		int rows = 0;
		try{
		
			PreparedStatement preStat = connection.prepareStatement(statement);
		 
			//Get the info on the repository
			BeanDescriptor beanDescriptor = view.getBeanDescriptor();
			PrimaryKeyMap pkMap = beanDescriptor.getPrimaryKeyMap();
			ArrayList pks = (ArrayList)pkMap.getPrimaryKey();
			ArrayList items = beanDescriptor.getItemDescriptors();
			ArrayList itemDescriptors = unionItemDescriptors(pks,items); 
			ItemDescriptor item = null;
			
			String propertyName = "";
			String dataType = "";
			Object propertyValue = null;
			int length = propertiesNames.length;
	        String paramsStr = "";
			Object params[] = new Object[itemDescriptors.size()];
			for(int i = 0; i < length; i++){
		 	   	
				for(int j = 0; j < itemDescriptors.size(); j++){		    			
					
					item = (ItemDescriptor)itemDescriptors.get(j);
					propertyName = item.getPropertyName();
					
					if(propertiesNames[i].equals(propertyName)){				
						 
						 if(item.isPrimaryKey() && !pkMap.isComposite()){
							
							logger.throwing(className,"update()",
											new QueryException(statement,
											                   Messages.message("sql.primaryKeyUpdate")
											                   )
										   );	    	
							throw new QueryException(statement,
							                         Messages.message("sql.primaryKeyUpdate")
							                        );	
						 }
						 else{						 
						 
						 	dataType = item.getDataType();
						 	propertyValue = values[i];
						 
						 	int types = Types.getSQLType(dataType);
						
						 	preStat.setObject(i + 1,propertyValue,types);
						 }
					}
				}
			}
			rows = preStat.executeUpdate();
			preStat.close();
			logger.log(Level.INFO,Messages.format("Statement.ExecuteFirstHalf",
				                               statement));
			/*loop to get value of params.
			 * It is needed because i don't know the length of paramters
			 * that are used in update statement
			 */
			paramsStr = parseParameters(params);
	
			logger.log(Level.INFO,Messages.format("Statement.ExecuteSecondHalf",
				                               params));
			logger.log(Level.INFO,Messages.format("Statement.rows",
				                              new Integer(rows)));
		}
		catch(SQLException e){
			logger.throwing(className,"update()",
							new QueryException(statement,e.getMessage()));	    	
			throw new QueryException(statement,e.getMessage());	    	     
		}
		catch(MappingException e){
			logger.throwing(className,"update()",
							new QueryException(statement,e.getMessage())
						   );
			throw new QueryException(statement,e.getMessage());
		}					                             
		return rows;
	}


	/**
	 * Return a list of fields
	 *  
	 *  @param item
	 *  @return String
	 *
	 */
	protected String parseFields(List item){
             
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < item.size(); i++){
			buff.append(item.get(i)).append(" = ?");
			if(i < item.size() - 1)
				buff.append(",");
		}
		return buff.toString();
	}
}
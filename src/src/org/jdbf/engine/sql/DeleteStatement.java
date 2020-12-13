/*
 * 07/05/2002 - 23:46:45
 *
 * $RCSfile: DeleteStatement.java,v $ - JDBF Object Relational mapping system
 * Copyright (C) 2002 JDBF Developer Team
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

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

import org.jdbf.engine.basic.ObjectMapped;
import org.jdbf.engine.criteria.*;
import org.jdbf.engine.mapping.*;
import org.jdbf.engine.repository.*;

/**
 * <code>DeleteStatement</code> is that  class that represents the delete sql
 * statement.
 * DeleteStatement handles the creation of sql statement with the informations
 * specified in a RepositoryView object and provides to execute the delete statement.<br> 
 *
 */
 public class DeleteStatement extends SQLStatement{		
	/**
	 * Create the object and build the delete statement.
	 *
	 * @param repository 
	 * @param criteria is Criteria object, it may be null
	 * @param sqlInterface
	 */
	public DeleteStatement(RepositoryView repository,Criteria criteria,
			               SqlInterface sqlInterface){
		super(repository,null,criteria,sqlInterface);
    }

	/**
	 * Creates the empty object
	 */
	public DeleteStatement(){
		statement = "";
		criteria = null;
	}

	/**
	 * Builds the delete statement with the informations passed 
	 * by repository.<br>
	 * These informations are:
	 * <li> tableName;
	 * <li> columnTableName
	 *
	 * @param repository
	 * @param propertiesNames name of properties it may be null
	 * @param sqlInterface
	 * 
	 */
	public void buildStatement(RepositoryView repository,
			                   String[] propertiesNames,
				   			   SqlInterface sqlInterface){
	    BeanDescriptor beanDescriptor = repository.getBeanDescriptor();
	    String tableName = beanDescriptor.getTableName();
	    
	    String repositoryViewName = beanDescriptor.getRepositoryViewName();
	    ItemDescriptor item = beanDescriptor.getItemDescriptorFromName("OID");
	    StringBuffer condition = new StringBuffer();
	    Criteria criteria = createClauseOnPk(repositoryViewName,
										     repository
										    );
		condition.append(WHERE).append(criteria.getCriteria());
		statement = sqlInterface.getDeleteStatement(tableName,condition.toString());
	}


	/**
	 * Builds the delete statement with criteria.
	 *
	 * @param repository
	 * @param propertiesNames
	 * @param sqlInterface
	 * 
	 */
	public void buildStatementForCriteria(RepositoryView repository,
			                              String[] propertiesNames,
					                      SqlInterface sqlInterface){
		
		BeanDescriptor beanDescriptor = repository.getBeanDescriptor();
	    String tableName = beanDescriptor.getTableName();
	        
		StringBuffer condition = criteria.getCriteria();
		if(condition.length() > 0)
		    condition.insert(0,WHERE);
        statement = sqlInterface.getDeleteStatement(tableName,condition.toString()); 
	}


	/**
	 * Executes the delete statement and delete in database 
	 * the ObjectMapped object specified in obj.
	 *
	 * @param obj object to delete
	 * @param view contains the informations about property of object mapped
	 * @param con Connection object.
	 * @return int number of rows affected
	 * @throws QueryException if error occurs
	 * 
	 */
	 public int delete(ObjectMapped obj,RepositoryView view,
	                   Connection con)
     	throws QueryException{
	 
	 	int rows = 0;
	 	
	 	try{	 	
		 	//Build insert statement
			PreparedStatement preStat = con.prepareStatement(statement);
		 
		 	//Get the info on the repository
		 	BeanDescriptor beanDescriptor = view.getBeanDescriptor();
		 	ArrayList itemDescriptors = beanDescriptor.getItemDescriptors();
		 	PrimaryKeyMap pk = beanDescriptor.getPrimaryKeyMap();
		 	List pks = pk.getPrimaryKey();
		 	for(int i = 0; i < pks.size();i++){
		 	
		 		int columnIndex = i + 1;
		 		ItemDescriptor item = (ItemDescriptor)pks.get(i);
		    	String propertyName = item.getPropertyName();
				String dataType = item.getDataType();
				Object propertyValue = view.getPropertyValue(obj,propertyName);
				int types = org.jdbf.engine.sql.Types.getSQLType(dataType);
				preStat.setObject(columnIndex,propertyValue,types);
		 	}
		    rows = preStat.executeUpdate();
			preStat.close();
	 	}
	 	catch(SQLException e){
	 		logger.throwing(className,"delete()",
	 		                new QueryException(statement,e.getMessage()
	 		                 )
	 		                );
	 		throw new QueryException(statement,e.getMessage());
	 	}
	 	catch(MappingException e){
			logger.throwing(className,"delete()",
							new QueryException(statement,e.getMessage()
							  )
							);
			throw new QueryException(statement,e.getMessage());
	 	}	    
	    return rows;
	 }


	 /**
	  * Executes the delete statement and delete in database with the specific 
	  * criteria.
	  *
	  * @param con Connection object.
      *
	  * @return int number of rows affected
	  * @throws QueryException if error occurs.
	  * 
	  */
	 public int delete(Connection con)
	 	throws QueryException{
	 
	    int rows = 0;
	    try{	    
	    	//Build insert statement
	    	PreparedStatement preStat = con.prepareStatement(statement);	 	 	
	    	rows = preStat.executeUpdate();
	    	preStat.close();
	    }
	    catch(Exception e){
			logger.throwing(className,"delete()",
							new QueryException(statement,
							                   e.getMessage()
							  )
							);
			throw new QueryException(statement,e.getMessage());
						
	    }
	    return rows;
	 }
}

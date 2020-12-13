/*
 * 07/05/2002 - 23:46:45
 *
 * SelectStatement.java - JDBF Object Relational mapping system
 * Copyright (C) 2002 Giovanni Martone
 * giovannimartone@hotmail.com
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
import org.jdbf.engine.basic.ObjectMapped;
import org.jdbf.engine.caching.CacheManager;
import org.jdbf.engine.criteria.Criteria;
import org.jdbf.engine.mapping.*;
import org.jdbf.engine.reflection.*;
import org.jdbf.engine.repository.*;
import org.jdbf.engine.sql.*;
import org.jdbf.engine.sql.connection.*;

/**
 * <code>SelectStatement</code> is that  class that represents the select sql
 * statement.
 * SelectStatement handles the creation of sql statement with the informations
 * specified in a RepositoryView object and provides to execute the select statement.
 *
 * @author Giovanni Martone
 * @version "Revision": "Date":
 *
 */
 public class SelectStatement extends SQLStatement{

    /** SELECT statement */
    // Fixed Bug: #632320 (Giovanni Martone)
	protected final static String SELECT = SqlInterface.SELECT_ALL;
    
    /** FROM statement */
    protected final static String FROM = SqlInterface.FROM;
 	
		
    /**
     * Creates the SelectStatement object and build the statement
     *
     * @param view repository, propertiesNames names of properties to update
     */
    public SelectStatement(RepositoryView view,String[] propertiesNames,
		           Criteria criteria){
        super(view,null,criteria);	   
    }

	 
    /**
     * Creates the empty object
     */
    public SelectStatement(){
       super();
    }

	
    /**
     * Builds the select statement with the informations passed by repository.
     * These informations are:
     * <li> tableName;
     * <li> columnTableName
     *
     * @param repository, propertiesNames name of properties to select
     * @return String select statement
     */
    public String buildStatement(RepositoryView repository,String[] propertiesNames){
        try{
	    StringBuffer buff = new StringBuffer(SELECT);
	    BeanDescriptor beanDescriptor = repository.getBeanDescriptor();
	    ArrayList items = beanDescriptor.getItemDescriptors();
	    String tableName = beanDescriptor.getTableName();
	    buff.append(FROM).append(tableName);

	    if(criteria != null){
	        int length = criteria.getCriteria().length();				
		if(length > 0){
		    buff.append(" ").append(WHERE);			
		    buff.append(criteria.getCriteria());
		}
		ArrayList order = (ArrayList)criteria.getOrderConditions();			
		if(order.size() > 0)
		    buff.append(" ").append(criteria.getOrderByClause(items));
		}
		statement = buff.toString();
	}
	catch(MappingException e){
	    e.printStackTrace();
	}
	finally{
	    return statement;
	}		
    }


    public String buildStatementForCriteria(RepositoryView repository,String[] propertiesNames){
        return buildStatement(repository,null);
    }



    /**
     * Executes the select statement and retunathe ObjectMapped object specified in obj.
     *
     * @param String repositoryViewName, view contains the informations about property of object mapped,
     *        con Connection object,values arrays of values to search.
     *
     * @return QueryResults set of results
     * @throws SQLException,MappingException if datatype is invalid or table not exist.
     */
    public QueryResults select(String repositoryViewName,RepositoryView view,
	                       Connection connection)
        throws SQLException,MappingException{
	 
	 	
	StringBuffer identifier  = new StringBuffer(repositoryViewName);
	     
	String hashCode = null;	     
	if(criteria == null){
	    hashCode = "all";
	}
	else{
	    hashCode = String.valueOf(criteria.hashCode());
	}
        identifier.append(hashCode);    
	     
	//get object from cache
	QueryResults queryResults = (QueryResults)CacheManager.getCache(
	                             identifier.toString()
				    );
	if(queryResults == null){
                
	    //Build select statement
	    PreparedStatement preStat = connection.prepareStatement(statement);		
	    ResultSet results = preStat.executeQuery();		 
	    queryResults = new Cursor(getValuesFromResultSet(view,results),
		                      identifier.toString()
				     );
	    CacheManager.putCache(queryResults);
	    preStat.close();
	}   
	return queryResults;
    }


    /**
     * Get results of select statement from ResultSet. 
     *
     * It creates an instance of ObjectMapped object. Type of ObjectMapped 
     * is specified in repository, then it sets all properties of this 
     * object with the values 
     *
     * @param view RepositoryView object, results ResultSet object
     * @return ArrayList an collection of ObjectMapped
     * @throws SQLException,MappingException
     */
    protected ArrayList getValuesFromResultSet(RepositoryView view,ResultSet results) 
        throws SQLException,MappingException{
	 			
	ArrayList res = new ArrayList();
		
	BeanDescriptor beanDesc = view.getBeanDescriptor();
	ArrayList itemDesc = beanDesc.getItemDescriptors();
	java.util.HashMap props = new java.util.HashMap();
	props.put("repositoryViewName", beanDesc.getRepositoryViewName());
		
	//loop ResultSet
	while(results.next()){							
	    ObjectMapped map = ReflectionManager.createBean(beanDesc.getClassName(),props);
	    
	    //get values from ResultSet
	    for(int i = 0; i < itemDesc.size(); i++){
	        ItemDescriptor item = (ItemDescriptor)itemDesc.get(i);
		String propertyName = item.getPropertyName();
		String columnTableName = item.getColumnTableName();
		Object propertyValue = results.getObject(columnTableName);
		map = view.setPropertyValue(map,propertyName,propertyValue);				
	    }			
	    res.add(map);		    
	}		
        results.close();                        			                       
        return res;                       			 
    }
}

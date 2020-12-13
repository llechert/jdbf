/*
 * 25/10/2002 - 09:39:11
 *
 * $RCSfile: Criteria.java,v $ - JdbF Object Relational mapping system
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

package org.jdbf.engine.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.jdbf.castor.Messages;
import org.jdbf.engine.sql.SqlInterface;
import org.jdbf.engine.repository.RepositoryFactory;
import org.jdbf.engine.repository.RepositoryView;
import org.jdbf.engine.mapping.ItemDescriptor;
import org.jdbf.engine.mapping.PrimaryKeyMap;
import org.jdbf.engine.mapping.MappingException;


/**
 * <Code>Criteria</code> represents the condition that
 * can be added to WHERE keyword in sql statement.<br>
 * To add a criteria, you must invoke addXXX where XXX 
 * is the condition 
 * i.e. addSelectGreaterThan(propertyName,value) 
 * add "propertyName > value" to sql statement.<br>
 * To add nested condition you must invoke your method 
 * addXXX and then you invoke addOrCriteria or addAndCriteria.<br>
 * 
 */
public class Criteria implements Cloneable{
    	        
    private boolean distinct;    
    protected int hashCode;    
    protected List orderConditions;	
    protected StringBuffer criteria;
    protected String className;
    protected String repositoryName;
    protected ArrayList itemDescriptors;
    protected Logger logger;
    protected PrimaryKeyMap pk;

    
	
    /**
     * Creates a object given repositoryViewName
     * 
     * @param repositoryName
     * 
     */
    public Criteria(String repositoryName){
        
        this.repositoryName = repositoryName;
        	
        itemDescriptors = (ArrayList)RepositoryFactory.getItemDescriptors(repositoryName);
	    criteria = new StringBuffer();
	    className = this.getClass().getName();		
	    orderConditions  = new ArrayList();
	    hashCode = -1;
	    logger = Logger.getLogger(className);
	    RepositoryView view = (RepositoryView)RepositoryFactory.getRepositoryFromName(repositoryName);
	    pk = view.getBeanDescriptor().getPrimaryKeyMap();
    }


    /**
     * Set a List of ItemDescriptor
     *
     * @param list
     *
     */
    public void setItemDescriptors(ArrayList list){
	    itemDescriptors = list;
    }

	
    /**
     * Return column name for a attribute name specified 
     * in attrName
     *
     * @param attrName attirbute name of object
     * @return String column name
     *
     */
    public String getColumnTableName(String attrName){
	    String columnName = null;
	    
	    List keys = pk.getPrimaryKey();
	    int size = keys.size();
	    for(int i = 0; i < size; i++){
			ItemDescriptor item = (ItemDescriptor)keys.get(i);
			if(attrName.equals(item.getPropertyName())){
				columnName = item.getColumnTableName();
				return columnName;
			}
	    }
	    
	    size = itemDescriptors.size();
	    for (int i = 0; i < size; i++ ){
	        ItemDescriptor item = (ItemDescriptor)itemDescriptors.get(i);
	        if(attrName.equals(item.getPropertyName())){
		        columnName = item.getColumnTableName();
				return columnName;
	        }
	    }
	    return columnName;
    }

    
    /**
     * Creates and returns a copy of this object.
     *
     * @return the copy of this object
     *
     */
    public Object clone(){
      	Criteria obj = null;
		try {
			obj = (Criteria)super.clone();
			obj.criteria = new StringBuffer();			
			obj.orderConditions = new ArrayList();
			obj.orderConditions.addAll(this.orderConditions);
		}
		catch (CloneNotSupportedException e) {
			// Cannot happen -- we support 
			// clone, and so does ArrayList
		}
		return obj;	
    }
	
	
	/**
	 * Indicates whether some other object is "equal to" this 
	 * one. 
	 *
	 * @param obj - the reference object with which to compare.
	 * @return true if this object is the same as the obj argument; 
	 * false otherwise.
	 *
	 */
	public boolean equals (Object obj){
		if (obj == null 
			|| !(getClass().equals(obj.getClass())))
			return false;
		
		Criteria criteriaC = (Criteria)obj;
		if (distinct != criteriaC.distinct
			|| criteria != criteriaC.criteria
			|| orderConditions.size() != criteriaC.orderConditions.size()
			|| !repositoryName.equals(criteriaC.repositoryName)
			)
			return false;

		
		return orderConditions.equals(criteriaC.orderConditions);				
    }
	
	
	/**
	 * Return the repositoryName attribute
	 *
	 * @return repositoryName
	 *
	 */
	public String getRepositoryName(){
        return repositoryName;
    }
	
	
	/**
	 * Returns a hash code value for the object.
	 *
	 * @return a hash code value for this object.
	 *
	 */
	public int hashCode(){
		if (hashCode == -1) {
			hashCode = repositoryName.hashCode();
			hashCode += criteria.hashCode();
			hashCode += orderConditions.size();
			if (distinct)
				hashCode++;
			
			for (int i=0; i<orderConditions.size(); i++)
				hashCode += orderConditions.get(i).hashCode();

		}
		return hashCode;   
    }

    	            	    
	/**
	 * Sets a new value fo repositoryName attribute.
	 *
	 * @param repositoryName class name 
	 *
	 */
	protected void setRepositoryName(String repositoryName){
        this.repositoryName = repositoryName;
    }

    /**
     * Return a list of condition "ORDER BY"
     * @return List
     */
	public List getOrderConditions(){
		return orderConditions;
	}

  	/**
  	 * Return true if isDistinct,false otherwise
  	 * 
  	 * @return boolean
  	 */
	public boolean isDistinct(){
		return distinct;
	}

    /**
     * Set flag distinct
     * 
     * @param distinct
     */
    public void setDistinct(boolean distinct){
		this.distinct = distinct;
	}

	/**
	 * Return conditions
	 * 
	 * @return StringBuffer
	 */
	public StringBuffer getCriteria(){
		return criteria;
	}

    /**
     * Add condition given propertyName and condition.
     * 
     * You don't use this method directly, because it 
     * is used by addXXX method.
     * 
     * @param attributeName
     * @param condition
     */	
    public void addCondition(String attributeName, String condition){        
		criteria.append(attributeName).append(condition);		
    }

	/**
	 * Add condition given propertyName, condition and value.
	 * 
	 * You don't use this method directly, because it 
	 * is used by addXXX method.
	 * 
	 * @param attributeName
	 * @param operator
	 * @param value
	 */	
    public void addCondition(String attributeName, String operator, Object value){
		
		criteria.append(attributeName).append(" ").append(operator).append(" ");
		if(value instanceof String)
			criteria.append("'").append(value).append("'");
		else
			criteria.append(value);
		logger.log(Level.FINEST,Messages.format("Criteria.addCondition",criteria));					
    }
    
	

	/**
	 * Add condition given propertyName, condition and boolean value.
	 * 
	 * You don't use this method directly, because it 
	 * is used by addXXX method.
	 * 
	 * @param attributeName
	 * @param operator
	 * @param value
	 */	
    public void addCondition(String attributeName, String operator, boolean value){
		addCondition(attributeName, operator, new Boolean(value));
    }
	
	/**
	 * Add condition given propertyName, condition and double value.
	 * 
	 * You don't use this method directly, because it 
	 * is used by addXXX method.
	 * 
	 * @param attributeName
	 * @param operator
	 * @param value
	 */	
    public void addCondition(String attributeName, String operator, double value){
		addCondition(attributeName, operator, new Double(value));
    }
    
	/**
	 * Add condition given propertyName, condition and float value.
	 * 
	 * You don't use this method directly, because it 
	 * is used by addXXX method.
	 * 
	 * @param attributeName
	 * @param operator
	 * @param value
	 */	
    public void addCondition(String attributeName, String operator, float value){
		addCondition(attributeName, operator, new Float(value));
    }
    
	/**
	 * Add condition given propertyName, condition and int value.
	 * 
	 * You don't use this method directly, because it 
	 * is used by addXXX method.
	 * 
	 * @param attributeName
	 * @param operator
	 * @param value
	 */	
    public void addCondition(String attributeName, String operator, int value){
		addCondition(attributeName, operator, new Integer(value));
    }

	/**
	 * Add condition given propertyName, condition and long value.
	 * 
	 * You don't use this method directly, because it 
	 * is used by addXXX method.
	 * 
	 * @param attributeName
	 * @param operator
	 * @param value
	 */	
    public void addCondition(String attributeName, String operator, long value){
		addCondition(attributeName, operator, new Long(value));
    }

	/**
	 * Add equal condition given propertyName and value.
	 *  
	 * @param attributeName
	 * @param value
	 */	
    public void addSelectEqualTo(String attributeName, Object value){
		String columnName = getColumnTableName(attributeName);
		addCondition(columnName, SqlInterface.EQUAL, value);	
    }
	
	/**
	 * Add equal condition given propertyName and int value.
	 *  
	 * @param attrName
	 * @param value
	 */
    public void addSelectEqualTo(String attrName, int value){ 
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.EQUAL, value);		
    }
	
	/**
	 * Add equal condition given propertyName and long value.
	 *  
	 * @param attrName
	 * @param value
	 */
    public void addSelectEqualTo(String attrName, long value){ 
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.EQUAL, value);
    }

	/**
	 * Add equal condition given propertyName and double value.
	 *  
	 * @param attrName
	 * @param value
	 */
    public void addSelectEqualTo(String attrName, double value){ 
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.EQUAL, value);
    }

	/**
	 * Add equal condition given propertyName and boolean value.
	 *  
	 * @param name
	 * @param value
	 */
    public void addSelectEqualTo(String name, boolean value){ 
        String columnName = getColumnTableName(name);
		addCondition(columnName, SqlInterface.EQUAL, value);
    }
	
	/**
	 * Add less condition (<) given propertyName and value.
	 *  
	 * @param attrName
	 * @param value
	 */
    public void addSelectLessThan(String attrName, Object value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.LESS_THAN, value);
    }

	/**
	 * Add less condition (<) given propertyName and int value.
	 *  
	 * @param attrName
	 * @param value
	 */
    public void addSelectLessThan(String attrName, int value){ 
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.LESS_THAN, value);
    }

	/**
	 * Add less condition (<) given propertyName and long value.
	 *  
	 * @param attrName
	 * @param value
	 */
    public void addSelectLessThan(String attrName, long value){ 
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.LESS_THAN, value);
    }

	/**
	 * Add less condition (<) given propertyName and long value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectLessThan(String attrName, double value){ 
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.LESS_THAN, value);
    }

	/**
	 * Add less/equal condition (<=) given propertyName and value.
	 *  
	 * @param attrName
	 * @param value
	 */
    public void addSelectLessOrEqual(String attrName, Object value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.LESS_OR_EQUAL, value);
    }

	/**
	 * Add less/equal condition (<=) given propertyName and int value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectLessOrEqual(String attrName, int value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.LESS_OR_EQUAL, value);
    }
  
	/**
	 * Add less/equal condition (<=) given propertyName and long value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectLessOrEqual(String attrName, long value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.LESS_OR_EQUAL, value);
    }

	/**
	 * Add less/equal condition (<=) given propertyName and double value.
	 *  
	 * @param attrName
	 * @param value
	 */ 
	public void addSelectLessOrEqual(String attrName, double value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.LESS_OR_EQUAL, value);
    }

	/**
	 * Add greater than condition (>) given propertyName and value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectGreaterThan(String attrName, Object value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.GREATER_THAN, value);
    }

	/**
	 * Add greater than condition (>) given propertyName and int value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectGreaterThan(String attrName, int value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.GREATER_THAN, value);
    }

	/**
	 * Add greater than condition (>) given propertyName and long value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectGreaterThan(String attrName, long value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.GREATER_THAN, value);
    }

	/**
	 * Add greater than condition (>) given propertyName and double value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectGreaterThan(String attrName, double value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.GREATER_THAN, value);
    }
 
	/**
	 * Add greater than condition (>=) given propertyName and value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectGreaterOrEqual(String attrName, Object value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.GREATER_OR_EQUAL, value);
    }

	/**
	 * Add greater than condition (>=) given propertyName and int value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectGreaterOrEqual(String attrName, int value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.GREATER_OR_EQUAL, value);
    }

	/**
	 * Add greater than condition (>=) given propertyName and long value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectGreaterOrEqual(String attrName, long value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.GREATER_OR_EQUAL, value);
    }

	/**
	 * Add greater than condition (>=) given propertyName and double value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectGreaterOrEqual(String attrName, double value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.GREATER_OR_EQUAL, value);
    }

	/**
	 * Add not equal condition (<>) given propertyName and value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectNotEqual(String attrName, Object value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.NOT_EQUAL, value);
    }

	/**
	 * Add not equal condition (<>) given propertyName and int value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectNotEqual(String attrName, int value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.NOT_EQUAL, value);
    }

	/**
	 * Add not equal condition (<>) given propertyName and long value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectNotEqual(String attrName, long value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.NOT_EQUAL, value);
    }

	/**
	 * Add not equal condition (<>) given propertyName and double value.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectNotEqual(String attrName, double value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.NOT_EQUAL, value);
    }

	/**
	 * Add like condition given propertyName and value.
	 * 
	 * You can add % character in value parameter.
	 *  
	 * @param attrName
	 * @param value
	 */
	public void addSelectLike(String attrName, String value){
        String columnName = getColumnTableName(attrName);
		addCondition(columnName, SqlInterface.LIKE, value);
    }

	/**
	 * Add or condition given another criteria. 
	 * 
	 * Criteria parameter is enclosed from open 
	 * and closed parenthesis.
	 *
	 * @param criteria condition to append to OR clause
	 * 
	 */
	public void addOrCriteria(Criteria criteria){
        
		this.criteria.append(" ").append(SqlInterface.OR)
				     .append("(").append(criteria.getCriteria())
				     .append(")");
    }
    
	/**
	 * Add and condition given another criteria.
	 *
	 * @param criteria condition to append to OR clause
	 * 
	 */
	public void addAndCriteria(Criteria criteria){
        
			this.criteria.append(" ").append(SqlInterface.AND)
			             .append(" ").append(criteria.getCriteria())
			             .append(" ");
		}

	/**
	 * Add order by condition given attrName to order and isAscending 
	 * to specify the ordering. 
	 *
	 * @param attrName
	 * @param isAscending if true the ordering is ascending,false is 
	 * descending
	 * 
	 */
    public void addOrderBy(String attrName, boolean isAscending){
		String columnName = getColumnTableName(attrName);
		orderConditions.add(new OrderCriteria(attrName, isAscending));
    }

	/**
	 * Add order by condition given attrName in ascending ordering.
	 *
	 * @param attrName
	 * 
	 */
    public void addOrderBy(String attrName){
		String columnName = getColumnTableName(attrName);
		addOrderBy(attrName, true);
    }

    
	/**
     * Builds a string for the 'ORDER BY' clause
     *
     * @param items to order
     * @return an SQL string for the 'ORDER BY' clause
     * @exception MappingException if an error occurs
     */
    public StringBuffer getOrderByClause(ArrayList items) throws MappingException{
		if (orderConditions.size() == 0) return null;

		StringBuffer sb = new StringBuffer();
		sb.append(SqlInterface.ORDER_BY);
		for (int i = 0; i < orderConditions.size(); i++) {
			OrderCriteria criteriaC = (OrderCriteria)orderConditions.get(i);
			if (i > 0) sb.append(", ");
				sb.append(criteriaC.asSqlClause(items));			
		}
		return sb;
	
    }

    
    /**
     * Return a Stirng representation of Criteria object
     * 
     * @return String
     */
	public String toString(){
		StringBuffer sb = new StringBuffer(); 
	    sb.append(className)
		  .append("[").append("\n")
		  .append("criteria=").append(criteria).append("\n")
		  .append("order=").append(orderConditions).append("\n")
		  .append("]");	
		
		return sb.toString();
    }

	/**
	 * 
	 * <code>Criteria</code> represents order by criteria
	 * 
	 */
    static class OrderCriteria {
		/**
		 * column name to order
		 */
		private String name;
		
		/**
		 * Type of ordering, true is ascending, 
		 * false is descending
		 */
		private boolean isAscending;

		/**
		 * Hascode value
		 */
		private int hashCode = -1;

		/**
		 * Creates order by criteria given column name to order
		 * and ordering.
		 * 
		 * @param name
		 * @param isAscending true is ascending,
		 * false is descending
		 */
		OrderCriteria(String name, boolean isAscending) {
			this.name = name;
			this.isAscending = isAscending;
		}

		/**
		 * Return hashcode of this class
		 * 
		 * @return int
		 */
		public int hashCode(){
			if (hashCode == -1) {
			hashCode = name.hashCode();
			if (isAscending)
				hashCode++;
			}
			return hashCode;
		}

		/**
		 * Return true is current object is equal to obj
		 * false otherwise
		 * 
		 * @param obj
		 * @return boolean
		 */
		public boolean equals(Object obj){
			if (obj == null || !(obj instanceof OrderCriteria))
				return false;

			OrderCriteria criteria = (OrderCriteria)obj;
				return isAscending == criteria.isAscending
					   && name.equals(criteria.name);
		}

		/**
		 * Return a order by in String representation.
		 * 
		 * @param items
		 * @return String
		 * @throws MappingException
		 */
		public String asSqlClause(ArrayList items) throws MappingException{
			
			StringBuffer sb = new StringBuffer();					
			for(int i = 0; i < items.size(); i++){
				ItemDescriptor item = (ItemDescriptor)items.get(i);
				if(item.getPropertyName().equals(name)){
					sb.append(item.getColumnTableName());
					if (isAscending) {					
						sb.append(" " + SqlInterface.ASC);
					}
					else{
						sb.append(" " + SqlInterface.DESC);
					}
				}
			}
			return sb.toString();			
		}
	}
}
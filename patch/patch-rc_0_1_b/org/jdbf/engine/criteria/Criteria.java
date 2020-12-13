/*
 * 25/10/2002 - 09:39:11
 *
 * Criteria.java - JDBF Object Relational mapping system
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

package org.jdbf.engine.criteria;

import java.util.ArrayList;
import java.util.List;

import org.jdbf.engine.sql.SqlInterface;
import org.jdbf.engine.mapping.ItemDescriptor;
import org.jdbf.engine.mapping.MappingException;


/**
 * This class implements the Criteria class 
 *
 * @author <a href="giovannimartone@hotmail.com">Giovanni Martone</a>
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

    
	
	/**
	 * Creates a object
	 */
	public Criteria(String repositoryName){
        this.repositoryName = repositoryName;
		criteria = new StringBuffer();
		className = this.getClass().getName();		
		orderConditions  = new ArrayList();
		hashCode = -1;
    }


	public void setItemDescriptors(ArrayList list){
		itemDescriptors = list;
	}

    
	/**
	 * Creates and returns a copy of this object.
	 *
	 * @return the copy of this object
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
	 * Indicates whether some other object is "equal to" this one. 
	 *
	 * @param obj - the reference object with which to compare.
	 * @return true if this object is the same as the obj argument; false otherwise.
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
	 */
	public String getRepositoryName(){
        return repositoryName;
    }
	
	
	/**
	 * Returns a hash code value for the object.
	 *
	 * @return a hash code value for this object.
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
	 */
	protected void setRepositoryName(String repositoryName){
        this.repositoryName = repositoryName;
    }

    
	public List getOrderConditions(){
		return orderConditions;
	}

  
	public boolean isDistinct(){
		return distinct;
	}

    public void setDistinct(boolean distinct){
		this.distinct = distinct;
	}


	public StringBuffer getCriteria(){
		return criteria;
	}

    	
    public void addCondition(String attributeName, String condition){        
		criteria.append(attributeName).append(condition);		
    }

    public void addCondition(String attributeName, String operator, Object value){
        criteria.append(attributeName).append(operator);
		if(value instanceof String)
			criteria.append("'").append(value).append("'");
		else
			criteria.append(value);					
    }

    public void addCondition(String attributeName, String operator, boolean value){
        addCondition(attributeName, operator, new Boolean(value));
    }

    public void addCondition(String attributeName, String operator, double value){
		addCondition(attributeName, operator, new Double(value));
    }
    

    public void addCondition(String attributeName, String operator, float value){
		addCondition(attributeName, operator, new Float(value));
    }
    
    public void addCondition(String attributeName, String operator, int value){
		addCondition(attributeName, operator, new Integer(value));
    }

    public void addCondition(String attributeName, String operator, long value){
		addCondition(attributeName, operator, new Long(value));
    }

    public void addSelectEqualTo(String attributeName, Object value){
		addCondition(attributeName, SqlInterface.EQUAL, value);	
    }


    public void addSelectEqualTo(String attrName, int value){ 
        addCondition(attrName, SqlInterface.EQUAL, value);		
    }

    public void addSelectEqualTo(String attrName, long value){ 
        addCondition(attrName, SqlInterface.EQUAL, value);
    }

    public void addSelectEqualTo(String attrName, double value){ 
        addCondition(attrName, SqlInterface.EQUAL, value);
    }

    public void addSelectEqualTo(String name, boolean value){ 
        addCondition(name, SqlInterface.EQUAL, value);
    }

    public void addSelectLessThan(String attrName, Object value){
        addCondition(attrName, SqlInterface.LESS_THAN, value);
    }

    public void addSelectLessThan(String attrName, int value){ 
        addCondition(attrName, SqlInterface.LESS_THAN, value);
    }

    public void addSelectLessThan(String attrName, long value){ 
        addCondition(attrName, SqlInterface.LESS_THAN, value);
    }

    
	public void addSelectLessThan(String attrName, double value){ 
        addCondition(attrName, SqlInterface.LESS_THAN, value);
    }


    public void addSelectLessOrEqual(String attrName, Object value){
        addCondition(attrName, SqlInterface.LESS_OR_EQUAL, value);
    }

    
	public void addSelectLessOrEqual(String attrName, int value){
        addCondition(attrName, SqlInterface.LESS_OR_EQUAL, value);
    }
  

	public void addSelectLessOrEqual(String attrName, long value){
        addCondition(attrName, SqlInterface.LESS_OR_EQUAL, value);
    }

    
	public void addSelectLessOrEqual(String attrName, double value){
        addCondition(attrName, SqlInterface.LESS_OR_EQUAL, value);
    }

    
	public void addSelectGreaterThan(String attrName, Object value){
        addCondition(attrName, SqlInterface.GREATER_THAN, value);
    }

    
	public void addSelectGreaterThan(String attrName, int value){
        addCondition(attrName, SqlInterface.GREATER_THAN, value);
    }

    
	public void addSelectGreaterThan(String attrName, long value){
        addCondition(attrName, SqlInterface.GREATER_THAN, value);
    }

    
	public void addSelectGreaterThan(String attrName, double value){
        addCondition(attrName, SqlInterface.GREATER_THAN, value);
    }
 
    
	public void addSelectGreaterOrEqual(String attrName, Object value){
        addCondition(attrName, SqlInterface.GREATER_OR_EQUAL, value);
    }

    
	public void addSelectGreaterOrEqual(String attrName, int value){
        addCondition(attrName, SqlInterface.GREATER_OR_EQUAL, value);
    }

    
	public void addSelectGreaterOrEqual(String attrName, long value){
        addCondition(attrName, SqlInterface.GREATER_OR_EQUAL, value);
    }

    
	public void addSelectGreaterOrEqual(String attrName, double value){
        addCondition(attrName, SqlInterface.GREATER_OR_EQUAL, value);
    }


	public void addSelectNotEqual(String attrName, Object value){
        addCondition(attrName, SqlInterface.NOT_EQUAL, value);
    }


	public void addSelectNotEqual(String attrName, int value){
        addCondition(attrName, SqlInterface.NOT_EQUAL, value);
    }

    
	public void addSelectNotEqual(String attrName, long value){
        addCondition(attrName, SqlInterface.NOT_EQUAL, value);
    }


	public void addSelectNotEqual(String attrName, double value){
        addCondition(attrName, SqlInterface.NOT_EQUAL, value);
    }

    
	public void addSelectLike(String attrName, String value){
        addCondition(attrName, SqlInterface.LIKE, value);
    }


	public void addOrCriteria(Criteria criteria){
        
		this.criteria.append(SqlInterface.OR).append("(").append(criteria).append(")");
    }


    public void addOrderBy(String attrName, boolean isAscending){
		orderConditions.add(new OrderCriteria(attrName, isAscending));
    }

    public void addOrderBy(String attrName){
		addOrderBy(attrName, true);
    }

    
	/**
     * Builds a string for the 'ORDER BY' clause
     *
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

    
	public String toString(){
		StringBuffer sb = new StringBuffer(); 
	    sb.append(className)
		  .append("[").append("\n")
		  .append("criteria=").append(criteria).append("\n")
		  .append("order=").append(orderConditions).append("\n")
		  .append("]");	
		
		return sb.toString();
    }

    static class OrderCriteria {
		private String name;
		private boolean isAscending;

		private int hashCode = -1;

		OrderCriteria(String name, boolean isAscending) {
			this.name = name;
			this.isAscending = isAscending;
		}

		
		public int hashCode(){
			if (hashCode == -1) {
			hashCode = name.hashCode();
			if (isAscending)
				hashCode++;
			}
			return hashCode;
		}

		
		public boolean equals(Object obj){
			if (obj == null || !(obj instanceof OrderCriteria))
				return false;

			OrderCriteria criteria = (OrderCriteria)obj;
				return isAscending == criteria.isAscending
					   && name.equals(criteria.name);
		}

		
		public String asSqlClause(ArrayList items) throws MappingException{
			
			StringBuffer sb = new StringBuffer();					
			for(int i = 0; i < items.size(); i++){
				ItemDescriptor item = (ItemDescriptor)items.get(i);
				if(item.getPropertyName().equals(name)){
					sb.append(item.getPropertyName());
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
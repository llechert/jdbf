/*
* 03/07/2002 - 22:56:11
*
* $RCSfile: ItemDescriptor.java,v $ - JdbF Object Relational mapping system
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
 * <code>ItemDescriptor</code> represents the objects descritors for objects 
 * "item" that are specified in the repository.
 *
 * @see org.jdbf.engine.mapping.ObjectDescriptor
 * @see org.jdbf.engine.mapping.BeanDescriptor
 *    
 */
public class ItemDescriptor extends ObjectDescriptor{

	/** name of column on the table */
	private String columnTableName;
	/** data type */
	private String dataType;
	/** property name of object */
	private String propertyName;
	/** unique key on table */
	private boolean isPrimaryKey;
	/** format pattern for date */
	private String formatPattern;
	
	
	/**
	 * Constructs the object
	 */
	public ItemDescriptor(){
		super();
		columnTableName = new String();	
		dataType = new String();
		propertyName = new String();
		isPrimaryKey = false;
		formatPattern = new String();
	}
	
	
	/**
	 * Returns name of column on the table
	 * @return String
	 */	
	public String getColumnTableName(){
		return columnTableName;
	}
	
	
	/**
	 * Returns data type
	 * @return String
	 */
	public String getDataType(){
		return dataType;
	}
	
	
	/**
	 * Returns format pattern for date object
	 *
	 * @return String
	 */
	public String getFormatPattern(){
		return formatPattern;
	}
	
	
	/**
	 * Returns name of property of the Java object
	 * @return String
	 */
	public String getPropertyName(){
		return propertyName;
	}
	
	
	/**
	 * Specifies if itemDescriptor is unique key
	 * @return true if it is unique key, false otherwise
	 */
	public boolean isPrimaryKey(){
		return isPrimaryKey;
	}
	
	
	/**
	 * Sets name of column on the table specified in columnTableName
	 * @param columnTableName
	 */
	public void setColumnTableName(String columnTableName){
		this.columnTableName = columnTableName;
	}
	
				
	/**
	 * Sets data type specified in dataType
	 * @param dataType
	 */
	public void setDataType(String dataType){
		this.dataType = dataType;
	}
	
	
	/**
	 * Sets if itemDescriptor is unique key
	 * @param isPrimaryKey true if it is unique key, false otherwise
	 */	
	public void setIsPrimaryKey(boolean isPrimaryKey){
		this.isPrimaryKey = isPrimaryKey;
	}
	
	
	/**
	 * Sets name of property name of Java object 
	 * specified in propertyName
	 * 
	 * @param formatPattern
	 */
	public void setFormatPattern(String formatPattern){
		this.formatPattern = formatPattern;
	}


	/**
	 * Sets name of property name of Java object specified 
	 * in propertyName
	 * 
	 * @param propertyName
	 */
	public void setPropertyName(String propertyName){
		this.propertyName = propertyName;
	}
	
	
	/**
	 * Return the object as String
	 * @return String
	 */
	public String toString(){
		StringBuffer buff = new StringBuffer();
		buff.append(classOwn).append("[").append("\n")
		    .append("className ").append(className).append("\n")
			.append("columnTableName ").append(columnTableName).append("\n")
			.append("dataType ").append(dataType).append("\n")
			.append("formatPattern ").append(formatPattern).append("\n")
			.append("isPrimaryKey ").append(isPrimaryKey).append("\n")
			.append("propertyName ").append(propertyName).append("\n")			
			.append("tableName ").append(tableName).append("\n")
			.append("repositoryViewName ").append(repositoryViewName).append("\n")
			.append("]").append("\n");
			
		return buff.toString();
	}						
}
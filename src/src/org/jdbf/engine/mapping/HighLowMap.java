/*
 * 11/18/2002 - 15:02:45
 *
 * $RCSfile: HighLowMap.java,v $ - JdbF Object Relational mapping system
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
 * <code>HighLowMap</code> provides mapping information for generation of keys
 * using a key-values table.
 *
 * This is not the HighLow approach described by Scott Ambler in his
 * paper titled <a href="http://www.ambysoft.com/mappingObjects.pdf">
 * Mapping Objects To Relational Databases</a>
 *
 * Scott Ambler calls this the key-values approach, but others
 * use "HighLow" to describe a modified key-values approach which does
 * not use database access for each key.
 * 
 */
public class HighLowMap extends GeneratorMap{
    
    /**
     * The name of the column the low value
     */
    private String nextColumn;

    
    /**
     * The name of the column for the  key value.
     */
    private String keyColumn;

    /**
     * The name of the column for the table name.
     */
    private String tableColumn;


    /**
     * High value
     */
     private String high;


     /**
      * Low value
      */
     private Object low;


    /**
     * The name of the table which holds the key-values.  It's
     * multi-row table having two columns, one for the identifier
     * for the table name and the other for the next key value
     * of the corresponding table.
     */
    private String table;

	/**
	 * Creates object given table name,name of high key, next value for low
	 * and table column
	 * @param table
	 * @param keyColumn
	 * @param nextColumn
	 * @param tableColumn
	 */
    public HighLowMap(String table,String keyColumn,String nextColumn,String tableColumn){
        super("highlow");
		this.table = table;
		this.keyColumn = keyColumn;
		this.nextColumn = nextColumn;
		this.tableColumn = tableColumn;
		high = null;
		low = null;
    }

	/**
	 * Get High
	 * @return String
	 */
    public String getHigh(){
        return high;
    }

	/**
	 * Get low
	 * @return String
	 */
    public Object getLow(){
    	return low;
    }
    
    /**
     * Get table name
     * @return String
     */
    public String getTable(){
		return table;
    }

	/**
	 * Get table column
	 * @return String
	 */
    public String getTableColumn(){
		return tableColumn;
    }

    /**
     * Get type
     * 
     * @return String
     */
    public String getType(){
        return "highlow";
    }

    
    /**
     * Get next column
     * @return String
     */
    public String getNextColumn(){
    	return nextColumn;
    }

    /**
     * Get key column
     * @return String
     */
    public String getKeyColumn(){
		return keyColumn;
    }

   	/**
   	 * Set high
   	 * @param high
   	 */ 
    public void setHigh(String high){
        this.high = high;
    }

	/**
	 * Set low
	 * @param low
	 */
    public void setLow(Object low){
        this.low = low;
    }

    /**
     * Set next column
     * @param nextColumn
     */
    public void setNextColumn(String nextColumn){
    	this.nextColumn = nextColumn;
    }

    /**
     * Set key column
     * @param keyColumn
     */
    public void setKeyColumn(String keyColumn){
		this.keyColumn = keyColumn;
    }

    /**
     * Set table column
     * @param tableColumn
     */
    public void setTableColumn(String tableColumn){
		this.tableColumn = tableColumn;
    }

    /**
     * Set table name
     * @param table
     */
    public void setTable(String table){
 		this.table = table;
    }

	/**
	 * Return a Stirng representation of this object
	 * @return String
	 */
    public String toString(){
    	String str = super.toString();
		StringBuffer buff = new StringBuffer(str);
		buff.append("table " + table).append("\n")
	    	.append("tableColumn " + tableColumn).append("\n")
	    	.append("keyColumn " + keyColumn).append("\n")
	    	.append("nextColumn " + nextColumn).append("\n")
	    	.append("high " + high).append("\n")
	    	.append("low " + low).append("\n")
	    	.append("]");
	
       return buff.toString();
    }
    
}
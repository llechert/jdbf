/*
 * 24/09/2002 - 23:55:27
 *
 * Cursor.java - JDBF Object Relational mapping system
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

import java.util.List;

import org.jdbf.engine.basic.ObjectMapped;

/**
 * This subclass of QueryResults provides rows
 * of an sql query form cache.
 *
 * @author Giovanni Martone
 * @version "id" "Date"
 *
 */
public class Cursor extends QueryResults{
    
    /** List of results */
    private List results;
    
    /** Current index */
    private int index = -1;

    /** Array of results */
    private ObjectMapped[] fields;


    /**
     * Creates the Cursor object
     *
     * @param results list of results
     * @param id identifier into cache
     * @see SelectStatement#select(String,RepositoryView,Connection,Object[]) select
     */    
    public Cursor(List results,Object id){
        super(id);		
		this.results = results;
    }


    /**    
     * Retrieves first object in Cursor.
     *
     * @return the <code>ObjectMapped</code> at the index 0
     */
    public ObjectMapped first(){
        //Fixed bug: #692322 (Giovanni Martone)
		if(results != null){
			fields = new ObjectMapped[1];
			fields[0] = (ObjectMapped)results.get(0);
			return fields[0];   
		}
		else
			return null;
		
    }


    /**
     * Retrieves an object from an sql row.
     *
     * @return the <code>ObjectMapped</code> at the given index
     */
    public ObjectMapped getObject(){
		return fields[index];
    }


    /**
     * Retrieves last object in Cursor
     *
     * @param the <code>ObjectMapped</code> at the last index
     */
    public ObjectMapped last(){
        //Fixed bug: #692323 (Giovanni Martone)
		if(results != null){
			fields = new ObjectMapped[1];
			fields[0] = (ObjectMapped)results.get(results.size() - 1);
			return fields[0];
		}
		else
			return null;
    }

    
    /**
     * The index into the results list is incremented,
     * and true is returned if there is another result.
     *
     * @return true if there is another row.
     */
    public boolean next() {
		fields = new ObjectMapped[results.size()];
		boolean res = (results.size() > ++index);		
		if (res) {
			fields[index] = (ObjectMapped)results.get(index);			
		}
		return res;
    }


    /**
     * Creates the identifier into cache.Append the name
     * with hashcode of values array
     *
     * @param name name of repositoryView, values values to search
     */
    public void setIdentifier(String name,Object[] values){
        
		StringBuffer buff = new StringBuffer(name);

		for(int i = 0; i < values.length; i++){	    
			String hashCode = String.valueOf(values[i].hashCode());
			buff.append(hashCode);
		}

		setIdentifier(buff.toString());
    }
}

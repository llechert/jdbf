/*
 * 24/09/2002 - 23:51:27
 *
 * $RCSfile: QueryResults.java,v $ - JDBF Object Relational mapping system
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

import java.util.logging.Logger;

import org.jdbf.engine.basic.*;


/**
 * <code>QueryResults<code> is the class-mapped equivalent of 
 * a JDBC <code>ResultsSet</code>.
 * 
 * These are the results produced by the given <code>SelectStatement</code>.
 */

public abstract class QueryResults{
    
    /** Class name */
    protected String className;

    /**
     * Logger object
     */
    protected Logger logger;
    
        
    /**
     * Creates a QueryResults object
     *
     */
    public QueryResults(){
      
		className = this.getClass().getName();
		logger = Logger.getLogger(className);	
    }
    
    /**
     * Return true if there are object in QueryResults,
     * false otherwise
     * @return boolean
     */
    public abstract boolean next();

	/**
	 * Return ObjectMaped indexes current position
	 * @return ObjectMapped
	 */
    public abstract ObjectMapped getObject();

	/**
	 * Close QueryResults object
	 *
	 */
    public abstract void close();
    
    
    /**
     * Return size of QueryResults
     * @return int
     */
    public abstract int size();
}
/*
 * 
 * $Log: QueryResults.java,v $
 * Revision 1.10  2004/06/28 22:16:03  gmartone
 * remove old implementation of cache for task 80429 (Enanchement DCS)
 *
 * Revision 1.9  2004/05/20 22:41:55  gmartone
 * Changed for task 99073 (Coverage Javadocs)
 *
 * 
 */

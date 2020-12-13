/*
 * 30/10/2002 - 09:37:11
 *
 * $RCSfile: SelectCriteria.java,v $ - JdbF Object Relational mapping system
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

/**
 * 
 * <code>SelectCriteria</code> represent condition to append 
 * to select statement.<br>
 * 
 */
public class SelectCriteria extends Criteria{
    	
	/**
	 * Create DeleteCriteria given repositoryViewName
	 * @param repositoryName
	 */
	public SelectCriteria(String repositoryName) {
    	super(repositoryName);
    }    
}


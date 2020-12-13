/*
 * 20/01/2003 - 10:22:45
 *
 * $RCSfile: InterbaseInterface.java,v $ - JDBF Object Relational mapping system
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

import org.jdbf.engine.mapping.MappingException;


/**
 * <code>InterbaseInterface</code> overrides SqlInterface's methods 
 * for constructing sql statements where Interbase's implementaion 
 * differs from ANSI or is not specified by ANSI.
 *
 */
public class InterbaseInterface extends SqlInterface{


	/**
     *  Return the lower function.
	 *
	 *  Interbase has not the "Lower" function
	 *
	 * @return String in lower case
	 * @throws MappingExcpetion
     */
    public String getClauseStringLower() throws MappingException{
		throw new MappingException("mapping.dbFeatureNotSupported");
    }


	/**
	 * Return the current timeStamp statement
	 *
	 * @return current timestamp statement
	 */
	public String getCurrentTimeStampStatement()throws MappingException{
		return "select current_timestamp from rdb$database;";
    }


	/**
     * Forms an sql sequence statement given name
	 *
	 * @param name
	 * @return Sql statement for selecting a sequence key.
	 * @throws MappingException
	 *
     */
    public String getSelectSequenceStatement(String name)
    	throws MappingException{
		
		return new StringBuffer("select gen_id(").append(name)
			.append(",1) from rdb$database").toString();
    }
}
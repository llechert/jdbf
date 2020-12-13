/*
 * 20/01/2003 - 20:29:11
 *
 * $RCSfile: SybaseInterface.java,v $ - JDBF Object Relational mapping system
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

/**
 *
 * <code>SybaseInterface</code>overrides SqlInterface's methods for constructing sql statements
 * where Sybase's implementaion differs from ANSI or is not specified by ANSI.
 *
 */
public class SybaseInterface extends SqlServerInterface{


	/**
     * Forms an sql insert id statement
	 *
	 * @return String Sql statement for selecting a insert id key.
	 * 
	 *
     */
	public String getSelectInsertIdStatement(){
		return "SELECT @@identity";
    }
}

/*
* 03/07/2002 - 22:56:11
*
* $RCSfile: ObjectDescriptor.java,v $ - JdbF Object Relational mapping system
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
 * <code>ObjectDescriptor</code> represents the objects that are specifies in the repository.
 * @see ItemDescriptor
 * @see BeanDescriptor
 * 
 */
public class ObjectDescriptor{

	/** Class own */
	protected String classOwn;

	/** Class name */
	protected String className;

	/** Name of repository item */
	protected String repositoryViewName;

	/** Name of table */
	protected String tableName;


	/**
	 * Constructs the object
	 */
	public ObjectDescriptor(){
		classOwn = this.getClass().getName();
		className = new String();
		repositoryViewName = new String();
		tableName = new String();
	}


	/**
	 * Return class own
	 * @return String
	 */
	public String getClassOwn(){
		return classOwn;
	}


	/**
	 * Return class name of class mapped
	 * @return String
	 */
	public String getClassName(){
		return className;
	}


	/**
	 * Return name of reposiroey item
	 * @return String name of repository item
	 */
	public String getRepositoryViewName(){
		return repositoryViewName;
	}


	/**
	 * Return table name of table mapped to Java object
	 * @return String table name
	 */
	public String getTableName(){
		return tableName;
	}


	/**
	 * Set class own
	 * @param className
	 */
	public void setClassName(String className){
		this.className = className;
	}


	/**
	 * Set name of repository item
	 * @param repositoryViewName
	 */
	public void setRepositoryViewName(String repositoryViewName){
		this.repositoryViewName = repositoryViewName;
	}


	/**
	 * Set name of table mapped to Java object
	 * @param tableName
	 */
	public void setTableName(String tableName){
		this.tableName = tableName;
	}
}
/*
 * 03/12/2002 - 14:08:11
 *
 * $RCSfile: Repository.java,v $ - JDBF Object Relational mapping system
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
package org.jdbf.engine.repository;

import org.jdbf.engine.basic.ObjectMapped;
import org.jdbf.engine.mapping.MappingException;


/**
 * <code>Repository</code> is the interfaces of repository.
 */
public interface Repository{

	/**
	 * Return value of property specified in propertyName for
	 * ObjectMapped
	 * @param map
	 * @param propertyName
	 * @return Object
	 * @throws MappingException
	 */
	public Object getPropertyValue(ObjectMapped map,String propertyName) 
		throws MappingException;
	
	/**
	 * Set value for property specified in proertyName
	 * @param map
	 * @param propertyName
	 * @param propertyValue
	 * @return ObjectMapped
	 * @throws MappingException
	 */
	public ObjectMapped setPropertyValue(ObjectMapped map,String propertyName, 
	        Object propertyValue) throws MappingException;
}
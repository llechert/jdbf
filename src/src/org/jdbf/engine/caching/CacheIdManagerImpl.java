/*
 * 14/06/2004 - 23:12:27
 *
 * $RCSfile: CacheIdManagerImpl.java,v $ - JDBF Object Relational mapping system
 * Copyright (C) 2002-2004 JDBF Development Team
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

package org.jdbf.engine.caching;

import java.util.List;
import org.jdbf.engine.basic.ObjectMapped;
import org.jdbf.engine.basic.PrimaryKey;
import org.jdbf.engine.mapping.ItemDescriptor;
import org.jdbf.engine.mapping.PrimaryKeyMap;
import org.jdbf.engine.mapping.MappingException;
import org.jdbf.engine.reflection.ReflectionManager;



/**
 * <code>CacheIdManager</code> handles the creation of cache id for object.<br>
 * Cache id value is created appending all value fo al primary key for this object.
 * 
 */
public class CacheIdManagerImpl implements CacheIdManager {
	
	/**
	 * Creates an empty object
	 */
    public  CacheIdManagerImpl() {}
    
	/**
	 * 
	 * Creates id cache for an object specified in obj.
	 * 
	 * Value of cache id is created on value of primary key.
	 *
	 * @param pk PrimaryKeyMap
	 * @param object ObjectMapped
	 * @return String cache id
	 * @throws CacheException
	 *  
	 */
	public String createCacheId(PrimaryKeyMap pk, 
								ObjectMapped object)
		throws CacheException{
		
		String str = null;
		try{
			StringBuffer buf = new StringBuffer();
			List pks = pk.getPrimaryKey();
			
			for(int i = 0; i < pks.size(); i++){
				ItemDescriptor item = (ItemDescriptor)pks.get(i);
				String pkName = item.getPropertyName();
				Object value = ReflectionManager.getValueByName(
			                                  object,pkName);
			   			                                  
			    buf.append(value);			    
			    str = buf.toString();
			}
		}
		catch(MappingException e){
			throw new CacheException(e);
		}
			
		return str;
	}
	
	
	/**
	 * 
	 * Creates id cache for an object.
	 * 
	 * Value of cache id is created on value of primary key
	 * specified in pk parameter.
	 *
	 * @param pk PrimaryKey
	 * @param pkMap PrimaryKeyMap
	 * @return String cache id
	 * @throws CacheException
	 *  
	 */
	public String createCacheId(PrimaryKey pk,PrimaryKeyMap pkMap) 
		throws CacheException {
		
		String str = null;
		try{
			StringBuffer buf = new StringBuffer();
			List pks = pkMap.getPrimaryKey();

			for(int i = 0; i < pks.size(); i++){
				ItemDescriptor item = (ItemDescriptor)pks.get(i);
				String pkName = item.getPropertyName();				
				Object value = pk.getValueKey(pkName);								
				buf.append(value);				
				str = buf.toString();
			}			
		}
		catch(Exception e){
			throw new CacheException(e);
		}
		return str;
	}   

}




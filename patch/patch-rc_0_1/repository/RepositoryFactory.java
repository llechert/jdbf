/*
 * 11/06/2002 - 11:53:11
 *
 * RepositoryFactory.java - JDBF Object Relational mapping system
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
package org.jdbf.engine.repository;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.jdbf.engine.mapping.MappingException;

/**
 * <code>RepositoryFactory</code> is a factory of Repository.
 * Collection of Repository are loaded on startup and are putted 
 * in this class so they are available.
 *
 * @author Giovanni Martone
 * @version "id" "Date"
 *
 */
public class RepositoryFactory{

	/** Contains Repository */
	private static Map repositories = Collections.synchronizedMap(new HashMap());

	
	/**
	 * Creates a empty object
	 */
	public RepositoryFactory(){}


	/**
	 * Creates a object.
	 *
	 * @param map that contains the Repository object
	 */
	public RepositoryFactory(Map map){
		repositories = map;
	}


	/**
	 * Add Repository with the specific key.
	 *
	 * @param key key of Repository, the key is the name of RepositoryView
	 *        repository Repository object
	 * @throws MappingException if a repository with name specified in key 
	 *        already exits
	 *
	 */
	public void addRepository(String key, Repository repository)
		throws MappingException{
		
		if(repositories.containsKey(key))
		    throw new MappingException("mapping.duplicateRepositoryViewName",
			                       key
			                      );
		else
		    repositories.put(key,repository);
	}

	
	/**
	 * Return the Repository with the name specified in key paramater
	 *
	 * @param key name of repository
	 * @return Repository
	 *
	 */
	public Repository getRepository(String key){
	    return (Repository)repositories.get(key);
	}


	/**
	 * Return a Map of Repository
	 *
	 * @return Map
	 */
	public Map getRepositories(){
	    return repositories;
	}


	/**
	 * Return a list of ItemDescriptor of a specific repository specified 
	 * in repository name
	 *
	 * @param repositoryName
	 * @return list 
	 *
	 */
	 public static java.util.List getItemDescriptors(String repositoryName){
	     RepositoryView view = (RepositoryView)repositories.get(repositoryName);
	     return view.getBeanDescriptor().getItemDescriptors();
	 }
}

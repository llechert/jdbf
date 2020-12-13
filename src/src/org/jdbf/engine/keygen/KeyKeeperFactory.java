/*
 * 09/01/2003 - 21:29:27
 *
 * $RCSfile: KeyKeeperFactory.java,v $ - JDBF Object Relational mapping system
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
package org.jdbf.engine.keygen;

import java.util.ArrayList;


/**
 * <code>KeyKeeperFactory</code> holds a list of KeyKeeper for all
 * HighLow keys.
 *
 */
public class KeyKeeperFactory {
    
    /**
     * Creates an empty object
     *
     */
    private KeyKeeperFactory() {}

    /** 
     * List of <code>KeyKeeperEntry</code> 
     * holding the instantiated keykeepers 
     */
    static ArrayList keyKeepers = new ArrayList();

    
    /**
     * Single entry of KeyKeeper
     */
    public static class KeyKeeperEntry {
        
	/**
	 * Logic name of KeyKeeper
	 */
	String name;
	
	
	/**
	 * KeyKeeper object
	 */		 
	KeyKeeper keyKeeper;


	/**
	 * Creates a KeyKeeperEntry given a name and KeyKeeper 
	 * object
	 * 
	 * @param name
	 * @param keyKeeper
	 */
	KeyKeeperEntry(String name, KeyKeeper keyKeeper) {
    		this.name = name;
    		this.keyKeeper = keyKeeper;
        }
    }


    /**
     * Adds a <code>KeyKeeper</code> to the list of 
     * instantiated interfaces.
     *
     * @param name the short name of the <code>KeyKeeper</code>
     */
    private static void  addKeyKeeper(String name,KeyKeeper keeper){
		
		keyKeepers.add(new KeyKeeperEntry(name, keeper));
    }
	
	
    /**
     * Return  an <code>KeyKeeper</code> for the given 
     * class name.
     *
     * @param name the short name of the <code>KeyKeeper</code>
     * @return a <code>KeyKeeper</code> for the given 
     * class name.
     */
    private static KeyKeeper getKeyKeeperIntern(String name){

		KeyKeeperEntry entry = null;
		int size = keyKeepers.size();
		for (int i=0; i<size; i++) {
			entry = (KeyKeeperEntry)keyKeepers.get(i);
		    	if (entry.name.equals(name))
		        	return entry.keyKeeper;
		}
		return null;
    }


    /**
     * Return a KeyKeeper specified in name
     *
     * @param name of the key generator 
     * @return a <code>KeyKeeper</code> indexed by the given name
     */
    public synchronized static KeyKeeper getKeyKeeper(String name){
	
		String low = name.toLowerCase();
		KeyKeeper keeper = getKeyKeeperIntern(low);
		return keeper;
    }
}

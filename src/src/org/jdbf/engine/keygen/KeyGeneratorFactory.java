/*
 * 11/14/2002 - 09:40:11
 *
 * $RCSfile: KeyGeneratorFactory.java,v $ - JDBF Object Relational mapping system
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
 * <code>KeyGeneratorFactory</code> holds a list of key 
 * generators.<br>
 *
 */
public class KeyGeneratorFactory {
    
    /**
     * Creates an empty object
     *
     */
    private KeyGeneratorFactory() {}

    /** 
     * List of <code>SqlInterfaceEntry</code> 
     * holding the instantiated interfaces 
     */
    static ArrayList generators = new ArrayList();

    
    /**
     * Single entry of KeyGenerator
     */
    static class KeyGeneratorEntry {
		
    /**
     * Logic name of KeyGenerator
     */
    String name;
		
    /**
     * KeyGenerator object
     */
    KeyGenerator keyGenerator;
		
    /**
     * Creates a KeyGeneratorEntry given name and keyGenerator
     * 
     * @param name
     * @param keyGenerator
     */
    KeyGeneratorEntry(String name, KeyGenerator keyGenerator) {
		this.name = name;
		this.keyGenerator = keyGenerator;
    }
}

    
   /**
    * Interface that creates the KeyGenerator object
    */
   static interface KeyGeneratorCreator {
       KeyGenerator create();
    }

    /** A list of the available key generators */
    private static KeyGeneratorCreator[] keyGenerators = 
	
		new KeyGeneratorCreator [] {	    	
			new KeyGeneratorCreator() {
		    	
			    public KeyGenerator create() {
			        return new MaxKeyGenerator();
		    	    }
		    
			    public String toString() {
				return "max";
		    	    }		
		},
	    	new KeyGeneratorCreator() {
		    
			    public KeyGenerator create() {
				return new HighLowKeyGenerator();
		    	    }
		    	
			    public String toString() {
				return "highlow";
		    	    }
		},
	        new KeyGeneratorCreator() {
		    
		    	    public KeyGenerator create() {
			    	return new SequenceKeyGenerator();
		    	    }
		    
		    	    public String toString() {
			        return "sequence";
		            }
		},
	    	new KeyGeneratorCreator() {
		    	
		    	    public KeyGenerator create() {
				return new IdentityKeyGenerator();
		    	    }
		    
		    	    public String toString() {
		        	return "identity";
		    	    }
		}
	};


    /**
     * Adds a <code>SqlInterface</code> to the list of 
     * instantiated interfaces.
     *
     * @param name the short name of the <code>SqlInterface</code>
     * @return <code>KeyGenerator</code> added to the list
     * @throws KeyGenerationException if error occurs
     *
     */
    private static KeyGenerator addKeyGenerator(String name)
 		throws KeyGenerationException{

		KeyGenerator generator = null;
		for (int i=0; i<keyGenerators.length; i++) {
	    	if (name.equals(keyGenerators[i].toString())) {
	        	generator = keyGenerators[i].create();
				generators.add(new KeyGeneratorEntry
					(keyGenerators[i].toString(), generator));
				return generator;
	     	}
		}
		return null;
    }
	

    /**
     * Return  an <code>KeyGenerator</code> for the given 
     * class name.
     *
     * @param name the short name of the <code>SqlInterface</code>
     * @return <code>KeyGenerator</code> for the given 
     * class name.
     * @throws KeyGenerationException if error occurs
     *
     */
    private static KeyGenerator getKeyGeneratorIntern(String name)
		throws KeyGenerationException{

		KeyGeneratorEntry entry = null;
		int size = generators.size();
		for (int i=0; i<size; i++) {
	    	entry = (KeyGeneratorEntry)generators.get(i);
	    	if (entry.name.equals(name))
	        	return entry.keyGenerator;
		}
		return null;
    }

    
    /**
     * Return the KeyGenerator specified in name
     *
     * @param name of the key generator 
     * @return <code>KeyGenerator</code> indexed by the given name
     * @throws KeyGenerationException if error occurs
     *
     */
    public synchronized static KeyGenerator getKeyGenerator(String name)
		throws KeyGenerationException{
	
		String low = name.toLowerCase();
		KeyGenerator generator = getKeyGeneratorIntern(low);
		if (generator != null)
   	    	return generator;

		generator = addKeyGenerator(low);
		if (generator != null)
	    	return generator;

		throw new KeyGenerationException("mapping.keyGenNotFound", name);
    }
}
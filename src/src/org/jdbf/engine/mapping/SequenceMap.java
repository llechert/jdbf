/*
 * 03/02/2003 - 21:24:45
 *
 * $RCSfile: SequenceMap.java,v $ - JdbF Object Relational mapping system
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
 * <code>SequenceMap</code>provides configuration 
 * for a key generator which uses sequences.
 */
public class SequenceMap extends GeneratorMap{
    
    /** name of sequence */
	private String sequenceName;

    /**
	 * Creates the object
	 *
	 * @param type of GeneratorMap
	 * @param sequenceName name of sequence
	 *
	 */
	public SequenceMap(String type,String sequenceName){
    	super(type);
		setSequenceName(sequenceName);
    }

	/**
	 * Return type
	 * @return String
	 */
    public String getType(){
		return "sequence";
	}

	/**
	 * Return sequence name
	 * @return String
	 */
    public String getSequenceName(){
		return sequenceName;
	}

	/**
	 * Set sequence name
	 * @param sequenceName
	 */
    public void setSequenceName(String sequenceName){
		this.sequenceName = sequenceName;
	}
	
	/**
	 * Return a String representation of this object
	 * @return String
	 */
	public String toString(){
    	String str = super.toString();
		StringBuffer buff = new StringBuffer(str);
		buff.append("type " + type).append("\n")
	    	.append("sequenceName " + sequenceName).append("\n")
	    	.append("]");	
    
		return buff.toString();
    }
}

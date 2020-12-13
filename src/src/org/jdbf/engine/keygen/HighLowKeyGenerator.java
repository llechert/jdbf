/*
 * 20/01/2003 - 10:17:56
 *
 * $RCSfile: HighLowKeyGenerator.java,v $ - JDBF Object Relational mapping system
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

import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.sql.Connection;
import java.util.logging.Logger;
import java.util.logging.Level;
    
import org.jdbf.castor.Messages;

import org.jdbf.engine.mapping.*;
import org.jdbf.engine.repository.RepositoryView;
import org.jdbf.engine.sql.SqlInterface;


/**
 * 
 * <code>HighLowKeyGenerator</code> generates a key generator composed by
 * a high value and by a low value. High value is fixed to same value and low 
 * value is incremented.<br>
 * 
 * This is not the HighLow approach described by Scott Ambler in 
 * <a href="http://www.ambysoft.com/mappingObjects.pdf">
 * Mapping Objects To Relational Databases</a>.
 *
 * Scott Ambler calls this the key-values approach, but others
 * use "HighLow" to describe a modified key-values approach which 
 * does not use database access for each key. 
 *
 */
public class HighLowKeyGenerator implements KeyGenerator{
    
    /**
     * Holds a KeyKeeper for each table.  The table's name 
     * is the key and the value is a  KeyKeeper.
     */
    private Map keys = Collections.synchronizedMap(new HashMap());

	/**
	 * Class name
	 */
    private static final String CLASS_NAME = "org.jdbf.engine.keygen.HighLowKeyGenerator";

    /**
     * Logger object
     */
    private Logger logger = Logger.getLogger(CLASS_NAME);

    
    /**
     * Generation of key is before insert.
     * 
     * @return boolean
     */
    public boolean isBeforeInsert(){
        return true;
    }

    
    /**
     * Returns a generated key.
     *
     * @param view RepositoryView object
     * @param conn Connection the databas
     * @param vendor name of database vendor may be null
     * @param sqlInterface
     * @return Object that represents the key
     * @exception KeyGenerationException if an error occurs
     * 
     */
    public Object generateKey(RepositoryView view, Connection conn,String vendor,
					          SqlInterface sqlInterface)
        throws KeyGenerationException{
	
		logger.log(Level.INFO,Messages.message("HighLow.generateKey"));
		HighLowMap hiloMap = (HighLowMap)
		view.getBeanDescriptor().getGeneratorMap();
		String tableName = view.getBeanDescriptor().getTableName();
		KeyKeeper keyKeeper;
		Object key = null;
		synchronized (keys) {
		    keyKeeper = (KeyKeeper)keys.get(tableName);
		    if (keyKeeper == null) {
		        keyKeeper = new KeyKeeper(tableName,hiloMap,sqlInterface);
	        }
		    		   
		    key = keyKeeper.nextKey(conn,vendor);
		    keys.put(tableName, keyKeeper);		   		  
		}
		logger.log(Level.INFO,Messages.format("HighLow.generatedKey",key));
		return key;
    }
}
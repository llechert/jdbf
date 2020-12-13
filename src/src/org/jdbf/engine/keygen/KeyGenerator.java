/*
 * 11/14/2002 - 09:40:11
 *
 * $RCSfile: KeyGenerator.java,v $ - JDBF Object Relational mapping system
 * Copyright (C) 2002 JDBF Development Team 
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

import java.math.BigDecimal;
import java.sql.Connection;

import org.jdbf.engine.repository.RepositoryView;
import org.jdbf.engine.sql.SqlInterface;

/**
 * <code>KeyGenerator</code> representes the interface for 
 * key generator.
 *
 */
public interface KeyGenerator{
    
    /** Default value of key */
    public BigDecimal ONE = new BigDecimal(1);
    
    
    /**
     * Generation of key is before insert.
     */
	public boolean isBeforeInsert();
    
    
    /**
     * This generates a key for a field in
     * the object described by the give view.
     *
     * @param view provides mapping of class to sql
     * @param conn connection against database 
     * @param vendor of database vendor
     * @param sqlInterface
     * @exception KeyGenerationException if an error occurs
     * 
     */
    Object generateKey(RepositoryView view,Connection conn,String vendor,
					   SqlInterface sqlInterface)
        throws KeyGenerationException;
}
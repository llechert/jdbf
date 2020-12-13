/*
 * 14/06/2004 - 12.23.38
 *
 * $RCSfile: CacheException.java,v $ - JDBF Object Relational mapping system
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

package org.jdbf.engine.caching;

import org.jdbf.engine.JDBFException;

/**
 * <code>CacheException</code> handles all error that occur during
 * operation against cache.
 * 
 */
public class CacheException extends JDBFException {

	/**
	 * Embedded exception
	 */
	private Exception except;
    
    /**
	 * Creates exception with message
	 * @param message
	 */
    public CacheException(String message) {
        super(message);
      
    }

    /**
	 * Creates excpetion with message parsed with type param
	 * @param message
	 * @param type
	 */
    public CacheException(String message, Object type) {
        super(message, type);
       
    }

    /**
	 * Creates exception with message parsed with type param
	 * @param message
	 * @param args
	 */
    public CacheException(String message, Object[] args) {
        super(message, args);
        
    }
    
	/**
	 * Creates exception by Exception
	 * @param except
	 */
	public CacheException(Exception except)
	{
		this("mapping.nested", except.toString());
		this.except = except;
	}

	/**
	 * Return embedded exception
	 * @return Exception
	 */
	public Exception getException() {
		return except;
	}

}

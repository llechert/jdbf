/*
* 10/09/2002 - 20:11:11
*
* $RCSFile$ - JDBF Object Relational mapping system
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
package org.jdbf.engine;

import org.jdbf.engine.configuration.Configuration;


/**
 * <code>Configurable</code> represents a interface for jdbf 
 * configuration. All classes that read configuration from file
 * must implements Configurable
 * 
 */
public interface Configurable {
    
    /**
     * Read configuration. Start point is specified in conf.
     * @param conf
     * @throws Exception
     */
    void setConfiguration(Configuration conf) throws Exception;
}

 

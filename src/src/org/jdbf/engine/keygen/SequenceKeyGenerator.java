/*
 * 03/02/2003 - 21:21:03
 *
 * $RCSfile: SequenceKeyGenerator.java,v $ - JDBF Object Relational mapping system
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
    
import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.jdbf.castor.Messages;


import org.jdbf.engine.repository.RepositoryView;
import org.jdbf.engine.mapping.MappingException;
import org.jdbf.engine.mapping.SequenceMap;
import org.jdbf.engine.sql.SqlInterface;

/**
 * 
 * <code>SequenceKeyGenerator</code> handles the generation of key
 *  by sequence.<br> The sequence statement is created by specific
 * SqlInterface object.
 * 
 */
public class SequenceKeyGenerator implements KeyGenerator{
    
    /**
     * Class name
     */
    private static final String CLASS_NAME = "org.jdbf.engine.keygen.SequenceKeyGenerator";

    /**
     * Logger object
     */
    private Logger logger = Logger.getLogger(CLASS_NAME);
    
    /**
     * Generation of key is before insert.
     */
    public boolean isBeforeInsert(){
		return true;
    }

    
    /**
     * Obtains a key from a sequence table.
     *
     * @param view RepositoryView object
     * @param conn Connection the databas
     * @param vendor name of database vendor may be null
     * @return the key
     * @exception MappingException if an error occurs
     * 
     */
    public Object generateKey(RepositoryView view,
    						  Connection conn,String vendor,
					          SqlInterface sqlInterface)
		throws KeyGenerationException{
	
		Object identity = null;
        ResultSet rs = null;;
		Statement stmt = null;

		logger.log(Level.INFO,Messages.message("SequenceKeyGen.generateKey"));
		// Create SQL sentence of the form
		// "SELECT nextval(sequenceName)"
		try {
	    
	    	SequenceMap sequenceMap = (SequenceMap)view.getBeanDescriptor().getGeneratorMap();
	    	stmt = conn.createStatement();
	    	rs = stmt.executeQuery(
	    			sqlInterface.getSelectSequenceStatement(
	    					sequenceMap.getSequenceName()));
	    	if (rs.next()) {
	        	identity = rs.getObject(1);				
	    	}
	    	else
                logger.throwing(CLASS_NAME,"generateKey()",
			    new KeyGenerationException(Messages.message("mapping.keyGenFailed")
		           ));
                throw new KeyGenerationException(Messages.message("mapping.keyGenFailed"));
		}
		catch (SQLException excep) {
	    	logger.throwing(CLASS_NAME,"generateKey()",
    				new KeyGenerationException(excep)
		           );
	    	throw new KeyGenerationException(excep);
		}
		catch (MappingException excep) {
	    	logger.throwing(CLASS_NAME,"generateKey()",
    				new KeyGenerationException(excep)
		           );
	    	throw new KeyGenerationException(excep);
		}
		finally {
	    	try {
	        	if (rs != null)
		    		rs.close();
				stmt.close();
	    	}
	    	catch (SQLException excep) {
	    		logger.throwing(CLASS_NAME,"generateKey()",
    				new KeyGenerationException(excep)
		           );
	    		throw new KeyGenerationException(excep);	
	    	}
	    	finally {
	        	rs = null;
				stmt = null;
	    	}
			logger.log(Level.INFO,Messages.format("SequenceKeyGen.generatedKey",identity));
			return identity;
		}
	
    }
}
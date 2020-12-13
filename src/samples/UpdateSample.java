/*
* May 20, 2004 - 4:02:00 PM
*
* $RCSfile: UpdateSample.java,v $ - JDBF Object Relational mapping system
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

import org.jdbf.engine.caching.CacheException;
import org.jdbf.engine.criteria.*;
import org.jdbf.engine.mapping.MappingException;
import org.jdbf.engine.sql.*;
import org.jdbf.engine.transaction.TransactionException;

public class UpdateSample extends Sample {

    /**
     * @throws Exception
     */
    public UpdateSample() throws Exception {
        super();
    }
    
    
	public void updateObject() 
		throws QueryException,MappingException,CacheException{

		String[] properties = {"price","groupId"};
		Product prod = new Product("product");
		prod.setOID(new Integer(14));
		prod.setName("AS70 Canon");
		prod.setPrice(300);
		prod.setGroupId(0);
		int rows = database.update(prod,properties);
		printRowsAffected(rows);	
	}

	public void updateWithCriteria(String repositoryViewName)
		throws QueryException,MappingException{		
			
		String[] properties = {"groupId"};
		Object[] values = {new Integer(1)};
		UpdateCriteria criteria = new UpdateCriteria("product");
		criteria.addSelectEqualTo("OID",14);
		int rows = database.updateForCriteria(repositoryViewName,properties,values,criteria);
		printRowsAffected(rows);        
	}
	
	
    public static void main(String[] args) {
		UpdateSample sample = null;
		try{
			sample = new UpdateSample();
			sample.beginTransaction();
			sample.updateObject();
			sample.commitTransaction();
		}
		catch(Exception e){
			try{
				sample.rollbackTransaction();
			}
			catch(TransactionException txe){
				txe.printStackTrace();
			}			
			e.printStackTrace();
		}
		finally{
			sample.close();
		}
    }
}

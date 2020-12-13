/*
* May 20, 2004 - 3:28:36 PM
*
* $RCSfile: DeleteSample.java,v $ - JDBF Object Relational mapping system
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

public class DeleteSample extends Sample {

    /**
     * @throws Exception
     */
    public DeleteSample() throws Exception {
        super();
    }
    
    
	public void deleteObject()
		throws QueryException,MappingException,CacheException{
		
		Product prod = new Product("product");
		prod.setOID(new Integer(14));
		prod.setName("AS70 Canon");
		prod.setPrice(300);
		prod.setGroupId(0);
		int rows = database.delete(prod);
		
		printRowsAffected(rows);        
	}

	public void deleteWithCriteria(String repositoryViewName)
		throws QueryException,MappingException{
		DeleteCriteria deleteCriteria = new DeleteCriteria(repositoryViewName);
		deleteCriteria.addSelectLike("name","AS70%");
		int rows = database.deleteForCriteria(deleteCriteria);
		printRowsAffected(rows);   
	}

    public static void main(String[] args) {
		DeleteSample sample = null;
		try{
			sample = new DeleteSample();
			sample.beginTransaction();
			
			sample.deleteObject();
			//sample.deleteWithCriteria("product");
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

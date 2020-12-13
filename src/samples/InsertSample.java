/*
* May 20, 2004 - 3:48:52 PM
*
* $RCSfile: InsertSample.java,v $ - JDBF Object Relational mapping system
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

import org.jdbf.engine.transaction.TransactionException;
import org.jdbf.engine.mapping.MappingException;
import org.jdbf.engine.sql.*;
import org.jdbf.engine.keygen.KeyGenerationException;

public class InsertSample extends Sample {

    /**
     * @throws Exception
     */
    public InsertSample() throws Exception {
        super();
    }
    
	public void insertObject()
		throws QueryException,MappingException,
			   KeyGenerationException{
			   	
		Product prod = new Product("product");		
		//in insert don't set OID because it is found by
		//KeyGeneration system
		prod.setName("AS40 Canon");
		prod.setPrice(139);
		prod.setGroupId(0);
		System.out.println("prod " + prod);
		int rows = database.insert(prod);
		printRowsAffected(rows);
	}

    public static void main(String[] args) {
		InsertSample sample = null;
		try{
			sample = new InsertSample();
			sample.beginTransaction();
			sample.insertObject();
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


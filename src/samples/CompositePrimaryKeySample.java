/*
* May 27, 2004 - 4:55:19 PM
*
* RCSfile - JDBF Object Relational mapping system
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

import org.jdbf.engine.sql.QueryException;
import org.jdbf.engine.sql.QueryResults;
import org.jdbf.engine.mapping.MappingException;
import org.jdbf.engine.basic.PrimaryKey;
import org.jdbf.engine.caching.CacheException;

public class CompositePrimaryKeySample extends Sample {

    /**
     * @throws Exception
     */
    public CompositePrimaryKeySample() throws Exception {
        super();
    }
    
	public QueryResults selectByPrimaryKey(String repositoryViewName,PrimaryKey pk)
			throws QueryException,MappingException,CacheException{
		
			
			QueryResults results = database.selectByPrimaryKey(repositoryViewName,pk);
			return results;	
	}
		
	public void printQueryResults(QueryResults results){
			while(results.next()){
				System.out.println(results.getObject());
			}
	}

    public static void main(String[] args) {
    	try{
    	
    		CompositePrimaryKeySample sample = new CompositePrimaryKeySample();
    		PrimaryKey pk = new PrimaryKey();
    		pk.setValueKey("OID",new Integer(1));
    		pk.setValueKey("lineId",new Integer(0));
			QueryResults results = sample.selectByPrimaryKey("orderline",pk);
			sample.printQueryResults(results);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    }
}

/*
* May 20, 2004 - 2:42:24 PM
*
* $RCSfile: SelectSample.java,v $ - JDBF Object Relational mapping system
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

import org.jdbf.engine.criteria.*;
import org.jdbf.engine.sql.QueryException;
import org.jdbf.engine.sql.QueryResults;
import org.jdbf.engine.mapping.MappingException;
import org.jdbf.engine.caching.CacheException;

public class SelectSample extends Sample{

    public SelectSample()throws Exception{
    	super();
    }
    
    public QueryResults selectWithCriteria(String repositoryViewName)
        throws QueryException,MappingException,CacheException{
		
		SelectCriteria selectCriteria = new SelectCriteria("product");
		selectCriteria.addSelectEqualTo("groupId",0);
		Criteria criteria = new Criteria("product");
		criteria.addSelectLessThan("OID",13);
		selectCriteria.addAndCriteria(criteria);
		selectCriteria.addOrderBy("OID",true);
		QueryResults results = database.select(repositoryViewName,selectCriteria);
		return results;	
    }
    
	public QueryResults selectAll(String repositoryViewName)
		throws QueryException,MappingException,CacheException{
		
		
		QueryResults results = database.select(repositoryViewName,null);
		return results;	
	}
    
    public void printQueryResults(QueryResults results){
    	while(results.next()){
    		System.out.println(results.getObject());
    	}
    }
    
    

    public static void main(String[] args) {
    	try{
			SelectSample sample = new SelectSample();
			//QueryResults results = sample.selectWithCriteria("product");
			QueryResults results = sample.selectAll("product");
			sample.printQueryResults(results);
			
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
    
    }
}

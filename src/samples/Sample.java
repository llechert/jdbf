/*
* May 20, 2004 - 2:44:54 PM
*
* $RCSfile: Sample.java,v $ - JDBF Object Relational mapping system
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

import org.jdbf.engine.LayerManager;
import org.jdbf.engine.database.DatabaseImpl;
import org.jdbf.engine.database.Database;
import org.jdbf.engine.transaction.TransactionException;

public class Sample {

    protected  LayerManager layer;
    
    protected DatabaseImpl database;
    
    
    public Sample() throws Exception {
		String fileLoggingConf = ".." + System.getProperty("file.separator") +
		                  "conf" +  System.getProperty("file.separator") +
		                  "logging.properties";
		layer = LayerManager.getInstance("repository.xml",
										 fileLoggingConf
										);
		database = (DatabaseImpl)getDatabase();
		System.out.println("database " + database);
    }
    
	protected Database getDatabase()throws Exception{        
			return layer.getDatabase("database.xml");
	}
	
	protected void printRowsAffected(int rows){
		System.out.println("rows affected " + rows);
	}
	
	protected void beginTransaction()
		throws TransactionException{
		
		database.beginTransaction();
	}
	
	protected void commitTransaction()
		throws TransactionException{
		
		database.commitTransaction();	
	}
	
	protected void rollbackTransaction()
		throws TransactionException{
		
		database.rollbackTransaction();
	}
	
	protected void close(){
		database.close();
	}
	
	

}

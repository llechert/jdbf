/*
 * 09/28/2002 - 12:15:57
 * $RCSfile: LayerManager.java,v $ - JDBF Object Relational mapping system
 * 
 * http://jdbf.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

package org.jdbf.engine;

import java.io.FileInputStream;
import java.util.logging.*;
import java.util.Collection;


/*
 project's packages and classes
*/
import org.jdbf.engine.database.*;
import org.jdbf.engine.repository.RepositoryFactory;
import org.jdbf.engine.xml.XPathManager;
import org.jdbf.engine.caching.CacheManager;


/**
 * <code>LayerManager</code> is the entry point of layer.<br>
 * 
 * This class load all information from repository, 
 * creates all database connection that are logically 
 * stored in Database object and finally initializes logger
 * system.
 */
 
public class LayerManager {

    /** Current instance */
    private static LayerManager instance;

    /** RepositoryFactory object */
    private RepositoryFactory repFactory;

	/** LogManager object */
	private LogManager logManager;

	/** Logger object */
	private Logger logger;
	
	/** CacheManager object */
	private CacheManager cacheManager;


    /**
     * Creates a LayerManager object.
	 * It loads all repositoryView that are configured in repository
	 * and it initializes logger system.
     *
     * @param  fileName
     * @throws Exception
     *
     */
    protected LayerManager(String fileName,String fNameConf)throws Exception {		
		
		logManager = LogManager.getLogManager();
		logManager.readConfiguration(new FileInputStream(fNameConf));
		XPathManager xPath = new XPathManager(fileName);
		repFactory = xPath.getRepositoryFactory();
		cacheManager = CacheManager.getInstance();
		Collection coll = repFactory.getRepositories().values();
		cacheManager.loadCache(coll);
    }


    /**
     * Retrieves a new instance of database
     *
     * @param  fileNameConf
     * @return Database
     * @throws Exception
     *
     */
    public Database getDatabase(String fileNameConf)throws Exception{

        DatabaseImpl db = new DatabaseImpl(fileNameConf,repFactory,
                                           cacheManager);
	    return db;
    }

    /**
     * Retrieves current instance of this object.
     *
     * @param  fileName
     * @return current instance of LayerManager
     * @throws Expcetion
     *
     */
    public static synchronized LayerManager getInstance(String fileName,String fileNameConf) throws Exception{

       if(instance == null) {

          instance = new LayerManager(fileName,fileNameConf);
       }
       return instance;
    }
 }

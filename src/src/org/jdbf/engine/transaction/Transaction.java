/*
* 04/04/2002 - 23:12:27
*
* $RCSfile: Transaction.java,v $ - JdbF Object Relational mapping system
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
package org.jdbf.engine.transaction;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.Connection;

import javax.transaction.Status;

/**
 * 
 * <code>Transaction</code> is a abstract class which represents a transaction.
 * A transaction context is required in order to perform operations
 * against the database. The only way to begin a new transaction is
 * through the creation of a new transaction.
 *
 */

public abstract class Transaction{

    /**
     * The transaction status.
     */
    protected int status;


    /**
     * Class name;
     */
    protected String className;

    /**
     * Logger object
     */
    protected Logger logger;
    
    /**
     * Create a new transaction context
     * setting the status as active. 
     *
     * This method is used by the
     * explicit transaction model.
     */
    public Transaction(){
        status = Status.STATUS_ACTIVE;
        className = this.getClass().getName();
        logger = Logger.getLogger(className);
    }


    /**
     * 
     * Return the status of transaction
     *
     * @return true if the transaction is open,false otherwise
     *
     */
    public boolean isOpen(){
        logger.log(Level.FINEST,"status " + status);
        return (status == Status.STATUS_ACTIVE || status == Status.STATUS_MARKED_ROLLBACK);
    }


    /**
     * Commits all operations
     *
     * @param  conn - connection to commit
     * @throws TransactionException
     */
    public abstract void commit(Connection conn)
    	throws TransactionException;



    /**
     * Rollback all operations and closes the transaction
     *
     * @param  conn - connection to rollback
     * @throws TransactionException Method called while
     *   transaction is not in progress
     */
    public abstract void rollback(Connection conn)
        throws TransactionException;
}
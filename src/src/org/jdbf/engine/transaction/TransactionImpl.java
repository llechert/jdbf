/*
* 04/04/2002 - 23:12:27
*
* $RCSfile: TransactionImpl.java,v $ - JdbF Object Relational mapping system
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

import java.sql.Connection;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.transaction.Status;

import org.jdbf.castor.Messages;



/**
 * 
 * <code>Transaction</code> is a class which represents a transaction.
 * A transaction context is required in order to perform operations
 * against the database. The only way to begin a new transaction is
 * through the creation of a new transaction.
 *
 */
public class TransactionImpl extends Transaction{

    /**
     * Logger object
     */
    private Logger logger;


    /**
     * Create a new transaction context. 
     * 
     * 
     * This method is used by the explicit transaction model.
     */
    public TransactionImpl(){        
        super();
        logger = Logger.getLogger(className);
    }


    /**
     * Commits connection
     *
     * @param connection to commit
     * @throws TransactionException
     */
     public void commit(Connection connection) 
     	throws TransactionException{

	    // Never commit transaction that has been marked for rollback
        try{
            
            logger.log(Level.INFO,Messages.message("TransactionImpl.committing"));
	        
            if (status == Status.STATUS_MARKED_ROLLBACK ) {

	            connection.rollback();
                logger.throwing(className,"commit()",
                      new TransactionException( "transaction.markedRollback" ));
	            throw new TransactionException( "transaction.markedRollback" );
	        }

	        status = Status.STATUS_COMMITTING;

            // Go through  the connection opened in this transaction,
	        // commit and close it.
	        connection.commit();

	        status = Status.STATUS_COMMITTED;
	    }
	    catch ( Exception except ) {
	        // Any error that happens, we're going to rollback the transaction.
            status = Status.STATUS_MARKED_ROLLBACK;
            logger.log(Level.WARNING,Messages.message(
                                             "transaction.markedRollback"));
	        try{

	            connection.rollback();
                
                logger.log(Level.INFO,Messages.message(
                                            "TransactionImpl.rollbacking"));
                logger.throwing(className,"commit()",
                      new TransactionException(except.getMessage()));
                throw new TransactionException(except.getMessage());
	        }
	        catch(Exception ex){
                logger.throwing(className,"commit()",
                      new TransactionException(except.getMessage()));
	            throw new TransactionException(ex.getMessage());
	        }
        }
     }


    /**
     * Rollback connection
     *
     * @param connection to rollback
     * @throws TransactionException Method called while
     *         transaction is not in progress
     *
     */
    public void rollback(Connection connection) 
    	throws TransactionException{

	    try{

            logger.log(Level.INFO,Messages.message(
                                            "TransactionImpl.rollbacking"));
	        if( status != Status.STATUS_ACTIVE 
                && 
                status != Status.STATUS_MARKED_ROLLBACK ){
                	               
                    logger.throwing(className,"roollback()",
                              new TransactionException(
                                  Messages.message("transaction.noTransaction")));
                    throw new TransactionException(
                            Messages.message("transaction.noTransaction"));
            }
            // Go through all the connections opened in this transaction,
            // rollback and close them one by one.
            connection.rollback();
            status = Status.STATUS_ROLLEDBACK;
	    }
	    catch(Exception e){
	        logger.throwing(className,"roolback()",
                      new TransactionException(e.getMessage()));
            throw new TransactionException(e.getMessage());
	    }
    }
}
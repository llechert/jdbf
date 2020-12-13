/*
 * 07/05/2002 - 23:46:45
 *
 * $RCSfile: SelectStatement.java,v $ - JDBF Object Relational mapping system
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
package org.jdbf.engine.sql;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;

import org.jdbf.castor.Types;
import org.jdbf.engine.basic.ObjectMapped;
import org.jdbf.engine.basic.PrimaryKey;
import org.jdbf.engine.criteria.Criteria;
import org.jdbf.engine.mapping.*;
import org.jdbf.engine.reflection.*;
import org.jdbf.engine.repository.*;
import org.jdbf.castor.Messages;

/**
 * <code>SelectStatement</code> is that  class that represents the select sql
 * statement.
 * SelectStatement handles the creation of sql statement with the informations
 * specified in a RepositoryView object and provides to execute the select statement.
 *
 */
public class SelectStatement extends SQLStatement {

    /**
     * Creates object given the repository,propertiesNames,
     * criteria and currnet slqInterface
     * @param repository
     * @param propertiesNames
     * @param criteria may be null
     * @param sqlInterface
     */
    public SelectStatement(
        RepositoryView repository,
        String[] propertiesNames,
        Criteria criteria,
        SqlInterface sqlInterface) {
        super(repository, null, criteria, sqlInterface);
    }

    /**
     * Creates the empty object
     */
    public SelectStatement() {
        super();
    }

    /**
     * Builds the select statement with the informations passed by repository.
     * These informations are:
     * <li> tableName;
     * <li> columnTableName
     *
     * @param repository
     * @param propertiesNames name of properties to select
     * @param sqlInterface specific SqlInterface
     *
     */
    public void buildStatement(
        RepositoryView repository,
        String[] propertiesNames,
        SqlInterface sqlInterface) {
        logger.log(Level.INFO, Messages.message("Statement.build"));
        BeanDescriptor beanDescriptor = repository.getBeanDescriptor();

        ArrayList pks =
            new ArrayList(beanDescriptor.getPrimaryKeyMap().getPrimaryKey());
        ArrayList items =
            new ArrayList(
                unionItemDescriptors(pks, beanDescriptor.getItemDescriptors()));
        String tableName = beanDescriptor.getTableName();

        statement =
            sqlInterface.getSelectStatement(tableName, parseFields(items));
        logger.log(
            Level.INFO,
            Messages.format("Statement.statement", statement));
    }

    /**
     * Builds the select statement which has criteria.
     *
     * @param repository
     * @param propertiesNames
     * @param sqlInterface specific sqlInterface
     *
     */
    public void buildStatementForCriteria(
        RepositoryView repository,
        String[] propertiesNames,
        SqlInterface sqlInterface) {

        try {
            logger.log(Level.INFO, Messages.message("Statement.build"));
            BeanDescriptor beanDescriptor = repository.getBeanDescriptor();

            ArrayList pks =
                new ArrayList(
                    beanDescriptor.getPrimaryKeyMap().getPrimaryKey());
            ArrayList items =
                new ArrayList(
                    unionItemDescriptors(
                        pks,
                        beanDescriptor.getItemDescriptors()));
            String tableName = beanDescriptor.getTableName();

            StringBuffer buff = new StringBuffer();
            StringBuffer conditions = criteria.getCriteria();
            int length = conditions.length();
            if (length > 0) {
                /*
                 * Bug Fixing (Gmartone) 953275
                 */
                //buff.append(" ").append(WHERE);		          
                /*
                 * End bug fixing (Gmartone)
                 */
                buff.append(conditions);
            }
            ArrayList order = (ArrayList) criteria.getOrderConditions();
            if (order.size() > 0)
                buff.append(" ").append(criteria.getOrderByClause(items));
            statement =
                sqlInterface.getSelectStatement(
                    tableName,
                    parseFields(items),
                    buff.toString());
            logger.log(
                Level.INFO,
                Messages.format("Statement.statement", statement));
        }
        catch (MappingException e) {
            logger.log(Level.SEVERE, Messages.message(e.getMessage()));
        }
    }

    /**
     * Executes the select statement and retuns a QueryResults object 
     *
     * @param repositoryViewName view contains the informations about property of object mapped
     * @param view Connection
     * @param connection
     * @return QueryResults set of results
     * @throws QueryException if error occurs.
     *
     */
    public QueryResults select(
        String repositoryViewName,
        RepositoryView view,
        Connection connection)
        throws QueryException {

        Cursor queryResults = null;
        try {

            //Build select statement
            PreparedStatement preStat = connection.prepareStatement(statement);
            ResultSet results = preStat.executeQuery();
            queryResults = new Cursor(getValuesFromResultSet(view, results));
            
            preStat.close();
        }
        catch (SQLException e) {
            logger.throwing(
                className,
                "select()",
                new QueryException(statement, e.getMessage()));
            throw new QueryException(statement, e.getMessage());
        }
        catch (MappingException e) {
            logger.throwing(
                className,
                "select()",
                new QueryException(statement, e.getMessage()));
            throw new QueryException(statement, e.getMessage());
        }
        return queryResults;
    }

    /**
     * Get results of select statement from ResultSet. 
     *
     * It creates an instance of ObjectMapped object. Type of ObjectMapped 
     * is specified in repository, then it sets all properties of this 
     * object with the values 
     *
     * @param view RepositoryView object
     * @param results ResultSet object
     * @return ArrayList an collection of ObjectMapped
     * @throws SQLException
     * @throws MappingException
     *
     */
    protected ArrayList getValuesFromResultSet(
        RepositoryView view,
        ResultSet results)
        throws SQLException, MappingException {

        ArrayList res = new ArrayList();

        BeanDescriptor beanDesc = view.getBeanDescriptor();
        //ArrayList itemDesc = (ArrayList)beanDesc.getPrimaryKeyMap()
        //.getPrimaryKey();

        //itemDesc.addAll(beanDesc.getItemDescriptors());
        ArrayList pks =
            new ArrayList(beanDesc.getPrimaryKeyMap().getPrimaryKey());
        ArrayList items = new ArrayList(beanDesc.getItemDescriptors());
        ArrayList itemDesc = new ArrayList(unionItemDescriptors(pks, items));

        java.util.HashMap props = new java.util.HashMap();
        props.put("repositoryViewName", beanDesc.getRepositoryViewName());

        //loop ResultSet
        while (results.next()) {

            ObjectMapped map =
                ReflectionManager.createBean(beanDesc.getClassName(), props);
            ResultSetMetaData metaData = results.getMetaData();
            //get values from ResultSet
            for (int i = 0; i < itemDesc.size(); i++) {
                int columnIndex = i + 1;
                ItemDescriptor item = (ItemDescriptor) itemDesc.get(i);
                String propertyName = item.getPropertyName();

                String columnTableName = item.getColumnTableName();

                if (columnTableName
                    .equals(metaData.getColumnName(columnIndex))) {

                    Object propertyValue = results.getObject(columnTableName);

                    Object[] params = { propertyName, propertyValue };
                    logger.log(
                        Level.FINEST,
                        Messages.format("Statement.value", params));
                    if (propertyValue == null) {
                        propertyValue = Types.getDefault(item.getDataType());
                    }
                    else {
                        if (!propertyName.equals("OID")) {

                            Object obj = null;
                            Class toClass =
                                ReflectionManager.getPropertyType(
                                    map,
                                    propertyName);
                            Class fromClass = propertyValue.getClass();

                            propertyValue =
                                convertValue(
                                    propertyName,
                                    propertyValue,
                                    map,
                                    item,
                                    fromClass,
                                    toClass);
                        }
                    }

                    /*
                     * OID is inherited fields and it cannot 
                     * be setted by ReflectionManager directly
                     */
                    if (propertyName.equals("OID")) {
                        map.setOID(propertyValue);
                    }
                    else
                        map =
                            view.setPropertyValue(
                                map,
                                propertyName,
                                propertyValue);

                }

            }
            logger.log(Level.FINEST, Messages.message(map.toString()));
            res.add(map);
        }
        results.close();

        return res;
    }


	/**
	 * Create WHERE clause on primaryKey specified in primaryKey input parameter
	 * and return an Criteria object that represents this clause
	 * 
	 * @param repositoryName
	 * @param view
	 * @param primaryKey
	 * @return Criteria 
	 */
    public Criteria createClauseOnPk(String repositoryName,RepositoryView view,
        							 PrimaryKey primaryKey) {

        PrimaryKeyMap pk = view.getBeanDescriptor().getPrimaryKeyMap();
        ArrayList keys = (ArrayList) pk.getPrimaryKey();
        Criteria criteria = new Criteria(repositoryName);

        for (int i = 0; i < keys.size(); i++) {

            Criteria internal = null;
            if (i > 0)
                internal = new Criteria(repositoryName);

            ItemDescriptor item = (ItemDescriptor) keys.get(i);
            Object value = primaryKey.getValueKey(item.getPropertyName());
            String columnName = item.getPropertyName();

            if (i == 0)
                criteria.addSelectEqualTo(columnName, value);
            else
                internal.addSelectEqualTo(columnName, value);

            if (i > 0)
                criteria.addAndCriteria(internal);
        }
        return criteria;
    }
}
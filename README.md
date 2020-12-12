# jdbf
Object-Relational mapping system


JDBF is a JDBC-based Object-Relational mapping system. It needs a xml configurator file called "repository" where objects are mapped. The JDBF generates SQL for retreiving, saving, and deleting objects.

What is JdbF ?

Jdbf is an JDBC-based object-relational mapping that allows Java developers to quickly implement database access in their applications. It generates SQL for retreiving, saving, and deleting objects. Other features are shown below:

+ handling database transaction
+ caching data
+ multi vendor's database supporting
+ marshalling and unmarshalling
+ key generation of ids

Jdbf's structure is logically composed from two macrosystems. These macrosystem are:
+ MMS (Mapping Management System)
+ SMS (Sql Management System)

Mapping Management System

Mapping Management System manages mapping between Java objects and database. This mapping is represented by repository file where a table is mapped with Java object in XML format. An example of repository is shown below
	    
<repositoryView name="product" table-name="Product" object-name="Product" database-name="jdbf">
  <item property-name="OID" primary-key="yes" data-type="int" column-name="id" />
  <item property-name="name" primary-key="no" data-type="string" column-name="name" />
  <item property-name="price" primary-key="no" data-type="int" column-name="price" />          
  <item property-name="groupId" primary-key="no" data-type="int" column-name="group_id" />          
  ....
</repositoryView>
	  
Mapping is very simple.Mapping between column table and property of Java object is specified with tag <item/> where is specified the name of property, the name of column and other information. From this file Jdbf will create in automatically and trasparently way the sql statement, using other important macrosystem: SMS.

Sql Management System

Sql Management System is more modular system that MMS. In fact it is composed from other modules which are delegated to manage the operation that occur between creation of sql statement ant its execution.

These mnodules are:

+ Data Caching System (DCS) module that manages data cache
+ Transaction System (TS) module that manages the transation against database
+ Key Generation System (KGS) module that manages the generation of unique id (OID) for object
+ Query Builder System (QBS) module that manages the creation of sql statement
+ Database Connection System (DCoS) module that manages the creation and usage of database connection

External tools

+ Xalan (XML library)
+ JTA (Java Transacton API)
+ JCache (version 1.0 beta4)
+ Ant (Build Tool)

Environment configuraton

Jdbf need configuration to work well. This configuration is very simple. Configure repository where mapping between Java objects and database is specified
configure file that defines the connection to various datadases

Supported database
Jdbf supports following relational database:
+ mysql
+ Oracle
+ PostgreSQL
+ SQL Server
+ Hypersonic
+ Sybase
+ Informix

Minimum version of JVM is 1.4.X

JdbF java object-relational mapping system is developed under GNU LGPL license. Infomation about the GNU LGPL license can be found GNU homepage.

JdbF Team 2002-2004.


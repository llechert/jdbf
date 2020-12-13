

Patch Instructions and Documentation
-------------------------------------------

The patch needs the sources. You should download the sources. After that you have dowloaded and you have extracted the sources
you should extract this patch under JDBF_HOME/src/org/jdbf/engine. This action rewrites RepositoryFactory.java Criteria.java Cursor.java and SelectStatement.java.
After that you have done this action you should build jdbf.
Type "ant dist" that compiles and creates the file jar file.

Fixed Bugs
-------------------------------------------
ID                       Description
692320		Missing list of fields in select statement  
692322		NullPointer Exception in Cursor.first() method
692323		NullPointerException in Cursor.last() method
692905		Error in parser ORDER BY clause


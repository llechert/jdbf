JDBF Object Relational mapping system
Copyright (C) 2002 JDBF Development Team
http://jdbf.sourceforge.net


				=============================
				       HOW USE SAMPLES
				=============================


This document explain you how use samples contained in this location.
If you launch samples contains in this location you could understand
the power of JDBF.
To use samples is very simple. Read instructions shown below please.



1-Compiling JDBF

You must compile JDBF sources and creates a jar file for JDBF. 
Under jdbf type "ant dist" to create jdbf.jar.
If you have downloaded bin distribution, this operation is not needed.
Finally you must add jdbf.jar to CLASSPATH variable.




2-Editing database configuration

Edit database.xml file where you fill information about your local database 
(for samples MySql has been used).




3-Installing database schema

Create your database schema and run create.sql to create table.




4-Inserting table product

Insert some tupla in your table called product




5-Launching samples.

Launch samples typing java XXXSample where XXX is the name your functionality 
(i.e. java SelectSample) 


Enjoy to modify the criteria and repisitory.xml (try to modify keyGeneration)!

Thank you!

/*
* 07/06/2003 - 14:30:00
*
* $RCSfile: Unmarshaller.java,v $ - JDBF Object Relational mapping system
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
package org.jdbf.engine.xml;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.jdbf.castor.Messages;
import org.jdbf.engine.reflection.ReflectionManager;
import org.jdbf.engine.basic.ObjectMapped;
import org.jdbf.engine.configuration.ConfigurationImpl;
import org.jdbf.engine.configuration.ConfigurationBuilder;

/**
 * <code>Unmarshaller</code> is the class that return a java object from a 
 * xml representation.
 *
 */
public class Unmarshaller{
	
    /**
     * Class name
     */
    private static final String CLASS_NAME = "org.jdbf.engine.xml.Unmarshaller";

    /**
     * Logger object
     */
    private Logger logger;

    /**
	 *
	 * Creates an empty object
	 *
	 */
	public Unmarshaller(){ 
        logger = Logger.getLogger(CLASS_NAME);
    }
    
        
    /**
     * Return a ObjectMapped object from a String xml representation
     *
     * @param xml - the xml representation
     * @return ObjectMapped
     * @throws UnmarshallException
	 *
     */
    public ObjectMapped getUnmarshallObject(String xml)
        throws UnmarshallException{
        
        ConfigurationImpl child;
        ObjectMapped om = null;
        logger.log(Level.INFO,Messages.message("Unmarshaller.unmarshall"));
        try{
			byte [] byteArray = xml.getBytes();
			ConfigurationImpl conf = (ConfigurationImpl) 
						ConfigurationBuilder.build (
								new ByteArrayInputStream(byteArray)
						);
            
			child = (ConfigurationImpl) conf.getConfiguration("class-name");		  
            logger.log(Level.FINEST,"tag class-name " + child.getValue());
			Class classFor = Class.forName(child.getValue());
			om = (ObjectMapped) classFor.newInstance();
			child = (ConfigurationImpl) conf.getConfiguration("repository-name");
            logger.log(Level.FINEST,"tag repository-name " + child.getValue());
			om.setRepositoryViewName(child.getValue());
			Iterator iter = conf.getConfigurations("attributes");		  
			
            while (iter.hasNext()){
				child = (ConfigurationImpl) iter.next();
				Iterator iterChild = child.getConfigurations("attribute");			  
				while (iterChild.hasNext()) {
					conf = (ConfigurationImpl) iterChild.next();
					child = (ConfigurationImpl) conf.getConfiguration("name");
					String name = child.getValue();
                    logger.log(Level.FINEST,"tag name " + name);
					child = (ConfigurationImpl) conf.getConfiguration("value");
					String value = child.getValue();
                    logger.log(Level.FINEST,"tag value " + value);
					if(name.equals("OID"))
						om.setOID(value);
					else
					ReflectionManager.setValueByName(om,name,value);
				}
			}
	    }
	    catch(Exception e){
	        logger.throwing(CLASS_NAME,"getUnmarshallObject()",
                        new UnmarshallException("unmarshal.unmarshalFail",
                            om.getClassName()));

			throw new UnmarshallException("unmarshal.unmarshalFail",
										  om.getClassName()
										 );
	    }
	    return om;		
    }
}
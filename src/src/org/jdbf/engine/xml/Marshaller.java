/*
* 07/06/2003 - 14:30:00
*
* $RCSfile: Marshaller.java,v $ - JDBF Object Relational mapping system
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

import java.util.logging.Logger;
import java.util.logging.Level;

import org.jdbf.castor.Messages;
import org.jdbf.engine.reflection.ReflectionManager;
import org.jdbf.engine.basic.ObjectMapped;

/**
 * <code>Marshaller</code> is the class that return the xml rappresentation 
 * of ObjectMapped.
 * 
 * This xml representation includes all properties of ObjectMapped.
 *
 */
public class Marshaller{
	
	/** Class Name */
    private final static String CLASS_NAME = "org.jdbf.engine.xml.Marshaller";
    
    /** Logger object */
    private Logger logger;

    /**
	 *
	 * Creates an empty object
	 *
	 */
    public Marshaller(){
        logger = Logger.getLogger(CLASS_NAME);
    }
    
    
    /**
     * Return the XML representation of ObjectMapped object as String
     *
     * @param om the ObjectMapped object
     * @return String the XML representation of ObjectMapped object as String
     * @throws MappingException if error occurs in marshalling process
     *
     */
    public String getMarshallString(ObjectMapped om)throws MarshallException{        
        StringBuffer buff = new StringBuffer();
        String omClassName= om.getClassName();
        String lv1 = "  ";
        String lv2 = "     ";
        String lv3 = "        ";
        logger.log(Level.INFO,Messages.format("Marshaller.marshall",
                              omClassName));
        try{
            buff.append("<object>").append("\n")
                .append(lv1)
                .append("<class-name>").append(omClassName)
                .append("</class-name>").append("\n")
                .append(lv1)
                .append("<repository-name>").append(om.getRepositoryViewName())
                .append("</repository-name>").append("\n");
            Class c = Class.forName(omClassName);
            String[] fields = ReflectionManager.getDeclaredFieldsName(om);
            if (fields.length > 0)
                buff.append(lv1).append("<attributes>").append("\n");
				
				/*
				 * Bug fixing 957299 (Gmartone) 
				 */
				writeProperty(buff,lv2,lv3,"OID",om.getOID(),
                              "java.lang.Object");										  		
				/*
				 * End bug fixing (Gmartone)
				 */
				
                for(int i = 0; i < fields.length; i++){
                    String name = fields[i];
					Class cl = ReflectionManager.getPropertyType(om,name);
					String type = cl.getName();
                    Object value = ReflectionManager.getValueByName(om,name);
					writeProperty(buff,lv2,lv3,name,value,type);                 
                }
                if (fields.length > 0)
                    buff.append(lv1).append("</attributes>").append("\n");
                buff.append("</object>");
        }
        catch(Exception e){
            logger.throwing(CLASS_NAME,"getMarshallString()",
                            new MarshallException("marshal.marshalFail",
                                omClassName)
                            );
            throw new MarshallException("marshal.marshalFail",omClassName);
        }
        logger.log(Level.FINEST,buff.toString());
        return buff.toString();	
    }
    
    /**
     * Write xml representation for property.
     * 
     
     * @param buff
     * @param lv2
     * @param lv3
     * @param name
     * @param value
     * @param type
     * 
     */
    private void writeProperty(StringBuffer buff,String lv2,
                                  	   String lv3,String name,
                                       Object value,String type){
                                  	
		buff.append(lv2).append("<attribute>").append("\n")
			.append(lv3).append("<name>").append(name).append("</name>")
			.append("\n").append(lv3)
			.append("<value>").append(value).append("</value>")
			.append("\n").append(lv3)
			.append("<type>").append(type).append("</type>")
			.append("\n").append(lv2)
			.append("</attribute>").append("\n");
		
    }
}
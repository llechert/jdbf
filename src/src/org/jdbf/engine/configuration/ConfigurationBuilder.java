/*
 * 04/04/2002 - 23:12:27
 *
 * $RCSfile: ConfigurationBuilder.java,v $ - JDBF Object Relational mapping system
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
package org.jdbf.engine.configuration;

import java.io.*;

import org.xml.sax.*;
import org.xml.sax.helpers.*;

import org.jdbf.engine.xml.SAXConfigurationBuilder;
import org.jdbf.engine.mapping.MappingException;


/**
 *
 * This utility class will create a <code>Configuration</code> tree from an
 * XML file
 *
 */
public class ConfigurationBuilder {

   /**
    *
    * Return the parsed configuration tree.
    * 
	* @param is xml file to parse
	* @return Configuration configuration tree
	* @throws MappingException
    */
    public static Configuration build(InputStream is) throws MappingException{

        SAXConfigurationBuilder builder = new SAXConfigurationBuilder();

        try{
            XMLReader parser = XMLReaderFactory.createXMLReader(
				                  "org.apache.xerces.parsers.SAXParser"
			                   );

            parser.setContentHandler(builder);
            parser.parse( new InputSource( new InputStreamReader(is) ) );

        }
        catch (IOException except){
            throw new MappingException("mapping.missingDBConf",is.toString());
        }
	    catch (SAXException except){
            throw new MappingException(except);
        }

        //return current configuration
        return builder.getConfiguration();
    }

    /**
     *
     * Return the parsed configuration tree.
     *
	 * @param fileName name of file to parse
	 * @return Configuration is the configuration tree
	 * @throws MappingException
     */
    public static Configuration build(String fileName) throws MappingException{

        try{
			return build(new FileInputStream( new File(fileName)) );
		}
		catch(FileNotFoundException e){
			throw new MappingException(e);
		}
    }
}
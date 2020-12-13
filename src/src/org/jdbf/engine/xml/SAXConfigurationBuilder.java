/*
 * 04/04/2002 - 23:12:27
 *
 * $RCSfile: SAXConfigurationBuilder.java,v $ - JDBF Object Relational mapping system
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

import java.util.Stack;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import org.jdbf.castor.Messages;
import org.jdbf.engine.configuration.Configuration;
import org.jdbf.engine.configuration.ConfigurationImpl;

/**
 *
 * <code>SAXConfigurationBuilder</code> is the class that builds 
 * the SAX parser to parse configuration file.
 * 
 * This class is based on an implementation in Avalon.
 * That implementation is in org.apache.avalon.SAXConfigurationBuilder.
 * The author of that source is Pierpaolo Fumagalli. 
 * 
 */
public class SAXConfigurationBuilder extends DefaultHandler {

   /**
    * Class name
    */
   private static final String CLASS_NAME = 
                        "org.jdbf.engine.xml.SAXConfigurationBuilder";
   /** Logger object */
   private Logger logger;

   /** root of this configuration */
   private Configuration root;

   /** Stack object */
   private Stack stack = new Stack();


   /**
    * 
    * Creates object
    *
    */
   public SAXConfigurationBuilder(){
       logger = Logger.getLogger(CLASS_NAME);  
   }
   
   
   /**
    *
    * Return the parsed configuration tree.
    *
	* @return Configuration
    */
   public Configuration getConfiguration() {
       return(this.root);
   }

   /**
    *
    * Receive notification of the beginning of an element.
    *
	* @param uri - The Namespace URI, or the empty string if the element has no 
	*         Namespace URI or if Namespace processing is not being performed.
	* @param localName - The local name (without prefix), or the empty string if 
	*        Namespace processing is not being performed.
	* @param name - The qualified name (with prefix), or the empty string if 
	*        qualified names are not available.
    * @param atts - The attributes attached to the element. If there are no 
	*         attributes, it shall be an empty Attributes object.
    * @throws SAXException - Any SAX exception, possibly wrapping another exception
    * 
    */
   public void startElement (String uri,String localName,
		                     String name, Attributes atts) throws SAXException{
       
       logger.log(Level.FINEST,Messages.format("SAXParser.startEle",name));
       ConfigurationImpl conf = new ConfigurationImpl(name);
  
       if (this.root == null) this.root = conf;

       if ( !stack.isEmpty() ){
           ((ConfigurationImpl)stack.peek()).addConfiguration(conf);
       }
       stack.push(conf);
   }


   /**
    *
    * Receive notification of the beginning of a document.
    *
    * @throws SAXException - Any SAX exception, possibly wrapping another exception
    */
   public void startDocument () throws SAXException{
       logger.log(Level.FINEST,Messages.message("SAXParser.startDoc"));
       root = null;        
	   stack.clear();
   }


   /**
    *
    * Receive notification of character data.
	*    
    * @param ch - The characters.
    * @param start - The start position in the character array.
    * @param len - The number of characters to use from the character array.
	*
    */
    public void characters(char ch[], int start, int len) {
        logger.log(Level.FINEST,Messages.message("SAXParser.readChar"));
        ConfigurationImpl c = (ConfigurationImpl) this.stack.peek();
		c.appendValueData( new String(ch,start,len) );		
    }


   /**
    *
    * Receive notification of the end of an element.
    *
	* @param uri - The Namespace URI, or the empty string if the element has no 
	*         Namespace URI or if Namespace processing is not being performed.
	* @param localName - The local name (without prefix), or the empty string if 
	*        Namespace processing is not being performed.
	* @param qName - The qualified name (with prefix), or the empty string if 
	*        qualified names are not available.   
    * @throws SAXException - Any SAX exception, possibly wrapping another exception
    */
    public void endElement (String uri, String localName, String qName) 
		throws SAXException{
		
		logger.log(Level.FINEST,Messages.format("SAXParser.endEle",qName));
		stack.pop();
    }
}
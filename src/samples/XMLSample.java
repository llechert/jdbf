/*
* May 20, 2004 - 4:42:25 PM
*
* $RCSfile: XMLSample.java,v $ - JDBF Object Relational mapping system
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

import org.jdbf.engine.sql.Cursor;

public class XMLSample {
    
    /**
     * 
     */
    public XMLSample() {
        super();
        
    }

    public static void main(String[] args) {
    	try{
    		SelectSample sample = new SelectSample();
    		Cursor results = (Cursor)sample.selectAll("product");
    		Product prod = (Product)results.first();
    		String xml = prod.marshall();
    		System.out.println("===== Marshall =====");
    		System.out.println(xml);
    		prod = (Product)Product.unmarshall(xml);
			System.out.println("===== UnMarshall =====");
    		System.out.println(prod);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
}

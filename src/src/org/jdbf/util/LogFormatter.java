/*
* 18/05/2004 - 12:35:27
*
* $RCSfile: LogFormatter.java,v $ - JDBF Object Relational mapping system
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

package org.jdbf.util;

import java.io.*;
import java.text.*;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * <code>LogFormatter</code> is an utility class that handles the format of log
 * This format is show below: <br>
 * [current_date current_time] source_class_name.source_method_name(paramaters)<br>
 * Level_log: message <br>
 * 
 * For example [ 18-mag-2004 12.41.09 ] org.jdbf.engine.xml.XPathManager.getBeanDescriptor()<br>
 * INFO: Parsering mapped information for product
 * 
 * 
 * You can change this format. To do this you must implement a new class and
 * configure it as formatter in logging.properties
 *
 */
public class LogFormatter extends Formatter {

	/**
	 * Current date
	 */
    private Date dat = new Date();
    
    /**
     * Date time format
     */
    private final static String format = "{0,date} {0,time}";
    
    /**
     * MessageFormat object
     */
    private MessageFormat formatter;

	private Object args[] = new Object[1];

    // Line separator string.  This is the value of the line.separator
    // property at the moment that the LogFormatter was created.
    /**
     * Line separator
     */
    private String lineSeparator = System.getProperty("line.separator");

    /**
     * Format the given LogRecord.
     * @param record the log record to be formatted.
     * @return String a formatted log record
     */
    public synchronized String format(LogRecord record) {
		StringBuffer sb = new StringBuffer();
		// Minimize memory allocations here.
		dat.setTime(record.getMillis());
		args[0] = dat;
		StringBuffer text = new StringBuffer("[ ");
		if (formatter == null) {
	    	formatter = new MessageFormat(format);
		}
		formatter.format(args, text, null);
		sb.append(text);
		sb.append(" ] ");
		if (record.getSourceClassName() != null) {	
	    	sb.append(record.getSourceClassName());
		}
		else {
	    	sb.append(record.getLoggerName());
		}
		if (record.getSourceMethodName() != null) {	
	    	sb.append(".");
	    	sb.append(record.getSourceMethodName());
		}
		sb.append("(");
		Object[] params = record.getParameters();
		if(params != null){
			for(int i = 0; i < params.length; i++){
				sb.append(params[i]);
				if(params.length > i + 1)
					sb.append(",");				
			}	
		}
		sb.append(")");
		sb.append(lineSeparator);
		String message = formatMessage(record);
		sb.append(record.getLevel().getLocalizedName());
		sb.append(": ");
		sb.append(message);
		sb.append(lineSeparator);
		if (record.getThrown() != null) {
	    	try {
	        	StringWriter sw = new StringWriter();
	        	PrintWriter pw = new PrintWriter(sw);
	        	record.getThrown().printStackTrace(pw);
	        	pw.close();
				sb.append(sw.toString());
	    	}
	    	catch (Exception ex) {}
		}
		return sb.toString();
    }
}

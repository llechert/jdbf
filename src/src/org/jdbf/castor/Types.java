/**
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "Exolab" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of Exoffice Technologies.  For written permission,
 *    please contact info@exolab.org.
 *
 * 4. Products derived from this Software may not be called "Exolab"
 *    nor may "Exolab" appear in their names without prior written
 *    permission of Exoffice Technologies. Exolab is a registered
 *    trademark of Exoffice Technologies.
 *
 * 5. Due credit should be given to the Exolab Project
 *    (http://www.exolab.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY EXOFFICE TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * EXOFFICE TECHNOLOGIES OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 1999 (C) Exoffice Technologies Inc. All Rights Reserved.
 *
 */

/*
 * The original code is in package org.exolab.castor.mapping.loader.
 * It has been repackaged for jdbf.
 */
package org.jdbf.castor;



import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.math.BigDecimal;

// packages changed for opl
import org.jdbf.engine.mapping.MappingException;
import org.jdbf.util.MimeBase64Encoder;
import org.jdbf.util.MimeBase64Decoder;


/**
 * Type information. Can be used to convert from class to class,
 * get default value for every class.
 *
 */
public class Types{


    /**
     * List of all the default convertors between Java types.
     */
    static TypeConvertorInfo[] _typeConvertors = new TypeConvertorInfo[] {
        // Convertors to boolean
        new TypeConvertorInfo( java.lang.Short.class, java.lang.Boolean.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Boolean( ( (Short) obj ).shortValue() != 0 );
            }
            public String toString() { return "Short->Boolean"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.Boolean.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Boolean( ( (Integer) obj ).intValue() != 0 );
            }
            public String toString() { return "Integer->Boolean"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.lang.Boolean.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                switch ( ( (String) obj ).length() ) {
                    case 0: return Boolean.FALSE;
                    case 1: char ch = ( (String) obj ).charAt( 0 );
                        if (param == null || param.length() != 2 )
                            return ( ch == 'T' || ch == 't'  ) ? Boolean.TRUE : Boolean.FALSE;
                        else 
                            return ( ch == param.charAt( 1 ) ) ? Boolean.TRUE : Boolean.FALSE;
                    case 4: return ( (String) obj ).equalsIgnoreCase( "true" ) ? Boolean.TRUE : Boolean.FALSE;
                    case 5: return ( (String) obj ).equalsIgnoreCase( "false" ) ? Boolean.TRUE : Boolean.FALSE;
                }
                return Boolean.FALSE;
            }
            public String toString() { return "String->Boolean"; }
        } ),
	new TypeConvertorInfo( java.math.BigDecimal.class,
			       java.lang.Boolean.class, new TypeConvertor() {
	    public Object convert( Object obj, String param ) {
		return new Boolean( ( (java.math.BigDecimal) obj).intValue() != 0 );
	    }
	    public String toString() { return "BigDecimal->Boolean"; }
        } ),
        new TypeConvertorInfo( java.lang.Byte.class, java.lang.Boolean.class,
new TypeConvertor() {
            public Object convert( Object obj, String param ) {
		return new Boolean( ( (Byte) obj ).shortValue() != 0 );
	    }
	    public String toString() { return "Byte->Boolean"; }
        } ),

	// Convertors to integer
        new TypeConvertorInfo( java.lang.Byte.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Integer( ( (Byte) obj ).intValue() );
            }
            public String toString() { return "Byte->Integer"; }
        } ),
        new TypeConvertorInfo( java.lang.Short.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Integer( ( (Short) obj ).intValue() );
            }
            public String toString() { return "Short->Integer"; }
        } ),
        new TypeConvertorInfo( java.lang.Long.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Integer( ( (Long) obj ).intValue() );
            }
            public String toString() { return "Long->Integer"; }
        } ),
        new TypeConvertorInfo( java.lang.Float.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Integer( ( (Float) obj ).intValue() );
            }
            public String toString() { return "Float->Integer"; }
        } ),
        new TypeConvertorInfo( java.lang.Double.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Integer( ( (Double) obj ).intValue() );
            }
            public String toString() { return "Double->Integer"; }
        } ),
        new TypeConvertorInfo( java.math.BigDecimal.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Integer( ( (BigDecimal) obj ).intValue() );
            }
            public String toString() { return "BigDecimal->Integer"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return Integer.valueOf( (String) obj );
            }
            public String toString() { return "String->Integer"; }
        } ),
        new TypeConvertorInfo( java.util.Date.class, java.lang.Integer.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                _paramDateFormat.applyPattern( Types.getFullDatePattern( param ) );
                return new Integer( _paramDateFormat.format( (Date) obj ) );
            }
            public String toString() { return "Date->Integer"; }
       } ),
       new TypeConvertorInfo( java.lang.Boolean.class,
java.lang.Integer.class, new TypeConvertor() {
           public Object convert( Object obj, String param ) {
               return new Integer( ((Boolean) obj ).booleanValue() ? 1 : 0 );
           }
     	   public String toString() { return "Boolean->Integer"; }
       } ),

        // Convertors to long
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.Long.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Long( ( (Integer) obj ).longValue() );
            }
            public String toString() { return "Integer->Long"; }
        } ),
        new TypeConvertorInfo( java.lang.Short.class, java.lang.Long.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Long( ( (Short) obj ).longValue() );
            }
            public String toString() { return "Short->Long"; }
        } ),
        new TypeConvertorInfo( java.lang.Float.class, java.lang.Long.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Long( ( (Float) obj ).longValue() );
            }
            public String toString() { return "Float->Long"; }
        } ),
        new TypeConvertorInfo( java.lang.Double.class, java.lang.Long.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Long( ( (Double) obj ).longValue() );
            }
            public String toString() { return "Double->Long"; }
        } ),
        new TypeConvertorInfo( java.math.BigDecimal.class, java.lang.Long.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Long( ( (BigDecimal) obj ).longValue() );
            }
            public String toString() { return "BigDecimal->Long"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.lang.Long.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return Long.valueOf( (String) obj );
            }
            public String toString() { return "String->Long"; }
        } ),
        // Convertors to short
        new TypeConvertorInfo( java.lang.Byte.class, java.lang.Short.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Short( ( (Byte) obj ).shortValue() );
            }
            public String toString() { return "Byte->Short"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.Short.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Short( ( (Integer) obj ).shortValue() );
            }
            public String toString() { return "Integer->Short"; }
        } ),
        new TypeConvertorInfo( java.lang.Long.class, java.lang.Short.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Short( ( (Long) obj ).shortValue() );
            }
            public String toString() { return "Long->Short"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.lang.Short.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return Short.valueOf( (String) obj );
            }
            public String toString() { return "String->Short"; }
        } ),
       new TypeConvertorInfo( java.lang.Boolean.class, java.lang.Short.class,
new TypeConvertor() {
           public Object convert( Object obj, String param ) {
               return new Short( ((Boolean) obj ).booleanValue() ? (short)1 :
(short)0 );
           }
           public String toString() { return "Boolean->Short"; }
       } ),
   
        // Convertors to byte
        new TypeConvertorInfo( java.lang.Short.class, java.lang.Byte.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Byte( ( (Short) obj ).byteValue() );
            }
            public String toString() { return "Short->Byte"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.Byte.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Byte( ( (Integer) obj ).byteValue() );
            }
            public String toString() { return "Integer->Byte"; }
        } ),
        new TypeConvertorInfo( java.lang.Boolean.class, java.lang.Byte.class, new TypeConvertor() {
	     public Object convert( Object obj, String param ) {
		 return new Byte( ((Boolean) obj ).booleanValue() ? (byte)1 :
				  (byte)0 );
	     }
	     public String toString() { return "Boolean->Byte"; }
	} ),
    
	// Convertors to double
        new TypeConvertorInfo( java.lang.Float.class, java.lang.Double.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Double( ( (Float) obj ).floatValue() );
            }
            public String toString() { return "Float->Double"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.Double.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Double( (double) ( (Integer) obj ).intValue() );
            }
            public String toString() { return "Integer->Double"; }
        } ),
        new TypeConvertorInfo( java.lang.Long.class, java.lang.Double.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Double( (double) ( (Long) obj ).longValue() );
            }
            public String toString() { return "Long->Double"; }
        } ),
        new TypeConvertorInfo( java.math.BigDecimal.class, java.lang.Double.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Double( ( (BigDecimal) obj ).doubleValue() );
            }
            public String toString() { return "BigDecimal->Double"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.lang.Double.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return Double.valueOf( (String) obj );
            }
            public String toString() { return "Double->String"; }
        } ),
        // Convertors to float
        new TypeConvertorInfo( java.lang.Double.class, java.lang.Float.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Float( ( (Double) obj ).floatValue() );
            }
            public String toString() { return "Double->Float"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.Float.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Float( (float) ( (Integer) obj ).intValue() );
            }
            public String toString() { return "Integer->Float"; }
        } ),
        new TypeConvertorInfo( java.lang.Long.class, java.lang.Float.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Float( (float) ( (Long) obj ).longValue() );
            }
            public String toString() { return "Long->Float"; }
        } ),
        new TypeConvertorInfo( java.math.BigDecimal.class, java.lang.Float.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new Float( ( (BigDecimal) obj ).floatValue() );
            }
            public String toString() { return "BigDecimal->Float"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.lang.Float.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return Float.valueOf( (String) obj );
            }
            public String toString() { return "String->Float"; }
        } ),
        // Convertors to big decimal
        new TypeConvertorInfo( java.lang.Double.class, java.math.BigDecimal.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new BigDecimal( ( (Double) obj ).doubleValue() );
            }
            public String toString() { return "Double->BigDecimal"; }
        } ),
        new TypeConvertorInfo( java.lang.Float.class, java.math.BigDecimal.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new BigDecimal( ( (Float) obj ).floatValue() );
            }
            public String toString() { return "Float->BigDecimal"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.math.BigDecimal.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return BigDecimal.valueOf( ( (Integer) obj ).intValue() );
            }
            public String toString() { return "Integer->BigDecimal"; }
        } ),
        new TypeConvertorInfo( java.lang.Long.class, java.math.BigDecimal.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return BigDecimal.valueOf( ( (Long) obj ).longValue() );
            }
            public String toString() { return "Long->BigDecimal"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, java.math.BigDecimal.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new BigDecimal( (String) obj );
            }
            public String toString() { return "String->BigDecimal"; }
        } ),
        new TypeConvertorInfo( java.util.Date.class, java.math.BigDecimal.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                _paramDateFormat.applyPattern( Types.getFullDatePattern( param ) );
                return new BigDecimal( _paramDateFormat.format( (Date) obj ) + ".0" );
            }
            public String toString() { return "Date->BigDecimal"; }
        } ),
	new TypeConvertorInfo( java.lang.Boolean.class,
			       java.math.BigDecimal.class, new TypeConvertor() {
	    public Object convert( Object obj, String param ) {
		return BigDecimal.valueOf( ( (Boolean) obj ).booleanValue() ? 1 : 0 );
	    }
	    public String toString() { return "Boolean->BigDecimal"; }
	} ),
	// Convertors to string
        new TypeConvertorInfo( java.lang.Short.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj.toString();
            }
            public String toString() { return "Short->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj.toString();
            }
            public String toString() { return "Integer->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Long.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj.toString();
            }
            public String toString() { return "Long->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Float.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj.toString();
            }
            public String toString() { return "Float->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Double.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj.toString();
            }
            public String toString() { return "Double->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Object.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj.toString();
            }
            public String toString() { return "Object->String"; }
        } ),
        new TypeConvertorInfo( java.util.Date.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                if ( param == null || param.length() == 0 )
                    return obj.toString();
                else {
		    _paramDateFormat.applyPattern( param );
                    return _paramDateFormat.format( (Date) obj );
                }
            }
            public String toString() { return "Date->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Character.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new String( obj.toString() );
            }
            public String toString() { return "Character->String"; }
        } ),
        new TypeConvertorInfo( char[].class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new String( (char[]) obj );
            }
            public String toString() { return "chars->String"; }
        } ),
        new TypeConvertorInfo( byte[].class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                MimeBase64Encoder encoder;

                encoder = new MimeBase64Encoder();
                encoder.translate( (byte[]) obj );
                return new String( encoder.getCharArray() );
            }
            public String toString() { return "bytes->String"; }
        } ),
        new TypeConvertorInfo( java.lang.Boolean.class, java.lang.String.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                if ( param == null || param.length() != 2 )
                    return ( (Boolean) obj ).booleanValue() ? "T" : "F";
                else 
                    return ( (Boolean) obj ).booleanValue() ? param.substring( 1, 2 ) : param.substring( 0, 1 );
            }
            public String toString() { return "Boolean->String"; }
        } ),
        // Convertors to character/byte array
        new TypeConvertorInfo( java.lang.String.class, java.lang.Character.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return ( new Character( ( (String ) obj ).charAt( 0 ) ) );
            }
            public String toString() { return "String->Character"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, char[].class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return ( (String ) obj ).toCharArray();
            }
            public String toString() { return "String->chars"; }
        } ),
        new TypeConvertorInfo( java.lang.String.class, byte[].class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                MimeBase64Decoder decoder;

                decoder = new MimeBase64Decoder();
                decoder.translate( (String ) obj );
                return decoder.getByteArray();
            }
            public String toString() { return "String->bytes"; }
        } ),
        // Convertors to date
        new TypeConvertorInfo( java.lang.String.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                try {
                    if ( param == null || param.length() == 0 )
                        return _dateFormat.parse( (String) obj );
                    else {
                        _paramDateFormat.applyPattern( param );
                        return _paramDateFormat.parse( (String) obj );
                    }
                } catch ( ParseException except ) {
                    throw new IllegalArgumentException( except.toString() );
                }
            }
            public String toString() { return "String->Date"; }
        } ),
        new TypeConvertorInfo( java.lang.Integer.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                try {
                    _paramDateFormat.applyPattern( Types.getFullDatePattern( param ) );
                    return _paramDateFormat.parse( obj.toString() );
                } catch ( ParseException except ) {
                    throw new IllegalArgumentException( except.toString() );
                }
            }
            public String toString() { return "Integer->Date"; }
        } ),
        new TypeConvertorInfo( java.math.BigDecimal.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                try {
                    _paramDateFormat.applyPattern( Types.getFullDatePattern( param ) );
                    return _paramDateFormat.parse( obj.toString() );
                } catch ( ParseException except ) {
                    throw new IllegalArgumentException( except.toString() );
                }
            }
            public String toString() { return "BigDecimal->Date"; }
        } ),
        new TypeConvertorInfo( java.util.Date.class, java.sql.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new java.sql.Date( ( (java.util.Date) obj ).getTime() );
            }
            public String toString() { return "util.Date->sql.Date"; }
        } ),
        new TypeConvertorInfo( java.sql.Date.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj;
            }
            public String toString() { return "sql.Date->util.Date"; }
        } ),
        new TypeConvertorInfo( java.util.Date.class, java.sql.Time.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new java.sql.Time( ( (java.util.Date) obj ).getTime() );
            }
            public String toString() { return "util.Date->sql.Time"; }
        } ),
        new TypeConvertorInfo( java.sql.Time.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj;
            }
            public String toString() { return "sql.Time->util.Date"; }
        } ),
        new TypeConvertorInfo( java.util.Date.class, java.sql.Timestamp.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return new java.sql.Timestamp( ( (java.util.Date) obj ).getTime() );
            }
            public String toString() { return "util.Date->sql.Timestamp"; }
        } ),
        new TypeConvertorInfo( java.sql.Timestamp.class, java.util.Date.class, new TypeConvertor() {
            public Object convert( Object obj, String param ) {
                return obj;
            }
            public String toString() { return "sql.Timestamp->util.Date"; }
        } )
    };
	


    /**
     * Information about a specific Java type.
     */
    static class TypeInfo{

        /**
         * The short type name (e.g. <tt>integer</tt>).
         */
        final String  shortName;
        
        /**
         * The default value for the type, if known.
         */
        final Object  defValue;
        
        TypeInfo( String shortName, Object defValue ){
            this.shortName  = shortName;
            this.defValue   = defValue;
        }
        
    }

    
    /**
     * List of all the simple types supported by Castor.
     */
 static TypeInfo[] _typeInfos = new TypeInfo[] {
        //            shortName      defValue
        new TypeInfo( "string",      null ),
        new TypeInfo( "integer",     new Integer( 0 ) ),
        new TypeInfo( "long",        new Long( 0 ) ),
        new TypeInfo( "boolean",     Boolean.FALSE ),
        new TypeInfo( "double",      new Double( 0 ) ),
        new TypeInfo( "float",       new Float( 0 ) ),
        new TypeInfo( "bigdecimal",  new BigDecimal( 0 ) ),
        new TypeInfo( "byte",        new Byte( (byte) 0 ) ),
        new TypeInfo( "date",        new java.util.Date() ),
        new TypeInfo( "timestamp",   null ),
        new TypeInfo( "time",        null ),
        new TypeInfo( "short",       new Short( (short) 0 ) ),
        new TypeInfo( "char",        new Character( (char) 0 ) ),
       /* 
	new TypeInfo( "locale",      null,
                      java.util.Locale.class,     true,     null ),
        
          new TypeInfo( Stream,     "stream",      java.io.InputStream.class,  null ),
          new TypeInfo( Reader,     "reader",      java.io.Reader.class,       null ),
          new TypeInfo( XML,        "xml",         org.w3c.dom.Document.class, org.w3c.dom.Element.class ),
          new TypeInfo( Serialized, "ser",         java.io.Serializable.class, null )
        */
	
    };

    
    
    
    /**
     * Information used to locate a type convertor.
     */
    static class TypeConvertorInfo{
       
        /**
         *  The type being converted to.
         */
        final Class toType;
        
        /**
         * The type being converted from.
         */
        final Class fromType;

        /**
         * The convertor.
         */        
        final TypeConvertor convertor;
        
        TypeConvertorInfo( Class fromType, Class toType, TypeConvertor convertor ){
            this.fromType  = fromType;
            this.toType    = toType;
            this.convertor = convertor;
        }
        
    }


    /**
     * Date format used by the date convertor when nonempy parameter 
     * is specified.
     */
    private static SimpleDateFormat _paramDateFormat = new SimpleDateFormat();

    /**
     * Date format used by the date convertor.
     */
    private static DateFormat _dateFormat = new SimpleDateFormat();
	
	
    /**
     * Returns the default value for this Java type (e.g. 0 for integer, empty
     * string) or null if no default value is known. The default value only
     * applies to primitive types (that is, <tt>Integer.TYPE</tt> but not
     * <tt>java.lang.Integer</tt>).
     *
     * @param shortName name
     * @return The default value or null
     */
    public static Object getDefault(String shortName){
        for ( int i = 0 ; i < _typeInfos.length ; ++i ) {
            if ( _typeInfos[ i ].shortName == shortName )
                return _typeInfos[ i ].defValue;
        }
        return null;
    }


    /**
     * Returns a type convertor. A type convertor can be used to convert
     * an object from Java type <tt>fromType</tt> to Java type <tt>toType</tt>.
     *
     * @param fromType The Java type to convert from
     * @param toType The Java type to convert to
     * @throws MappingException No suitable convertor was found
     */
    public static TypeConvertor getConvertor( Class fromType, Class toType )
        throws MappingException{
        // first seek for exact match
        // TODO: the closest possible match
        for ( int i = 0 ; i < _typeConvertors.length ; ++i ) {
            if ( _typeConvertors[ i ].fromType.equals( fromType ) &&
                 toType.equals( _typeConvertors[ i ].toType ) ) 
                return _typeConvertors[ i ].convertor;
        }

        // else seek for any match
        for ( int i = 0 ; i < _typeConvertors.length ; ++i ) {
            if ( _typeConvertors[ i ].fromType.isAssignableFrom( fromType ) &&
                 toType.isAssignableFrom( _typeConvertors[ i ].toType ) ) 
                return _typeConvertors[ i ].convertor;
        }
        throw new MappingException( "mapping.noConvertor", fromType.getName(), toType.getName() );
    }
    

    /**
     * Transforms short date format pattern into full format pattern
     * for SimpleDateFormat (e.g., "YMD" to "yyyyMMdd").
     *
     * @param pattern The short pattern
     * @return The full pattern
     */
    public static String getFullDatePattern( String pattern ){        
        StringBuffer sb;
        int len; 

        if ( pattern == null || pattern.length() == 0 )
            return "yyyyMMdd";
        
        sb = new StringBuffer();
        len = pattern.length();
        
        for ( int i = 0; i < len; i++ ) {
            switch ( pattern.charAt( i ) ) {
            case 'y': case 'Y': sb.append( "yyyy" ); break;
            case 'M':           sb.append( "MM" ); break;
            case 'd': case 'D': sb.append( "dd" ); break;
            case 'h': case 'H': sb.append( "HH" ); break;
            case 'm':           sb.append( "mm" ); break;
            case 's':           sb.append( "ss" ); break;
            case 'S':           sb.append( "SSS" ); break;
            }
        }        
        return sb.toString();
    }
    
}

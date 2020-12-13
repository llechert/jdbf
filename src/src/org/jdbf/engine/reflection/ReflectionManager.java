/*
* 03/12/2002 - 09:56:11
*
* $RCSfile: ReflectionManager.java,v $ - JDBF Object Relational mapping system
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
package org.jdbf.engine.reflection;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;

import org.jdbf.engine.basic.ObjectMapped;
import org.jdbf.engine.mapping.MappingException;



/**
 * <code>ReflectionManager</code> is the class that handles via reflection all 
 * operationa to object that has been mapped on repository.
 * These operations are the:<br>
 * <UL>
 *	<LI>creation of object
 *	<LI>return value of particular property
 *      <LI>set value of particular property
 *      <LI>return type of particular property
 * </UL>
 * 
 * This class is very important when a set of result is returned from result set, because this set of data
 * is needed to create a object that will be return in Cursor object.
 *
 */
public class ReflectionManager{

    /**
     * Class name
     */
    private static String CLASS_NAME = "org.jdbf.engine.reflection.ReflectionManager";

    /**
     * Create a ObjectMapped by a name and a hashmap that contains
     * a list with the names of the properties and relative values.
     * If props is null the object created is null.
     *
     * @param name  name fo class
     * @param props list property/value
     * @return      ObjectMapped
     * @throws MappingException
     */
    public static ObjectMapped createBean(String name, HashMap props)
    	throws MappingException {

		Class oBeanClass = null;
        ObjectMapped oBean = null;
        Method oMethod = null;

        String sProp;
	
	
        try {
            if(props == null){
				oBeanClass = Class.forName(name);
				oBean = (ObjectMapped)oBeanClass.newInstance();
	    	}
	    	else{
				Iterator eKeys = props.keySet().iterator();
				oBeanClass = Class.forName(name);
				oBean = (ObjectMapped)oBeanClass.newInstance();
				while (eKeys.hasNext()) {
		    		sProp = (String) eKeys.next();
		    		oMethod = ReflectionManager.getWriteMethod(oBean, sProp);

		    		ReflectionManager.invokeWriteMethod(oBean, oMethod,
														(String) props.get(sProp)
									   );		    
				}
	    	}
	    
        }
		catch (ClassNotFoundException e) {
      
			throw new MappingException("class.notFound",name);
		         
	    
        }
        catch (Exception e) {
	    
	    	throw new MappingException(e);
        }
        return oBean;
    }


    /**
     * Create a non primitive type for value
     *
     * @param  cls   Class object (primitive type)
     * @param  value Value
     * @return       Not primitive type
     *
     */
    public static Object createNotPrimitiveObject(Class cls,String value) {

		Object oValue = null;
        if (cls == Boolean.TYPE) {
            oValue = new Boolean(value);
        } else if (cls == Byte.TYPE) {
            oValue = new Byte(value);
        } else if (cls == Character.TYPE) {
            if ((value != null) && (value.length() > 0)) {
                oValue = new Character(value.charAt(0));
            }
	    else {		
			   throw new IllegalArgumentException("missing char");		          
            }
        } else if (cls == Short.TYPE) {
            oValue = new Short(value);
        } else if (cls == Integer.TYPE) {
            oValue = new Integer(value);
        } else if (cls == Long.TYPE) {
            oValue = new Long(value);
        } else if (cls == Float.TYPE) {
            oValue = new Float(value);
        } else if (cls == Double.TYPE) {
            oValue = new Double(value);
        }
        return oValue;
    }


    /**
     * Return the name of properties of ObjectMapped object.
     * Array contains the name of properties of the superclass only.
     *
     * @param  object    ObjectMapped object
     * @return String[]  Array that cotains the properties of superclass
     *
     */
    public static String[] getDeclaredFieldsName(ObjectMapped object) {
        return getPropertyNames(object, true);
    }


    /**
     * Return the name of properties of ObjectMapped object.
     * Array contains the name of properties of ObjectMapped object
     *
     * @param  object    ObjectMapped object
     * @return String[]  Array that contains the properties
     *
     */
    public static String[] getFieldsNames(ObjectMapped object) {
        return getPropertyNames(object, false);
    }


    /**
     * Return name of every property of ObjectMapped object.
     * This method return the properties of superclass also.
     *
     * @param  object    ObjectMapped object
     * @param   pk    <tt>true</tt> if properties of superclass,false otherwise
     * @return String[]       Array that contains every property
     *
     */
    public static String[] getPropertyNames(ObjectMapped object, boolean pk) {

        Object params[] = {object.getClassName(),new Boolean(pk)};
	
		boolean bPkProp;
        ArrayList vProp = new ArrayList();
        String[] aProp;
        String sName;

		Field[] fields = null;
		if(pk){
  	    	fields = object.getClass().getDeclaredFields();
		}
		else{
	    	fields = object.getClass().getFields();
		}

        //converts in array the vector that
		//contains then names of properties
        aProp = new String[fields.length];
        for (int i = 0; i < fields.length; ++i) {
            Field f = fields[i];	    
	    	aProp[i] = f.getName();
	    }
        return aProp;
    }


    /**
     * Return the type of property di un ObjectMapped object, 
     * specified the name of property.
     * This method returns the type returned by get method of propName
     *
     * @param  object    ObjectMapped object
     * @param  propName  name of property
     * @return Class     type of property
     * @throws MappingException
     */
    public static Class getPropertyType(ObjectMapped object, String propName)
    	throws MappingException {

        Class c = null;
		Object params[] = {object.getClassName(),propName};
        
		try{
  	    	PropertyDescriptor pd = new PropertyDescriptor(propName,object.getClass());
	    	c =  pd.getPropertyType();
        }
        catch(IntrospectionException e){            
			throw new MappingException(e);
        }
		return c;
    }


    /**
     * Return the Method object.This object is the get method of property
     * specified in propName.
     *
     * @param  object    ObjectMapped object
     * @param  propName  name of property
     * @return           Method object
     * @throws MappingException
     *
     */
    public static Method getReadMethod(ObjectMapped object,String propName)
		throws MappingException {

		Method method = null;
		Object params[] = {object.getClassName(),propName};
        
		try{
 	    	//Return the Method object
	    	PropertyDescriptor pd = new PropertyDescriptor(propName,object.getClass());
	    	method = pd.getReadMethod();
		}
		catch (IntrospectionException e) {
	    
			throw new MappingException(e);
		}
		return method;
    }


    /**
     * Return value of property of the ObjectMapped object.
     *
     * @param  object    ObjectMapped object
     * @param  propName  name of property
     * @return           value of property specified in propName;
     * @throws MappingException
     */
    public static Object getValueByName(ObjectMapped object, String propName)
    	throws MappingException {
        
        Object value = null;
        Method method = null;
		
		try {

	    	method = ReflectionManager.getReadMethod(object, propName);
	    	value = method.invoke(object, null);
		}
		catch (InvocationTargetException e) {
	    	throw new MappingException("field.invokeTarget",method.getName());
		}
		catch (Exception e) {
	    	throw new MappingException(e);
		}
		return value;
    }


    /**
     * Return the Method object.This object is the set method of property
     * specified in propName.
     *
     * @param  object    ObjectMapped object
     * @param  propName  name of property
     * @return           Method object
     * @throws MappingException
     *
     */
    public static Method getWriteMethod(ObjectMapped object,String propName)
		throws MappingException{
	
		Method method = null;
		Object params[] = {object.getClassName(),propName};
        
        try{
            //Return the Method object
  	    	PropertyDescriptor pd = new PropertyDescriptor(propName,object.getClass());
	    	method = pd.getWriteMethod();
        }
        catch(IntrospectionException e){
            throw new MappingException(e);
        }
		
		return method;
    }


    /**
     * Invoke setter method of a property
     *
     * @param  object    ObjectMapped object
     * @param  method    Mehod object
     * @param  value   	 value to set
     * @throws MappingException
     *
     */
    public static void invokeWriteMethod(ObjectMapped object, Method method, Object value)
		throws MappingException {

		try {
	    	//invoke the setter method
            method.invoke(object, new Object[]{value});
        }
		catch (InvocationTargetException e) {
            throw new MappingException("mapping.noSuchMethod",
	    		       object.getClass().getName(),method.getName()
	    			      );
	    }
        catch (Exception e) {
            throw new MappingException(e);
        }
    }


    /**
     * Invoke the setter method. It converts the value as parameter.
     * <tt>String</tt>   nothing
     * <tt>int</tt>      Integer.parseInt(value)
     * <tt>double</tt>   Double.parseDouble(value)
     * Finally if value is primitive type , it tries to wrapper it into on primitive type.
     *
     * @param  object    ObjectMapped object
     * @param  method    Mehod object
     * @param  newValue   	 value to set
     * @throws InvocationTargetException
     *
     */
    public static void invokeWriteMethod(ObjectMapped object, Method method,String newValue)
		throws MappingException {

		Object value = newValue;
        Constructor oConstr = null;
	
	    Class oPClass = method.getParameterTypes()[0];

        try {
            if (oPClass.isPrimitive()) {
	        	value = createNotPrimitiveObject(oPClass, newValue);
            }
	    	else{
	        	if (oPClass != String.class) {
                    oConstr = oPClass.getConstructor(new Class[]{String.class});
                    value = oConstr.newInstance(new Object[]{newValue});
                }
            }
            // invoke the settter method
            method.invoke(object, new Object[]{value});
        }
        catch (NoSuchMethodException e) {
            
			throw new MappingException("mapping.noSuchMethod",
				       object.getClass().getName(),method.getName());		    
        }
        catch (InvocationTargetException e) {
            
			throw new MappingException("field.invokeTarget",
		                        method.getName());	    
        }
        catch (Exception e) {            
			throw new MappingException(e);	    
        }
    }


    /**
     * Return true if field specified in propertyName is primitive,
     * false otherwise
     *
     * @param object
     * @param propertyName
     * @return true if primitive,false otherwise
     *
     */
    public static boolean isPrimitive(ObjectMapped object,String propertyName)
        throws MappingException{

        Class c = ReflectionManager.getPropertyType(object, propertyName);
        return c.isPrimitive();
    }


   /**
     * Set the value of property of ObjectMapped object
     *
     * @param  object    ObjectMapped object
     * @param  propName  name of property
     * @param  value     value to set in the property
     * @return ObjectMapped
     * @throws MappingException
     */
    public static ObjectMapped  setValueByName(ObjectMapped object, String propName,
					       					   Object value)
		throws MappingException {
			
        Method method = null;
		try {	    
	    	method = ReflectionManager.getWriteMethod(object, propName);

	    	if (value instanceof String) {
                invokeWriteMethod(object, method, (String) value);
            }
	    	else{
                invokeWriteMethod(object, method, value);
            }
        }
        catch (Exception e) {
            
			throw new MappingException(e);
        }

        return object;
    }

}
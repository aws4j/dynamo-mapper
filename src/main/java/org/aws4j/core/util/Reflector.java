package org.aws4j.core.util;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aws4j.data.dynamo.util.PropertyPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reflector {

	private static Logger log = LoggerFactory.getLogger(Reflector.class);

	public static Object getPropertyValue(Object obj, String propertyPath) {

		if (StringUtil.isEmpty(propertyPath))
			throw new IllegalArgumentException();

		PropertyPath propPath = new PropertyPath(propertyPath);

		// log.debug( "propertyPath = " + propertyPath );
		// log.debug( "getFirstProperty = " + propPath.getFirstProperty() );

		Method getter = getGetterMethod(obj.getClass(),
				propPath.getFirstProperty());
		Object value = invoke(obj, getter);

		if (value == null)
			return null;

		if (propPath.hasSecondProperty()) {
			return getPropertyValue(value, propPath.getSecondProperty());
		} else {
			return value;
		}
	}

	public static List<Annotation> getAnnotations(Class<?> clazz,
			PropertyDescriptor property) {

		Method getter = property.getReadMethod();

		List<Annotation> annotations = new ArrayList<Annotation>();

		annotations.addAll(Arrays.asList(getter.getDeclaredAnnotations()));
		Field propertyField = getFieldOrNull(getter.getDeclaringClass(),
				property.getName());
		if (propertyField != null) {
			annotations.addAll(Arrays.asList(propertyField
					.getDeclaredAnnotations()));
		}
		return annotations;
	}

	public static List<PropertyDescriptor> getPropertyDescriptors(Class<?> clazz) {

		List<PropertyDescriptor> propertyList = new ArrayList<PropertyDescriptor>();

		List<PropertyDescriptor> properties = Beans.getPropertyDescriptors(clazz);
		for (PropertyDescriptor property : properties) {
			// if( property.getReadMethod() != null && property.getWriteMethod()
			// != null ) {
			if (property.getReadMethod() != null) {
				propertyList.add(property);
			}
		}
		return propertyList;
	}

	public static Field getField(Class<?> clazz, String name) {
		Field field = getFieldOrNull(clazz, name);
		if (field == null) {
			throw new IllegalArgumentException("Field " + name
					+ " not found in " + clazz.getName());
		}
		return field;
	}

	public static Field getFieldOrNull(Class<?> clazz, String name) {

		// log.trace( "getField " + clazz.toString() + " : " + name );

		try {
			return clazz.getDeclaredField(name);
		} catch (NoSuchFieldException e) {

		} catch (SecurityException e) {
			// for Check SuperClass
		}

		if (clazz.getSuperclass() != null) {
			Class<?> superClass = clazz.getSuperclass();
			return getFieldOrNull(superClass, name);
		} else {
			return null;
		}
	}

	public static Method getGetterMethod(Class<?> clazz, String name) {

		// log.trace( clazz + ":" + name );

		PropertyDescriptor propDesc = Beans.getPropertyDescriptor(clazz, name);

		try {
			return propDesc.getReadMethod();
		} catch (SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Method getSetterMethod(Class<?> clazz, String name) {

		PropertyDescriptor propDesc = Beans.getPropertyDescriptor(clazz, name);

		try {
			return propDesc.getWriteMethod();
		} catch (SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Object invoke(Object obj, Method method, Object... args) {

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static <T> T newInstance(Class<T> clazz) {

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static Class<?> getGenericFirstParameter(Type propertyType) {
		if (propertyType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) propertyType;
			Type[] typeArgs = parameterizedType.getActualTypeArguments();
			if ( typeArgs.length >= 1 ) {
				return (Class<?>) parameterizedType.getActualTypeArguments()[0];
			}
		}
		return null;
	}

	public static Class<?> getGenericSecondParameter(Type propertyType) {
		if (propertyType instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) propertyType;
			Type[] typeArgs = parameterizedType.getActualTypeArguments();
			if ( typeArgs.length >= 2 ) {
				return (Class<?>) parameterizedType.getActualTypeArguments()[1];
			}
		}
		return null;
	}
}

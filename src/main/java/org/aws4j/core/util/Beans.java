package org.aws4j.core.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

public class Beans {

	// TODO all
	private static BeanInfo getBeanInfo( Class<?> clazz ) {
		try {
			return Introspector.getBeanInfo( clazz );
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public static List<PropertyDescriptor> getPropertyDescriptors( Class<?> clazz ) {
		return Arrays.asList( getBeanInfo( clazz ).getPropertyDescriptors() );
	}

	public static PropertyDescriptor getPropertyDescriptor( Class<?> clazz, String name ) {
		try {
			return new PropertyDescriptor( name, clazz );
		} catch (IntrospectionException e) {
			throw new IllegalArgumentException(e);
		}
	}

}

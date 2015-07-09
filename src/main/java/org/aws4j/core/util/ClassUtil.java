package org.aws4j.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtil {

	@SuppressWarnings("unchecked")
	public static <T extends Object> Class<T> getClass( T obj ) {
		return (Class<T>) obj.getClass();
	}

	public static Class<?> getClass( Type type ) {
		if ( type instanceof ParameterizedType ) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			return (Class<?>) parameterizedType.getRawType();
		}
		else {
			return (Class<?>) type.getClass();
		}
	}
}

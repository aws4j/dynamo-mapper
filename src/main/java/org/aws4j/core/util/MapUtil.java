package org.aws4j.core.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class MapUtil {

	public static boolean isMap( Type type ) {
		Class<?> clazz = null;
		if (type instanceof Class) {
			clazz = (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			clazz = (Class<?>) parameterizedType.getRawType();
		}

		if ( Map.class.isAssignableFrom( clazz ) ) {
			return true;
		} else {
			return false;
		}

	}

	// TODO
	public static Class<?> getGenericKeyParameter( Type type ) {
		return Reflector.getGenericFirstParameter( type );
	}

	// TODO
	public static Class<?> getGenericValueParameter( Type type ) {
		return Reflector.getGenericSecondParameter( type );
	}

}

package org.aws4j.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aws4j.data.core.exception.CollectionIsEmptyException;

public class Collections {

	public static Class<?> getGenericParameter( Type type ) {
		return Reflector.getGenericFirstParameter( type );
	}

	public static boolean isCollectionType(Type type) {
		Class<?> clazz = null;
		if (type instanceof Class) {
			clazz = (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			clazz = (Class<?>) parameterizedType.getRawType();
		}

		if (Collection.class.isAssignableFrom(clazz)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isList( Type type ) {
		Class<?> clazz = null;
		if (type instanceof Class) {
			clazz = (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			clazz = (Class<?>) parameterizedType.getRawType();
		}

		if ( List.class.isAssignableFrom( clazz ) ) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean isSet( Type type ) {
		Class<?> clazz = null;
		if (type instanceof Class) {
			clazz = (Class<?>) type;
		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;
			clazz = (Class<?>) parameterizedType.getRawType();
		}

		if ( Set.class.isAssignableFrom( clazz ) ) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean isCollection(Object obj) {
		return (obj instanceof Collection) ? true : false;
	}

	public static boolean containsClass(Collection collection, Class<?> clazz) {

		for (Object obj : collection) {
			if (obj instanceof Annotation) {
				Annotation annotation = (Annotation) obj;
				if (clazz.equals(annotation.annotationType())) {
					return true;
				}
			} else {
				if (clazz.equals(obj.getClass())) {
					return true;
				}
			}
		}
		return false;
	}

	public static Class<?> getCollectionClass(Collection<?> collection) {
		Object item = getFirstOrNull(collection);
		if (item != null) {
			return item.getClass();
		} else {
			return null;
		}

	}

	// TODO for what?
	public static <K, V> Map<K, V> toMap(Collection<K> keys, Class<V> modelClass) {

		Map<K, V> map = new HashMap<K, V>();
		for (K key : map.keySet()) {
			map.put(key, null);
		}
		;
		return map;
	}

	public static <T> T getFirst(Collection<T> collection) {
		T result = getFirstOrNull(collection);
		if (result != null) {
			return result;
		}
		throw new CollectionIsEmptyException();
	}

	public static <T> T getFirstOrNull(Collection<T> collection) {

		if (Iterables.isEmpty(collection))
			return null;

		return Iterables.get(collection, 0);
	}

	public static Collection newCollection(Class<? extends Collection> clazz) {

		if (List.class.isAssignableFrom(clazz)) {
			return new ArrayList();
		} else if (Set.class.isAssignableFrom(clazz)) {
			return new HashSet();
		} else {
			throw new IllegalArgumentException("Not Supported Collection type."
					+ clazz);
		}
	}
}

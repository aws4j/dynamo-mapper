package org.aws4j.core.util;

import java.lang.reflect.Type;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class JacksonUtil {

	public static JavaType getJavaType(Type type) {
		return TypeFactory.defaultInstance().constructType(type);
	}

}

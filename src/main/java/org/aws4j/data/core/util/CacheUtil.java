package org.aws4j.data.core.util;

import org.aws4j.data.dynamo.model.Cacheable;

public class CacheUtil {

	public static <M> Cacheable getCacheable( Class<M> modelClass ) {
		return modelClass.getAnnotation(Cacheable.class);
	}
}

package org.aws4j.data.core.util;

import org.aws4j.data.core.Key;

public class KeyUtil {

	public static Key getKey( Object model ) {
		return Models.getKey( model );
	}

	public static <M> Key createKey( Class<M> modelClass, String keyString ) {
		return Models.createKey( modelClass, keyString );
	}

}

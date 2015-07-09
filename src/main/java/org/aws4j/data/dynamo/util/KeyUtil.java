package org.aws4j.data.dynamo.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aws4j.core.util.BaseEncoder;
import org.aws4j.core.util.Reflector;
import org.aws4j.data.dynamo.model.DynamoKey;

public class KeyUtil {

	private static final String DEFAULT_ENCODE = "UTF-8";

	public static String encode( String plainKeyString ) {
		return plainKeyString;
		//return BaseEncoder.encode( plainKeyString );
	}

	public static String decode( String encodedKeyString ) {
		return BaseEncoder.decode( encodedKeyString );
	}

	public static <T extends Collection> T makeKeyCollection( Class<T> collectionClass,  Collection<String> keyStrings ) {

		T collection = Reflector.newInstance( collectionClass );
		for ( String keyString : keyStrings ) {
			collection.add( KeyUtil.decode( keyString ) );
		}
		return collection;
	}

	public static <T extends Collection<String>> T makeKeyStringCollection( Class<T> collectionClass,  Collection<DynamoKey> keys ) {

		T collection = Reflector.newInstance( collectionClass );
		for ( DynamoKey key : keys ) {
			collection.add( key.getKeyString() );
		}
		return collection;
	}
}

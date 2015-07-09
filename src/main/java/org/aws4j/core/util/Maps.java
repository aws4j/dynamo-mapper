package org.aws4j.core.util;

import java.util.HashMap;
import java.util.Map;

public class Maps {

	public static <K, V> Map<K, V> newHashMap( Pair<K, V>... pairs ) {
		Map<K, V> map = new HashMap<K, V>();
		for( Pair<K, V> pair : pairs ) {
			map.put( pair.getKey(), pair.getValue() );
		}
		return (Map<K, V>)map;
	}

	public static <K, V> Pair<K, V> pair( K key, V val ) {
		return new Pair<K, V>( key, val );
	}
}

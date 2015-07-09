package org.aws4j.core.util;

import java.util.Map;

public class Pair<K, V> implements Map.Entry<K, V> {

	private K key;
	private V val;

	public Pair( K key, V val ) {
		this.key = key;
		this.val = val;
	}

	@Override
	public K getKey() {
		return key;
	}

	@Override
	public V getValue() {
		return val;
	}

	@Override
	public V setValue( V value ) {
		return value;
	}
}
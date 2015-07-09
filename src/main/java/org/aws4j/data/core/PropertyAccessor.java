package org.aws4j.data.core;

public interface PropertyAccessor<M, V> {

	V get( M model );

	void set( M model, V value );
}

package org.aws4j.core.util;

import org.aws4j.core.exception.NotImplementedException;

public class Iterables {

	public static <E> boolean isEmpty( Iterable<E> e ) {
		// TODO
		return com.google.common.collect.Iterables.isEmpty( e );
	}

	public static <E> int size( Iterable<E> e ) {
		return com.google.common.collect.Iterables.size( e );
	}

	public static <E> E get( Iterable<E> e, int position ) {
		return com.google.common.collect.Iterables.get( e, position );
	}

}

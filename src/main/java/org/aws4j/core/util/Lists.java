package org.aws4j.core.util;

import java.util.ArrayList;
import java.util.List;

public class Lists {

	public static <E> List<E> newArrayList() {
		return new ArrayList<E>();
	}

	public static <E> List<E> newArrayList( E e ) {
		List<E> list = new ArrayList<E>();
		list.add( e );
		return list;
	}

	public static <E> List<E> asList( E first, E... second ) { return null; }
}

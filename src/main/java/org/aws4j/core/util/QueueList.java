package org.aws4j.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class QueueList<T> {

	private LinkedList<T> values;

	public QueueList( Collection<T> values ) {
		this.values = new LinkedList<T>( values );
	}

	public List<T> poll( int maxSize ) {
		List<T> list = new ArrayList<T>();
		for ( int i = 0 ; i < maxSize ; i++ ) {
			T value = values.poll();
			if ( value == null ) {
				return list;
			}
			list.add( value );
		}
		return list;
	}

	public boolean isNotEmpty() {
		return ! values.isEmpty();
	}

	public boolean isEmpty() {
		return values.isEmpty();
	}
}

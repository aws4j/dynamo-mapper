package org.aws4j.core.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

	public static <E> List<E> getSubList( List<E> list , int start, int end ) {

		// TODO listの型を見て生成
		List<E> results = new ArrayList<E>();
		for ( int i = start ; i <= end ; i++ ) {
			results.add( list.get( i ) );
		}
		return results;
	}
}

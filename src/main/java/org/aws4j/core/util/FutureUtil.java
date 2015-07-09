package org.aws4j.core.util;

import java.util.concurrent.Future;

public class FutureUtil {

	public static <T> T getResult( Future<T> task ) {

		try {
			return task.get();
		}
		catch ( Exception e ) {
			throw new IllegalStateException( e );
		}
	}
}

package org.aws4j.core.util;

import java.io.UnsupportedEncodingException;

import org.aws4j.core.exception.NotImplementedException;

public class StringUtil {

	private static final String DEFAULT_ENCODING = "UTF-8";

	public static byte[] getBytes(String str) {
		try {
			return str.getBytes(DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("This is Unexpected Exception.");
		}
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static String[] split(String str, String delimiter) {
		throw new NotImplementedException();// StringUtils.split( str, delimiter
											// );
	}

	public static String[] split(String str, String delimiter, int limit) {
		throw new NotImplementedException();// StringUtils.split( str,
											// delimiter, limit );
	}

	public static String join(String separator, Iterable<String> strings) {
		throw new NotImplementedException();// StringUtils.join
	}
}

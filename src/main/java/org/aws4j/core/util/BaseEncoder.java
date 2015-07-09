package org.aws4j.core.util;

import java.io.UnsupportedEncodingException;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Longs;

public class BaseEncoder {

	private static final String DEFAULT_ENCODE = "UTF-8";

	public static String encode( String str ) {
		return new String( BaseEncoding.base64Url().omitPadding().encode( StringUtil.getBytes( str ) ) );
	}

	public static String decode( String encodedKeyString ) {
		try {
			return new String( BaseEncoding.base64Url().omitPadding().decode( encodedKeyString ), DEFAULT_ENCODE );
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	public static String encode( Long l ) {
		return new String( BaseEncoding.base32Hex().omitPadding().encode( Longs.toByteArray( l ) ) );
	}
}

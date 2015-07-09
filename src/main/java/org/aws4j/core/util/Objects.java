package org.aws4j.core.util;



public class Objects {

	public static int hashCode( Object... objects ) {
		return com.google.common.base.Objects.hashCode(objects);
	}

	public static boolean equal( Object me, Object other ) {
		return com.google.common.base.Objects.equal(me, other);
	}
}

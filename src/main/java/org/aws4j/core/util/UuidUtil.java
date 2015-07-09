package org.aws4j.core.util;

import java.util.UUID;

public class UuidUtil {

	public static String randomUuid() {
		return UUID.randomUUID().toString();
	}
}

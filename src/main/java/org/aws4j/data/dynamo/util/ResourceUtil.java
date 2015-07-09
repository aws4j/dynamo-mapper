package org.aws4j.data.dynamo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by ats on 2014/04/21.
 */
public class ResourceUtil {

	public static InputStream getInputStream(String resource) {

		InputStream stream = ResourceUtil.class.getResourceAsStream( resource );
		if ( stream == null ) stream = Thread.currentThread().getContextClassLoader().getResourceAsStream( resource );

		if ( stream == null ) {
			File file = new File(resource);
			try {
				return new FileInputStream(file);
			} catch (Exception ex) {
				//
			}

			try {
				URL sourceUrl = new URL(resource);
				if (sourceUrl != null) {
					stream = sourceUrl.openStream();
				}
			} catch (Exception e) {
				return null;
			}
		}

		return stream;
	}

}

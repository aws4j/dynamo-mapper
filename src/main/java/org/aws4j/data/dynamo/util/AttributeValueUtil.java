package org.aws4j.data.dynamo.util;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class AttributeValueUtil {

	public static boolean isNull( AttributeValue attr ) {

		Boolean isnull = attr.isNULL();
		if ( isnull == null ) {
			return false;
		}
		else if ( isnull == true ) {
			return true;
		}
		else {
			return false;
		}
	}
}

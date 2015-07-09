package org.aws4j.data.dynamo.attribute;

import java.util.Date;

public class DateAttribute<M> extends NumberAttribute<M, Date> {

	public DateAttribute( Class<M> modelClass, String name ) {
		super( modelClass, Date.class, name );
	}
}

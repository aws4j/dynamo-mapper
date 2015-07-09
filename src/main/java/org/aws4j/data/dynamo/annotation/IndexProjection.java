package org.aws4j.data.dynamo.annotation;

import com.amazonaws.services.dynamodbv2.model.ProjectionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface IndexProjection {

	public String name();
	public ProjectionType type();
}
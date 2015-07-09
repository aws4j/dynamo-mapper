package org.aws4j.data.dynamo.annotation;

import java.lang.annotation.*;

@Retention( RetentionPolicy.RUNTIME )
@Target({ ElementType.FIELD, ElementType.METHOD} )
public @interface DynamoDBAutoGenerateKey {

	public String key();
	public String subKeyPropertyName() default "";
	public String format() default "";
}
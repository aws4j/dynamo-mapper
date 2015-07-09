package org.aws4j.data.dynamo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.aws4j.data.core.annotation.Model;
import org.aws4j.data.dynamo.model.DynamoKeyHandler;

@Retention( RetentionPolicy.RUNTIME )
@Target({ ElementType.TYPE } )
@Model( keyHandler = DynamoKeyHandler.class )
public @interface DynamoTable {
	String tableName();
	String keyName() default "";
}

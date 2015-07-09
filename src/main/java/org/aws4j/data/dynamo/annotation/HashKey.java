package org.aws4j.data.dynamo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.aws4j.data.core.annotation.KeyAttribute;

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.FIELD, ElementType.METHOD} )
@KeyAttribute
public @interface HashKey {
}

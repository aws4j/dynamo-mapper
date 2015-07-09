package org.aws4j.data.dynamo.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Fetch {

    FetchType type() default FetchType.KEY;

    String by();

	String on() default "";

    public enum FetchType {
        KEY, QUERY
    }
}

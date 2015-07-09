package org.aws4j.data.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.aws4j.data.core.BasicKeyHandler;
import org.aws4j.data.core.KeyHandler;

@Retention( RetentionPolicy.RUNTIME )
@Target({ ElementType.ANNOTATION_TYPE })
public @interface Model {

	String keyName() default "";

	Class<? extends KeyHandler> keyHandler() default BasicKeyHandler.class;
}

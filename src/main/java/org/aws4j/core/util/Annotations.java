package org.aws4j.core.util;

import java.lang.annotation.Annotation;
import java.util.Collection;

public class Annotations {

	@SuppressWarnings("unchecked")
	public static <A extends Annotation> A getAnnotation( Class<A> annotationType, Collection<? extends Annotation> annotations ) {

		for( Annotation anno : annotations ) {
			if ( annotationType.isInstance( anno ) ) {
				return (A) anno;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <A extends Annotation> A getAnnotatedAnnotation( Class<A> annotationType , Collection<? extends Annotation> annotations ) {

		for( Annotation anno : annotations ) {
			for( Annotation m : anno.annotationType().getAnnotations() ) {
				if ( annotationType.isInstance( m ) ) {
					return (A) anno;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <A extends Annotation> A getAnnotationsAnnotation( Class<A> annotationType , Collection<? extends Annotation> annotations ) {

		for( Annotation anno : annotations ) {
			for( Annotation m : anno.annotationType().getAnnotations() ) {
				if ( annotationType.isInstance( m ) ) {
					return (A) m;
				}
			}
		}
		return null;
	}
}

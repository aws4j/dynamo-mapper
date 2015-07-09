package org.aws4j.data.core.util;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import org.aws4j.core.util.Reflector;
import org.aws4j.core.util.StringUtil;
import org.aws4j.data.dynamo.annotation.Attribute;

public class AttributeRef {

	private String propertyName;
	private Method getter;
	private Method setter;
	private Type propertyType;
	private List<Annotation> annotations;

	public AttributeRef( PropertyDescriptor propDesc, List<Annotation> annotations ) {
		this.propertyName = propDesc.getName();
		this.getter = propDesc.getReadMethod();
		this.setter = propDesc.getWriteMethod();
		this.propertyType = getter.getGenericReturnType();
		this.annotations = annotations;
	}

	public <T> T getAnnotation( Class<T> annotationClass ) {

		for( Annotation annotation : annotations ) {
			if ( annotationClass.equals( annotation.annotationType() ) ) {
				return (T)annotation;
			}
		}
		return null;
	}

	public Type getPropertyType() {
		return propertyType;
	}

	public Method getGetter() {
		return getter;
	}

	public Method getSetter() {
		return setter;
	}

	public String getAttributeName() {

		Attribute attr = getAnnotation( Attribute.class );

		if ( StringUtil.isNotEmpty( attr.name() ) ) {
			return attr.name();
		}
		else {
			return propertyName;
		}
	}

	public String getPropertyName() {
		return propertyName;
	}

	public boolean contains( Class<? extends Annotation> annotationClass ) {
		for ( Annotation annotation : annotations ) {
			if ( annotationClass.equals( annotation.annotationType() ) ) {
				return true;
			}
		}
		return false;
	}

	public Object getPropertyValue( Object model ) {
		return Reflector.invoke( model, getter );
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}

}

package org.aws4j.data.core.util;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aws4j.core.util.Collections;
import org.aws4j.core.util.Reflector;
import org.aws4j.data.core.Key;
import org.aws4j.data.dynamo.annotation.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Models {

	private static final Logger Log = LoggerFactory.getLogger( Models.class );

	private static final Map<Class<?>, ModelRef<?>> modelRefs = new ConcurrentHashMap<Class<?>, ModelRef<?>>();

	protected static Key getKey( Object model ) {
		if ( model == null ) return null;
		ModelRef<?> modelRef = getModelRef( model.getClass() );
		return modelRef.getKeyHandler().getKey( model );
	}

	protected static <M> Key createKey( Class<M> modelClass, String keyString ) {
		ModelRef<?> modelRef = getModelRef( modelClass );
		return modelRef.getKeyHandler().decodeKey( modelClass, keyString );
	}

	protected static <M> Key createKey( Class<M> modelClass, Object... keyValues ) {
		ModelRef<?> modelRef = getModelRef( modelClass );
		return modelRef.getKeyHandler().createKey( modelClass, Arrays.asList( keyValues ) );
	}

	private static AttributeRef makeAttributeRef( Class<?> modelClass, PropertyDescriptor propDesc ) {
		List<Annotation> annotations = Reflector.getAnnotations( modelClass, propDesc );
		if ( ! Collections.containsClass( annotations, Attribute.class ) ) {
			return null;
		}
		return new AttributeRef( propDesc, annotations );
	}

	/*
	private static <M> List<Annotation> checkAndGetModelAnnotations( Class<M> modelClass ) {
		List<Annotation> modelAnnotations = Arrays.asList( modelClass.getAnnotations() );
		for( Annotation anno : modelClass.getAnnotations() ) {
			Model modelAnnotation = anno.getClass().getAnnotation( Model.class );
		}
		if ( modelAnno == null ) {
			throw new IllegalStateException( "Must be set Model annotation.");
		}
		return modelAnnotations;
	}
	*/

	private static <M> ModelRef<M> loadModelRef( Class<M> modelClass ) {

		List<Annotation> modelAnnotations = Arrays.asList( modelClass.getAnnotations() );

		Map<String, AttributeRef> attributes = new HashMap<String, AttributeRef>();
		//TODO
		List<PropertyDescriptor> properties = Reflector.getPropertyDescriptors( modelClass );
		for( PropertyDescriptor prop : properties ) {
			AttributeRef attrRef = makeAttributeRef( modelClass, prop );
			if( attrRef != null ) {
				attributes.put( prop.getName(), attrRef );
			}
		};
		//
		return new ModelRef<M>( modelClass );
	}

	public static <M> ModelRef<M> getModelRef( Class<M> modelClass ) {
		@SuppressWarnings("unchecked")
		ModelRef<M> modelRef = (ModelRef<M>) modelRefs.get( modelClass );
		if( modelRef == null ) {
			modelRef = loadModelRef( modelClass );
		}
		return modelRef;
	}
}

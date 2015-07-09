package org.aws4j.data.core.util;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aws4j.core.util.Annotations;
import org.aws4j.core.util.Collections;
import org.aws4j.core.util.Reflector;
import org.aws4j.core.util.StringUtil;
import org.aws4j.data.core.KeyHandler;
import org.aws4j.data.core.annotation.Model;
import org.aws4j.data.core.exception.IllegalModelException;
import org.aws4j.data.dynamo.annotation.Attribute;
import org.aws4j.data.dynamo.annotation.DynamoDBAutoGenerateKey;
import org.aws4j.data.dynamo.annotation.HashKey;
import org.aws4j.data.dynamo.annotation.IndexProjection;
import org.aws4j.data.dynamo.annotation.IndexProjections;
import org.aws4j.data.dynamo.annotation.RangeKey;
import org.aws4j.data.dynamo.util.IndexManager;
import org.aws4j.data.dynamo.util.IndexSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AttributeRef access provider layer
 */
public class ModelRef<M> {

	private final static Logger Log = LoggerFactory.getLogger( ModelRef.class );

	private Model modelAnno;
	private List<? extends Annotation> modelAnnotations;
	private Class<M> modelClass;
	private Map<String, AttributeRef> attributes;
	private KeyHandler keyHandler;
	private IndexManager indexManager; //TODO move to ModelMeta

	// TODO move to ModelMeta
	private List<IndexProjection> getIndexProjections( Class<M> modelClass ) {

		List<IndexProjection> returnlist = new ArrayList<IndexProjection>();
		IndexProjection indexProjection = modelClass.getAnnotation( IndexProjection.class );
		if( indexProjection != null ) {
			returnlist.add( indexProjection );
		}
		IndexProjections indexProjections = modelClass.getAnnotation( IndexProjections.class );
		if( indexProjections != null ) {
			for( IndexProjection projection : indexProjections.value() ) {
				returnlist.add( projection );
			}
		}
//		log.debug( modelClass + " IndexProjection size = " + returnlist.size() );
		return returnlist;
	}

	private AttributeRef makeAttributeRef( Class<?> modelClass, PropertyDescriptor propDesc ) {
		List<Annotation> annotations = Reflector.getAnnotations( modelClass, propDesc );
		if ( ! Collections.containsClass( annotations, Attribute.class ) ) {
			return null;
		}
		return new AttributeRef( propDesc, annotations );
	}

	private Map<String, AttributeRef> getAttributeRefs( Class<M> modelClass ) {
		Map<String, AttributeRef> attributes = new HashMap<String, AttributeRef>();
		List<PropertyDescriptor> properties = Reflector.getPropertyDescriptors( modelClass ); // TODO
		for( PropertyDescriptor prop : properties ) {
			AttributeRef attrRef = makeAttributeRef( modelClass, prop );
			if( attrRef != null ) {
				attributes.put( prop.getName(), attrRef );
			}
		};
		return attributes;
	}

	// Constructor
	public ModelRef( Class<M> modelClass ) {

		this.modelClass = modelClass;
		modelAnnotations = Arrays.asList( modelClass.getAnnotations() );
		modelAnno = Annotations.getAnnotation( Model.class, modelAnnotations );
		if ( modelAnno == null ) {
			modelAnno = Annotations.getAnnotationsAnnotation( Model.class, modelAnnotations );
		}
		if ( modelAnno == null ) {
			throw new IllegalModelException("Model family annotation not found.");
		}
		this.keyHandler = Reflector.newInstance( modelAnno.keyHandler() );
		this.attributes = getAttributeRefs( modelClass );
	}

	// TODO move to ModelMeta
	/*
	public String getAutoGenerateKeyString( M model ) {

		DynamoDBAutoGenerateKey autoGenerateKey = getAnnotationOrNull( DynamoDBAutoGenerateKey.class );
		if ( autoGenerateKey == null ) return null;

		String keyString = autoGenerateKey.key();
		String propertyName = autoGenerateKey.subKeyPropertyName();
		if ( StringUtil.isNotEmpty( propertyName ) ) {
			Object value = getPropertyValue( model, propertyName );
			if ( value == null ) {
				throw new IllegalStateException("Cannot make auto generete keyString");
			}
			// TODO
			keyString = keyString + ":" + value.toString();
		}
		return keyString;
	}
	*/

	// TODO move to ModelMeta
	public void setAutoGeneratePropertyValue( M model, Integer generatedNumber ) {

		DynamoDBAutoGenerateKey autoGenerateKey = getAnnotation( DynamoDBAutoGenerateKey.class );
		String value = null;
		if ( StringUtil.isNotEmpty( autoGenerateKey.format() ) ) {
			value = String.format( autoGenerateKey.format(), generatedNumber );
		}
		else {
			value = String.valueOf( generatedNumber );
		}
		Reflector.invoke( model, getAnnotatedAttributeRef( DynamoDBAutoGenerateKey.class ).getSetter(), value );
	}

	public Class<M> getModelClass() {
		return modelClass;
	}

	public String getModelName() {
		return keyHandler.getKeyName( modelClass );
	}

	public List<AttributeRef> getAttributes() {
		return new ArrayList<AttributeRef>( attributes.values() );
	}

	@SuppressWarnings("unchecked")
	public <A extends Annotation> A getAnnotation( Class<A> annotationType ) {
		for ( Annotation anno : modelAnnotations ) {
			if ( annotationType.isInstance( anno ) ) {
				return (A) anno;
			}
		}
		return null;
	}

	public Object getPropertyValue( M model, String propertyName ) {

//		log.trace( model.getClass() + " : " + propertyName );

		AttributeRef attrRef = attributes.get( propertyName );
		if ( attrRef != null ) {
			return Reflector.invoke( model, attrRef.getGetter() );
		}
		else {
			return Reflector.getPropertyValue( model, propertyName );
		}
	}

	public void setPropertyValue( M model, String propertyName, Object value ) {
		Reflector.invoke( model, attributes.get( propertyName ).getSetter(), value );
	}

	public Object getAnnotatedPropertyValue( Object model, Class<? extends Annotation> annotationClass ) {

		AttributeRef attrRef = getAnnotatedAttributeRef( annotationClass );
		if ( attrRef != null ) {
			return Reflector.invoke( model, attrRef.getGetter() );
		}
		else {
			return null;
		}
	}

	public AttributeRef getAttributeRef( String attributeName ) {
		return attributes.get( attributeName );
	}

	public AttributeRef getAnnotatedAttributeRef( Class<? extends Annotation> annotationClass ) {
		for ( String name : attributes.keySet() ) {
			AttributeRef attrRef = attributes.get( name );
			if ( attrRef.contains( annotationClass ) ) {
				return attrRef;
			}
		}
		return null;
	}

	public String getIndexName( String hashKeyName, String rangeKeyName ) {

		return indexManager.getIndexName( hashKeyName, rangeKeyName );
	}

	public IndexSchema getIndex( String hashKeyName, String rangeKeyName ) {
		return indexManager.getIndexSchema( hashKeyName, rangeKeyName );
	}

	public AttributeRef getHashKeyAttributeRef() {
		return getAnnotatedAttributeRef( HashKey.class );
	}

	public AttributeRef getRangeKeyAttributeRef() {
		return getAnnotatedAttributeRef( RangeKey.class );
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}

	public Model getModelAnnotation() {
		return modelAnno;
	}

}

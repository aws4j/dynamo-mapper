package org.aws4j.data.core;

import java.util.List;

import org.aws4j.core.exception.NotImplementedException;
import org.aws4j.core.util.StringUtil;
import org.aws4j.data.core.annotation.KeyAttribute;
import org.aws4j.data.core.annotation.Model;
import org.aws4j.data.core.util.ModelRef;
import org.aws4j.data.core.util.Models;

public class BasicKeyHandler extends AbstractKeyHandler {

	@Override
	public Key getKey( Object model ) {
		ModelRef<?> modelRef = getModelRef( model.getClass() );
		throw new NotImplementedException();
		// TODO
		/*
		List<?> keyValues = modelRef.getAnnotatedPropertyValues( model, KeyAttribute.class );
		return BasicKey.create( this, modelClass, keyValues );
		*/
	}

	@Override
	public Key createKey( Class<?> modelClass, List<?> keyValues ) {
		return BasicKey.create( this, modelClass, keyValues );
	}

	@Override
	public String encodeKey( Class<?> modelClass, List<?> keyValues ) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public Key decodeKey( Class<?> modelClass, String keyString ) {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

	@Override
	public String getKeyName( Class<?> modelClass ) {
		ModelRef<?> modelRef = getModelRef( modelClass );
		String keyName = modelRef.getAnnotation( Model.class ).keyName();
		return ( StringUtil.isNotEmpty( keyName ) ) ? keyName : modelClass.getSimpleName();
	}

	/*
	private Key createKey( Class<M> modelClass, Object primaryKey, Object secondaryKey ) {
		return BasicKey.create( modelClass, primaryKey, secondaryKey );
	}

	public <M> Key getKey( M model ) {
		@SuppressWarnings("unchecked")
		ModelRef<M> modelRef = (ModelRef<M>) Models.getModelRef( model.getClass() );
		Object primaryKeyValue; //= modelRef.getAnnotatedPropertyValue( model, PrimaryKey.class );
		Object secondayKeyValue; // = modelRef.getAnnotatedPropertyValue( model, SecondaryKey.class );
		if ( modelRef.hasAnnotatedPropery( PrimaryKey.class ) ) {
			primaryKeyValue = modelRef.getAnnotatedPropertyValue( model, PrimaryKey.class );
		}
		if ( modelRef.hasAnnotatedPropery( SecondaryKey.class ) ) {
			secondayKeyValue = modelRef.getAnnotatedPropertyValue( model, SecondaryKey.class );
		}
		return createKey( modelClass, primaryKey, secondaryKey );
	}

	public <M> String getKeyName( Class<M> modelClass ) {
		ModelRef<M> modelRef = Models.getModelRef( modelClass );
	}
	*/

}

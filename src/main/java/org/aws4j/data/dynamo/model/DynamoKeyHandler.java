package org.aws4j.data.dynamo.model;

import java.util.List;

import org.aws4j.core.exception.NotImplementedException;
import org.aws4j.data.core.AbstractKeyHandler;
import org.aws4j.data.core.Key;
import org.aws4j.data.core.util.ModelRef;
import org.aws4j.data.dynamo.annotation.HashKey;
import org.aws4j.data.dynamo.annotation.RangeKey;


public class DynamoKeyHandler extends AbstractKeyHandler {

	@Override
	public Key getKey( Object model ) {
		ModelRef<?> modelRef = getModelRef( model.getClass() );
		Object hashKey = modelRef.getAnnotatedPropertyValue( model, HashKey.class );
		Object rangeKey = modelRef.getAnnotatedPropertyValue( model, RangeKey.class );
		return DynamoKey.create( model.getClass(), hashKey, rangeKey );
	}

	@Override
	public Key createKey( Class<?> modelClass, List<?> keyValues ) {
		throw new NotImplementedException();
	}

	@Override
	public String encodeKey( Class<?> modelClass, List<?> keyValues ) {
		throw new NotImplementedException();
	}

	@Override
	public Key decodeKey( Class<?> modelClass, String keyString ) {
		throw new NotImplementedException();
	}

	@Override
	public String getKeyName( Class<?> modelClass ) {
		throw new NotImplementedException();
	}

}

package org.aws4j.data.core;

import java.util.List;

/**
 * Models support layer
 */
public interface KeyHandler {

//	<M> Key getKey( Object model );
//
//	<M> Key createKey( Class<M> modelClass, List<?> keyValues );
//
//	<M> String encodeKey( Class<M> modelClass, List<?> keyValues );
//
//	<M> Key decodeKey( Class<M> modelClass, String keyString );
//
//	<M> String getModelName( Class<M> modelClass );


	Key getKey( Object model );

	Key createKey( Class<?> modelClass, List<?> keyValues );

	String encodeKey( Class<?> modelClass, List<?> keyValues );

	Key decodeKey( Class<?> modelClass, String keyString );

	String getKeyName( Class<?> modelClass );

}
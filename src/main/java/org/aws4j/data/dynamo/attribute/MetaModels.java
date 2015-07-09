package org.aws4j.data.dynamo.attribute;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aws4j.data.core.util.Models;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetaModels {

	private static final Logger Log = LoggerFactory.getLogger( MetaModels.class );

	private static final Map<Class<?>, MetaModel<?>> modelRefs = new ConcurrentHashMap<Class<?>, MetaModel<?>>();

	private static <M> MetaModel<M> loadMetaModel( Class<M> modelClass ) {
		MetaModel<M> meta = new MetaModel<M>( Models.getModelRef( modelClass ) );
		modelRefs.put( modelClass, meta );
		return meta;
	}

	public static <M> MetaModel<M> getMeta( Class<M> modelClass ) {
		@SuppressWarnings("unchecked")
		MetaModel<M> meta = (MetaModel<M>) modelRefs.get( modelClass );
		if( meta == null ) {
			meta = loadMetaModel( modelClass );
		}
		return meta;
	}
}

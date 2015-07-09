package org.aws4j.data.dynamo.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aws4j.core.util.Lists;
import org.aws4j.core.util.StringUtil;
import org.aws4j.data.core.util.AttributeRef;
import org.aws4j.data.core.util.ModelRef;
import org.aws4j.data.dynamo.annotation.HashKey;
import org.aws4j.data.dynamo.annotation.IndexHashKey;
import org.aws4j.data.dynamo.annotation.IndexProjection;
import org.aws4j.data.dynamo.annotation.IndexRangeKey;
import org.aws4j.data.dynamo.annotation.RangeKey;

import com.amazonaws.services.dynamodbv2.model.ProjectionType;

public class IndexManager {

	public static final String KEY_INDEX = "index";

	private Map<String, IndexSchema> indexSchemas = new HashMap<String, IndexSchema>();

	private void putIndexHashKeyName( String indexName, String hashKeyName ) {

		if ( StringUtil.isEmpty( indexName ) ) return;

		if ( ! indexSchemas.containsKey( indexName ) ) {
			indexSchemas.put( indexName, new IndexSchema( indexName ) );
		}
		indexSchemas.get( indexName ).setHashKeyName( hashKeyName );
	}

	private void putIndexRangeKeyName( String indexName, String rangeKeyName ) {

		if ( StringUtil.isEmpty( indexName ) ) return;

		if ( ! indexSchemas.containsKey( indexName ) ) {
			indexSchemas.put( indexName, new IndexSchema( indexName ) );
		}
		indexSchemas.get( indexName ).setRangeKeyName( rangeKeyName );
	}

	private void putIndexRangeKeyNameWithHashKeyName( String indexName, String hashKeyName, String rangeKeyName ) {

		if ( StringUtil.isEmpty( indexName ) ) return;

		if ( ! indexSchemas.containsKey( indexName ) ) {
			indexSchemas.put( indexName, new IndexSchema( indexName ) );
		}
		indexSchemas.get( indexName ).setHashKeyName( hashKeyName );
		indexSchemas.get( indexName ).setRangeKeyName( rangeKeyName );
	}

	public IndexManager( ModelRef modelRef, Map<String, AttributeRef> attributes,  List<IndexProjection> indexProjections ) {

		// TODO later
		for( String name : attributes.keySet() ) {
			AttributeRef attrRef = attributes.get( name );

			if ( attrRef.contains( HashKey.class ) ) {
				putIndexHashKeyName( KEY_INDEX, name );
			}
			if ( attrRef.contains( RangeKey.class ) ) {
				putIndexRangeKeyName( KEY_INDEX, name );
			}
		}

		// TODO
		indexSchemas.get( KEY_INDEX ).setProjectionType( ProjectionType.ALL );

		for ( IndexProjection indexProjection : indexProjections ) {
			IndexSchema index = indexSchemas.get( indexProjection.name() );
			if ( index == null ) {
				throw new IllegalStateException( indexProjection.name() + " not exists IndexSchema.");
			}
			else {
				index.setProjectionType( indexProjection.type() );
			}
		}
	}

	private IndexSchema getIndexSchemaByHashKeyOnly( String hashKeyName ) {

		// TODO Consider Index Priority
		for ( String indexName : indexSchemas.keySet() ) {
			IndexSchema indexSchema = indexSchemas.get( indexName );
			if ( indexSchema.getHashKeyName().equals( hashKeyName ) ) {
				return indexSchema;
			}
		};
		return null;
	}

	private IndexSchema getIndexSchemaByHashKeyAndRangeKey( String hashKeyName, String rangeKeyName ) {

		for ( String indexName : indexSchemas.keySet() ) {
			IndexSchema indexSchema = indexSchemas.get( indexName );
//			log.debug( "hash = " + indexSchema.getHashKeyName() + " range = " + indexSchema.getRangeKeyName() );
			if ( hashKeyName.equals( indexSchema.getHashKeyName() ) &&
					rangeKeyName.equals( indexSchema.getRangeKeyName() ) ) {
//				log.trace( indexSchema.getProjectionType().name() );
				return indexSchema;
			}
		};
		return null;
	}

	public String getIndexName( String hashKeyName, String rangeKeyName ) {
		IndexSchema indexSchema = getIndexSchema( hashKeyName, rangeKeyName );
		if ( indexSchema != null ) {
			return indexSchema.getIndexName();
		}
		return null;
	}

	public IndexSchema getIndexSchema( String hashKeyName, String rangeKeyName ) {

		if( rangeKeyName != null ) {
			return getIndexSchemaByHashKeyAndRangeKey( hashKeyName, rangeKeyName );
		}
		else {
			return getIndexSchemaByHashKeyOnly( hashKeyName );
		}
	}

}

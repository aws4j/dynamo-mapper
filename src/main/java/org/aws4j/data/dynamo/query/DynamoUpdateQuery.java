package org.aws4j.data.dynamo.query;

import java.util.HashMap;
import java.util.Map;

import org.aws4j.core.exception.NotImplementedException;
import org.aws4j.data.core.FilterCriteria;
import org.aws4j.data.core.Key;
import org.aws4j.data.core.UpdateCriteria;
import org.aws4j.data.dynamo.attribute.DynamoFilterCriteria;
import org.aws4j.data.dynamo.attribute.DynamoUpdateCriteria;
import org.aws4j.data.dynamo.attribute.MetaModel;
import org.aws4j.data.dynamo.model.DynamoKey;
import org.aws4j.data.dynamo.service.dynamo.DynamoMapper;

public class DynamoUpdateQuery<M> {

	private DynamoMapper dynamo;
	private MetaModel<M> meta;
	private DynamoKey key;
	private Map<String, DynamoUpdateCriteria<M, ?>> updates = new HashMap<String, DynamoUpdateCriteria<M, ?>>();
	private Map<String, DynamoFilterCriteria<M, ?>> conditions = new HashMap<String, DynamoFilterCriteria<M, ?>>();

	public DynamoUpdateQuery( DynamoMapper dynamo, MetaModel<M> meta ) {
		this.dynamo = dynamo;
		this.meta = meta;
	}

	public DynamoUpdateQuery<M> setKey( DynamoKey key ) {
		this.key = key;
		return this;
	}

	public DynamoUpdateQuery<M> set( DynamoUpdateCriteria<M, ?> updateCriteria ) {
		updates.put( updateCriteria.getName(), updateCriteria );
		return this;
	}

	public DynamoUpdateQuery<M> filter( DynamoFilterCriteria<M, ?> filterCriteria ) {
		conditions.put( filterCriteria.getName(), filterCriteria );
		return this;
	}

	public void execute() {
		dynamo.update( meta, key, updates.values(), conditions.values() );
	}

}

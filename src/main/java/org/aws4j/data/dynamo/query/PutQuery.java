package org.aws4j.data.dynamo.query;

import org.aws4j.core.exception.NotImplementedException;
import org.aws4j.data.dynamo.attribute.MetaModel;
import org.aws4j.data.dynamo.service.dynamo.DynamoMapper;

public class PutQuery<M> {

	private DynamoMapper dynamo;
	private MetaModel<M> meta;
	private M model;

	public PutQuery( DynamoMapper dynamo, MetaModel<M> meta ) {
		this.dynamo = dynamo;
		this.meta = meta;
	}

	public PutQuery<M> set( M model ) {
		this.model = model;
		return this;
	}

	public PutQuery<M> ifAbsent() {
		throw new NotImplementedException();
	}

	public void execute() {
		dynamo.put( model );
	}

}

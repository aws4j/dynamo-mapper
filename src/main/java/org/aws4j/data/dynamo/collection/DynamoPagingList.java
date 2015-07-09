package org.aws4j.data.dynamo.collection;

import org.aws4j.core.exception.NotImplementedException;
import org.aws4j.data.core.AbstractPagingList;
import org.aws4j.data.core.PagingList;
import org.aws4j.data.dynamo.query.DynamoQuery;
import org.aws4j.data.dynamo.service.dynamo.DynamoMapper;

public class DynamoPagingList<M> extends AbstractPagingList<M> {

	private static final long serialVersionUID = 1L;

	public DynamoPagingList( DynamoMapper dynamoMapper, DynamoQuery<M> query ) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public PagingList<M> getNextPage() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}

}

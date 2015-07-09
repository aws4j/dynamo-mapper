package org.aws4j.data.dynamo.query;

import java.util.List;

import org.aws4j.data.core.FilterCriteria;
import org.aws4j.data.core.Query;
import org.aws4j.data.core.SortCriteria;
import org.aws4j.data.dynamo.attribute.MetaModel;
import org.aws4j.data.dynamo.service.dynamo.DynamoMapper;

public class DynamoQuery<M> implements Query<M> {

	private DynamoMapper mapper;
	private MetaModel<M> meta;
	private FilterCriteria<M> filterCriteria;
	private SortCriteria<M> sortCriteria;

	public DynamoQuery( DynamoMapper mapper, MetaModel<M> meta ) {
		this.mapper = mapper;
	}

	@Override
	public Query<M> filter( FilterCriteria<M> filterCriteria ) {
		this.filterCriteria = filterCriteria;
		return this;
	}

	@Override
	public List<M> getAsList() {
		return mapper.query( this );
	}


	@Override
	public Query<M> sortBy( SortCriteria<M> sortCriteria ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FilterCriteria<M> getCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortCriteria<M> getSortCriteria() {
		return sortCriteria;
	}

	public Class<M> getModelClass() {
		return meta.getModelClass();
	}
}

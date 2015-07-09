package org.aws4j.data.dynamo.attribute;

import java.util.Collection;

import org.aws4j.core.exception.NotImplementedException;
import org.aws4j.data.core.Comparison;
import org.aws4j.data.core.FilterCriteria;

public class DynamoFilterCriteria<M, V> implements FilterCriteria<M> {

	protected Class<M> modelClass;
	protected Class<V> valueClass;
	protected String name;
	protected Comparison comparison;
	protected Collection<V> values;

	public DynamoFilterCriteria( Class<M> modelClass, Class<V> valueClass, Comparison comparison, String name, Collection<V> values ) {
		this.modelClass = modelClass;
		this.valueClass = valueClass;
		this.comparison = comparison;
		this.values = values;
		this.name = name;
	}

	@Override
	public FilterCriteria<M> and( FilterCriteria<M> filter ) {
		throw new NotImplementedException();
	}

	public String getName() {
		return name;
	}
}

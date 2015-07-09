package org.aws4j.data.core;

import java.util.Collection;

public class SingleCondition<M, V> implements FilterCriteria<M> {

	protected Class<M> modelClass;
	protected Class<V> valueClass;
	protected String name;
	protected Comparison comparison;
	protected Collection<V> values;

	public SingleCondition( Class<M> modelClass, Class<V> valueClass, Comparison comparison, String name, Collection<V> values ) {
		this.modelClass = modelClass;
		this.valueClass = valueClass;
		this.comparison = comparison;
		this.values = values;
		this.name = name;
	}

	@Override
	public FilterCriteria<M> and( FilterCriteria<M> condition ) {
		return new ComplexCondition<M>( this, condition, LogicalOperator.AND );
	}


}

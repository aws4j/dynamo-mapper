package org.aws4j.data.core;

public class ComplexCondition<M> implements FilterCriteria<M> {

	public ComplexCondition( FilterCriteria<M> conditionA, FilterCriteria<M> conditionB, LogicalOperator operator ) {

	}

	@Override
	public FilterCriteria<M> and(FilterCriteria<M> condition) {
		return new ComplexCondition<M>( this, condition, LogicalOperator.AND );
	}

}

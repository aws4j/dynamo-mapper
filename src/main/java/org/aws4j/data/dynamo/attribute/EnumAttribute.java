package org.aws4j.data.dynamo.attribute;

import java.util.Collection;

import org.aws4j.core.util.Lists;
import org.aws4j.data.core.Comparison;
import org.aws4j.data.core.FilterCriteria;
import org.aws4j.data.core.SingleCondition;

public class EnumAttribute<M, V> extends Attribute<M, V> {

	public EnumAttribute( Class<M> modelClass, Class<V> valueClass, String name ) {
		// TODO Auto-generated constructor stub
	}

	public FilterCriteria<M> eq( V value ) {
		return new SingleCondition<M, V>( modelClass, valueClass, Comparison.EQ, name, Lists.newArrayList( value ) );
	}

	public FilterCriteria<M> in( Collection<V> values ) {
		return new SingleCondition<M, V>( modelClass, valueClass, Comparison.IN, name, values );
	}
}

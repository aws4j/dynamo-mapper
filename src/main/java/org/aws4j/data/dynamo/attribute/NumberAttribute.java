package org.aws4j.data.dynamo.attribute;

import org.aws4j.core.util.Lists;
import org.aws4j.data.core.Comparison;
import org.aws4j.data.core.FilterCriteria;
import org.aws4j.data.core.SingleCondition;
import org.aws4j.data.core.SingleSortCriteria;
import org.aws4j.data.core.SortCriteria;
import org.aws4j.data.core.SortOrder;
import org.aws4j.data.core.UpdateCriteria;

public class NumberAttribute<M, V> extends Attribute<M, V>{

	public NumberAttribute( Class<M> modelClass, Class<V> valueClass, String name ) {
		this.modelClass = modelClass;
		this.valueClass = valueClass;
		this.name = name;
	}

	public DynamoFilterCriteria<M, V> eq( V value ) {
		return new DynamoFilterCriteria<M, V>( modelClass, valueClass, Comparison.EQ, name, Lists.newArrayList( value ) );
	}

	public DynamoFilterCriteria<M, V> ge( V value ) {
		return new DynamoFilterCriteria<M, V>( modelClass, valueClass, Comparison.GE, name, Lists.newArrayList( value ) );
	}

	public DynamoFilterCriteria<M, V> gt( V value ) {
		return new DynamoFilterCriteria<M, V>( modelClass, valueClass, Comparison.GT, name, Lists.newArrayList( value ) );
	}

	public DynamoFilterCriteria<M, V> le( V value ) {
		return new DynamoFilterCriteria<M, V>( modelClass, valueClass, Comparison.LE, name, Lists.newArrayList( value ) );
	}

	public DynamoFilterCriteria<M, V> lt( V value ) {
		return new DynamoFilterCriteria<M, V>( modelClass, valueClass, Comparison.LT, name, Lists.newArrayList( value ) );
	}

	public DynamoUpdateCriteria<M, V> set( V value ) {
		return new DynamoUpdateCriteria<M, V>( modelClass, valueClass, DynamoUpdateMethod.SET, name, value );
	}

	public DynamoUpdateCriteria<M, V> add( V value ) {
		return new DynamoUpdateCriteria<M, V>( modelClass, valueClass, DynamoUpdateMethod.ADD, name, value );
	}

	public final SortCriteria<M> Asc = new SingleSortCriteria<M, V>( modelClass, valueClass, SortOrder.ASC, name );

	public final SortCriteria<M> Desc = new SingleSortCriteria<M, V>( modelClass, valueClass, SortOrder.DESC, name );

}

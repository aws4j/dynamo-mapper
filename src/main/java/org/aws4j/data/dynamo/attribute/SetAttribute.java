package org.aws4j.data.dynamo.attribute;

import java.util.Set;

import org.aws4j.data.core.UpdateCriteria;

import com.google.common.collect.Sets;

public class SetAttribute<M, V> extends Attribute<M, Set<V>> {


	public SetAttribute( Class<M> modelClass, Class<V> valueClass, String name ) {
		// TODO Auto-generated constructor stub
	}

	public UpdateCriteria<M> set( Set<V> values ) {
		return new DynamoUpdateCriteria<M, Set<V>>( modelClass, valueClass, DynamoUpdateMethod.SET, name, values );
	}

	@SuppressWarnings("unchecked")
	public UpdateCriteria<M> add( V value ) {
		return add( Sets.newHashSet( value ) );
	}

	public UpdateCriteria<M> add( Set<V> values ) {
		return new DynamoUpdateCriteria<M, Set<V>>( modelClass, valueClass, DynamoUpdateMethod.ADD, name, values );
	}

	@SuppressWarnings("unchecked")
	public UpdateCriteria<M> remove( V value ) {
		return remove( Sets.newHashSet( value ) );
	}

	public UpdateCriteria<M> remove( Set<V> values ) {
		return new DynamoUpdateCriteria<M, Set<V>>( modelClass, valueClass, DynamoUpdateMethod.REMOVE, name, values );
	}
}

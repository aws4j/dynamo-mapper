package org.aws4j.data.dynamo.attribute;

import java.lang.reflect.Type;

import org.aws4j.data.core.UpdateCriteria;

import com.amazonaws.services.dynamodbv2.model.AttributeAction;

public class DynamoUpdateCriteria<M, V> implements UpdateCriteria<M> {

	protected Class<M> modelClass;
	protected Class<V> valueClass;
	protected DynamoUpdateMethod updateMethod;
	protected String name;
	protected V value;

	public DynamoUpdateCriteria( Class<M> modelClass, Class<V> valueClass, DynamoUpdateMethod updateMethod, String name, V value ) {
		this.modelClass = modelClass;
		this.valueClass = valueClass;
		this.updateMethod = updateMethod;
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return valueClass; // TODO
	}

	public V getValue() {
		return value;
	}

	// TODO
	public AttributeAction getAction() {
		switch (updateMethod) {
			case SET:
				return AttributeAction.PUT;
			case ADD:
				return AttributeAction.ADD;
			case REMOVE:
				return AttributeAction.DELETE;
		}
		throw new IllegalStateException();
	}
}

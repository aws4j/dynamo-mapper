package org.aws4j.data.dynamo.attribute;

public abstract class Attribute<M, V> {

	protected Class<M> modelClass;
	protected Class<V> valueClass;
	protected String name;

	public String getName() {
		return name;
	}
}

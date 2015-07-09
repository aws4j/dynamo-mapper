package org.aws4j.data.dynamo.attribute;

public class OneToManyAttribute<M, V, E extends MetaModel<V>> extends RelationalAttribute<M, V, E>{

	public OneToManyAttribute(Class<M> modelClass, Class<V> valueClass, Class<E> metaClass, String name) {
	}

	public E meta() {
		return null;
	}

}

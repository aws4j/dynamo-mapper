package org.aws4j.data.dynamo.attribute;

import org.aws4j.data.core.Comparison;
import org.aws4j.data.core.FilterCriteria;

public class StringAttribute<M> extends Attribute<M, String>{

	public StringAttribute( Class<M> modelClass, String name ) {
		this.modelClass = modelClass;
		this.valueClass = String.class;
		this.name = name;
	}
/*
	public Condition<M> eq( String value ) {
		return new Condition<M>( modelClass, valueClass, Comparison.EQ, name, value );
	}

	public Condition<M> in( String... values ) {
		return new Condition<M>( modelClass, valueClass, Comparison.EQ, name, values );
	}
*/
}

package org.aws4j.data.dynamo.attribute.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractValueConverter<V> implements AttributeValueConverter<V> {

	protected String valueToString( V value ) {
		return String.valueOf( value );
	}

	protected List<String> setToStringCollection( Set<V> values ) {
		List<String> strings = new ArrayList<String>();
		for ( V value : values ) {
			if( value != null ) {
				strings.add( valueToString( value ) );
			}
		}
		return strings;
	}
 }

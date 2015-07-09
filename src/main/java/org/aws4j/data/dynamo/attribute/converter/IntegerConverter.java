package org.aws4j.data.dynamo.attribute.converter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class IntegerConverter extends AbstractValueConverter<Integer> {

	private static Logger log = LoggerFactory.getLogger( IntegerConverter.class );

	public static final IntegerConverter instance = new IntegerConverter();

	private IntegerConverter() {};

	private Integer toInteger( String s ) {
		return Integer.valueOf( s );
	}

	@Override
	public AttributeValue convert( Integer value ) {
		return new AttributeValue().withN( valueToString( value ) );
	}

	@Override
	public AttributeValue convertFromSet( Set<Integer> values ) {
		List<String> valueStrings = setToStringCollection( values );
		if ( valueStrings.isEmpty() ) return null;
		return new AttributeValue().withNS( valueStrings );
	}

	@Override
	public Integer deconvert( AttributeValue attrValue ) {
		return toInteger( attrValue.getN() );
	}

	@Override
	public Set<Integer> deconvertToSet( AttributeValue attrValue ) {
		if ( attrValue.getNS() == null ) return null;
		Set<Integer> integers = new HashSet<Integer>();
		for ( String s : attrValue.getNS() ) {
			integers.add( toInteger( s ) );
		}
		return integers;
	}
}

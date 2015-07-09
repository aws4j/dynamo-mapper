package org.aws4j.data.dynamo.attribute.converter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class DoubleConverter extends AbstractValueConverter<Double> {

	private static final Logger Log = LoggerFactory.getLogger( DoubleConverter.class );

	public static final DoubleConverter instance = new DoubleConverter();

	private DoubleConverter() {};

	private Double toDouble( String s ) {
        return Double.valueOf( s );
	}

	@Override
	public AttributeValue convert( Double value ) {
		return new AttributeValue().withN( valueToString( value ) );
	}

	@Override
	public AttributeValue convertFromSet( Set<Double> values ) {
		List<String> valueStrings = setToStringCollection( values );
		if ( valueStrings.isEmpty() ) return null;
		return new AttributeValue().withNS( valueStrings );
	}

	@Override
	public Double deconvert( AttributeValue attrValue ) {
		return toDouble( attrValue.getN() );
	}

	@Override
	public Set<Double> deconvertToSet( AttributeValue attrValue ) {
		if ( attrValue.getNS() == null ) return null;
		Set<Double> doubles = new HashSet<Double>();
		for ( String s : attrValue.getNS() ) {
			doubles.add( toDouble( s ) );
		}
		return doubles;
	}

}

package org.aws4j.data.dynamo.attribute.converter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class FloatConverter extends AbstractValueConverter<Float>{

	private static Logger log = LoggerFactory.getLogger( FloatConverter.class );

	public static final FloatConverter instance = new FloatConverter();

	private Float toFloat( String s ) {
        return Float.valueOf( s );
	}

	@Override
	public AttributeValue convert ( Float value ) {
		return new AttributeValue().withN( valueToString( value ) );
	}

	@Override
	public AttributeValue convertFromSet ( Set<Float> values ) {
		List<String> valueStrings = setToStringCollection( values );
		if ( valueStrings.isEmpty() ) return null;
		return new AttributeValue().withNS( valueStrings );
	}

	@Override
	public Float deconvert( AttributeValue attrValue ) {
		return toFloat( attrValue.getN() );
	}

	@Override
	public Set<Float> deconvertToSet( AttributeValue attrValue ) {
		if ( attrValue.getNS() == null ) return null;
		Set<Float> floats = new HashSet<Float>();
		for ( String s : attrValue.getNS() ) {
			floats.add( toFloat( s ) );
		}
		return floats;
	}
}

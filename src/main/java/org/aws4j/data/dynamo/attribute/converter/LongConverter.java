package org.aws4j.data.dynamo.attribute.converter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class LongConverter extends AbstractValueConverter<Long> {

	private static Logger log = LoggerFactory.getLogger( LongConverter.class );

	public static final LongConverter instance = new LongConverter();

	private LongConverter() {};

	private Long toLong( String s ) {
        return Long.valueOf( s );
	}

	@Override
	public AttributeValue convert( Long value ) {
		return new AttributeValue().withN( valueToString( value ) );
	}

	@Override
	public AttributeValue convertFromSet( Set<Long> values ) {
		List<String> valueStrings = setToStringCollection( values );
		if ( valueStrings.isEmpty() ) return null;
		return new AttributeValue().withNS( valueStrings );
	}

	@Override
	public Long deconvert( AttributeValue attrValue ) {
		return toLong( attrValue.getN() );
	}

	@Override
	public Set<Long> deconvertToSet( AttributeValue attrValue ) {
		if ( attrValue.getNS() == null ) return null;
		Set<Long> longs = new HashSet<Long>();
		for ( String s : attrValue.getNS() ) {
			longs.add( toLong( s ) );
		}
		return longs;
	}

}

package org.aws4j.data.dynamo.attribute.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class StringConverter implements AttributeValueConverter<String> {

	public static final StringConverter instance = new StringConverter();

	private StringConverter() {};

	@Override
	public AttributeValue convert( String value ) {
		return new AttributeValue().withS( value );
	}

	@Override
	public AttributeValue convertFromSet( Set<String> values ) {
		List<String> strings = new ArrayList<String>();
		for( String s : values ) {
			if ( s != null ) {
				strings.add( s );
			}
		}
		if ( strings.isEmpty() ) return null;
		return new AttributeValue().withSS( strings );
	}

	/*
	@Override
	public AttributeValue convert( List<String> values ) {
		List<AttributeValue> attrs = new ArrayList<AttributeValue>();
		for( String s : values ) {
			if ( s != null ) {
				attrs.add( convert( s ) );
			}
		}
		if ( attrs.isEmpty() ) return null;
		return new AttributeValue().withL( attrs );
	}
	*/

	@Override
	public String deconvert( AttributeValue attrValue ) {
		return attrValue.getS();
	}

	@Override
	public Set<String> deconvertToSet( AttributeValue attrValue ) {
		if ( attrValue.getSS() == null ) return null;
		return new HashSet<String>( attrValue.getSS() );
	}

	/*
	@Override
	public List<String> deconvertToList( AttributeValue attrValue ) {
		if ( attrValue.getL() == null ) return null;
		List<String> strings = new ArrayList<String>();
		for ( AttributeValue attr : attrValue.getL() ) {
			strings.add( deconvert( attr ) );
		}
		return strings;
	}
	*/
}

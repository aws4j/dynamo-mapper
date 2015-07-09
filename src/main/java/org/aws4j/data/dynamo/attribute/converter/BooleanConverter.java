package org.aws4j.data.dynamo.attribute.converter;

import java.util.Set;

import org.aws4j.core.exception.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class BooleanConverter extends AbstractValueConverter<Boolean> {

	private static Logger log = LoggerFactory.getLogger( BooleanConverter.class );

	public static final BooleanConverter instance = new BooleanConverter();

	private BooleanConverter() {};

	@Override
	public AttributeValue convert( Boolean value ) {
		return new AttributeValue().withBOOL( value );
	}

	@Override
	public AttributeValue convertFromSet( Set<Boolean> values ) {
		throw new NotImplementedException();
	}

	@Override
	public Boolean deconvert( AttributeValue attrValue ) {
		return attrValue.getBOOL();
	}

	@Override
	public Set<Boolean> deconvertToSet( AttributeValue attrValue ) {
		throw new NotImplementedException();
	}
}

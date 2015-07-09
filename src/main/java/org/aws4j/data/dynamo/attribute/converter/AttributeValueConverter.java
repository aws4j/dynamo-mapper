package org.aws4j.data.dynamo.attribute.converter;

import java.util.Set;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public interface AttributeValueConverter<V> {

	AttributeValue convert( V value );

	AttributeValue convertFromSet( Set<V> set );

	V deconvert( AttributeValue attrValue );

	Set<V> deconvertToSet( AttributeValue attrValue );
}

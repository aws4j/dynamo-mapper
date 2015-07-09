package org.aws4j.data.dynamo.service.dynamo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.aws4j.core.util.MapUtil;
import org.aws4j.data.dynamo.attribute.converter.AttributeValueConverter;
import org.aws4j.data.dynamo.attribute.converter.BooleanConverter;
import org.aws4j.data.dynamo.attribute.converter.DoubleConverter;
import org.aws4j.data.dynamo.attribute.converter.FloatConverter;
import org.aws4j.data.dynamo.attribute.converter.IntegerConverter;
import org.aws4j.data.dynamo.attribute.converter.JsonConverter;
import org.aws4j.data.dynamo.attribute.converter.LongConverter;
import org.aws4j.data.dynamo.attribute.converter.StringConverter;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public abstract class AbstractAttributeConvertManager {

	protected static final ConcurrentMap<Class<?>, AttributeValueConverter<?>> converters;

	static {
		converters = new ConcurrentHashMap<Class<?>, AttributeValueConverter<?>>() {
			{
				put( String.class, StringConverter.instance );
				put( Integer.class, IntegerConverter.instance );
				put( Long.class, LongConverter.instance );
				put( Float.class, FloatConverter.instance );
				put( Double.class, DoubleConverter.instance );
				put( Boolean.class, BooleanConverter.instance );
			}
		};
	}

	@SuppressWarnings("unchecked")
	protected <V> AttributeValueConverter<V> getConverter( Type parameterType ) {
		AttributeValueConverter<V> converter = (AttributeValueConverter<V>) converters
				.get(parameterType);
		if (converter != null) {
			return converter;
		} else {
			return new JsonConverter<V>(parameterType);
		}
	}

	protected boolean isStringValueMap( Type propertyType ) {
		if ( MapUtil.isMap( propertyType ) && String.class.equals( MapUtil.getGenericKeyParameter( propertyType ) ) ) {
			return true;
		}
		return false;
	}

	protected <V> AttributeValue convertToAttributeValue( Class<V> parameterClass, List<V> list ) {
		AttributeValueConverter<V> converter = getConverter( parameterClass );
		List<AttributeValue> attrValues = new ArrayList<AttributeValue>();
		for( V value : list ) {
			if ( value == null ) {
				attrValues.add( new AttributeValue().withNULL( true ) );
			}
			else {
				attrValues.add( converter.convert( value ) );
			}
		}
		return new AttributeValue().withL( attrValues );
	}

	protected <V> AttributeValue convertToAttributeValue( Class<V> valueParamClass, Map<String, V> map ) {
		AttributeValueConverter<V> converter = getConverter( valueParamClass );
		Map<String,AttributeValue> attrValueMap = new HashMap<String,AttributeValue>();
		for( String name : map.keySet() ) {
			V value = map.get( name );
			if ( value == null ) {
				attrValueMap.put( name, new AttributeValue().withNULL( true ) );
			}
			else {
				attrValueMap.put( name, converter.convert( value ) );
			}
		}
		return new AttributeValue().withM( attrValueMap );
	}

}

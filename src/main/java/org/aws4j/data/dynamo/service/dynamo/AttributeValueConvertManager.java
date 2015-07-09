package org.aws4j.data.dynamo.service.dynamo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.aws4j.core.util.Collections;
import org.aws4j.core.util.MapUtil;
import org.aws4j.data.dynamo.attribute.converter.AttributeValueConverter;
import org.aws4j.data.dynamo.util.AttributeValueUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class AttributeValueConvertManager extends AbstractAttributeConvertManager {

	private static Logger log = LoggerFactory.getLogger(AttributeValueConvertManager.class);

	public static final AttributeValueConvertManager INSTANCE = new AttributeValueConvertManager();

	private AttributeValueConvertManager() {};

	private <V> List<V> deconvertFromList( Class<V> parameterClass, List<AttributeValue> attrValueList ) {
		AttributeValueConverter<V> converter = getConverter( parameterClass );
		List<V> list = new ArrayList<V>();
		for ( AttributeValue attr : attrValueList ) {
			if ( AttributeValueUtil.isNull( attr ) ) {
				list.add( null );
			}
			else {
				list.add( converter.deconvert( attr ) );
			}
		}
		return list;
	}

	private <V> Map<String, V> deconvertFromMap( Class<V> valueParamClass, Map<String, AttributeValue> attrValueMap ) {
		AttributeValueConverter<V> converter = getConverter( valueParamClass );
		Map<String, V> map = new HashMap<String, V>();
		for ( String name : attrValueMap.keySet() ) {
			log.debug( "name = " + name );
			AttributeValue attr = attrValueMap.get( name );
			log.debug( "attr = " + attr );
			if ( AttributeValueUtil.isNull( attr ) ) {
				map.put( name, null );
			}
			else {
				map.put( name, converter.deconvert( attr ) );
			}
		}
		return map;
	}

	////// public /////

	@SuppressWarnings("unchecked")
	public <V> AttributeValue convert( Type propertyType, Object value ) {

		if ( Collections.isList( propertyType ) ) {
			List<V> list = (List<V>) value;
			Class<V> parameterClass = (Class<V>) Collections.getGenericParameter( propertyType );
			return convertToAttributeValue( parameterClass, list );
		}
		else if ( Collections.isSet( propertyType ) ) {
			Set<V> set = (Set<V>) value;
			Class<V> parameterClass = (Class<V>) Collections.getGenericParameter( propertyType );
			AttributeValueConverter<V> converter = getConverter( parameterClass );
			return converter.convertFromSet( set );
		}
		else if ( isStringValueMap( propertyType ) ) {
			Class<V> valueParamClass = (Class<V>) MapUtil.getGenericValueParameter( propertyType );
			Map<String, V> map = (Map<String, V>) value;
			return convertToAttributeValue( valueParamClass, map );
		}
		else {
			//
			Class<V> parameterClass = (Class<V>) value.getClass();
			return getConverter(parameterClass).convert((V) value);
		}
	}

	@SuppressWarnings("unchecked")
	public <V> Object deconvert(Type propertyType, AttributeValue attrValue) {
		if ( Collections.isList( propertyType ) ) {
			Class<V> parameterClass = (Class<V>) Collections.getGenericParameter( propertyType );
			return deconvertFromList( parameterClass, attrValue.getL() );
		}
		else if ( Collections.isSet( propertyType ) ) {
			Class<V> parameterClass = (Class<V>) Collections.getGenericParameter( propertyType );
			return getConverter( parameterClass ).deconvertToSet( attrValue );
		}
		else if ( isStringValueMap( propertyType ) ) {
			Class<V> valueParamClass = (Class<V>) MapUtil.getGenericValueParameter( propertyType );
			return deconvertFromMap( valueParamClass, attrValue.getM() );
		}
		else {
			return getConverter(propertyType).deconvert(attrValue);
		}
	}
}

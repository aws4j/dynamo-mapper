package org.aws4j.data.dynamo.service.dynamo;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aws4j.core.util.Collections;
import org.aws4j.core.util.MapUtil;
import org.aws4j.data.dynamo.collection.AddArrayList;
import org.aws4j.data.dynamo.collection.AddHashSet;
import org.aws4j.data.dynamo.collection.RemoveHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;

public class AttributeValueUpdateConvertManager extends AbstractAttributeConvertManager {

	private static final Logger Log = LoggerFactory.getLogger( AttributeValueUpdateConvertManager.class );

	public static final AttributeValueUpdateConvertManager INSTANCE = new AttributeValueUpdateConvertManager();

	private AttributeValueUpdateConvertManager() {};

	private AttributeAction selectAction ( Class<?> propClass ) {
		Log.debug( propClass.toString() );
		if ( AddArrayList.class.equals( propClass ) || AddHashSet.class.equals( propClass ) ) {
			Log.debug("selectAction is ADD");
			return AttributeAction.ADD;
		}
		else if ( RemoveHashSet.class.equals( propClass ) ) {
			Log.debug("selectAction is DELETE");
			return AttributeAction.DELETE;
		}
		else {
			Log.debug("selectAction is PUT");
			return AttributeAction.PUT;
		}
	}

	private AttributeValueUpdate makeAttributeValueUpdate( AttributeValue attrValue, AttributeAction action ) {
		if ( attrValue != null ) {
			return new AttributeValueUpdate().withAction( action ).withValue( attrValue );
		}
		else {
			return null;
		}
	}

	////// public /////

	@SuppressWarnings("unchecked")
	public <V> AttributeValueUpdate convert( Type propertyType, Object value ) {

		if ( Collections.isList( propertyType ) ) {
			List<V> list = (List<V>) value;
			if ( list.isEmpty() ) return null;
			Class<V> parameterClass = (Class<V>) Collections.getGenericParameter( propertyType );
			return makeAttributeValueUpdate(
					convertToAttributeValue( parameterClass, list ), selectAction( list.getClass() ) );
		}
		else if ( Collections.isSet( propertyType ) ) {
			Class<V> parameterClass = (Class<V>) Collections.getGenericParameter( propertyType );
			Set<Object> set = (Set<Object>) value;
			if ( set.isEmpty() ) return null;
			return makeAttributeValueUpdate(
					getConverter( parameterClass ).convertFromSet( set ), selectAction( set.getClass() ) );
		}
		else if ( isStringValueMap( propertyType ) ) {
			Class<V> valueParamClass = (Class<V>) MapUtil.getGenericValueParameter( propertyType );
			Map<String, V> map = (Map<String, V>) value;
			return makeAttributeValueUpdate(
					convertToAttributeValue( valueParamClass, map ), AttributeAction.PUT );
		}
		else {
			Class<V> parameterClass = (Class<V>) value.getClass();
			return makeAttributeValueUpdate(
					getConverter( parameterClass ).convert( (V) value), AttributeAction.PUT );
		}
	}

}

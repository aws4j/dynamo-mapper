package org.aws4j.data.dynamo.attribute.converter;

import java.lang.reflect.Type;
import java.util.Set;

import org.aws4j.core.exception.NotImplementedException;
import org.aws4j.core.util.JacksonUtil;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter<V> implements AttributeValueConverter<V> {

	private Type parameterType;
	private ObjectMapper mapper;

	public JsonConverter(Type parameterType) {
		this.parameterType = parameterType;
		this.mapper = new ObjectMapper();
	};

	// TODO Util
	private String toJsonString(V value) {
		try {
			return mapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	private V toValue(String json) {
		try {
			return mapper.readValue(json,
					JacksonUtil.getJavaType(parameterType));
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public AttributeValue convert(V value) {
		return new AttributeValue().withS(toJsonString(value));
	}

	@Override
	public AttributeValue convertFromSet(Set<V> values) {
		throw new NotImplementedException();
	}

	@Override
	public V deconvert(AttributeValue attrValue) {
		// TODO Auto-generated method stub
		return toValue(attrValue.getS());
	}

	@Override
	public Set<V> deconvertToSet(AttributeValue attrValue) {
		throw new NotImplementedException();
	}

}

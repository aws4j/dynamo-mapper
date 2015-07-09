package org.aws4j.core.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

	private static final ObjectMapper mapper = new ObjectMapper()
			.setSerializationInclusion(JsonInclude.Include.NON_NULL);;

	public static <M> String toJson(M model) {
		try {
			return mapper.writeValueAsString(model);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException(e);
		}
	}

	public static <M> M fromJson(Class<M> modelClass, String json) {
		try {
			return mapper.readValue(json, modelClass);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}

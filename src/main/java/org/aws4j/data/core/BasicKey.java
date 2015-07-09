package org.aws4j.data.core;

import java.util.Arrays;
import java.util.List;

public class BasicKey implements Key {

	public static BasicKey create( BasicKeyHandler keyHandler, Class<?> modelClass, List<?> keyValues ) {
		return new BasicKey( keyHandler, modelClass, keyValues );
	}

	public static BasicKey create( BasicKeyHandler keyHandler, Class<?> modelClass, Object... keyValues ) {
		return create( keyHandler, modelClass, Arrays.asList( keyValues ) );
	}

	protected final KeyHandler keyHandler;
	protected final Class<?> modelClass;
	protected final List<?> keyValues;

	private BasicKey( BasicKeyHandler keyHandler, Class<?> modelClass, List<?> keyValues ) {
		this.keyHandler = keyHandler;
		this.modelClass = modelClass;
		this.keyValues = keyValues;
	}

	@Override
	public String getKeyString() {
		return keyHandler.encodeKey( modelClass, keyValues );
	}

	@Override
	public Class<?> getModelClass() {
		return modelClass;
	}
}

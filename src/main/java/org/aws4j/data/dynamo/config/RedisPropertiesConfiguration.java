package org.aws4j.data.dynamo.config;

import java.util.ResourceBundle;

public class RedisPropertiesConfiguration extends AbstractRedisConfiguration {

	private static final String DEFAULT_BASE_NAME = "redis";

	private ResourceBundle bundle;

	public RedisPropertiesConfiguration() {
		this.bundle = ResourceBundle.getBundle(DEFAULT_BASE_NAME);
	}

	public RedisPropertiesConfiguration( String baseName ) {
		this.bundle = ResourceBundle.getBundle( baseName );
	}

	private String getStringOrDefault( RedisConfigParam param, String defaultValue ) {
		if ( bundle.containsKey( param.getPath() ) ) {
			return bundle.getString( param.getPath() );
		}
		return defaultValue;
	}

	private Integer getIntegerOrDefault( RedisConfigParam param, Integer defaultValue ) {
		if ( bundle.containsKey( param.getPath() ) ) {
			return Integer.valueOf( bundle.getString( param.getPath() ) );
		}
		return defaultValue;
	}

	private Boolean getBooleanOrDefault( RedisConfigParam param, Boolean defaultValue ) {
		if ( bundle.containsKey( param.getPath() ) ) {
			return Boolean.valueOf( bundle.getString( param.getPath() ) );
		}
		return defaultValue;
	}

	@Override
	public String getHost() {
		return getStringOrDefault( RedisConfigParam.Host, DEFAULT_HOST );
	}

	@Override
	public Integer getPort() {
		return getIntegerOrDefault( RedisConfigParam.Port, DEFAULT_PORT );
	}

	@Override
	public Integer getTimeoutMillis() {
		return getIntegerOrDefault( RedisConfigParam.Timeout, DEFAULT_TIMEOUT_MILLIS );
	}

	@Override
	public Boolean isMock() {
		return getBooleanOrDefault( RedisConfigParam.Mock, false );
	}
}

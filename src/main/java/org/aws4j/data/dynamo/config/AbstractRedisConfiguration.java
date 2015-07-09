package org.aws4j.data.dynamo.config;

import org.aws4j.data.cache.redis.RedisConfiguration;

public abstract class AbstractRedisConfiguration implements RedisConfiguration {

	protected static final String DEFAULT_HOST = "localhost";
	protected static final Integer DEFAULT_PORT = 6379;
	protected static final Integer DEFAULT_TIMEOUT_MILLIS = 300;

}

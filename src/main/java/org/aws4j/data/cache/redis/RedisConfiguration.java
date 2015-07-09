package org.aws4j.data.cache.redis;

public interface RedisConfiguration {

	String getHost();

	Integer getPort();

	Integer getTimeoutMillis();

	Boolean isMock();

}

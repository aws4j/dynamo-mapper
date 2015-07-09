package org.aws4j.data.dynamo.config;

public enum RedisConfigParam {

	Host("redis.host"), //
	Port("redis.port"), //
	Timeout("redis.timeout"), //
	Mock("redis.mock");

	private String path;

	private RedisConfigParam(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}

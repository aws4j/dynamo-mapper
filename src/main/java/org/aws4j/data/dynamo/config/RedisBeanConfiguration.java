package org.aws4j.data.dynamo.config;

public class RedisBeanConfiguration extends AbstractRedisConfiguration {

	private String host;
	private Integer port;
	private Integer timeoutMillis;
	private Boolean mock = false;

	@Override
	public String getHost() {
		return (host == null) ? DEFAULT_HOST : host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Override
	public Integer getPort() {
		return (port == null) ? DEFAULT_PORT : port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	@Override
	public Integer getTimeoutMillis() {
		return (timeoutMillis == null) ? DEFAULT_TIMEOUT_MILLIS : timeoutMillis;
	}

	public void setTimeoutMillis(Integer timeoutMillis) {
		this.timeoutMillis = timeoutMillis;
	}

	@Override
	public Boolean isMock() {
		return mock;
	}

	public void isMock(Boolean mock) {
		this.mock = mock;
	}
}

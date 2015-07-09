package org.aws4j.data.dynamo.config;

public class BeanConfiguration implements Configuration {

	private String awsAccessKey;
	private String awsSecretKey;
	private String endPoint;
	private String tableNamePrefix;
	private String proxyHost;
	private Integer proxyPort;

	private Boolean mock = false;

	@Override
	public String getAwsAccessKey() {
		return awsAccessKey;
	}

	@Override
	public String getAwsSecretKey() {
		return awsSecretKey;
	}

	@Override
	public String getEndPoint() {
		return endPoint;
	}

	public void setAwsAccessKey(String awsAccessKey) {
		this.awsAccessKey = awsAccessKey;
	}

	public void setAwsSecretKey(String awsSecretKey) {
		this.awsSecretKey = awsSecretKey;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public Boolean isMock() {
		return mock;
	}

	public void isMock(Boolean mock) {
		this.mock = mock;
	}

	public String getTableNamePrefix() {
		return tableNamePrefix;
	}

	public void setTableNamePrefix(String tableNamePrefix) {
		this.tableNamePrefix = tableNamePrefix;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}
}

package org.aws4j.data.dynamo.config;

public enum ConfigParam {

	AccessKey("dynamo.accessKey"), //
	SecretKey("dynamo.secretKey"), //
	EndPoint("dynamo.endPoint"), //
	TableNamePrefix("dynamo.tableName.prefix"), //
	ProxyHost("dynamo.proxy.host"), //
	ProxyPort("dynamo.proxy.port"), //
	Mock("dynamo.mock");

	private String path;

	private ConfigParam(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

}

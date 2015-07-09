package org.aws4j.data.dynamo.config;

public interface Configuration {

	String getAwsAccessKey();

	String getAwsSecretKey();

	String getEndPoint();

	String getTableNamePrefix();

	String getProxyHost();

	Integer getProxyPort();

	Boolean isMock();
}

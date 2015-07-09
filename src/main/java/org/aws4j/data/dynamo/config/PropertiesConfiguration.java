package org.aws4j.data.dynamo.config;

import java.util.ResourceBundle;

public class PropertiesConfiguration implements Configuration {

	private static final String DEFAULT_BASE_NAME = "dynamo";

	private ResourceBundle bundle;

	public PropertiesConfiguration() {
		this.bundle = ResourceBundle.getBundle(DEFAULT_BASE_NAME);
	}

	public PropertiesConfiguration( String baseName ) {
		this.bundle = ResourceBundle.getBundle( baseName );
	}

	private String getString( ConfigParam param ) {
		if ( bundle.containsKey( param.getPath() ) ) {
			return bundle.getString( param.getPath() );
		}
		return null;
	}

	private Integer getInteger( ConfigParam param ) {
		if ( bundle.containsKey( param.getPath() ) ) {
			return Integer.valueOf( bundle.getString( param.getPath() ) );
		}
		return null;
	}

	private Boolean getBoolean( ConfigParam param ) {
		if ( bundle.containsKey( param.getPath() ) ) {
			return Boolean.valueOf( bundle.getString( param.getPath() ) );
		}
		return null;
	}

	public String getAwsAccessKey() {
		return getString( ConfigParam.AccessKey );
	}

	@Override
	public String getAwsSecretKey() {
		return getString( ConfigParam.SecretKey );
	}

	@Override
	public String getEndPoint() {
		return getString( ConfigParam.EndPoint );
	}

	@Override
	public String getTableNamePrefix() {
		return getString( ConfigParam.TableNamePrefix );
	}

	@Override
	public String getProxyHost() {
		return getString( ConfigParam.ProxyHost );
	}

	@Override
	public Integer getProxyPort() {
		return getInteger( ConfigParam.ProxyPort );
	}

	@Override
	public Boolean isMock() {
		return getBoolean( ConfigParam.Mock );
	}
}

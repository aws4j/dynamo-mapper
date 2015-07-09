package org.aws4j.data.dynamo.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class TypeSafeConfiguration implements Configuration {

	private Config conf;

	private String getString( ConfigParam param ) {
		if ( conf.hasPath( param.getPath() ) ) {
			return conf.getString( param.getPath() );
		} else {
			return null;
		}
	}

	private Integer getInteger( ConfigParam param ) {
		if ( conf.hasPath( param.getPath() ) ) {
			return Integer.valueOf( conf.getInt( param.getPath() ) );
		} else {
			return null;
		}
	}

	private Boolean getBoolean( ConfigParam param ) {
		if ( conf.hasPath( param.getPath() ) ) {
			return Boolean.valueOf( conf.getBoolean( param.getPath() ) );
		} else {
			return null;
		}
	}


	public TypeSafeConfiguration() {
		this.conf = ConfigFactory.load();
	}

	public TypeSafeConfiguration(Config conf) {
		this.conf = conf;
	}

	@Override
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

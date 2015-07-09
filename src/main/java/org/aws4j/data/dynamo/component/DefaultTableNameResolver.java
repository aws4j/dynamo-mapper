package org.aws4j.data.dynamo.component;

import org.aws4j.core.util.StringUtil;
import org.aws4j.data.dynamo.attribute.MetaModels;
import org.aws4j.data.dynamo.service.dynamo.TableNameResolver;

public class DefaultTableNameResolver implements TableNameResolver {

	private String prefix;

	private String makeTableName( String modelsName ) {
		if (StringUtil.isNotEmpty(prefix)) {
			return prefix + modelsName;
		} else {
			return modelsName;
		}
	}

	@Override
	public <M> String getTableName( Class<M> modelClass ) {
		return makeTableName( MetaModels.getMeta( modelClass ).getTableName() );
	}

	public void setTableNamePrefix( String prefix ) {
		this.prefix = prefix;
	}

	public DefaultTableNameResolver withPrefix( String prefix ) {
		setTableNamePrefix( prefix );
		return this;
	}
}

package org.aws4j.data.dynamo.service.dynamo;

public interface TableNameResolver {

	<M> String getTableName( Class<M> modelClass );

}

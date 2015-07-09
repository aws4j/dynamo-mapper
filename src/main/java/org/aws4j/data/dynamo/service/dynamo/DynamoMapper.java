package org.aws4j.data.dynamo.service.dynamo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.aws4j.core.exception.NotImplementedException;
import org.aws4j.core.util.ClassUtil;
import org.aws4j.core.util.Collections;
import org.aws4j.core.util.QueueList;
import org.aws4j.core.util.Reflector;
import org.aws4j.core.util.StringUtil;
import org.aws4j.data.core.Datastore;
import org.aws4j.data.core.Key;
import org.aws4j.data.core.Query;
import org.aws4j.data.core.util.InMemoryQuery;
import org.aws4j.data.dynamo.attribute.DynamoFilterCriteria;
import org.aws4j.data.dynamo.attribute.DynamoUpdateCriteria;
import org.aws4j.data.dynamo.attribute.MetaModel;
import org.aws4j.data.dynamo.attribute.MetaModels;
import org.aws4j.data.dynamo.component.DefaultTableNameResolver;
import org.aws4j.data.dynamo.config.Configuration;
import org.aws4j.data.dynamo.model.DynamoKey;
import org.aws4j.data.dynamo.query.DynamoQuery;
import org.aws4j.data.dynamo.query.PutQuery;
import org.aws4j.data.dynamo.query.DynamoUpdateQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.internal.StaticCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemRequest;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemResult;
import com.amazonaws.services.dynamodbv2.model.BatchWriteItemRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.DeleteRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.PutRequest;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.ReturnConsumedCapacity;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

public class DynamoMapper implements Datastore {

	private static final Logger Log = LoggerFactory.getLogger(DynamoMapper.class);

	private static final int MAX_BATCH_GET_SIZE = 100;
	private static final int MAX_BATCH_WRITE_SIZE = 25;

	protected AmazonDynamoDB dynamo;
	protected Configuration conf;

	protected TableNameResolver tableNameResolever;
	protected InMemoryQuery inMemoryQuery;

	public DynamoMapper( Configuration conf ) {
		this.dynamo = new AmazonDynamoDBClient( getCredentialsProvider( conf ), getClientConfiguration( conf ) );
		this.dynamo.setEndpoint( conf.getEndPoint() ); // TODO
		this.tableNameResolever = new DefaultTableNameResolver().withPrefix( conf.getTableNamePrefix() );
		this.conf = conf;
	}

	private AWSCredentialsProvider getCredentialsProvider(Configuration conf) {
		if ( StringUtil.isNotEmpty( conf.getAwsAccessKey() )
				&& StringUtil.isNotEmpty( conf.getAwsSecretKey() ) ) {
			return new StaticCredentialsProvider(
					new BasicAWSCredentials( conf.getAwsAccessKey(), conf.getAwsSecretKey() ) );
		} else {
			return new DefaultAWSCredentialsProviderChain();
		}
	}

	private ClientConfiguration getClientConfiguration(Configuration conf) {
		ClientConfiguration clientConf = new ClientConfiguration();
		if ( StringUtil.isNotEmpty( conf.getProxyHost() ) ) {
			clientConf.setProxyHost( conf.getProxyHost() );
			clientConf.setProxyPort( conf.getProxyPort() );
		}
		return clientConf;
	}

	private boolean isMock() {
		return ( conf.isMock() != null ) ? conf.isMock() : false;
	}

	@SuppressWarnings("unchecked")
	private <M> String getTableName( Key key) {
		Class<M> modelClass = (Class<M>) key.getModelClass();
		return getTableName( modelClass );
	}

	private <M> String getTableName(Class<M> modelClass) {
		return tableNameResolever.getTableName( modelClass );
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////

	private <M> MetaModel<M> getMeta(Class<M> modelClass) {
		return MetaModels.getMeta( modelClass );
	}

	private Map<String, AttributeValue> toKeyMap( Key key ) {
		if( key instanceof DynamoKey ) {
			return getMeta( key.getModelClass() ).toKeyMap( (DynamoKey) key );
		}
		throw new IllegalArgumentException( "Key must be DynamoKey. " + key.getClass() );
	}

	private <M> Map<String, AttributeValue> getKeyMap( M model ) {
		return getMeta( ClassUtil.getClass( model ) ).getKeyMap( model );
	}

	private <M> Map<String, AttributeValue> toItemMap(M model) {
		return getMeta( ClassUtil.getClass( model ) ).toItemMap( model );
	}

	private <M> Map<String, AttributeValueUpdate> getUpdateItemMap( M model ) {
		return getMeta( ClassUtil.getClass( model ) ).getUpdateItemMap( model );
	}

	private <M> M toModel( Class<M> modelClass, Map<String, AttributeValue> itemMap ) {
		if ( itemMap == null ) 	return null;
		return getMeta( modelClass ).toModel( itemMap );
	}

	// //////// Request Maker Section //////////

	private GetItemRequest makeGetItemRequest( Key key ) {
		return new GetItemRequest()
			.withTableName( getTableName( key ) )
			.withReturnConsumedCapacity( ReturnConsumedCapacity.TOTAL )
			.withKey( toKeyMap( key ) );
	}

	private <M> PutItemRequest makePutItemRequest( M model ) {
		return new PutItemRequest()
			.withTableName( getTableName( ClassUtil.getClass( model ) ) )
			.withReturnConsumedCapacity( ReturnConsumedCapacity.TOTAL )
			.withItem( toItemMap( model ) );
	}

	private <M> UpdateItemRequest makeUpdateItemRequest (M model ) {
		return new UpdateItemRequest()
				.withTableName( getTableName( ClassUtil.getClass( model ) ) )
				.withReturnConsumedCapacity( ReturnConsumedCapacity.TOTAL )
				.withKey( getKeyMap( model ) )
				.withAttributeUpdates( getUpdateItemMap( model ) )
				.withReturnValues( ReturnValue.ALL_NEW );
	}

	private <M> UpdateItemRequest makeUpdateItemRequest( MetaModel<M> meta, DynamoKey key, Collection<DynamoUpdateCriteria<M, ?>> updates, Collection<DynamoFilterCriteria<M, ?>> conditions ) {
		return new UpdateItemRequest()
			.withTableName( getTableName( meta.getModelClass() ) )
			.withReturnConsumedCapacity( ReturnConsumedCapacity.TOTAL )
			.withKey( toKeyMap( key ) )
			.withAttributeUpdates( meta.makeAttributeUpdates( updates ) )
			.withExpected( meta.makeExpected( conditions )  )
			.withReturnValues( ReturnValue.ALL_NEW );
	}

	private <M> QueryRequest extractQueryRequest( Query<M> query ) {
//		return new QueryRequestExtractor( query ).extract();
		throw new NotImplementedException();
	}

	private <M> DeleteItemRequest makeDeleteItemRequest( M model ) {
		return new DeleteItemRequest()
			.withTableName( getTableName( ClassUtil.getClass( model ) ) )
			.withReturnConsumedCapacity( ReturnConsumedCapacity.TOTAL )
			.withKey( getKeyMap( model )  );
	}

	private <M, K extends Key> BatchGetItemRequest makeBatchGetItemRequest( Class<M> modelClass, Collection<K> keys ) {
		Collection<Map<String, AttributeValue>> keyMaps = new ArrayList<Map<String, AttributeValue>>();
		for ( Key key : keys ) {
			keyMaps.add( toKeyMap( key ) );
		}
		return new BatchGetItemRequest().addRequestItemsEntry(
					getTableName( modelClass ),
					new KeysAndAttributes().withKeys( keyMaps ) );
	}

	private <M> List<WriteRequest> makeWriteRequestsForPut(Class<M> modelClass, Collection<M> models ) {
		List<WriteRequest> writeRequests = new ArrayList<WriteRequest>();
		for (M model : models) {
			writeRequests.add( new WriteRequest( new PutRequest( toItemMap( model ) ) ) );
		}
		return writeRequests;
	}

	@SuppressWarnings("unused")
	private <M> BatchWriteItemRequest makeBatchPutItemRequest( Class<M> modelClass, Collection<M> models ) {
		String tableName = getTableName( modelClass );
		return new BatchWriteItemRequest()
			.addRequestItemsEntry(tableName, makeWriteRequestsForPut( modelClass, models ) );
	}

	private <M> List<WriteRequest> makeWriteRequestsForDelete(	Collection<Key> keys ) {
		List<WriteRequest> writeRequests = new ArrayList<WriteRequest>();
		for( Key key : keys ) {
			writeRequests.add( new WriteRequest( new DeleteRequest( toKeyMap( key ) ) ) );
		}
		return writeRequests;
	}

	@SuppressWarnings("unused")
	private <M> BatchWriteItemRequest makeBatchDeleteItemRequest(
			Collection<Key> keys) {
		String tableName = getTableName(Collections.getFirst(keys));
		return new BatchWriteItemRequest()
			.addRequestItemsEntry(tableName, makeWriteRequestsForDelete( keys ) );
	}

	private <M> CreateTableRequest makeCreateTableRequest(Class<M> modelClass,
			long readUnits, long writeUnits) {

		MetaModel<M> meta = getMeta(modelClass);

		return new CreateTableRequest().withTableName(getTableName(modelClass))
				.withKeySchema(meta.getKeySchema())
				// .withAttributeDefinitions(meta.getAttributeDefinitions())
				// .withLocalSecondaryIndexes( meta.getLocalSecondaryIndexes() )
				// .withGlobalSecondaryIndexes( meta.getGlobalSecondaryIndexes()
				// )
				.withProvisionedThroughput(
						new ProvisionedThroughput(readUnits, writeUnits));
	}

	private <M> DeleteTableRequest makeDeleteTableRequest( Class<M> modelClass ) {
		return new DeleteTableRequest().withTableName( getTableName( modelClass ) );
	}

	// //////// Public Method Section //////////

	public <M, K extends Key> M get( Class<M> modelClass, K key ) {

		if (isMock())
			return Reflector.newInstance(modelClass);

		GetItemResult result = dynamo.getItem( makeGetItemRequest( key ) );
		Log.debug( result.getConsumedCapacity().getCapacityUnits().toString() );

		return toModel( modelClass, result.getItem() );
	}

	public <M, K extends Key> List<M> get( Class<M> modelClass, Collection<K> keys ) {

		// TODO fail safe
		List<M> models = new ArrayList<M>();

		if (isMock())
			return models;

		QueueList<K> remainKeys = new QueueList<K>(keys);
		while ( remainKeys.isNotEmpty() ) {
			List<K> requestKeys = remainKeys.poll( MAX_BATCH_GET_SIZE );
			BatchGetItemResult result = dynamo.batchGetItem( makeBatchGetItemRequest( modelClass, requestKeys ) );
			List<Map<String, AttributeValue>> itemMapList = result.getResponses().get( getTableName( modelClass ) );
			for ( Map<String, AttributeValue> itemMap : itemMapList ) {
				models.add( toModel( modelClass, itemMap ) );
			}
		}
		return models;
	}

	@Override
	public <M, K extends Key> Map<K, M> getAsMap( Class<M> modelClass, Collection<K> keys ) {
		// TODO Auto-generated method stub
		return null;
	}

	public <M> List<M> query( DynamoQuery<M> query ) {
		/*
		DynamoQueryExtractor<M> queryExtractor = new DynamoQueryExtractor<M>( query );
		QueryResult result = dynamo.query( queryExtractor.extractQueryRequest() );
		List<M> models = new ArrayList<M>();
		for ( Map<String, AttributeValue> itemMap : result.getItems() ) {
			models.add( toModel( query.getModelClass() , itemMap ) );
		}
		return inMemoryQuery.query( models, queryExtractor.extractInMemoryQuery() );
		*/
		throw new NotImplementedException();
	}

	public <M> M create( PutQuery<M>  putQuery ) {
		throw new NotImplementedException();
	}

	public <M> M update( MetaModel<M> meta, DynamoKey key, Collection<DynamoUpdateCriteria<M, ?>> updates, Collection<DynamoFilterCriteria<M, ?>> conditions ) {
		UpdateItemResult result = dynamo.updateItem( makeUpdateItemRequest( meta, key, updates, conditions ) );
		return meta.toModel( result.getAttributes() );
	}

	public <M> M update( M model ) {

		if (isMock())
			return model;

		UpdateItemResult result = dynamo.updateItem( makeUpdateItemRequest( model ) );
		Log.debug( "update = " + result.getConsumedCapacity().toString() );
		return toModel( ClassUtil.getClass( model ), result.getAttributes() );
	}

	@Deprecated
	public <M> void put( M model ) {

		if (isMock())
			return;

		PutItemResult result = dynamo.putItem( makePutItemRequest( model ) );
		Log.debug( result.getConsumedCapacity().toString() );
	}


	/*
	public <M> List<Key> update( Class<M> modelClass, Collection<M> models ) {
		throw new NotImplementedException();
	}
	*/

	public <M> void delete( M model ) {
		DeleteItemResult result = dynamo.deleteItem( makeDeleteItemRequest( model ) );
		Log.debug( result.getConsumedCapacity().toString() );
	}

	public <M> List<Key> delete( Class<M> modelClass, Collection<M> models ) {
		throw new NotImplementedException();
	}

	public <M> void createTable( Class<M> modelClass, long readUnits,	long writeUnits ) {
		dynamo.createTable( makeCreateTableRequest( modelClass, readUnits, writeUnits ) );
	}

	public <M> void deleteTable( Class<M> modelClass ) {
		dynamo.deleteTable(makeDeleteTableRequest(modelClass));
	}

	@Override
	public <M> M create(M model) {
		// TODO Auto-generated method stub
		return null;
	}


	// //////// mini //////////

}

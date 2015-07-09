package org.aws4j.data.dynamo.service.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;

public class DynamoCounterService {

	private static String COUNTER_TABLE_NAME = "counter";
	private static String COUNTER_TABLE_HASH_KEY_NAME = "category";
	private static String COUNTER_TABLE_COUNT_ATTRIBUTE_NAME = "count";

	private AmazonDynamoDB db;

	public DynamoCounterService( AmazonDynamoDB db ) {
		this.db = db;
	}

	private UpdateItemRequest makeUpdateItemRequest( String key ) {

		return new UpdateItemRequest()
				.withTableName( COUNTER_TABLE_NAME )
				.addKeyEntry( COUNTER_TABLE_HASH_KEY_NAME, new AttributeValue( key ) )
				.addAttributeUpdatesEntry( COUNTER_TABLE_COUNT_ATTRIBUTE_NAME, new AttributeValueUpdate()
						.withAction( AttributeAction.ADD )
						.withValue( new AttributeValue().withN( "1" ) ) )
				.withReturnValues( ReturnValue.UPDATED_NEW );
	}

	public Integer getNextNumber( String key ) {

		UpdateItemResult result = db.updateItem( makeUpdateItemRequest( key ) );
		return Integer.valueOf( result.getAttributes().get( COUNTER_TABLE_COUNT_ATTRIBUTE_NAME ).getN() );
	}
}

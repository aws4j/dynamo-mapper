package org.aws4j.data.dynamo.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "counter")
public class Counter {

	private String category;
	private Integer count;

	@DynamoDBHashKey( attributeName="counter" )
	public String getCategory() {
		return category;
	}
	public void setCategory( String category ) {
		this.category = category;
	}

	@DynamoDBAttribute( attributeName="count" )
	public Integer getCount() {
		return count;
	}
	public void setCount( Integer count ) {
		this.count = count;
	}

}

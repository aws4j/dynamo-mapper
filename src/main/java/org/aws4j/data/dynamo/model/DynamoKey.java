package org.aws4j.data.dynamo.model;

import org.aws4j.core.util.Objects;
import org.aws4j.data.core.Key;
import org.aws4j.data.dynamo.util.KeyUtil;

import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;

public class DynamoKey implements Key {

	private static final int KEYSTRING_MODEL_NAME_PART = 0;
	private static final int KEYSTRING_HASH_PART = 1;
	private static final int KEYSTRING_RANGE_PART = 2;
	private static final String KEYSTRING_DELIMITER = ":";

	public static DynamoKey create(Class<?> modelClass, Object hashKey) {
		return new DynamoKey(modelClass, hashKey);
	}

	public static DynamoKey create( Class<?> modelClass, Object hashKey, Object rangeKey ) {
		return new DynamoKey( modelClass, hashKey, rangeKey );
	}

	private Class<?> modelClass;

	private Object hashKey;

	private Object rangeKey;

	private DynamoKey(Class<?> modelClass, Object hashKey, Object rangeKey) {

		// TODO modelClass Check
		this.modelClass = modelClass;
		this.hashKey = hashKey;
		this.rangeKey = rangeKey;
	}

	private DynamoKey(Class<?> modelClass, Object hashKey) {

		// TODO modelClass Check
		this.modelClass = modelClass;
		this.hashKey = hashKey;
	}

	/*
	public DynamoKey(String encodedKeyString) {

		String keyString = KeyUtil.decode(encodedKeyString);

		String[] keyParts = StringUtil.split(keyString, KEYSTRING_DELIMITER);
		if (keyParts.length != 2 && keyParts.length != 3) {
			throw new IllegalArgumentException("[" + keyString
					+ "] is not keyString!");
		}

		modelClass = DynamoReflector.getModelRefByModelName(
				keyParts[KEYSTRING_MODEL_NAME_PART]).getModelClass();
		hashKey = keyParts[KEYSTRING_HASH_PART];

		if (keyParts.length == 3) {
			rangeKey = keyParts[KEYSTRING_RANGE_PART];
		}
	}
	*/

	@Override
	public String getKeyString() {

		String modelName = modelClass.getName(); // TODO

		String keyString = modelName
				+ KEYSTRING_DELIMITER
				+ (isHashKeyOnly() ? hashKey.toString() : hashKey.toString() + KEYSTRING_DELIMITER	+ rangeKey.toString());
		return KeyUtil.encode(keyString);
	}

	public Class<?> getModelClass() {
		return modelClass;
	}

	public void setModelClass(Class<?> modelClass) {
		this.modelClass = modelClass;
	}

	public Object getHashKey() {
		return hashKey;
	}

	public void setHashKey(Object hashKey) {
		this.hashKey = hashKey;
	}

	public Object getRangeKey() {
		return rangeKey;
	}

	public void setRangeKey(String rangeKey) {
		this.rangeKey = rangeKey;
	}

	public KeyPair getKeyPair() {

		KeyPair keyPair = new KeyPair();
		keyPair.setHashKey(hashKey);
		keyPair.setRangeKey(rangeKey);
		return keyPair;
	}

	public boolean isHashKeyOnly() {
		return rangeKey == null ? true : false;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode( modelClass, hashKey, rangeKey );
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		DynamoKey that = (DynamoKey) o;
		return Objects.equal(this.modelClass, that.modelClass)
				&& Objects.equal(this.hashKey, that.hashKey)
				&& Objects.equal(this.rangeKey, that.rangeKey);
	}

	@Override
	public String toString() {
		return modelClass.toString() + ":" + hashKey + ":" + rangeKey;
	}

}

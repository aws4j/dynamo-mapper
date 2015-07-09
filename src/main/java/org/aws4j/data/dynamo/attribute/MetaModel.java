package org.aws4j.data.dynamo.attribute;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aws4j.core.exception.NotImplementedException;
import org.aws4j.core.util.Reflector;
import org.aws4j.data.core.util.AttributeRef;
import org.aws4j.data.core.util.ModelRef;
import org.aws4j.data.core.util.Models;
import org.aws4j.data.dynamo.annotation.DynamoTable;
import org.aws4j.data.dynamo.annotation.HashKey;
import org.aws4j.data.dynamo.annotation.RangeKey;
import org.aws4j.data.dynamo.model.DynamoKey;
import org.aws4j.data.dynamo.service.dynamo.AttributeValueConvertManager;
import org.aws4j.data.dynamo.service.dynamo.AttributeValueUpdateConvertManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

public class MetaModel<M> {

	private static final Logger Log = LoggerFactory.getLogger( MetaModel.class );

	private static AttributeValueConvertManager attrConvertManager = AttributeValueConvertManager.INSTANCE;
	private static AttributeValueUpdateConvertManager updateConvertManager = AttributeValueUpdateConvertManager.INSTANCE;

	private ModelRef<M> modelRef;
	protected Class<M> modelClass;
	protected DynamoTable table;
	protected List<Attribute<?, ?>> attributes = new ArrayList<Attribute<?, ?>>();

	public MetaModel( Class<M> modelClass ) {
		this( Models.getModelRef(modelClass) );
	}

	protected <A extends Attribute<M, ?>> A addAttribute( A attribute ) {
		attributes.add( attribute );
		return attribute;
	}

	protected StringAttribute<M> createStringAttribute( String name ) {
		return addAttribute( new StringAttribute<M>( modelClass, name ) );
	}

	protected <V> NumberAttribute<M, V> createNumberAttribute(	Class<V> valueClass, String name ) {
		return addAttribute( new NumberAttribute<M, V>( modelClass, valueClass, name ) );
	}

	protected DateAttribute<M> createDateAttribute( String name ) {
		return addAttribute( new DateAttribute<M>( modelClass, name ) );
	}

	protected <V> SetAttribute<M, V> createSetAttribute( Class<V> valueClass, String name ) {
		return addAttribute( new SetAttribute<M, V>( modelClass, valueClass, name ) );
	}

	protected <V> EnumAttribute<M, V> createEnumAttribute( Class<V> valueClass, String name ) {
		return addAttribute( new EnumAttribute<M,V>( modelClass, valueClass, name ) );
	}

	// TODO temp

	public MetaModel( ModelRef<M> modelRef ) {
		this.modelRef = modelRef;
		this.modelClass = modelRef.getModelClass();
		this.table = modelRef.getAnnotation( DynamoTable.class );
		if ( table == null ) {
			throw new IllegalStateException("MetaModel must be set DynamoTable annotation.");
		}
	}

	private List<AttributeRef> getAttributesExceptedKeys() {
		List<AttributeRef> attrRefs = new ArrayList<AttributeRef>();
		for ( AttributeRef attrRef : modelRef.getAttributes() ) {
			if ( ! attrRef.contains( HashKey.class ) && ! attrRef.contains( RangeKey.class ) ) {
				attrRefs.add( attrRef );
			}
		}
		return attrRefs;
	}

	private AttributeValue toAttributeValue( Type propertyType, Object value ) {
		if ( value == null ) return null;
		return attrConvertManager.convert( propertyType, value );
	}

	private AttributeValueUpdate toAttributeValueUpdate( Type propertyType, Object value ) {
		if ( value == null ) return null;
		return updateConvertManager.convert( propertyType, value );
	}

	private Object toPropertyValue(Type propertyType, AttributeValue attrValue) {
		return attrConvertManager.deconvert( propertyType, attrValue );
	}

	// TODO support B
	private ScalarAttributeType toScalarAttributeType(Type propertyType) {
		if (propertyType instanceof Class) {
			Class<?> clazz = (Class<?>) propertyType;
			if (Integer.class.equals(clazz) || Long.class.equals(clazz)
					|| Float.class.equals(clazz) || Double.class.equals(clazz)) {

				return ScalarAttributeType.N;
			}
		}
		return ScalarAttributeType.S;
	}

	private List<AttributeDefinition> getAttributeDefinitions() {
		List<AttributeDefinition> attrDefs = new ArrayList<AttributeDefinition>();
		AttributeRef hashKeyAttr = modelRef.getHashKeyAttributeRef();
		AttributeRef rangeKeyAttr = modelRef.getRangeKeyAttributeRef();
		attrDefs.add(new AttributeDefinition(hashKeyAttr.getAttributeName(),
				toScalarAttributeType(hashKeyAttr.getPropertyType())));
		if (rangeKeyAttr != null) {
			attrDefs.add(new AttributeDefinition(rangeKeyAttr
					.getAttributeName(), toScalarAttributeType(rangeKeyAttr.getPropertyType())));
		}
		return attrDefs;
	}

	public DynamoKey getKey( M model ) {
		AttributeRef hashKeyAttr = modelRef.getHashKeyAttributeRef();
		AttributeRef rangeKeyAttr = modelRef.getRangeKeyAttributeRef();
		if (rangeKeyAttr != null) {
			return DynamoKey.create(
					modelClass,
					modelRef.getPropertyValue(model,
							hashKeyAttr.getPropertyName()),
					modelRef.getPropertyValue(model,
							rangeKeyAttr.getPropertyName()));
		} else {
			return DynamoKey.create(
					modelClass,
					modelRef.getPropertyValue(model,
							hashKeyAttr.getPropertyName()));
		}

	}

	public Map<String, AttributeValue> toKeyMap( DynamoKey key ) {
		Map<String, AttributeValue> keyMap = new HashMap<String, AttributeValue>();
		AttributeRef hashKeyAttr = modelRef.getHashKeyAttributeRef();
		AttributeRef rangeKeyAttr = modelRef.getRangeKeyAttributeRef();
		keyMap.put(	hashKeyAttr.getAttributeName(),	toAttributeValue(hashKeyAttr.getPropertyType(),	key.getHashKey() ) );
		if ( rangeKeyAttr != null ) {
			keyMap.put(
					rangeKeyAttr.getAttributeName(),
					toAttributeValue(rangeKeyAttr.getPropertyType(),
							key.getRangeKey()));
		}
		return keyMap;
	}

	public Map<String, AttributeValue> getKeyMap( M model ) {
		Map<String, AttributeValue> keyMap = new HashMap<String, AttributeValue>();
		AttributeRef hashKeyAttr = modelRef.getHashKeyAttributeRef();
		AttributeRef rangeKeyAttr = modelRef.getRangeKeyAttributeRef();
		keyMap.put(	hashKeyAttr.getAttributeName(),
				toAttributeValue( hashKeyAttr.getPropertyType(), hashKeyAttr.getPropertyValue( model ) ) );
		if ( rangeKeyAttr != null ) {
			keyMap.put(
					rangeKeyAttr.getAttributeName(),
					toAttributeValue( rangeKeyAttr.getPropertyType(),
							rangeKeyAttr.getPropertyValue( model ) ) );
		}
		return keyMap;
	}

	public List<KeySchemaElement> getKeySchema() {
		List<KeySchemaElement> keySchemas = new ArrayList<KeySchemaElement>();
		AttributeRef hashKeyAttr = modelRef.getHashKeyAttributeRef();
		AttributeRef rangeKeyAttr = modelRef.getRangeKeyAttributeRef();
		keySchemas.add(new KeySchemaElement(hashKeyAttr.getAttributeName(),
				KeyType.HASH));
		if (rangeKeyAttr != null) {
			keySchemas.add(new KeySchemaElement(
					rangeKeyAttr.getAttributeName(), KeyType.RANGE));
		}
		return keySchemas;
	}



	public List<LocalSecondaryIndex> getLocalSecondaryIndexes() {
		List<LocalSecondaryIndex> indexes = new ArrayList<LocalSecondaryIndex>();

		return indexes;
	}

	public List<GlobalSecondaryIndex> getGlobalSecondaryIndexes() {
		List<GlobalSecondaryIndex> indexes = new ArrayList<GlobalSecondaryIndex>();

		return indexes;
	}

	public Map<String, AttributeValue> toItemMap(M model) {
		Map<String, AttributeValue> itemMap = new HashMap<String, AttributeValue>();
		for( AttributeRef attrRef : modelRef.getAttributes() ) {
			Object propValue = modelRef.getPropertyValue( model, attrRef.getPropertyName() );
			AttributeValue attrValue = toAttributeValue( attrRef.getPropertyType(), propValue );
			Log.debug( attrRef.getAttributeName() + " [" + propValue + "] to AttributeValue " + attrValue );
			if ( attrValue != null ) {
				itemMap.put( attrRef.getAttributeName(), attrValue );
			}
		}
		return itemMap;
	}

	public Map<String, AttributeValueUpdate> getUpdateItemMap( M model ) {
		Map<String, AttributeValueUpdate> updateMap = new HashMap<String, AttributeValueUpdate>();
		for( AttributeRef attrRef : getAttributesExceptedKeys() ) {
			Object propValue = modelRef.getPropertyValue( model, attrRef.getPropertyName() );
			AttributeValueUpdate attrUpdate = toAttributeValueUpdate( attrRef.getPropertyType(), propValue );
			if ( attrUpdate != null ) {
				Log.debug( attrRef.getAttributeName() + " [" + propValue + "] to AttributeValueUpdate " + attrUpdate );
				updateMap.put( attrRef.getAttributeName(), attrUpdate );
			}
		}
		return updateMap;
	}

	public M toModel(Map<String, AttributeValue> item ) {
		Log.debug("modelClass = " + modelClass);
		Log.debug("modelClass = " + item);
		M model = Reflector.newInstance(modelClass);
		for (String attrName : item.keySet()) {
			AttributeValue attrValue = item.get(attrName);
			AttributeRef attrRef = modelRef.getAttributeRef(attrName);
			modelRef.setPropertyValue(model, attrRef.getPropertyName(),
					toPropertyValue(attrRef.getPropertyType(), attrValue));
		}
		return model;
	}

	public Map<String, AttributeValueUpdate> makeAttributeUpdates( Collection<DynamoUpdateCriteria<M, ?>> updates ) {
		Map<String, AttributeValueUpdate> updateMap = new HashMap<String, AttributeValueUpdate>();
		for( DynamoUpdateCriteria<M, ?> update : updates ) {
			AttributeValueUpdate attrUpdate = new AttributeValueUpdate()
				.withAction( update.getAction() )
				.withValue( toAttributeValue( update.getType(), update.getValue() ) );
			updateMap.put(update.getName(), attrUpdate );
		}
		return updateMap;
	}

	public Map<String, ExpectedAttributeValue> makeExpected( Collection<DynamoFilterCriteria<M, ?>> conditions ) {
		Map<String, ExpectedAttributeValue> expectedMap = new HashMap<String, ExpectedAttributeValue>();
		for( DynamoFilterCriteria<M, ?> condition : conditions ) {
//			ExpectedAttributeValue expected = new ExpectedAttributeValue().withComparisonOperator()
		}
		return expectedMap;
	}

	public String getTableName() {
		return table.tableName();
	}

	public Class<M> getModelClass() {
		return modelClass;
	}
}

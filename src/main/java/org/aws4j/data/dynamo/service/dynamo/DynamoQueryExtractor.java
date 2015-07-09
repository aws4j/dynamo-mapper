package org.aws4j.data.dynamo.service.dynamo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamoQueryExtractor<M> {

	private static Logger log = LoggerFactory.getLogger( DynamoQueryExtractor.class );

	/*
	private Class<M> modelClass;
	private Query<M> query;
	private ModelRef<M> modelRef; // TODO consider
	private Map<Class<? extends Annotation>, Set<String>> annotatedQueryAttributes = new HashMap<Class<? extends Annotation>, Set<String>>();

	private QueryPropertyManager propertyManager;

	public DynamoQueryExtractor( DynamoQuery<M> sorceQuery ) {

		this.modelClass = sorceQuery.getModelClass();
		this.modelRef = Models.getModelRef( modelClass );

		Query query = new Query();

		// convert PartialKeyProperty condition
		for ( Entry<String, > entry : sorceQuery.getCondition().enrtySet() ) {

			log.debug( "source condition name is " + name );
			AttributeRef attrRef = modelRef.getAttributeRef( name );
			if ( attrRef.contains( PartialKeyProperty.class ) ) {
				String attrName = attrRef.getAnnotation( PartialKeyProperty.class ).by();

				// TODO RangeKey convert support
				query.addCondition( attrName, convertConditionValuesFromKeyStringToHashKeyString( sorceQuery.getCondition( name ) ) );
			}
			else {
				query.addCondition( name, sorceQuery.getCondition( name ) );
			}
		}

		// preparing annotatedQueryAttributes
		for( String name : query.getConditions().keySet() ) {
			log.debug( "modified condition name is " + name );
			AttributeRef attrRef = modelRef.getAttributeRef( name );
			for( Annotation annotation : attrRef.getAnnotations() ) {
				Class<? extends Annotation> annotationClass = annotation.annotationType();
				if ( ! annotatedQueryAttributes.containsKey( annotation.annotationType() ) ) {
					annotatedQueryAttributes.put( annotationClass, new LinkedHashSet<String>() );
				}
				annotatedQueryAttributes.get( annotationClass ).add( name );
			}
		}
		this.query = query;
		propertyManager = new QueryPropertyManager( modelClass, query );

		selectUsingIndexAndMarkingProperty();
	}

	private Condition convertConditionValuesFromKeyStringToHashKeyString( Condition sourceCondition ) {

		List<String> values = new ArrayList<String>();

		for( Object value : sourceCondition.getValues() ) {
			String keyString = (String)value;
			values.add( DynamoKey.create( keyString ).getHashKey() );
		}
		return new Condition( sourceCondition.getComparison(), values.toArray() );
	}

	private boolean containsKeyAttribute( Class<? extends Annotation>... annotationClasses ) {

		for( Class<? extends Annotation> annotationClass : annotationClasses ) {
			if ( ! annotatedQueryAttributes.containsKey( annotationClass ) ) return false;
		}
		return true;
	}

	private String getAnnotatedPropertyName( Class<? extends Annotation> annotationClass ) {

		return annotatedQueryAttributes.get( annotationClass ).stream().findFirst().get();
	}

	private ComparisonOperator toComparisonOperator( Comparison comparison ) {

		switch( comparison ){

			case EQ: return ComparisonOperator.EQ;
			case LT: return ComparisonOperator.LT;
			case LE: return ComparisonOperator.LE;
			case GT: return ComparisonOperator.GT;
			case GE: return ComparisonOperator.GE;
			case BTW: return ComparisonOperator.BETWEEN;

			default:
				throw new NotImplementedException();
		}
	}

	private List<AttributeValue> toAttributeValue( Collection<?> values ) {

		List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
		for( Object value : values ) {
			Class<?> valueClass = value.getClass();

			DynamoDBMarshaller marshaller = MarshallerManager.getMarshaller( valueClass );

			if( marshaller != null ) {
				String marshalledValue = marshaller.marshall( value );
				attributeValues.add( new AttributeValue( marshalledValue ) );
			}
			else {
				// TODO 一旦Stringのみ対応
				log.debug( "toAttributeValue = " + value );
				attributeValues.add( new AttributeValue( value.toString() ) );
			}
		}
		return attributeValues;
	}

	private Condition toDynamoDBCondition( Condition condition ) {

		return new Condition()
				.withComparisonOperator( toComparisonOperator( condition.getComparison() ) )
				.withAttributeValueList( toAttributeValue( condition.getValues() ) );
	}

	private M makeHashKeyObject( String propertyName, Object value ) {

		log.debug( "makeHashKey propertyName = " + propertyName + " value = " + value );

		M hashKeyObject = Reflector.newInstance( modelClass );
		modelRef.setPropertyValue( hashKeyObject, propertyName, value );
		return hashKeyObject;
	}

	private String toAttributeName( String properyName ) {
		return modelRef.getAttributeRef( properyName ).getAttributeName();
	}

	private Map<String, Condition> makeRangeKeyCondition( String rangeKeyName, Condition rangeKeyCondition ) {

		if ( rangeKeyName == null ) return null;

		Map<String, Condition> rangeKeyConditions = new HashMap<>();
		rangeKeyConditions.put( toAttributeName( rangeKeyName ), toDynamoDBCondition( rangeKeyCondition ) );
		return rangeKeyConditions;
	}

	private DynamoDBQueryExpression<M> makeQueryExpression( M hashKeyObject , Map<String, Condition> rangeKeyConditions, String indexName ) {

		log.debug( hashKeyObject + " indexName = " + indexName );

		DynamoDBQueryExpression<M> dynamoQuery = new DynamoDBQueryExpression<M>().withHashKeyValues( hashKeyObject );
		if ( rangeKeyConditions != null ) {
			dynamoQuery.setRangeKeyConditions( rangeKeyConditions );
		}
		if ( indexName != null ) {
			dynamoQuery.setIndexName( indexName );
		}
		return dynamoQuery;
	}

	private void setAnnotatedPropertyToHashKey( Class<? extends Annotation> annotationClass ) {

		String propName = getAnnotatedPropertyName( annotationClass );
		log.trace( "SetProperty " + propName + " to HashKey" );
		propertyManager.setPropertyToHashKey( propName );
	}

	private void setAnnotatedPropertyToRangeKey( Class<? extends Annotation> annotationClass ) {

		String propName = getAnnotatedPropertyName( annotationClass );
		log.trace( "SetProperty " + propName + " to RangeKey" );
		propertyManager.setPropertyToRangeKey( propName );
	}


	private void selectUsingIndexAndMarkingProperty() {

		// RangeKey Index
		if ( containsKeyAttribute( HashKey.class, RangeKey.class ) ) {

			setAnnotatedPropertyToHashKey( HashKey.class );
			setAnnotatedPropertyToRangeKey( RangeKey.class );
			log.debug( "RangeKeyIndex used." );
		}
		// Local Secondary Index
		else if ( containsKeyAttribute( HashKey.class, IndexRangeKey.class ) ) {

			setAnnotatedPropertyToHashKey( HashKey.class );
			setAnnotatedPropertyToRangeKey( IndexRangeKey.class );
			log.debug( "LocalSecondaryIndex " + propertyManager.getIndexName() + " used." );
		}
		// Global Secondary Index ( with Range )
		else if ( containsKeyAttribute( IndexHashKey.class, IndexRangeKey.class ) ) {

			setAnnotatedPropertyToHashKey( IndexHashKey.class );
			setAnnotatedPropertyToRangeKey( IndexRangeKey.class );
			log.debug( "GlobalSecondaryIndex " + propertyManager.getIndexName() + " used." );
		}
		// HashKey Only
		else if ( containsKeyAttribute( HashKey.class ) ) {

			setAnnotatedPropertyToHashKey( HashKey.class );
			log.debug( "HashKeyIndex used." );
		}
		// Global Secondary Index ( HashKey Only )
		else if ( containsKeyAttribute( IndexHashKey.class ) ) {

			setAnnotatedPropertyToHashKey( IndexHashKey.class );
			log.debug( "GlobalSecondaryIndex HashKey Only Index used." );
		}
	}

	private Map<String, Condition> makeQueryFilter() {

		Map<String, Condition> queryFilter = new HashMap<String, Condition>();
		List<String> propertyNames = propertyManager.getCurrentInMemoryFilterProperties();
		for( String name : propertyNames ) {
			queryFilter.put( toAttributeName( name ), toDynamoDBCondition( query.getCondition( name ) ) );
			propertyManager.getQueryProperty( name ).setQueryType( QueryType.QUERY_FILTER );
		};
		return queryFilter;
	}

	public QueryRequest extract() {

		return new QueryRequest()
			.withTableName("") // use TableNameResolver
			.withIndexName("indexName")
			.withKeyConditions(null)
			.withQueryFilter(null)
			.withLimit(null);



		String hashKeyName = propertyManager.getHashKeyName();
		String rangeKeyName = propertyManager.getRangeKeyName();
		String indexName = propertyManager.getIndexName();

		log.debug( "HashKeyName = " + hashKeyName + " RangeKeyName = " + rangeKeyName );

		Condition hashKeyCondition = query.getCondition( hashKeyName );
		Condition rangeKeyCondition = query.getCondition( rangeKeyName );

		if ( hashKeyCondition.hasMultiValue() ) {
			for ( Object value : hashKeyCondition.getValues() ) {
				dynamoQueries.add(
						makeQueryExpression(
								makeHashKeyObject( hashKeyName, value ) ,
								makeRangeKeyCondition( rangeKeyName, rangeKeyCondition ),
								indexName
						)
				);
			}
		}
		else {
			dynamoQueries.add(
					makeQueryExpression(
							makeHashKeyObject( hashKeyName, hashKeyCondition.getValue() ),
							makeRangeKeyCondition( rangeKeyName, rangeKeyCondition ),
							indexName
					)
			);
		}

		if ( propertyManager.usingIndexHasAllAttributes() ) {
			for( DynamoDBQueryExpression<M> dynamoQuery: dynamoQueries ) {
				Map<String, Condition> queryFilter = makeQueryFilter();
				if ( ! queryFilter.isEmpty() ) {
					log.debug( "add queryFilter" );
					dynamoQuery.withQueryFilter( queryFilter );
				}
			};
		}
		log.debug( "DynamoQueries Size = " + dynamoQueries.size() );
		return dynamoQueries;
	}

	public boolean usingIndexHasAllAttribute() {
		return propertyManager.usingIndexHasAllAttributes();
	}

	public Query<M> getRemainsQuery() {
		Query remainsQuery = new Query();
		for( String name : propertyManager.getCurrentInMemoryFilterProperties() ) {
			remainsQuery.addCondition( name, query.getCondition( name ) );
		}
		return remainsQuery;
	}

	private static class QueryPropertyManager<M> {

		private Class<M> modelClass;
		private ModelRef<M> modelRef;
		private Map<String, QueryProperty> properties = new HashMap<String, QueryProperty>();

		public QueryPropertyManager( Class<M> modelClass, Query<M> query ) {

			this.modelClass = modelClass;
			this.modelRef = Models.getModelRef( modelClass );

			for( String name : query.getConditions().keySet() ) {
				properties.put( name, new QueryProperty( QueryType.INMEMORY_FILTER ) );
			};
		}

		private String getHashKeyName() {
			for( String name : properties.keySet() ) {
				if ( QueryType.INDEX_HASH.equals( properties.get( name ).getQueryType() ) ) {
					return name;
				}
			}
			return null;
		}

		private String getRangeKeyName() {
			for( String name : properties.keySet() ) {
				if ( QueryType.INDEX_RANGE.equals( properties.get( name ).getQueryType() ) ) {
					return name;
				}
			}
			return null;
		}

		public QueryProperty getQueryProperty( String name ) {
			return properties.get( name );
		}

		public void setPropertyToHashKey( String propertyName ) {
			properties.get( propertyName ).setQueryType( QueryType.INDEX_HASH );
		}

		public void setPropertyToRangeKey( String propertyName ) {
			properties.get( propertyName ).setQueryType( QueryType.INDEX_RANGE );
		}

		public String getIndexName() {
			return modelRef.getIndexName( getHashKeyName(), getRangeKeyName() );
		}

		public boolean usingIndexHasAllAttributes() {
			return modelRef.getIndex( getHashKeyName(), getRangeKeyName() ).hasAllAttributes();
		}

		public List<String> getCurrentInMemoryFilterProperties() {
			List<String> inmemories = new ArrayList<String>();
			for( String name : properties.keySet() ) {
				if ( QueryType.INMEMORY_FILTER.equals( properties.get( name ).getQueryType() ) ) {
					inmemories.add( name );
				}
			};
			return inmemories;
		}
	}

	private static class QueryProperty {

		private QueryType queryType;

		public QueryProperty( QueryType queryType ) {
			this.queryType = queryType;
		}

		public QueryType getQueryType() {
			return queryType;
		}
		public void setQueryType( QueryType queryType ) {
			this.queryType = queryType;
		}
	}

	private enum QueryType {
		INDEX_HASH,
		INDEX_RANGE,
		QUERY_FILTER,
		INMEMORY_FILTER
	}

	public QueryRequest extractQueryRequest() {
		throw new NotImplementedException();
	}

	public Query<M> extractInMemoryQuery() {
		throw new NotImplementedException();
	}
	*/
}

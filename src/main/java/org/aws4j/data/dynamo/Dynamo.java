package org.aws4j.data.dynamo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aws4j.core.exception.NotImplementedException;
import org.aws4j.core.util.Collections;
import org.aws4j.data.core.Cache;
import org.aws4j.data.core.Key;
import org.aws4j.data.core.Query;
import org.aws4j.data.core.exception.ModelNotFoundException;
import org.aws4j.data.core.util.InMemoryQuery;
import org.aws4j.data.core.util.KeyUtil;
import org.aws4j.data.dynamo.attribute.MetaModel;
import org.aws4j.data.dynamo.config.Configuration;
import org.aws4j.data.dynamo.query.PutQuery;
import org.aws4j.data.dynamo.query.DynamoUpdateQuery;
import org.aws4j.data.dynamo.service.KeyModelMatchingCollection;
import org.aws4j.data.dynamo.service.dynamo.DynamoMapper;
import org.aws4j.data.dynamo.service.mock.NoCacheDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dynamo {

	private static final Logger Log = LoggerFactory.getLogger( Dynamo.class );

	protected DynamoMapper dynamo;

	protected Cache cache;

	protected InMemoryQuery filterService = new InMemoryQuery();

	// TODO
	public Dynamo() {
		Log.debug("DynamoDB created.");
	}

	public Dynamo(Configuration conf) {
		this(conf, new NoCacheDelegate());
	}

	public Dynamo(Configuration conf, Cache cache) {
		this.dynamo = new DynamoMapper(conf);
		this.cache = cache; // TODO
	}

	public <M> M getOrNull(Class<M> modelClass, Key key) {

		M model = cache.get(modelClass, key);
		if (model != null) {
			Log.debug("get from redis");
			return model;
		}

		Log.debug("start get from dynamo" + key.getKeyString() );
		model = dynamo.get(modelClass, key);
		Log.debug("end get from dynamo");

		cache.put(model);
		return model;
	}

	public <M> M get(Class<M> modelClass, Key key) {
		M model = getOrNull(modelClass, key);
		if (model == null) {
			throw new ModelNotFoundException();
		}
		return model;
	}

	public <M, K extends Key> List<M> get(Class<M> modelClass, List<K> keys) {

		List<M> models = cache.get(modelClass, keys);
		KeyModelMatchingCollection<K, M> keyModels = new KeyModelMatchingCollection<K, M>(	modelClass, keys, models);

		if ( ! keyModels.isAllMatched() ) {
			Log.debug("get from dynamo");
			List<M> unmatchedModels = dynamo.get( modelClass, keyModels.getUnmatchedKeys() );
			cache.put( unmatchedModels );
			keyModels.addModels(unmatchedModels);
		}
		return keyModels.getModels();
	}

	@SuppressWarnings("unchecked")
	public <M, K extends Key> Map<K, M> getAsMap(Class<M> modelClass,	List<K> keys) {
		List<M> models = get( modelClass, keys );
		Map<K, M> results = Collections.toMap( keys, modelClass ); // TODO
		for( M model : models ) {
			results.put( (K) KeyUtil.getKey( model ), model );
		};
		return results;
	}

	/*
	 * private <M> List<M> getFromDynamo( Class<M> modelClass, List<Key> keys )
	 * {
	 *
	 * Map<String, List<Object>> resultMap = dynamo.batchGetItem( keys );
	 * results.forEach( ( k, v ) -> { v.forEach( o -> { log.debug( k + " : " + o
	 * ); } ); } ); return new ArrayList( resultMap.get(
	 * DynamoReflector.getTableName( modelClass ) ) ); }
	 */

	private ExecutorService execService = Executors.newCachedThreadPool();

	public <M> Query<M> query( MetaModel<M> metaModel ) {
		/*
		 * List<M> results = new ArrayList<>(); List<Future<List<M>>> queryTasks
		 * = new ArrayList<>();
		 *
		 * DynamoQueryExpressionExtractor<M> queryExtractor = new
		 * DynamoQueryExpressionExtractor<M>( modelClass, query );
		 *
		 * List<DynamoDBQueryExpression<M>> dynamoQueries =
		 * queryExtractor.extract();
		 *
		 * // async request for ( final DynamoDBQueryExpression<M> dynamoQuery :
		 * dynamoQueries ) {
		 *
		 * Future<List<M>> queryTask = execService.submit( new
		 * Callable<List<M>>() {
		 *
		 * @Override public List<M> call() throws Exception { try { return
		 * dynamo.query( modelClass, dynamoQuery ); } catch( Exception e ) {
		 * e.printStackTrace(); return null; } } }); queryTasks.add( queryTask
		 * ); }
		 *
		 * // collect result for ( Future<List<M>> queryTask : queryTasks ) {
		 * results.addAll( FutureUtil.getResult( queryTask ) ); } log.trace(
		 * "query results = " + results.size() );
		 *
		 * // reget if ( ! queryExtractor.usingIndexHasAllAttribute() ) {
		 * log.debug( "reget since using keys only index" ); results = get(
		 * modelClass, KeyUtil.getKeys( (Collection<Model>) results ) ); }
		 *
		 * results = filterService.filter( results,
		 * queryExtractor.getRemainsQuery() ); log.trace( "filter results = " +
		 * results.size() ); fetchService.fetch( modelClass, results, fetchNames
		 * ); return results;
		 */
		throw new NotImplementedException();
	}

	/*
	 * private <M> boolean needGenerateKey( M model ) {
	 *
	 * @SuppressWarnings("unchecked") ModelRef<M> modelRef =
	 * DynamoReflector.getModelRef( (Class<M>) model.getClass() ); AttributeRef
	 * attrRef = modelRef.getAnnotatedAttributeRef(
	 * DynamoDBAutoGenerateKey.class ); if ( attrRef != null ) { Object value =
	 * attrRef.getPropertyValue( model ); return ( value == null ) ? true :
	 * false; } return false; }
	 */

	public <M> PutQuery<M> put( MetaModel<M> meta ) {
		return new PutQuery<M>( dynamo, meta );
	}

	@Deprecated
	public <M> void put( M model ) {
		dynamo.put(model);
		cache.put(model);
	}

	@Deprecated
	public <M> void update( M model ) {
		M newModel = dynamo.update( model );
		Log.debug( newModel.toString() );
		cache.put( newModel );
	}

	public <M> DynamoUpdateQuery<M> update( MetaModel<M> meta ) {
		return new DynamoUpdateQuery<M>( dynamo, meta );
	}

	public <M> void create( M model ) {
		// TODO NotExists and AutoGenerateProperty is null check
		put( model );
	}

	public <M> void delete( M model ) {
		dynamo.delete( model );
	}

	/*
	 * private void setAutoGenerateKeyValue( Object model ) {
	 *
	 * String key = DynamoReflector.getAutoGenerateKeyString( model ); Integer
	 * nextNumber = counter.getNextNumber( key ); log.trace(
	 * "setAutoGenerateKeyValue = " + nextNumber );
	 * DynamoReflector.setAutoGeneratePropertyValue( model, nextNumber ); }
	 */

	/*
	 * private <M> Map<Class<?>, List<KeyPair>> makeKeyPairsMap( List<DynamoKey>
	 * keys ) {
	 *
	 * Map<Class<?>, List<KeyPair>> keyPairs = new HashMap<>();
	 *
	 * for ( DynamoKey key : keys ) { if ( ! keyPairs.containsKey(
	 * key.getModelClass() ) ) { keyPairs.put(key.getModelClass(), new
	 * ArrayList<KeyPair>() ); } keyPairs.get( key.getModelClass() ).add(
	 * key.getKeyPair() ); }; return keyPairs; }
	 */

	public <M> void createTable(Class<M> modelClass, long readUnits,
			long writeUnits) {
		dynamo.createTable(modelClass, readUnits, writeUnits);
	}

	public <M> void deleteTable(Class<M> modelClass) {
		dynamo.deleteTable(modelClass);
	}

}

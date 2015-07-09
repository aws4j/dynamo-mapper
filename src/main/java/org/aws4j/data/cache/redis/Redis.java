package org.aws4j.data.cache.redis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aws4j.core.util.ClassUtil;
import org.aws4j.core.util.JsonUtil;
import org.aws4j.core.util.Lists;
import org.aws4j.data.core.Cache;
import org.aws4j.data.core.Key;
import org.aws4j.data.core.util.CacheUtil;
import org.aws4j.data.core.util.KeyUtil;
import org.aws4j.data.dynamo.model.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Redis implements Cache {

	private static final Logger Log = LoggerFactory.getLogger(Redis.class);

	private RedisAccessor redis;
	private RedisConfiguration conf;

	// TODO Attributeマッピングされていない不要なプロパティを除外する
	private String makeJson( Object model ) {
		return JsonUtil.toJson( model );
	}

	@SuppressWarnings("unused")
	private <M> M fromJson( Class<M> modelClass, String json ) {
		return JsonUtil.fromJson( modelClass, json );
	}

	public Redis( RedisConfiguration conf ) {
		this.conf = conf;
		redis = new RedisAccessor( conf );
	}

	private <M> boolean notCache(Class<M> modelClass) {
		return ( CacheUtil.getCacheable( modelClass ) == null || conf.isMock()) ? true
				: false;
	}

	private <M> Integer getTimeout(Class<M> modelClass) {
		Cacheable cacheable = CacheUtil.getCacheable( modelClass );
		return ( cacheable != null ) ? cacheable.expire() : null;
	}

	private String[] makeKeysValues(List<?> models) {

		List<String> keysValues = new ArrayList<String>();
		for ( Object model : models ) {
			keysValues.add( KeyUtil.getKey( model ).getKeyString() );
			keysValues.add( makeJson( model ) );
		}
		return keysValues.toArray( new String[0] );
	}

	private String[] makeRedisKeyStrings( List<? extends Key> keys ) {
		List<String> keyStrings = new ArrayList<String>();
		for( Key key : keys ) {
			Log.debug( "keyString = " + key.getKeyString() );
			keyStrings.add( key.getKeyString() );
		}
		return keyStrings.toArray(new String[0]);
	}

	@Override
	public <M> M get( Class<M> modelClass, Key key ) {

		if( notCache( modelClass ) ) return null;

		List<M> models = get( modelClass, Lists.newArrayList( key ) );
		// TODO Util化？
		if ( models.size() == 1 ) {
			return models.get(0);
		} else if (models.size() == 0) {
			return null;
		}
		throw new IllegalStateException( "Unexpected return model count  = " + models.size() );
	}

	@Override
	public <M> List<M> get(Class<M> modelClass, List<? extends Key> keys) {

		List<M> models = new ArrayList<M>();

		if (notCache(modelClass))
			return models;

		// TODO Keyが存在しない場合の戻りjsonコレクションの状態を確認しコード修正
		List<String> jsons = redis.get(makeRedisKeyStrings(keys));
		Log.debug("jsons.size = " + jsons.size());
		for (String json : jsons) {
			if (json != null) {
				models.add(JsonUtil.fromJson(modelClass, json));
			}
		}
		return models;
	}

	@Override
	public <M> Map<Key, M> getAsMap( Class<M> modelClass, List<? extends Key> keys ) {

		List<M> models = get( modelClass, keys );
		Map<Key, M> keyModels = new HashMap<Key, M>();
		for( Key key : keys ) {
			keyModels.put( key, null );
		}
		for( M model : models ) {
			keyModels.put( KeyUtil.getKey( model ), model );
		}
		return keyModels;
	}

	@Override
	public <M> void put( M model ) {

		if ( model == null ) return;

		Class<M> modelClass = ClassUtil.getClass(model);

		if ( notCache( modelClass ) ) return;

		String key = KeyUtil.getKey( model ).getKeyString();
		Integer timeout = getTimeout( modelClass );
		String json = makeJson( model );
		Log.debug("key = [" + key + "] json = [" + json + "]");
		redis.set( key, json, timeout );
	}

	@Override
	public <M> void put( Class<M> modelClass, List<M> models ) {
		redis.msetnx( makeKeysValues( models ) );
	}

}

package org.aws4j.data.dynamo.service;


import java.util.*;

import org.aws4j.data.core.Key;
import org.aws4j.data.core.util.KeyUtil;
import org.aws4j.data.core.util.Models;
import org.aws4j.data.dynamo.attribute.MetaModel;
import org.aws4j.data.dynamo.attribute.MetaModels;

public class KeyModelMatchingCollection<K extends Key, M> {

	// TODO
	private Map<String, K> keyStringKeyMap = new HashMap<String, K>();
	private Map<String, M> keyStringModelMap = new LinkedHashMap<String, M>();
	private Class<M> modelClass;

	public KeyModelMatchingCollection( Class<M> modelClass, Collection<K> keys, Collection<M> models ) {

		this.modelClass = modelClass;
		for( K key : keys ) {
			this.keyStringModelMap.put( key.getKeyString(), null );
			this.keyStringKeyMap.put( key.getKeyString(), key );
		}
		matchModels( models );
	}

	private void matchModels( Collection<M> models ) {

		for( M model : models ) {
			Key key = KeyUtil.getKey( model );
			if ( keyStringModelMap.containsKey( key.getKeyString() ) ) {
				keyStringModelMap.put( key.getKeyString(), model );
			}
			else {
				throw new IllegalStateException( "Model's key not exists in key list." );
			}
		}
	}

	public List<K> getUnmatchedKeys() {

		List<K> unmatchedKeys = new ArrayList<K>();
		for ( String keyString : keyStringModelMap.keySet() ) {
			if ( keyStringModelMap.get( keyString ) == null ) {
				unmatchedKeys.add( keyStringKeyMap.get( keyString ) );
			}
		}
		return unmatchedKeys;
	}

	public boolean isAllMatched() {

		for( M model : keyStringModelMap.values() ) {
			if ( model == null ) {
				return false;
			}
		}
		return true;
	}

	public void addModels( Collection<M> models ) {
		matchModels( models );
	}

	public List<M> getModels() {
		return new ArrayList<M>( keyStringModelMap.values() );
	}

}

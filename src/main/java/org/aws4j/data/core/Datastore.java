package org.aws4j.data.core;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Datastore {

	public <M, K extends Key> M get( Class<M> modelClass, K key );

	public <M, K extends Key> List<M> get( Class<M> modelClass, Collection<K> keys );

	public <M, K extends Key> Map<K, M> getAsMap( Class<M> modelClass, Collection<K> keys );

//	public <M> List<M> query( Class<M> modelClass, Query<M> query );

	public <M> M create( M model );

	public <M> M update( M model );

	public <M> void put( M model );

}

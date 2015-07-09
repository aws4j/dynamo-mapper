package org.aws4j.data.core;

import java.util.List;
import java.util.Map;

import org.aws4j.data.core.Key;

public interface Cache {

	public <M> M get( Class<M> modelClass, Key key );

	public <M> List<M> get( Class<M> modelClass, List<? extends Key> keys );

	public <M> Map<Key, M> getAsMap( Class<M> modelClass, List<? extends Key> keys );

	// TODO 暫定IF
	public <M> void put( M model );

	public <M> void put( Class<M> modelClass, List<M> models );
}

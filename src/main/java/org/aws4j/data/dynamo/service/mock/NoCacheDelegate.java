package org.aws4j.data.dynamo.service.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aws4j.data.core.Cache;
import org.aws4j.data.core.Key;

public class NoCacheDelegate implements Cache {

	@Override
	public <M> M get(Class<M> modelClass, Key key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <M> List<M> get(Class<M> modelClass, List<? extends Key> keys) {
		return new ArrayList<M>();
	}

	@Override
	public <M> Map<Key, M> getAsMap(Class<M> modelClass, List<? extends Key> keys) {
		return new HashMap<Key, M>();
	}

	@Override
	public void put(Object model) {
		// TODO Auto-generated method stub

	}

	@Override
	public <M> void put( Class<M> modelClass, List<M> models) {
		// TODO Auto-generated method stub
	}
}

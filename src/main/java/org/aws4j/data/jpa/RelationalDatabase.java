package org.aws4j.data.jpa;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.aws4j.data.core.Datastore;
import org.aws4j.data.core.Key;

public class RelationalDatabase implements Datastore {

	protected EntityManager em;

	public RelationalDatabase( EntityManager em ) {
		this.em = em;
	}

	@Override
	public <M, K extends Key> M get( Class<M> modelClass, K key ) {
		return null;
	}

	@Override
	public <M, K extends Key> List<M> get( Class<M> modelClass, Collection<K> keys ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <M, K extends Key> Map<K, M> getAsMap( Class<M> modelClass, Collection<K> keys ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <M> M create( M model ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <M> M update( M model ) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <M> void put( M model ) {
		// TODO Auto-generated method stub

	}

}

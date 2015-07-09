package org.aws4j.data.core;

import java.util.ArrayList;
import java.util.List;

public class SingleSortCriteria<M, V> implements SortCriteria<M> {

	protected Class<M> modelClass;
	protected Class<V> valueClass;
	protected String name;
	protected SortOrder sortOrder;
	protected List<V> values = new ArrayList<V>();

	public SingleSortCriteria( Class<M> modelClass, Class<V> valueClass, SortOrder sortOrder, String name ) {
		this.modelClass = modelClass;
		this.valueClass = valueClass;
		this.sortOrder = sortOrder;
		this.name = name;
	}
}

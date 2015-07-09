package org.aws4j.data.core;

import java.util.List;

public interface Query<M> {

	Query<M> filter( FilterCriteria<M> condition );

	Query<M> sortBy( SortCriteria<M> sortCriteria );

	FilterCriteria<M> getCondition();

	SortCriteria<M> getSortCriteria();

	List<M> getAsList();
}

package org.aws4j.data.core;

public interface FilterCriteria<M> {

	FilterCriteria<M> and( FilterCriteria<M> filter );
}
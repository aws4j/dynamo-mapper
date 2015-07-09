package org.aws4j.data.core;

import java.util.List;

public interface PagingList<E> extends List<E> {

	PagingList<E> getNextPage();
}

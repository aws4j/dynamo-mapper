package org.aws4j.data.dynamo.util;

import org.aws4j.core.util.Objects;

import com.amazonaws.services.dynamodbv2.model.ProjectionType;

public class IndexSchema {

	private String indexName;
	private String hashKeyName;
	private String rangeKeyName;
	private ProjectionType projectionType;

	public IndexSchema( String indexName ) {

		this.indexName = IndexManager.KEY_INDEX.equals( indexName ) ? null : indexName;
	}

	public String getIndexName() {
		return indexName;
	}
	public void setIndexName( String indexName ) {
		this.indexName = indexName;
	}

	public String getHashKeyName() {
		return hashKeyName;
	}
	public void setHashKeyName( String hashKeyName ) {
		this.hashKeyName = hashKeyName;
	}

	public String getRangeKeyName() {
		return rangeKeyName;
	}
	public void setRangeKeyName( String rangeKeyName ) {
		this.rangeKeyName = rangeKeyName;
	}

	public ProjectionType getProjectionType() {
		return projectionType;
	}
	public void setProjectionType( ProjectionType projectionType ) {
		this.projectionType = projectionType;
	}

	public boolean hasAllAttributes() {
		return ProjectionType.ALL.equals( projectionType );
	}

	public int hashCode() {
		return Objects.hashCode( hashKeyName, rangeKeyName );
	}

	public boolean equals( Object o ) {

		if (this == o) return true;
		if ( o == null || getClass() != o.getClass() ) return false;
		IndexSchema that = (IndexSchema)o;
		return
				Objects.equal( this.hashKeyName, that.hashKeyName ) &&
				Objects.equal( this.rangeKeyName, that.rangeKeyName );
	}

}

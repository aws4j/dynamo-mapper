package org.aws4j.data.dynamo.util;

import java.util.Arrays;
import java.util.List;

import org.aws4j.core.util.ListUtil;
import org.aws4j.core.util.StringUtil;

public class PropertyPath {

	private static String DELIMITER = ".";

	private List<String> properties;

	public PropertyPath( String propertyPath ) {

		if ( StringUtil.isEmpty( propertyPath ) ) throw new IllegalArgumentException( "PropertyPath must be not null." );

		properties = Arrays.asList( StringUtil.split( propertyPath, DELIMITER ) );
	}

	public String getFetchName() {

		if ( hasSecondProperty() ) {
			return StringUtil.join( DELIMITER, ListUtil.getSubList( properties, 0, properties.size() - 2 ) );
		}
		else {
			return null;
		}
	}

	public String getFirstProperty() {

		return properties.get( 0 );
	}

	public String getSecondProperty() {

		if ( hasSecondProperty() ) {
			return StringUtil.join( DELIMITER ,ListUtil.getSubList( properties, 1, properties.size() - 1 ) );
		}
		else {
			return null;
		}
	}

	public boolean hasFetchName() {
		return hasSecondProperty();
	}

	public boolean hasSecondProperty() {
		return ( properties.size() > 1 ) ? true : false;
	}

}

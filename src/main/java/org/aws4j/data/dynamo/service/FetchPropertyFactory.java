package org.aws4j.data.dynamo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.aws4j.core.util.Iterables;
import org.aws4j.core.util.StringUtil;

public class FetchPropertyFactory {

    private void parse( FetchProperty parentProperty, String propertyPath ) {

        List<String> propertyNames = new ArrayList<String>( Arrays.asList( propertyPath.split("\\.", -1) ) );
        String propertyName = propertyNames.get( 0 );
        propertyNames.remove( 0 );
        FetchProperty property = new FetchProperty( parentProperty.getPropertyModelClass(), propertyName );
        if ( ! Iterables.isEmpty( propertyNames ) ) {
            parse( property, StringUtil.join( ".", propertyNames ) );
        }
        parentProperty.addChild( property );
    }

    public List<FetchProperty> parse( Class<?> modelClass, List<String> propertyPathList ) {

        FetchProperty root = new FetchProperty( modelClass ) ;
        for ( String propertyPath : propertyPathList ) {
            parse( root, propertyPath );
        }
        return root.getChildren();
    }
}
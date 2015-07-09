package org.aws4j.data.dynamo.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.aws4j.core.util.Reflector;
import org.aws4j.data.core.Datastore;
import org.aws4j.data.core.Query;
import org.aws4j.data.core.exception.ModelNotFoundException;
import org.aws4j.data.dynamo.Dynamo;
import org.aws4j.data.dynamo.model.DynamoKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelFetchService {

	private static Logger log = LoggerFactory.getLogger( ModelFetchService.class );

    private FetchPropertyFactory factory = new FetchPropertyFactory();

	private Dynamo dynamodb;

	// TODO temp
	public ModelFetchService( Dynamo dynamodb ) {
		this.dynamodb = dynamodb;
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Iterable keyFetch( Iterable models, FetchProperty property ) {

        // get Keys
        List<DynamoKey> fetchKeys = new ArrayList<DynamoKey>();
        Method keyGetter = property.getKeyGetterMethod();
        Method setter = property.getSetterMethod();
        for ( Object model : models) {
            if ( property.isCollection() ) {
                Collection<DynamoKey> keys = (Collection<DynamoKey>) Reflector.invoke( model, keyGetter );
                for ( DynamoKey key : keys ) {
					fetchKeys.add( key );
                }
            } else {
                DynamoKey key = (DynamoKey)Reflector.invoke( model, keyGetter );
                if( key != null ) {
					fetchKeys.add( key );
                }
            }
        }

//		log.debug( "fetchKeys size " + fetchKeys.size() );

        // batch get!
        Map<DynamoKey, ?> fetchModels = dynamodb.getAsMap( property.getPropertyModelClass(), fetchKeys );

        // fetch Property
        for ( Object model : models ) {
            if (property.isCollection()) {
				Collection<DynamoKey> keys = (Collection<DynamoKey>) Reflector.invoke( model, keyGetter );
                Collection collection = (Collection) Reflector.newInstance( property.getCollectionInstanceClass() );
                for ( DynamoKey key : keys ) {
                    collection.add( fetchModels.get( key ) );
                }
                Reflector.invoke( model, setter, collection );
            } else {
				DynamoKey key = (DynamoKey) Reflector.invoke( model, keyGetter );
                if ( key != null ) {
                    if ( ! fetchModels.containsKey( key ) ) {
                        throw new ModelNotFoundException( "key fetch target model is not found. key = " + model.toString() );
                    }
                    Reflector.invoke( model, setter, fetchModels.get( key ) );
                }
            }
        }
        return fetchModels.values();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Iterable queryFetch( Iterable models, FetchProperty property ) {

        Method keyGetter = property.getKeyGetterMethod();
        Method setter = property.getSetterMethod();
        List allFetchModels = new ArrayList();
        for ( Object model : models) {

            DynamoKey key = (DynamoKey)Reflector.invoke( model, keyGetter );
            List fetchModels = new ArrayList();//dynamodb.query( property.getPropertyModelClass(), new Query( property.getFetch().on(), key.getKeyString() ) );
            Reflector.invoke( model, setter, fetchModels );
            allFetchModels.addAll( fetchModels );
        }
        return allFetchModels;
    }

    /**
     * フェッチタイプ判定→子プロパティに再帰するレイヤ
     */
    @SuppressWarnings("rawtypes")
    private Iterable fetch( Iterable models, FetchProperty property ) {

        Iterable fetchModels = null;

        if( Fetch.FetchType.KEY.equals( property.getFetchType() ) ) {
            fetchModels = keyFetch( models, property );
        }
        else if ( Fetch.FetchType.QUERY.equals( property.getFetchType() ) ) {
            fetchModels = queryFetch( models, property );
        }
        else {
            throw new IllegalStateException("Not Implementation FetchType [" + property.getFetchType() +"]");
        }

        // children layer fetch
        fetch( property.getPropertyModelClass(), fetchModels, property.getChildren() );
        return models;
    }

    @SuppressWarnings("rawtypes")
    private Iterable fetch( Class modelClass, Iterable models, List<FetchProperty> properties ) {

        for ( FetchProperty property : properties ) {
            fetch( models, property );
        }
        return models;
    }

    @SuppressWarnings("unchecked")
    public <M> Iterable<M> fetch( Class<M> modelClass, Iterable<M> models, String... fetchNames ) {

        List<FetchProperty> properties = factory.parse( modelClass, Arrays.asList( fetchNames ) );
        return fetch( modelClass, models, properties );
    }
}

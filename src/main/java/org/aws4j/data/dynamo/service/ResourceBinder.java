package org.aws4j.data.dynamo.service;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMarshaller;

import org.aws4j.data.core.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ResourceBinder {
/*
	private static Logger log = LoggerFactory.getLogger( ResourceBinder.class );

	private DatastoreService datastore;

	private Query convertModelQuery( Class<? extends ResourceInfo> resourceClass, Query resourceQuery ) {

		ResourceRef resourceRef = ResourceReflectUtil.getResourceRef( resourceClass );

		Query modelQuery = new Query();
		resourceQuery.getConditions().forEach( ( propName, condition ) -> {
			log.debug( "propName = " + propName );
			modelQuery.addCondition( resourceRef.getModelPropertyNameByResoucePropertyName( propName ), condition );
		});
		return modelQuery;
	};

	private Object getMarshalledValue( Object value ) {

		if ( value == null ) return null;

		log.trace( "setPropertyWithMarshallingIfNeeded valueClass is " + value.getClass() );

		// TODO 抽象化 & 型チェック
		DynamoDBMarshaller marshaller = MarshallerManager.getMarshaller( value.getClass() );
		if ( marshaller == null ) {
			return value;
		}
		else {
			return marshaller.marshall( value );
		}
	}

	private <T extends ResourceInfo> List<T> bind( Class<T> resourceClass, List<? extends Model> models ) {

		List<T> resources = new ArrayList<>();
		models.forEach( model -> {
			resources.add( bind( resourceClass, model ) );
		});
		return resources;
	}

	private <T extends ResourceInfo> T bind( Class<T> resourceClass, Model model ) {

		ResourceRef resourceRef = ResourceReflectUtil.getResourceRef( resourceClass );
		ModelRef modelRef = DynamoReflectUtil.getModelRef( resourceRef.getModelClass() );

		T resource = ReflectUtil.newInstance( resourceClass );
		resourceRef.getPropertyRefs().forEach( propRef -> {

			if ( propRef.isKeyProperty() ) {
				ReflectUtil.invoke( resource, propRef.getSetter(), model.getKey().getKeyString() );
			}
			else {

				log.trace( "bind " + model.getClass().getName() + " " + propRef.getModelPropertyName() );

				Object value = modelRef.getPropertyValue( model, propRef.getModelPropertyName() );
				if ( CollectionUtil.isCollection( value ) ) {
					Collection marshalledValues = CollectionUtil.newCollection( (Class<? extends Collection>)propRef.getPropertyClass() );
					Collection collection = (Collection)value;
					for ( Object obj : collection ) {
						marshalledValues.add( getMarshalledValue( obj ) );
					}
					ReflectUtil.invoke( resource, propRef.getSetter(), marshalledValues );
				}
				else {
					log.trace( model.getClass() + " : " + propRef.getModelPropertyName() + " = " + value );
					ReflectUtil.invoke( resource, propRef.getSetter(), getMarshalledValue( value ) );
				}
			}
		} );
		return resource;
	}

	public <T extends ResourceInfo> List<T> getAccessible( Class<T> resourceClass, Query resourceQuery ) {

		ResourceRef resourceRef = ResourceReflectUtil.getResourceRef( resourceClass );
		resourceRef.getAccessController().addAccessFilter( resourceQuery );

		return bind(
				resourceClass, datastore.query(
						resourceRef.getModelClass(),
						convertModelQuery( resourceClass, resourceQuery ),
						resourceRef.getFetchNames()
				)
		);
	}

	public <T extends ResourceInfo> List<T> getAccessible( Class<T> resourceClass ) {
		return getAccessible( resourceClass, new Query() );
	}

	public <T extends ResourceInfo> List<T> query( Class<T> resourceClass, Query resourceQuery ) {

		ResourceRef resourceRef = ResourceReflectUtil.getResourceRef( resourceClass );

		return bind(
				resourceClass, datastore.query(
						resourceRef.getModelClass(),
						convertModelQuery( resourceClass, resourceQuery ),
						resourceRef.getFetchNames()
				)
		);
	}

	public <T extends ResourceInfo> T get( Class<T> resourceClass, String keyString ) {

		ResourceRef resourceRef = ResourceReflectUtil.getResourceRef( resourceClass );
		T resource = bind( resourceClass, datastore.get( resourceRef.getModelClass(), Key.create( keyString ) ) );
		resourceRef.getAccessController().readCheck( Lists.newArrayList( resource ) );

		return resource;
	}

	// TODO 要否検討
	public <T extends ResourceInfo> T get( Class<T> resourceClass ) {

		return null;
	}

	public void create( ResourceInfo resource ) {

	}

	public void update( ResourceInfo resource ) {

	}

	public void delete( ResourceInfo resource ) {

	}
*/
}

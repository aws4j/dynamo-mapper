package org.aws4j.data.dynamo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class ResourceReflectUtil {

	/*
	private static Logger log = LoggerFactory.getLogger( ResourceReflectUtil.class );

	private static Map<Class<? extends ResourceInfo>, ResourceRef> resourceRefs = Maps.newConcurrentMap();

	private static PropertyRef makePropertyRef( PropertyDescriptor property, Class<? extends ResourceInfo> resourceClass ) {

		String propName = property.getName();

		Method getter = property.getReadMethod();
		Method setter = property.getWriteMethod();

		List<Annotation> annotations = ReflectUtil.getAnnotations( resourceClass, property );
		// TODO Util
		for( Annotation annotation : annotations ) {
			if ( KeyProperty.class.equals( annotation.annotationType() ) || ModelProperty.class.equals( annotation.annotationType() ) ) {
				return new PropertyRef( propName, getter, setter, annotations );
			}
		}
		return null;
	}

	private static void loadResourceRef( Class<? extends ResourceInfo> resorceClass ) {

		ModelClass modelClass = resorceClass.getAnnotation( ModelClass.class );
		ResourceName resourceName = resorceClass.getAnnotation( ResourceName.class );
		AccessControl accessControl = resorceClass.getAnnotation( AccessControl.class );

		Map<String, PropertyRef> propertyRefs = new HashMap<>();

		List<PropertyDescriptor> props = ReflectUtil.getPropertyDescriptors( resorceClass );
		props.forEach( prop -> {
			PropertyRef propRef = makePropertyRef( prop, resorceClass );
			if( propRef != null ) {
				propertyRefs.put( prop.getName(), propRef );
			}
		} );
		resourceRefs.put( resorceClass, new ResourceRef( resorceClass, modelClass.value(), resourceName, accessControl, propertyRefs ) );
	}

	public static synchronized ResourceRef getResourceRef( Class<? extends ResourceInfo> resourceClass ) {

		log.debug( "getResourceRef " + resourceClass );

		if ( ! resourceRefs.containsKey( resourceClass ) ) loadResourceRef( resourceClass );

		return 	resourceRefs.get( resourceClass );
	}

	public static Key getKey( ResourceInfo resource ) {
		return new Key( getResourceRef( resource.getClass() ).getKeyString( resource ) );
	}

	public static String getResourceSingularName( Class<? extends ResourceInfo> resourceClass ) {
		return getResourceRef( resourceClass ).getSingularName();
	}

	public static String getResourcePluralName( Class<? extends ResourceInfo> resourceClass ) {
		return getResourceRef( resourceClass ).getPluralName();
	}

	public static class ResourceRef {

		private Class<? extends ResourceInfo> resourceClass;
		private Class<? extends Model> modelClass;
		private ResourceName resourceName;
		private AccessControl accessControl;
		private Map<String, PropertyRef> properties = new HashMap<>();

		public ResourceRef(
				Class<? extends ResourceInfo> resourceClass,
				Class<? extends Model> modelClass,
				ResourceName resourceName,
				AccessControl accessControl,
				Map<String, PropertyRef> properties
		) {
			this.resourceClass = resourceClass;
			this.modelClass = modelClass;
			this.resourceName = resourceName;
			this.accessControl = accessControl;
			this.properties = properties;
		}

		public String getKeyString( ResourceInfo resource ) {

			for( PropertyRef propRef: properties.values() ) {
				KeyProperty keyField = propRef.getAnnotation( KeyProperty.class );
				if ( keyField != null ) {
					return (String)ReflectUtil.invoke( resource, propRef.getGetter() );
				}
			}
			throw new IllegalStateException("Check " + resource.getClass() + " annotation.");
		}

		public Class<? extends Model> getModelClass() {
			return modelClass;
		}

		public AccessController getAccessController() {
			// TODO temp
			return ReflectUtil.newInstance( accessControl.value() );
		}

		public List<PropertyRef> getPropertyRefs() {

			return new ArrayList<PropertyRef>( properties.values() );
		}

		public String getSingularName() {
			return resourceName.singular();
		}

		public String getPluralName() {
			return resourceName.plural();
		}

		public String getModelPropertyNameByResoucePropertyName( String propName ) {
			return properties.get( propName ).getModelPropertyName();
		}

		public String[] getFetchNames() {

			List<String> fetchNames = new ArrayList<>();
			for ( PropertyRef propRef : properties.values() ) {

				log.debug( "Propertyname = " +  propRef.getModelPropertyName() );

				if ( propRef.hasFetchName() ) {

					log.debug( "FatchName = " +  propRef.getFetchName() );

					fetchNames.add( propRef.getFetchName() );
				}
			}
			return fetchNames.toArray( new String[0] );
		}
	}

	public static class PropertyRef {

		private String propertyName;
		private PropertyPath propertyPath;
		private Method getter;
		private Method setter;
		private List<Annotation> annotations;
		private boolean keyFied;

		public PropertyRef( String propertyName, Method getter, Method setter, List<Annotation> annotations ) {
			this.propertyName = propertyName;
			this.getter = getter;
			this.setter = setter;
			this.annotations = annotations;
			this.propertyPath = new PropertyPath( getModelPropertyName() );
		}

		public <A> A getAnnotation( Class<A> annotationClass ) {

			for( Annotation annotation : annotations ) {
				if ( annotationClass.equals( annotation.annotationType() ) ) {
					return (A)annotation;
				}
			}
			return null;
		}

		public Method getGetter() {
			return getter;
		}

		public Method getSetter() {
			return setter;
		}

		public Class<?> getPropertyClass() {
			return getter.getReturnType();
		}

		public String getModelPropertyName() {
			return getAnnotation( ModelProperty.class ).value();
		}

		public String getFetchName() {
			return propertyPath.getFetchName();
		}

		public boolean hasFetchName() {
			return propertyPath.hasFetchName();
		}

		public boolean isKeyProperty() {
			return ( getAnnotation( KeyProperty.class ) != null ) ? true : false;
		}
	}
	*/

}

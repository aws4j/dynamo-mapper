package org.aws4j.data.dynamo.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aws4j.core.util.Reflector;
import org.aws4j.data.dynamo.service.Fetch.FetchType;

public class FetchProperty {

    private Class<?> declaredModelClass;
    private String name;
    private Fetch fetch;
    //private Field keyField;
    private Method keyGetter;
    private Field fetchField;
    private Method getter;
    private Method setter;
    private Class<?> propertyClass;
//    private Class<?> keyPropertyClass;
	private String queryBy;

    private Map<String, FetchProperty> children = new HashMap<String, FetchProperty>();

    public FetchProperty( Class<?> declaredModelClass ) {
        this.declaredModelClass = declaredModelClass;
    }

    public FetchProperty(Class<?> declaredModelClass, String name) {
        this.declaredModelClass = declaredModelClass;
        this.name = name;
        reflect();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getDeclaredModelClass() {
        return declaredModelClass;
    }

    public void setModelClass(Class<?> modelClass) {
        this.declaredModelClass = modelClass;
    }

    public List<FetchProperty> getChildren() {
        return new ArrayList<FetchProperty>( children.values() );
    }

    public void addChild( FetchProperty property ) {

        String name = property.getName();
        if ( children.containsKey( name ) ) {
            children.get( name ).addChildren( property.getChildren() );
        } else {
            children.put( name, property );
        }
    }

    public void addChildren( List<FetchProperty> properties ) {
        for ( FetchProperty property : properties ) {
            addChild(property);
        }
    }

    private void checkSupportedProperty() {

        // TODO PropertyClass Check
		/*
        if ( ! Collection.class.isAssignableFrom( keyPropertyClass ) ) {
            throw new IllegalStateException("Key Property [" + keyPropertyClass + "] is not support.");
        }
        */
        // QueryFetch Check
        if ( FetchType.QUERY.equals( fetch.type() ) ) {
            if ( ! isCollection() ) {
                throw new IllegalStateException("Query Fetch must be with collection property field and a single property field.");
            }
        }
    }

    private void reflect() {

        try {
            fetchField = Reflector.getField( declaredModelClass, name );
            propertyClass = fetchField.getType();
            fetch = fetchField.getAnnotation( Fetch.class );
            if (fetch == null) {
                throw new IllegalArgumentException( declaredModelClass.getName() + "'s field[" + name + "] has not Fetch Annotation.");
            }
            //keyField = ReflectUtil.getField( declaredModelClass, fetch.by() );
            getter = Reflector.getGetterMethod( declaredModelClass, name );
	//		keyPropertyClass = getter.getType();
            setter = Reflector.getSetterMethod( declaredModelClass, name );
            keyGetter = Reflector.getGetterMethod( declaredModelClass, fetch.by() );

            checkSupportedProperty();

        } catch (SecurityException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public Fetch.FetchType getFetchType() {
        return fetch.type();
    }

    public Method getKeyGetterMethod() {
        return keyGetter;
    }

    public Method getGetterMethod() {
        return getter;
    }

    public Method getSetterMethod() {
        return setter;
    }

	public Fetch getFetch() {
		return fetch;
	}

    public Class<?> getPropertyModelClass() {

        if (name == null) {
            return declaredModelClass;
        }
        if ( isCollection() ) {

            Type type = fetchField.getGenericType();
            if ( type instanceof ParameterizedType ) {
                ParameterizedType ptype = ( ParameterizedType ) type;
                Type[] types = ptype.getActualTypeArguments();
                if ( types.length == 1 ) {
                    if ( types[0] instanceof Class ) {
                        return (Class<?>) types[0];
                    }
                }
            }
            throw new IllegalStateException("Not support generic type.");
        } else {
            return (Class<?>) propertyClass;
        }
    }

    // TODO interface support ( List / Set )
    public Class<?> getCollectionInstanceClass() {

        Type type = fetchField.getGenericType();
        if ( type instanceof ParameterizedType ) {
            ParameterizedType ptype = (ParameterizedType)type;
            Class<?> collectionClass = (Class<?>) ptype.getRawType();
            if ( List.class.equals( collectionClass ) ) {
                return ArrayList.class;
            } else if ( Set.class.equals( collectionClass ) ) {
                return HashSet.class;
            } else {
                return collectionClass;
            }
        }
        throw new IllegalStateException("Not support generic type.");
    }

    public boolean isCollection() {
        return Collection.class.isAssignableFrom( propertyClass );
    }
}

package org.aws4j.data.core.exception;

public class ModelNotFoundException extends RuntimeException {

	public ModelNotFoundException() {}

    public ModelNotFoundException( String message ) {
		super( message );
    }
}

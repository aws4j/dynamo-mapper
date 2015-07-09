package org.aws4j.data.core;

import org.aws4j.data.core.util.ModelRef;
import org.aws4j.data.core.util.Models;

public abstract class AbstractKeyHandler implements KeyHandler {

	protected ModelRef<?> getModelRef( Class<?> modelClass ) {
		return Models.getModelRef( modelClass );
	}

}

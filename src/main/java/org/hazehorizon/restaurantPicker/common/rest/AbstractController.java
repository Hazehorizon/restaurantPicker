package org.hazehorizon.restaurantPicker.common.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractController {
    protected static Logger logger = LoggerFactory.getLogger(AbstractController.class);

	protected <T> ResponseWrapper<T> createResponseWrapper(T data)
	{
		return new ResponseWrapper<T>(data);
	}

	protected ResponseWrapper<Void> createErrorResponse(String errorCode, String... messages)
	{
		return new ResponseWrapper<Void>(errorCode, messages);
	}
}

package org.hazehorizon.restaurantPicker.common.service;

public class ServiceException extends RuntimeException {
	public ServiceException(String arg0) {
		super(arg0);
	}

	public ServiceException(Throwable arg0) {
		super(arg0);
	}
}

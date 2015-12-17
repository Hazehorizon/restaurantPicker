package org.hazehorizon.restaurantPicker.common.service;

public class NotFoundException extends ServiceException {
	public NotFoundException(Class<?> type, Object identifier) {
		super("Cannot find " + type.getSimpleName() + ":" + identifier);
	}
}

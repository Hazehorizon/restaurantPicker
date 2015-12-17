package org.hazehorizon.restaurantPicker.common.rest.dto;

public abstract class AbstractDto<T> {
	public AbstractDto() {
	}

	public AbstractDto(T entity) {
	}
	
	public abstract void fill(T entity);
}

package org.hazehorizon.restaurantPicker.common.rest.dto;

import org.hazehorizon.restaurantPicker.model.AbstractEntity;

public abstract class AbstractEntityDto<T extends AbstractEntity> extends AbstractDto<T> {
	private Long id;

	public AbstractEntityDto() {
	}

	public AbstractEntityDto(T entity) {
		this.id = entity.getId();
	}

	public Long getId() {
		return id;
	}
}

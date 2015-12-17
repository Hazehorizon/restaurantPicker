package org.hazehorizon.restaurantPicker.vote.dto;

import org.hazehorizon.restaurantPicker.common.rest.dto.AbstractDto;
import org.hazehorizon.restaurantPicker.model.MenuItemEntity;

public class RestaurantMenuItemDto extends AbstractDto<MenuItemEntity> {
	private String name;
	private Double price;
	
	public RestaurantMenuItemDto(MenuItemEntity entity) {
		super(entity);
		this.name = entity.getName();
		this.price = entity.getPrice();
	}

	public String getName() {
		return name;
	}

	public Double getPrice() {
		return price;
	}

	@Override
	public void fill(MenuItemEntity entity) {
	}
}

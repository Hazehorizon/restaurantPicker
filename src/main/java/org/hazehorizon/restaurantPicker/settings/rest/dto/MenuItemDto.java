package org.hazehorizon.restaurantPicker.settings.rest.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hazehorizon.restaurantPicker.common.rest.dto.AbstractDto;
import org.hazehorizon.restaurantPicker.model.MenuItemEntity;

public class MenuItemDto extends AbstractDto<MenuItemEntity> {
	@NotNull
	private String name;
	@NotNull
	@Min(0)
	private Double price;
	
	public MenuItemDto() {
	}

	public MenuItemDto(MenuItemEntity entity) {
		this.name = entity.getName();
		this.price = entity.getPrice();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public void fill(MenuItemEntity entity) {
		entity.setName(name);
		entity.setPrice(price);
	}
}

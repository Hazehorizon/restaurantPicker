package org.hazehorizon.restaurantPicker.settings.rest.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hazehorizon.restaurantPicker.common.rest.dto.AbstractEntityDto;
import org.hazehorizon.restaurantPicker.model.MenuEntity;
import org.hazehorizon.restaurantPicker.model.MenuItemEntity;

public class MenuDto extends AbstractEntityDto<MenuEntity> {
	private String name;
	@NotNull
	private LocalDate date;
	@Valid
	private List<MenuItemDto> items = new ArrayList<>();
	
	public MenuDto() {
	}

	public MenuDto(MenuEntity entity) {
		super(entity);
		this.name = entity.getName();
		this.date = entity.getDate();
		this.items = entity.getItems().stream().map(MenuItemDto::new).collect(Collectors.toList());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public List<MenuItemDto> getItems() {
		return items;
	}

	public void setItems(List<MenuItemDto> items) {
		this.items = items;
	}

	@Override
	public void fill(MenuEntity entity) {
		entity.setName(name);
		entity.setDate(date);
		List<MenuItemEntity> itemEntities = entity.getItems();
		itemEntities.clear();
		items.stream().forEach(itemDto -> {
			MenuItemEntity itemEntity = new MenuItemEntity();
			itemDto.fill(itemEntity);
			itemEntities.add(itemEntity);
		});
	}
}

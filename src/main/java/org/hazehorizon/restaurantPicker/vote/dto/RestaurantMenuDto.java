package org.hazehorizon.restaurantPicker.vote.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Triple;
import org.hazehorizon.restaurantPicker.common.rest.dto.AbstractEntityDto;
import org.hazehorizon.restaurantPicker.model.MenuEntity;
import org.hazehorizon.restaurantPicker.model.RestaurantEntity;

public class RestaurantMenuDto extends AbstractEntityDto<RestaurantEntity> {
	private String name;
	private String description;
	private String address;
	private String phone;
	private String menuName;
	private Integer votes;
	private List<RestaurantMenuItemDto> items = new ArrayList<>();
	
	public RestaurantMenuDto(Triple<RestaurantEntity, MenuEntity, Integer> restaurantMenuVote) {
		super(restaurantMenuVote.getLeft());
		this.name = restaurantMenuVote.getLeft().getName();
		this.description = restaurantMenuVote.getLeft().getDescription();
		this.address = restaurantMenuVote.getLeft().getAddress();
		this.phone = restaurantMenuVote.getLeft().getPhone();
		this.menuName = restaurantMenuVote.getMiddle().getName();
		this.votes = restaurantMenuVote.getRight();
		this.items = restaurantMenuVote.getMiddle().getItems().stream().map(RestaurantMenuItemDto::new).collect(Collectors.toList());
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	public String getMenuName() {
		return menuName;
	}

	public Integer getVotes() {
		return votes;
	}

	public List<RestaurantMenuItemDto> getItems() {
		return items;
	}

	@Override
	public void fill(RestaurantEntity entity) {
	}
}

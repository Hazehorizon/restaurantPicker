package org.hazehorizon.restaurantPicker.settings.rest.dto;

import javax.validation.constraints.NotNull;

import org.hazehorizon.restaurantPicker.common.rest.dto.AbstractEntityDto;
import org.hazehorizon.restaurantPicker.model.RestaurantEntity;

public class RestaurantDto extends AbstractEntityDto<RestaurantEntity> {
	@NotNull
	private String name;
	private String description;
	private String address;
	private String phone;
	private Boolean active;
	
	public RestaurantDto() {
	}
	
	public RestaurantDto(RestaurantEntity entity) {
		super(entity);
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.address = entity.getAddress();
		this.phone = entity.getPhone();
		this.active = entity.isActive();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public void fill(RestaurantEntity entity) {
		entity.setName(name);
		entity.setDescription(description);
		entity.setAddress(address);
		entity.setPhone(phone);
		if (null != active) {
			entity.setActive(active);
	    }
	}
}

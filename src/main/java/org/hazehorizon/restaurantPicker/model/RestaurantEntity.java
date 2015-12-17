package org.hazehorizon.restaurantPicker.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


@Entity
public class RestaurantEntity extends AbstractEntity {
	private String name;
	private String description;
	private String address;
	private String phone;
	private boolean active = true;
	private List<MenuEntity> menus = new ArrayList<>();

	@Column(nullable=false)
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
	@Column(nullable=true)
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	@OneToMany(mappedBy="restaurantId", cascade=CascadeType.REMOVE, orphanRemoval=true)
	public List<MenuEntity> getMenus() {
		return menus;
	}
	public void setMenus(List<MenuEntity> menus) {
		this.menus = menus;
	}
}

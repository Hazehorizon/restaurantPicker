package org.hazehorizon.restaurantPicker.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"DATE", "RESTAURANT_ID"}))
public class MenuEntity extends AbstractEntity {
	private LocalDate date;
	private String name;
	private Long restaurantId;
	private List<MenuItemEntity> items = new ArrayList<>();
	
	@Column(name="DATE", nullable=false)
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="RESTAURANT_ID", nullable=false)
	public Long getRestaurantId() {
		return restaurantId;
	}
	public void setRestaurantId(Long restaurantId) {
		this.restaurantId = restaurantId;
	}
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(nullable=false)
	@OrderColumn(name="number")
	public List<MenuItemEntity> getItems() {
		return items;
	}
	public void setItems(List<MenuItemEntity> items) {
		this.items = items;
	}
}
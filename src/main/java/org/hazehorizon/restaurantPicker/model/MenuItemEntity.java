package org.hazehorizon.restaurantPicker.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class MenuItemEntity extends AbstractEntity {
	private int number;
	private String name;
	private Double price;
	
	@Column(insertable=false, updatable=false)
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	@Column(nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(nullable=false)
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
}

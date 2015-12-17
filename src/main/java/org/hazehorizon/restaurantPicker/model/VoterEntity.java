package org.hazehorizon.restaurantPicker.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class VoterEntity extends AbstractEntity {
	private String code;

	@Column(nullable=false, unique=true)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}

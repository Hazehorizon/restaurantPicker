package org.hazehorizon.restaurantPicker.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"DATE", "VOTER_ID"}))
public class VoteEntity extends AbstractEntity {
	private VoterEntity voter;
	private RestaurantEntity restaurant;
	private LocalDate date;
	@ManyToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="VOTER_ID", nullable=false)
	public VoterEntity getVoter() {
		return voter;
	}
	public void setVoter(VoterEntity voter) {
		this.voter = voter;
	}
	@ManyToOne
	@JoinColumn(nullable=false)
	public RestaurantEntity getRestaurant() {
		return restaurant;
	}
	public void setRestaurant(RestaurantEntity restaurant) {
		this.restaurant = restaurant;
	}
	@Column(name="DATE", nullable=false)
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
}

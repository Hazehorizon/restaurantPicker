package org.hazehorizon.restaurantPicker.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class UserEntity extends AbstractEntity {
	private String login;
	private String passwd;
	private boolean active = true;
	private List<RoleEntity> roles = new ArrayList<>();
	
	@Column(nullable=false, unique=true)
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	@Column(nullable=false)
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	@Column(nullable=false)
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable
	public List<RoleEntity> getRoles() {
		return roles;
	}
	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}
}

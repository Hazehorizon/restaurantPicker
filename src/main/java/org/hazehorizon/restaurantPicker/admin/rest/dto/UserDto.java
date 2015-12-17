package org.hazehorizon.restaurantPicker.admin.rest.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hazehorizon.restaurantPicker.common.rest.dto.AbstractEntityDto;
import org.hazehorizon.restaurantPicker.model.RoleEntity;
import org.hazehorizon.restaurantPicker.model.UserEntity;

public class UserDto extends AbstractEntityDto<UserEntity> {
	@NotNull
	private String login;
	private String passwd;
	private Boolean active;
	@NotNull
	@Size(min=1)
	private List<String> roles = new ArrayList<>();

	public UserDto() {
	}
	public UserDto(UserEntity entity) {
		super(entity);
		this.login = entity.getLogin();
		this.active = entity.isActive();
		this.roles = entity.getRoles().stream().map(role -> role.getCode()).collect(Collectors.toList());
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	@Override
	public void fill(UserEntity entity) {
		entity.setLogin(login);
		if (StringUtils.isNoneBlank(passwd)) {
			entity.setPasswd(passwd);
		}
		if (null != active) {
			entity.setActive(active);
		}
		entity.getRoles().clear();
		roles.stream().forEach(r -> {
			RoleEntity role = new RoleEntity();
			role.setCode(r);
			entity.getRoles().add(role);
		});
	}
}

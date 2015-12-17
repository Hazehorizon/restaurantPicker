package org.hazehorizon.restaurantPicker.admin.service;

import org.hazehorizon.restaurantPicker.model.UserEntity;
import org.springframework.data.domain.Page;

public interface UserService {
	Page<UserEntity> findAll(Integer pageNumber, Integer pageCount);
	UserEntity read(String login);
	UserEntity save(UserEntity user);
	void delete(String login);
}

package org.hazehorizon.restaurantPicker.settings.service;

import org.hazehorizon.restaurantPicker.model.RestaurantEntity;
import org.springframework.data.domain.Page;

public interface RestaurantService {
	Page<RestaurantEntity> findAll(Integer pageNumber, Integer pageCount);
	RestaurantEntity read(Long restaurantId);
	RestaurantEntity save(RestaurantEntity restaurant);
	void delete(Long restaurantId);
}

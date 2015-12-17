package org.hazehorizon.restaurantPicker.model.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.hazehorizon.restaurantPicker.model.MenuEntity;
import org.springframework.data.repository.CrudRepository;

public interface MenuRepository extends CrudRepository<MenuEntity, Long> {
	Optional<MenuEntity> findByRestaurantIdAndDate(Long restaurantId, LocalDate date);
}

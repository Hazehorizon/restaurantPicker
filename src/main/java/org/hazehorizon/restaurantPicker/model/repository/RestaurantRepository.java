package org.hazehorizon.restaurantPicker.model.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.hazehorizon.restaurantPicker.model.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RestaurantRepository extends PagingAndSortingRepository<RestaurantEntity, Long> {
	@Query(value="select re from RestaurantEntity as re, IN(re.menus) as mn where re.active = true and mn.date = ?1")
	Page<RestaurantEntity> findRestaurantsWithMenuForDate(LocalDate date, Pageable pageable);
	@Query(value="select re from RestaurantEntity as re, IN(re.menus) as mn where re.id = ?1 and re.active = true and mn.date = ?2")
	Optional<RestaurantEntity> readWithMenuForDate(Long restaurantId, LocalDate date);
}

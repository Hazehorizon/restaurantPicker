package org.hazehorizon.restaurantPicker.settings.service;

import java.time.LocalDate;

import org.hazehorizon.restaurantPicker.model.MenuEntity;

public interface MenuService {
	MenuEntity save(MenuEntity menu);
	MenuEntity read(Long restaurantId, LocalDate date);
	void delete(Long restaurantId, LocalDate date);
}

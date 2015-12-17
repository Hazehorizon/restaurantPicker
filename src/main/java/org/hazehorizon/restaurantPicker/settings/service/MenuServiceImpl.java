package org.hazehorizon.restaurantPicker.settings.service;

import java.time.LocalDate;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hazehorizon.restaurantPicker.common.service.AbstractService;
import org.hazehorizon.restaurantPicker.model.MenuEntity;
import org.hazehorizon.restaurantPicker.model.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MenuServiceImpl extends AbstractService implements MenuService {
	@Autowired
	private MenuRepository repository;
	
	@Override
	public MenuEntity save(MenuEntity menu) {
		return repository.save(menu);
	}

	@Override
	public MenuEntity read(Long restaurantId, LocalDate date) {
		Optional<MenuEntity> menu = repository.findByRestaurantIdAndDate(restaurantId, date);
		checkOptional(menu, MenuEntity.class, restaurantId + " " + date);
		return menu.get();
	}

	@Override
	public void delete(Long restaurantId, LocalDate date) {
		Optional<MenuEntity> menu = repository.findByRestaurantIdAndDate(restaurantId, date);
		if (menu.isPresent()) {
			repository.delete(menu.get());
			logger.info("Menu is deleted");
		}
	}
}

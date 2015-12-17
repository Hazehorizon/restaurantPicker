package org.hazehorizon.restaurantPicker.settings.service;

import javax.transaction.Transactional;

import org.hazehorizon.restaurantPicker.common.service.AbstractService;
import org.hazehorizon.restaurantPicker.common.service.NotFoundException;
import org.hazehorizon.restaurantPicker.model.RestaurantEntity;
import org.hazehorizon.restaurantPicker.model.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RestaurantServiceImpl extends AbstractService implements RestaurantService {
	@Autowired
	private RestaurantRepository repository;
	
	public Page<RestaurantEntity> findAll(Integer pageNumber, Integer pageCount) {
		return repository.findAll(createPageable(pageNumber, pageCount));
	}

	@Override
	public RestaurantEntity save(RestaurantEntity restaurant) {
		return repository.save(restaurant);
	}

	@Override
	public RestaurantEntity read(Long restaurantId) {
		RestaurantEntity restaurantEntity = repository.findOne(restaurantId);
		if (null == restaurantEntity) {
			throw new NotFoundException(RestaurantEntity.class, restaurantId);
		}
		return restaurantEntity;
	}

	@Override
	public void delete(Long restaurantId) {
		RestaurantEntity entity = repository.findOne(restaurantId);
		if (null != entity) {
			entity.setActive(false);
			logger.info("Restaurant is inactivated");
		}
	}
}

package org.hazehorizon.restaurantPicker.settings.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.hazehorizon.restaurantPicker.LoggingContext;
import org.hazehorizon.restaurantPicker.common.rest.AbstractController;
import org.hazehorizon.restaurantPicker.common.rest.ResponseWrapper;
import org.hazehorizon.restaurantPicker.model.RestaurantEntity;
import org.hazehorizon.restaurantPicker.settings.rest.dto.RestaurantDto;
import org.hazehorizon.restaurantPicker.settings.service.RestaurantService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/settings")
public class RestaurantController extends AbstractController {
	@Autowired
	private RestaurantService service;
	
	@RequestMapping(value="restaurants", method=RequestMethod.GET)
	ResponseWrapper<List<RestaurantDto>> getRestaurants(@RequestParam(required=false) Integer page,
			@RequestParam(required=false) Integer limit) {
		return createResponseWrapper(
				service.findAll(page, limit).getContent().stream().map(RestaurantDto::new).collect(Collectors.toList()));
	}

	@RequestMapping(value="restaurant", method=RequestMethod.POST)
	ResponseWrapper<RestaurantDto> createRestaurant(@Valid @RequestBody RestaurantDto dto) {
		try(MDC.MDCCloseable closeable = MDC.putCloseable(LoggingContext.CONTEXT, dto.getName())) {
			RestaurantEntity entity = new RestaurantEntity();
			dto.fill(entity);
			return createResponseWrapper(
					new RestaurantDto(service.save(entity)));
		}
	}
	
	@RequestMapping(value="restaurant/{restaurantId}", method=RequestMethod.PUT)
	ResponseWrapper<RestaurantDto> updateRestaurant(
			@PathVariable("restaurantId") Long restaurantId,
			@Valid @RequestBody RestaurantDto dto) {
		try(MDC.MDCCloseable closeable = MDC.putCloseable(LoggingContext.CONTEXT, Long.toString(restaurantId))) {
			RestaurantEntity entity = service.read(restaurantId);
			dto.fill(entity);
			return createResponseWrapper(
					new RestaurantDto(service.save(entity)));
		}
	}
	
	@RequestMapping(value="restaurant/{restaurantId}", method=RequestMethod.GET)
	ResponseWrapper<RestaurantDto> readRestaurant(@PathVariable("restaurantId") Long restaurantId) {
		return createResponseWrapper(new RestaurantDto(service.read(restaurantId)));
	}
	
	@RequestMapping(value="restaurant/{restaurantId}", method=RequestMethod.DELETE)
	ResponseWrapper<RestaurantDto> deleteRestaurant(@PathVariable("restaurantId") Long restaurantId) {
		try(MDC.MDCCloseable closeable = MDC.putCloseable(LoggingContext.CONTEXT, Long.toString(restaurantId))) {
			service.delete(restaurantId);
			return createResponseWrapper(null);
		}
	}	
}

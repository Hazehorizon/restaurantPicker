package org.hazehorizon.restaurantPicker.settings.rest;

import java.time.LocalDate;

import javax.validation.Valid;

import org.hazehorizon.restaurantPicker.LoggingContext;
import org.hazehorizon.restaurantPicker.common.rest.AbstractController;
import org.hazehorizon.restaurantPicker.common.rest.ResponseWrapper;
import org.hazehorizon.restaurantPicker.model.MenuEntity;
import org.hazehorizon.restaurantPicker.settings.rest.dto.MenuDto;
import org.hazehorizon.restaurantPicker.settings.service.MenuService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/settings/restaurant/{restaurantId}/menu")
public class MenuController extends AbstractController {
	@Autowired
	private MenuService service;
	
	@RequestMapping(method=RequestMethod.POST)
	ResponseWrapper<MenuDto> createMenu(@PathVariable("restaurantId") Long restaurantId, @Valid @RequestBody MenuDto dto) {
		try(MDC.MDCCloseable closeable = MDC.putCloseable(LoggingContext.CONTEXT, Long.toString(restaurantId) + " " + dto.getDate())) {
			MenuEntity entity = new MenuEntity();
			entity.setRestaurantId(restaurantId);
			dto.fill(entity);
			return createResponseWrapper(
					new MenuDto(service.save(entity)));
		}
	}

	@RequestMapping(value="{date}", method=RequestMethod.PUT)
	ResponseWrapper<MenuDto> updateMenu(
			@PathVariable("restaurantId") Long restaurantId,
			@DateTimeFormat(pattern = "yyyyMMdd") @PathVariable("date") LocalDate date,
			@Valid @RequestBody MenuDto dto) {
		try(MDC.MDCCloseable closeable = MDC.putCloseable(LoggingContext.CONTEXT, Long.toString(restaurantId) + " " + date)) {
			MenuEntity entity = service.read(restaurantId, date);
			dto.fill(entity);
			entity.setDate(date);
			return createResponseWrapper(
					new MenuDto(service.save(entity)));
		}
	}
	
	@RequestMapping(value="{date}", method=RequestMethod.GET)
	ResponseWrapper<MenuDto> readMenu(@PathVariable("restaurantId") Long restaurantId,
			@DateTimeFormat(pattern = "yyyyMMdd") @PathVariable("date") LocalDate date) {
		MenuEntity menu = service.read(restaurantId, date);
		return createResponseWrapper(null == menu ? null : new MenuDto(menu));
	}

	@RequestMapping(value="{date}", method=RequestMethod.DELETE)
	ResponseWrapper<MenuDto> deleteMenu(@PathVariable("restaurantId") Long restaurantId,
			@DateTimeFormat(pattern = "yyyyMMdd") @PathVariable("date") LocalDate date) {
		try(MDC.MDCCloseable closeable = MDC.putCloseable(LoggingContext.CONTEXT, Long.toString(restaurantId) + " " + date)) {
			service.delete(restaurantId, date);
			return createResponseWrapper(null);
		}
	}
}

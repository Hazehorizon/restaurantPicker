package org.hazehorizon.restaurantPicker.admin.rest;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.hazehorizon.restaurantPicker.LoggingContext;
import org.hazehorizon.restaurantPicker.admin.rest.dto.UserDto;
import org.hazehorizon.restaurantPicker.admin.service.UserService;
import org.hazehorizon.restaurantPicker.common.rest.AbstractController;
import org.hazehorizon.restaurantPicker.common.rest.ResponseWrapper;
import org.hazehorizon.restaurantPicker.model.UserEntity;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/system")
public class UserController extends AbstractController {
	@Autowired
	private UserService service;
	
	@RequestMapping(value="users", method=RequestMethod.GET)
	ResponseWrapper<List<UserDto>> getUsers(@RequestParam(required=false) Integer page,
			@RequestParam(required=false) Integer limit) {
		return createResponseWrapper(
				service.findAll(page, limit).getContent().stream().map(UserDto::new).collect(Collectors.toList()));
	}

	@RequestMapping(value="user", method=RequestMethod.POST)
	ResponseWrapper<UserDto> createUser(@Valid @RequestBody UserDto dto) {
		try(MDC.MDCCloseable closeable = MDC.putCloseable(LoggingContext.CONTEXT, dto.getLogin())) {
			UserEntity entity = new UserEntity();
			dto.fill(entity);
			return createResponseWrapper(
					new UserDto(service.save(entity)));
		}
	}
	
	@RequestMapping(value="user/{login}", method=RequestMethod.PUT)
	ResponseWrapper<UserDto> updateUser(
			@PathVariable("login") String login,
			@Valid @RequestBody UserDto dto) {
		try(MDC.MDCCloseable closeable = MDC.putCloseable(LoggingContext.CONTEXT, login)) {
			UserEntity entity = service.read(login);
			dto.fill(entity);
			entity.setLogin(login);
			return createResponseWrapper(
					new UserDto(service.save(entity)));
		}
	}
	
	@RequestMapping(value="user/{login}", method=RequestMethod.GET)
	ResponseWrapper<UserDto> readUser(@PathVariable("login") String login) {
		return createResponseWrapper(new UserDto(service.read(login)));
	}
	
	@RequestMapping(value="user/{login}", method=RequestMethod.DELETE)
	ResponseWrapper<UserDto> deleteRestaurant(@PathVariable("login") String login) {
		try(MDC.MDCCloseable closeable = MDC.putCloseable(LoggingContext.CONTEXT, login)) {
			service.delete(login);
			return createResponseWrapper(null);
		}
	}
}

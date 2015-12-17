package org.hazehorizon.restaurantPicker.vote.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.hazehorizon.restaurantPicker.LoggingContext;
import org.hazehorizon.restaurantPicker.common.rest.AbstractController;
import org.hazehorizon.restaurantPicker.common.rest.ResponseWrapper;
import org.hazehorizon.restaurantPicker.vote.dto.RestaurantMenuDto;
import org.hazehorizon.restaurantPicker.vote.service.VoteService;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping(value="/api/v1/vote")
public class VoteController extends AbstractController {
	@Autowired
	private VoteService service;
	
	@RequestMapping(value="restaurants", method=RequestMethod.GET)
	ResponseWrapper<List<RestaurantMenuDto>> getRestaurants(@RequestParam(required=false) Integer page,
			@RequestParam(required=false) Integer limit) {
		return createResponseWrapper(service.getTodayVotes(page, limit).getContent().stream().map(RestaurantMenuDto::new).collect(Collectors.toList()));
	}
	
	@RequestMapping(value="restaurant/{restaurantId}", method=RequestMethod.POST)
	ResponseWrapper<Void> vote(@PathVariable("restaurantId") Long restaurantId) {
		String remoteAddress = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
		try(MDC.MDCCloseable closeable = MDC.putCloseable(LoggingContext.CONTEXT, Long.toString(restaurantId) + " " + remoteAddress + " " + LocalDateTime.now())) {
			service.vote(restaurantId, remoteAddress);
			logger.debug("Voting completed successfully");
			return createResponseWrapper(null);
		}
	}
}

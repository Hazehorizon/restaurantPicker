package org.hazehorizon.restaurantPicker.common.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public abstract class AbstractService {
	protected static Logger logger = LoggerFactory.getLogger(AbstractService.class);
	private static final int DEFAULT_PAGE_SIZE = 20;
	
	protected Pageable createPageable(Integer pageNumber, Integer pageCount) {
		return new PageRequest(null == pageNumber ? 0 : pageNumber,
				null == pageCount ? DEFAULT_PAGE_SIZE : pageCount);
	}
	
	protected void checkOptional(Optional<?> optional, Class<?> type, Object identifier) {
		if (!optional.isPresent()) {
			throw new NotFoundException(type, identifier);
		}
	}
}

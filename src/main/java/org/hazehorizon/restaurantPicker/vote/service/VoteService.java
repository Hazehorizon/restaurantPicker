package org.hazehorizon.restaurantPicker.vote.service;

import org.apache.commons.lang3.tuple.Triple;
import org.hazehorizon.restaurantPicker.model.MenuEntity;
import org.hazehorizon.restaurantPicker.model.RestaurantEntity;
import org.springframework.data.domain.Page;

public interface VoteService {
	Page<Triple<RestaurantEntity, MenuEntity, Integer>> getTodayVotes(Integer pageNumber, Integer pageCount);
	void vote(Long restaurantId, String voterCode);
}

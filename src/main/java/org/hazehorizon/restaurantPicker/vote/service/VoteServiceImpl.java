package org.hazehorizon.restaurantPicker.vote.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.hazehorizon.restaurantPicker.common.service.AbstractService;
import org.hazehorizon.restaurantPicker.common.service.ServiceException;
import org.hazehorizon.restaurantPicker.model.MenuEntity;
import org.hazehorizon.restaurantPicker.model.RestaurantEntity;
import org.hazehorizon.restaurantPicker.model.VoteEntity;
import org.hazehorizon.restaurantPicker.model.VoterEntity;
import org.hazehorizon.restaurantPicker.model.repository.RestaurantRepository;
import org.hazehorizon.restaurantPicker.model.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class VoteServiceImpl extends AbstractService implements VoteService {
	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private VoteRepository voteRepository;

	@Override
	public Page<Triple<RestaurantEntity, MenuEntity, Integer>> getTodayVotes(Integer pageNumber, Integer pageCount) {
		LocalDate current = LocalDate.now();
		Page<RestaurantEntity> restaurants = restaurantRepository.findRestaurantsWithMenuForDate(current,
				createPageable(pageNumber, pageCount));
		logger.debug("There are " + restaurants.getNumberOfElements() + " active restaurants with today menu");
		Map<Long, Long> votes = 0 >= restaurants.getNumberOfElements()
					? Collections.emptyMap()
					: voteRepository.countVotesByRestarauntsAndDate(restaurants.getContent(), current).stream().collect(Collectors.toMap(row -> (Long)row[0], row -> (Long)row[1]));
		return restaurants.<Triple<RestaurantEntity, MenuEntity, Integer>>map(restaurant -> fromRestaurant(restaurant, current, votes));
	}

	@Override
	public void vote(Long restaurantId, String voterCode) {
		LocalDateTime current = currentDateTime();
		Optional<VoteEntity> vote = voteRepository.findByVoterCodeAndDate(voterCode, current.toLocalDate());
		Optional<RestaurantEntity> restaurant = restaurantRepository.readWithMenuForDate(restaurantId, current.toLocalDate());
		checkOptional(restaurant, RestaurantEntity.class, restaurantId);
		if (vote.isPresent()) {
			if (current.isBefore(LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0))) {
				vote.get().setRestaurant(restaurant.get());
				logger.info("Vote is changed");
			}
			else {
				logger.info("Vote is already done");
				throw new ServiceException("You have already voted at " + current.toLocalDate());
			}
		}
		else {
			VoteEntity newVote = new VoteEntity();
			newVote.setDate(current.toLocalDate());
			newVote.setRestaurant(restaurant.get());
			Optional<VoterEntity> voter = voteRepository.findVoter(voterCode);
			VoterEntity voterEntity = null;
			if (!voter.isPresent()) {
				voterEntity = new VoterEntity();
				voterEntity.setCode(voterCode);
				logger.info("New voter is created");
			}
			else {
				voterEntity = voter.get();
			}
			newVote.setVoter(voterEntity);
			voteRepository.save(newVote);
		}
	}
	
	private static Triple<RestaurantEntity, MenuEntity, Integer> fromRestaurant(RestaurantEntity restaurant, LocalDate date, Map<Long, Long> votes) {
		Long count = votes.get(restaurant.getId());
		return new ImmutableTriple<RestaurantEntity, MenuEntity, Integer>(
				restaurant, restaurant.getMenus().stream().filter(m -> m.getDate().equals(date)).findFirst().get(), null == count ? 0 : count.intValue());
	}
	
	protected LocalDateTime currentDateTime() {
		return LocalDateTime.now();
	}
}

package org.hazehorizon.restaurantPicker.vote.service;

import static org.hazehorizon.restaurantPicker.TestUtils.randomId;
import static org.hazehorizon.restaurantPicker.TestUtils.randomString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.hazehorizon.restaurantPicker.common.service.ServiceException;
import org.hazehorizon.restaurantPicker.model.MenuEntity;
import org.hazehorizon.restaurantPicker.model.RestaurantEntity;
import org.hazehorizon.restaurantPicker.model.VoteEntity;
import org.hazehorizon.restaurantPicker.model.VoterEntity;
import org.hazehorizon.restaurantPicker.model.repository.RestaurantRepository;
import org.hazehorizon.restaurantPicker.model.repository.VoteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


@RunWith(MockitoJUnitRunner.class)
public class VoteServiceImplTest {
	@Spy
	@InjectMocks
	private VoteServiceImpl testInstance;
	
	@Mock
	private RestaurantRepository restaurantRepository;
	@Mock
	private VoteRepository voteRepository;

	@Test
	public void testGetTodayVotesNoRestaurants() throws Exception {
		Page<RestaurantEntity> page = new PageImpl<>(Collections.emptyList());
		when(restaurantRepository.findRestaurantsWithMenuForDate(eq(LocalDate.now()), any(Pageable.class))).thenReturn(page);

		Page<Triple<RestaurantEntity, MenuEntity, Integer>> result = testInstance.getTodayVotes(1, 20);
		assertEquals(0, result.getNumberOfElements());
		
		verify(restaurantRepository).findRestaurantsWithMenuForDate(any(LocalDate.class), any(Pageable.class));
        verifyNoMoreInteractions(voteRepository);
	}
	
	@Test
	public void testGetTodayVotesNoVotes() throws Exception {
		Page<RestaurantEntity> page = new PageImpl<>(
				Stream.generate(() -> createRestaurant(null)).limit(10).collect(Collectors.toList()));
		when(restaurantRepository.findRestaurantsWithMenuForDate(eq(LocalDate.now()), any(Pageable.class))).thenReturn(page);
		when(voteRepository.countVotesByRestarauntsAndDate(Mockito.<List<RestaurantEntity>>any(), eq(LocalDate.now())))
			.thenReturn(Collections.emptyList());

		Page<Triple<RestaurantEntity, MenuEntity, Integer>> result = testInstance.getTodayVotes(1, 20);
		assertEquals(page.getNumberOfElements(), result.getNumberOfElements());
		result.getContent().stream().forEach(t -> {
			assertNotNull(t.getLeft());
			assertNotNull(t.getMiddle());
			assertEquals(0, t.getRight().intValue());
		});
		
		verify(restaurantRepository).findRestaurantsWithMenuForDate(any(LocalDate.class), any(Pageable.class));
		verify(voteRepository).countVotesByRestarauntsAndDate(Mockito.<List<RestaurantEntity>>any(), any(LocalDate.class));
	}

	@Test
	public void testGetTodayVotesVotes() throws Exception {
		Page<RestaurantEntity> page = new PageImpl<>(
				Stream.generate(() -> createRestaurant(null)).limit(10).collect(Collectors.toList()));
		List<Object[]> counts = new ArrayList<>();
		page.getContent().stream().forEach(r -> {
			if (1 == RandomUtils.nextInt(0, 2)) {
				counts.add(new Object[] {r.getId(), RandomUtils.nextLong(0, 10)});
			}
		});
		when(restaurantRepository.findRestaurantsWithMenuForDate(eq(LocalDate.now()), any(Pageable.class))).thenReturn(page);
		when(voteRepository.countVotesByRestarauntsAndDate(Mockito.<List<RestaurantEntity>>any(), eq(LocalDate.now())))
			.thenReturn(counts);

		Map<Long, Long> countsById = counts.stream().collect(Collectors.toMap(a -> (Long)a[0], a -> (Long)a[1]));
		Page<Triple<RestaurantEntity, MenuEntity, Integer>> result = testInstance.getTodayVotes(1, 20);
		assertEquals(page.getNumberOfElements(), result.getNumberOfElements());
		result.getContent().stream().forEach(t -> {
			Long restaurantId = t.getLeft().getId();
			assertNotNull(t.getLeft());
			assertNotNull(t.getMiddle());
			assertEquals(countsById.containsKey(restaurantId) ? countsById.get(restaurantId).longValue() : 0, t.getRight().longValue());
		});
		
		verify(restaurantRepository).findRestaurantsWithMenuForDate(any(LocalDate.class), any(Pageable.class));
		verify(voteRepository).countVotesByRestarauntsAndDate(Mockito.<List<RestaurantEntity>>any(), any(LocalDate.class));
	}
	
	@Test
	public void testVoteNoVodeNoVoter() throws Exception {
		Long restaurantId = randomId();
		String voterCode = randomString();
		LocalDate current = LocalDate.now();
		RestaurantEntity restaurant = createRestaurant(restaurantId);
			
		when(restaurantRepository.readWithMenuForDate(restaurantId, current)).thenReturn(Optional.of(restaurant));
		when(voteRepository.findByVoterCodeAndDate(voterCode, current)).thenReturn(Optional.empty());
		when(voteRepository.findVoter(voterCode)).thenReturn(Optional.empty());
		
		testInstance.vote(restaurantId, voterCode);
		
		ArgumentCaptor<VoteEntity> voteCaptor = ArgumentCaptor.forClass(VoteEntity.class);
		verify(voteRepository).save(voteCaptor.capture());
		
		VoteEntity vote = voteCaptor.getValue();
		assertEquals(restaurant, vote.getRestaurant());
		assertEquals(current, vote.getDate());
		assertEquals(voterCode, vote.getVoter().getCode());
	}	

	@Test
	public void testVoteNoVodeButVoter() throws Exception {
		Long restaurantId = randomId();
		String voterCode = randomString();
		LocalDate current = LocalDate.now();
		RestaurantEntity restaurant = createRestaurant(restaurantId);
		VoterEntity voter = new VoterEntity();
		voter.setCode(voterCode);
			
		when(restaurantRepository.readWithMenuForDate(restaurantId, current)).thenReturn(Optional.of(restaurant));
		when(voteRepository.findByVoterCodeAndDate(voterCode, current)).thenReturn(Optional.empty());
		when(voteRepository.findVoter(voterCode)).thenReturn(Optional.of(voter));
		
		testInstance.vote(restaurantId, voterCode);
		
		ArgumentCaptor<VoteEntity> voteCaptor = ArgumentCaptor.forClass(VoteEntity.class);
		verify(voteRepository).save(voteCaptor.capture());
		
		VoteEntity vote = voteCaptor.getValue();
		assertEquals(restaurant, vote.getRestaurant());
		assertEquals(current, vote.getDate());
		assertEquals(voter, vote.getVoter());
	}	
	
	@Test
	public void testVoteExistsBeforeEleven() throws Exception {
		Long restaurantId = randomId();
		LocalDate current = LocalDate.now();
		RestaurantEntity restaurant = createRestaurant(restaurantId);
		String voterCode = randomString();
		VoteEntity vote = new VoteEntity();
			
		when(restaurantRepository.readWithMenuForDate(restaurantId, current)).thenReturn(Optional.of(restaurant));
		when(voteRepository.findByVoterCodeAndDate(voterCode, current)).thenReturn(Optional.of(vote));

		when(testInstance.currentDateTime()).thenReturn(LocalDateTime.now().withHour(10));
		testInstance.vote(restaurantId, voterCode);

		assertEquals(restaurant, vote.getRestaurant());
	}	

	@Test(expected = ServiceException.class)
	public void testVoteExistsAfterEleven() throws Exception {
		Long restaurantId = randomId();
		LocalDate current = LocalDate.now();
		RestaurantEntity restaurant = createRestaurant(restaurantId);
		String voterCode = randomString();
		VoteEntity vote = new VoteEntity();
			
		when(restaurantRepository.readWithMenuForDate(restaurantId, current)).thenReturn(Optional.of(restaurant));
		when(voteRepository.findByVoterCodeAndDate(voterCode, current)).thenReturn(Optional.of(vote));

		when(testInstance.currentDateTime()).thenReturn(LocalDateTime.now().withHour(11).withMinute(0).withSecond(0).withNano(0));
		testInstance.vote(restaurantId, voterCode);
	}
	
	private RestaurantEntity createRestaurant(Long restaurantId) {
		RestaurantEntity restaurant = new RestaurantEntity();
		restaurant.setId(null == restaurantId ? randomId() : restaurantId);
		restaurant.setAddress(randomString());
		restaurant.setActive(true);
		restaurant.setName(randomString());
		restaurant.setDescription(randomString());
		restaurant.setPhone(randomString());
		MenuEntity menu = new MenuEntity();
		menu.setDate(LocalDate.now());
		menu.setName(randomString());
		restaurant.getMenus().add(menu);
		return restaurant;
	}
}

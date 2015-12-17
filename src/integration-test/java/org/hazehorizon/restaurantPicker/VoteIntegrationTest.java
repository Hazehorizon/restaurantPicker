package org.hazehorizon.restaurantPicker;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Optional;

import org.apache.commons.lang3.RandomUtils;
import org.hazehorizon.restaurantPicker.common.rest.ResponseWrapper;
import org.hazehorizon.restaurantPicker.model.MenuEntity;
import org.hazehorizon.restaurantPicker.model.RestaurantEntity;
import org.hazehorizon.restaurantPicker.model.VoteEntity;
import org.hazehorizon.restaurantPicker.model.VoterEntity;
import org.hazehorizon.restaurantPicker.model.repository.MenuRepository;
import org.hazehorizon.restaurantPicker.model.repository.RestaurantRepository;
import org.hazehorizon.restaurantPicker.model.repository.VoteRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class VoteIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private VoteRepository voteRepository;

    @Test
    public void shouldReturnTestRestaurants() throws Exception
    {
        mvc().perform(get("/api/v1/vote/restaurants"))
        	 .andExpect(status().isOk())
             .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
             .andExpect(jsonPath("$.data", hasSize(1)))
             .andExpect(jsonPath("$.data[0].id", is((int)RESTAURANT_ID)))
             .andExpect(jsonPath("$.data[0].name", notNullValue()))
             .andExpect(jsonPath("$.data[0].votes", is(0)))
             .andExpect(jsonPath("$.data[0].items", hasSize(3)));
    }

    @Test
    public void shouldNotReturnInactiveRestaurants() throws Exception
    {
    	RestaurantEntity dbRestaurant = restaurantRepository.findOne(RESTAURANT_ID);
		dbRestaurant.setActive(false);
		restaurantRepository.save(dbRestaurant);

        mvc().perform(get("/api/v1/vote/restaurants"))
	        .andExpect(status().isOk())
	        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
	        .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    public void shouldNotReturnNoMenuRestaurants() throws Exception
    {
    	Optional<MenuEntity> dbMenu = menuRepository.findByRestaurantIdAndDate(RESTAURANT_ID, LocalDate.now());
    	menuRepository.delete(dbMenu.get());

        mvc().perform(get("/api/v1/vote/restaurants"))
	        .andExpect(status().isOk())
	        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
	        .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    public void shouldReturnSeveralRestaurants() throws Exception
    {
    	RestaurantEntity dbRestaurant = new RestaurantEntity();
    	dbRestaurant.setName("new");
    	MenuEntity dbMenu = new MenuEntity();
    	dbMenu.setDate(LocalDate.now());
    	dbRestaurant.getMenus().add(dbMenu);
    	restaurantRepository.save(dbRestaurant);
    	dbMenu.setRestaurantId(dbRestaurant.getId());
    	menuRepository.save(dbMenu);

        mvc().perform(get("/api/v1/vote/restaurants"))
	   	    .andExpect(status().isOk())
	        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
	        .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    public void shouldReturnVotesForRestaurants() throws Exception
    {
    	int n = RandomUtils.nextInt(1, 10);
    	createVotes(n);

        mvc().perform(get("/api/v1/vote/restaurants"))
	   	 	.andExpect(status().isOk())
	        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
	        .andExpect(jsonPath("$.data", hasSize(1)))
	        .andExpect(jsonPath("$.data[0].votes", is(n)));
    }

    @Test
    public void shouldVote() throws Exception
    {
        assertEquals(0, voteRepository.count());
        mvc().perform(post("/api/v1/vote/restaurant/{restaurantId}", RESTAURANT_ID))
	   	 	.andExpect(status().isOk())
	        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
	        .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
	        .andExpect(jsonPath("$.data", nullValue()));

        assertEquals(1, voteRepository.count());
    }    

    private void createVotes(int n) {
    	RestaurantEntity dbRestaurant = restaurantRepository.findOne(RESTAURANT_ID);
    	for (int i = 0; i < n; ++i) {
	    	VoterEntity dbVoter = new VoterEntity();
	    	dbVoter.setCode("test"+i);
	    	VoteEntity dbVote = new VoteEntity();
	    	dbVote.setDate(LocalDate.now());
	    	dbVote.setVoter(dbVoter);
	    	dbVote.setRestaurant(dbRestaurant);
	    	voteRepository.save(dbVote);
    	}
    }
}

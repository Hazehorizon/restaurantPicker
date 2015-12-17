package org.hazehorizon.restaurantPicker;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.lang3.RandomStringUtils;
import org.hazehorizon.restaurantPicker.common.rest.ResponseWrapper;
import org.hazehorizon.restaurantPicker.model.RestaurantEntity;
import org.hazehorizon.restaurantPicker.model.repository.RestaurantRepository;
import org.hazehorizon.restaurantPicker.settings.rest.dto.RestaurantDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class RestaurantIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private RestaurantRepository repository;

    @Test
    public void shouldReturnTestRestaurant() throws Exception
    {
        mvc().perform(get("/api/v1/settings/restaurant/{restaurantId}", RESTAURANT_ID).with(httpBasic("admin","admin")))
        	 .andExpect(status().isOk())
             .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
             .andExpect(jsonPath("$.data.name", notNullValue()))
             .andExpect(jsonPath("$.data.active", is(true)));
    }

    @Test
    public void shouldAddRestaurant() throws Exception
    {
    	RestaurantDto restaurant = new RestaurantDto();
    	restaurant.setName("temporal");
    	restaurant.setDescription("description");
    	restaurant.setActive(true);
    	restaurant.setAddress(RandomStringUtils.random(DEFAULT_STING_LEN));
    	restaurant.setPhone(RandomStringUtils.random(DEFAULT_STING_LEN));
        mvc().perform(post("/api/v1/settings/restaurant").with(httpBasic("admin","admin")).contentType(MediaType.APPLICATION_JSON).content(toJson(restaurant)))
        	 .andExpect(status().isOk())
             .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
             .andExpect(jsonPath("$.data.name", is(restaurant.getName())))
             .andExpect(jsonPath("$.data.description", is(restaurant.getDescription())))
             .andExpect(jsonPath("$.data.active", is(restaurant.getActive())))
             .andExpect(jsonPath("$.data.address", is(restaurant.getAddress())))
             .andExpect(jsonPath("$.data.phone", is(restaurant.getPhone())))
        	 .andDo(result -> checkDbRestaurant(toDto(RestaurantDto.class, result.getResponse().getContentAsString())));
    }

    @Test
    public void shouldValidateAddRestaurant() throws Exception
    {
    	RestaurantDto restaurant = new RestaurantDto();
        mvc().perform(post("/api/v1/settings/restaurant").with(httpBasic("admin","admin")).contentType(MediaType.APPLICATION_JSON).content(toJson(restaurant)))
        	.andExpect(status().isBadRequest())
        	.andExpect(jsonPath("$.code", is(ResponseWrapper.VALIDATION_ERROR)))
        	.andExpect(jsonPath("$.messages", not(empty())));
    }

    @Test
    public void shouldUpdateRestaurant() throws Exception
    {
    	RestaurantDto restaurant = new RestaurantDto();
    	restaurant.setName(RandomStringUtils.random(DEFAULT_STING_LEN));
    	restaurant.setDescription(RandomStringUtils.random(DEFAULT_STING_LEN));
    	restaurant.setActive(true);
    	restaurant.setAddress(RandomStringUtils.random(DEFAULT_STING_LEN));
    	restaurant.setPhone(RandomStringUtils.random(DEFAULT_STING_LEN));
        mvc().perform(put("/api/v1/settings/restaurant/{restuarantId}", RESTAURANT_ID).with(httpBasic("admin","admin")).contentType(MediaType.APPLICATION_JSON).content(toJson(restaurant)))
        	 .andExpect(status().isOk())
             .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
             .andExpect(jsonPath("$.data.id", is((int)RESTAURANT_ID)))
             .andExpect(jsonPath("$.data.name", is(restaurant.getName())))
             .andExpect(jsonPath("$.data.description", is(restaurant.getDescription())))
             .andExpect(jsonPath("$.data.active", is(restaurant.getActive())))
             .andExpect(jsonPath("$.data.address", is(restaurant.getAddress())))
             .andExpect(jsonPath("$.data.phone", is(restaurant.getPhone())))
        	 .andDo(result -> checkDbRestaurant(toDto(RestaurantDto.class, result.getResponse().getContentAsString())));
    }
    
    @Test
    public void shouldValidateEditRestaurant() throws Exception
    {
    	RestaurantDto restaurant = new RestaurantDto();
        mvc().perform(put("/api/v1/settings/restaurant/{restuarantId}", RESTAURANT_ID).with(httpBasic("admin","admin")).contentType(MediaType.APPLICATION_JSON).content(toJson(restaurant)))
        	.andExpect(status().isBadRequest())
        	.andExpect(jsonPath("$.code", is(ResponseWrapper.VALIDATION_ERROR)))
        	.andExpect(jsonPath("$.messages", not(empty())));
    }

	@Test
	public void shouldReadRestaurants() throws Exception {
		mvc().perform(get("/api/v1/settings/restaurants").with(httpBasic("admin","admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
				.andExpect(jsonPath("$.data", hasSize(1)));
	}

	@Test
	public void shouldDeleteRestaurant() throws Exception {
		mvc().perform(delete("/api/v1/settings/restaurant/{restuarantId}", RESTAURANT_ID).with(httpBasic("admin","admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
				.andExpect(jsonPath("$.data", nullValue()));
		assertFalse(repository.findOne(RESTAURANT_ID).isActive());
	}

	@Test
	public void shouldSecurityDenyRestaurants() throws Exception {
		mvc().perform(get("/api/v1/settings/restaurants"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void shouldSecurityDenyWrongRestaurants() throws Exception {
		mvc().perform(get("/api/v1/settings/restaurants").with(httpBasic("system","system")))
				.andExpect(status().isForbidden());
	}

    private void checkDbRestaurant(RestaurantDto restaurant) {
    	RestaurantEntity dbRestaurant = repository.findOne(restaurant.getId());
    	assertNotNull(dbRestaurant);
    	assertEquals(restaurant.getId(), dbRestaurant.getId());
    	assertEquals(restaurant.getName(), dbRestaurant.getName());
    	assertEquals(restaurant.getActive(), dbRestaurant.isActive());
    	assertEquals(restaurant.getDescription(), dbRestaurant.getDescription());
    	assertEquals(restaurant.getAddress(), dbRestaurant.getAddress());
    	assertEquals(restaurant.getPhone(), dbRestaurant.getPhone());
    }
}

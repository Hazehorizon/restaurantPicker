package org.hazehorizon.restaurantPicker;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.hazehorizon.restaurantPicker.common.rest.ResponseWrapper;
import org.hazehorizon.restaurantPicker.model.MenuEntity;
import org.hazehorizon.restaurantPicker.model.repository.MenuRepository;
import org.hazehorizon.restaurantPicker.settings.rest.dto.MenuDto;
import org.hazehorizon.restaurantPicker.settings.rest.dto.MenuItemDto;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class MenuIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private MenuRepository repository;

    @Test
    public void shouldReturnTestMenu() throws Exception
    {
        mvc().perform(get("/api/v1/settings/restaurant/{restaurantId}/menu/{date}", RESTAURANT_ID, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
        		.with(httpBasic("admin","admin")))
        	 .andExpect(status().isOk())
             .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
             .andExpect(jsonPath("$.data.name", notNullValue()))
             .andExpect(jsonPath("$.data.items", not(empty())));
    }

    @Test
    public void shouldAddMenu() throws Exception
    {
    	MenuDto menu = new MenuDto();
    	menu.setDate(LocalDate.now().minusDays(1));
    	menu.setName("temporal");
    	MenuItemDto itemDto = new MenuItemDto();
    	itemDto.setName(RandomStringUtils.random(DEFAULT_STING_LEN));
    	itemDto.setPrice(0.95);
    	menu.getItems().add(itemDto);
        mvc().perform(post("/api/v1/settings/restaurant/{restaurantId}/menu", RESTAURANT_ID)
        		.with(httpBasic("admin","admin")).contentType(MediaType.APPLICATION_JSON).content(toJson(menu)))
        	 .andExpect(status().isOk())
             .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
             .andExpect(jsonPath("$.data.name", is(menu.getName())))
             .andExpect(jsonPath("$.data.items", hasSize(menu.getItems().size())))
             .andExpect(jsonPath("$.data.items[0].name", is(itemDto.getName())))
             .andExpect(jsonPath("$.data.items[0].price", is(itemDto.getPrice())));
       	 checkDbMenu(RESTAURANT_ID, menu);
    }

    @Test
    public void shouldValidateAddMenu() throws Exception
    {
    	MenuDto menu = new MenuDto();
    	menu.setDate(LocalDate.now().minusDays(1));
    	MenuItemDto itemDto = new MenuItemDto();
    	menu.getItems().add(itemDto);
        mvc().perform(post("/api/v1/settings/restaurant/{restaurantId}/menu", RESTAURANT_ID)
        		.with(httpBasic("admin","admin")).contentType(MediaType.APPLICATION_JSON).content(toJson(menu)))
        	.andExpect(status().isBadRequest())
        	.andExpect(jsonPath("$.code", is(ResponseWrapper.VALIDATION_ERROR)))
        	.andExpect(jsonPath("$.messages", not(empty())));
    }

    @Test
    public void shouldNotAddDuplicateMenu() throws Exception
    {
    	MenuDto menu = new MenuDto();
    	menu.setDate(LocalDate.now());
        mvc().perform(post("/api/v1/settings/restaurant/{restaurantId}/menu", RESTAURANT_ID)
        		.with(httpBasic("admin","admin")).contentType(MediaType.APPLICATION_JSON).content(toJson(menu)))
        	.andExpect(status().isBadRequest())
        	.andExpect(jsonPath("$.code", is(ResponseWrapper.GENERAL_ERROR)))
        	.andExpect(jsonPath("$.messages", not(empty())));
    }

    @Test
    public void shouldUpdateMenu() throws Exception
    {
    	MenuDto menu = new MenuDto();
    	menu.setDate(LocalDate.now());
    	menu.setName("temporal");
    	MenuItemDto itemDto = new MenuItemDto();
    	itemDto.setName(RandomStringUtils.random(DEFAULT_STING_LEN));
    	itemDto.setPrice(0.95);
    	menu.getItems().add(itemDto);
        mvc().perform(put("/api/v1/settings/restaurant/{restaurantId}/menu/{date}", RESTAURANT_ID, menu.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
        		.with(httpBasic("admin","admin")).contentType(MediaType.APPLICATION_JSON).content(toJson(menu)))
        	 .andExpect(status().isOk())
             .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
             .andExpect(jsonPath("$.data.name", is(menu.getName())))
             .andExpect(jsonPath("$.data.items", hasSize(menu.getItems().size())))
             .andExpect(jsonPath("$.data.items[0].name", is(itemDto.getName())))
             .andExpect(jsonPath("$.data.items[0].price", is(itemDto.getPrice())));
       	 checkDbMenu(RESTAURANT_ID, menu);
    }
    
    @Test
    public void shouldValidateEditMenu() throws Exception
    {
    	MenuDto menu = new MenuDto();
    	menu.setDate(LocalDate.now());
    	MenuItemDto itemDto = new MenuItemDto();
    	menu.getItems().add(itemDto);
        mvc().perform(put("/api/v1/settings/restaurant/{restaurantId}/menu/{date}", RESTAURANT_ID, menu.getDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
        		.with(httpBasic("admin","admin")).contentType(MediaType.APPLICATION_JSON).content(toJson(menu)))
        	.andExpect(status().isBadRequest())
        	.andExpect(jsonPath("$.code", is(ResponseWrapper.VALIDATION_ERROR)))
        	.andExpect(jsonPath("$.messages", not(empty())));
    }

	@Test
	public void shouldDeleteMenu() throws Exception {
		LocalDate current = LocalDate.now();
		mvc().perform(delete("/api/v1/settings/restaurant/{restaurantId}/menu/{date}", RESTAURANT_ID, current.format(DateTimeFormatter.ofPattern("yyyyMMdd")))
					.with(httpBasic("admin","admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
				.andExpect(jsonPath("$.data", nullValue()));
		assertFalse(repository.findByRestaurantIdAndDate(RESTAURANT_ID, current).isPresent());
	}

	@Test
	public void shouldSecurityDenyMenu() throws Exception {
        mvc().perform(get("/api/v1/settings/restaurant/{restaurantId}/menu/{date}", RESTAURANT_ID, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void shouldSecurityDenyWrongMenu() throws Exception {
        mvc().perform(get("/api/v1/settings/restaurant/{restaurantId}/menu/{date}", RESTAURANT_ID, LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
        		.with(httpBasic("system","system")))
				.andExpect(status().isForbidden());
	}

    private void checkDbMenu(Long restaurantId, MenuDto menu) {
    	Optional<MenuEntity> dbMenu = repository.findByRestaurantIdAndDate(restaurantId, menu.getDate());
    	assertTrue(dbMenu.isPresent());
    	assertEquals(menu.getName(), dbMenu.get().getName());
    	assertEquals(menu.getDate(), dbMenu.get().getDate());
    	assertEquals(menu.getItems().size(), dbMenu.get().getItems().size());
    }
}

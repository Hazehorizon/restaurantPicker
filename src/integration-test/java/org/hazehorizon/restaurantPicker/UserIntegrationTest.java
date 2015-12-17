package org.hazehorizon.restaurantPicker;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
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

import java.util.Optional;

import org.hazehorizon.restaurantPicker.admin.rest.dto.UserDto;
import org.hazehorizon.restaurantPicker.common.rest.ResponseWrapper;
import org.hazehorizon.restaurantPicker.model.UserEntity;
import org.hazehorizon.restaurantPicker.model.repository.UserRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class UserIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private UserRepository repository;

    @Test
    public void shouldReturnSystemUser() throws Exception
    {
        mvc().perform(get("/api/v1/system/user/{login}", SYSTEM_LOGIN).with(httpBasic("system","system")))
        	 .andExpect(status().isOk())
             .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
             .andExpect(jsonPath("$.data.id", is(SYSTEM_ID)))
             .andExpect(jsonPath("$.data.login", is(SYSTEM_LOGIN)))
             .andExpect(jsonPath("$.data.active", is(true)))
             .andExpect(jsonPath("$.data.roles", contains(SYSTEM_ROLE)));
    }
    
    @Test
    public void shouldReturnAdminUser() throws Exception
    {
        mvc().perform(get("/api/v1/system/user/{login}", ADMIN_LOGIN).with(httpBasic("system","system")))
        	.andExpect(status().isOk())
             .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
             .andExpect(jsonPath("$.data.id", is(ADMIN_ID)))
             .andExpect(jsonPath("$.data.login", is(ADMIN_LOGIN)))
             .andExpect(jsonPath("$.data.active", is(true)))
             .andExpect(jsonPath("$.data.roles", contains(ADMIN_ROLE)));
    }

    @Test
    public void shouldAddUser() throws Exception
    {
    	UserDto user = new UserDto();
    	user.setLogin("test");
    	user.setPasswd("test");
    	user.setActive(true);
    	user.getRoles().add(ADMIN_ROLE);
    	user.getRoles().add(SYSTEM_ROLE);
        mvc().perform(post("/api/v1/system/user").with(httpBasic("system","system")).contentType(MediaType.APPLICATION_JSON).content(toJson(user)))
        	 .andExpect(status().isOk())
             .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
             .andExpect(jsonPath("$.data.id", notNullValue()))
             .andExpect(jsonPath("$.data.login", is(user.getLogin())))
             .andExpect(jsonPath("$.data.active", is(true)))
             .andExpect(jsonPath("$.data.roles", containsInAnyOrder(ADMIN_ROLE, SYSTEM_ROLE)));
        checkDbUser(user);
    }

    @Test
    public void shouldUpdateUser() throws Exception
    {
    	UserDto user = new UserDto();
    	user.setLogin("admin");
    	user.setPasswd("test");
    	user.setActive(true);
    	user.getRoles().add(ADMIN_ROLE);
    	user.getRoles().add(SYSTEM_ROLE);
        mvc().perform(put("/api/v1/system/user/{login}", ADMIN_LOGIN).with(httpBasic("system","system")).contentType(MediaType.APPLICATION_JSON).content(toJson(user)))
        	 .andExpect(status().isOk())
             .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
             .andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
             .andExpect(jsonPath("$.data.id", notNullValue()))
             .andExpect(jsonPath("$.data.login", is(user.getLogin())))
             .andExpect(jsonPath("$.data.active", is(true)))
             .andExpect(jsonPath("$.data.roles", containsInAnyOrder(ADMIN_ROLE, SYSTEM_ROLE)));
        checkDbUser(user);
    }

	@Test
	public void shouldReadUsers() throws Exception {
		mvc().perform(get("/api/v1/system/users").with(httpBasic("system","system")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS))).andExpect(jsonPath("$.data", hasSize(2)));
	}

	@Test
	public void shouldDeleteUser() throws Exception {
		mvc().perform(delete("/api/v1/system/user/{login}", ADMIN_LOGIN).with(httpBasic("system","system")))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code", is(ResponseWrapper.SUCCESS)))
				.andExpect(jsonPath("$.data", nullValue()));
		Optional<UserEntity> dbUser = repository.findByLogin(ADMIN_LOGIN);
		assertFalse(dbUser.isPresent());
	}

	@Test
	public void shouldSecurityDenyUsers() throws Exception {
		mvc().perform(get("/api/v1/system/users"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void shouldSecurityDenyWrongUsers() throws Exception {
		mvc().perform(get("/api/v1/system/users").with(httpBasic("admin","admin")))
				.andExpect(status().isForbidden());
	}

    private void checkDbUser(UserDto user) {
    	Optional<UserEntity> dbUser = repository.findByLogin(user.getLogin());
    	assertTrue(dbUser.isPresent());
    	assertEquals(user.getLogin(), dbUser.get().getLogin());
    	assertEquals(user.getPasswd(), dbUser.get().getPasswd());
    	assertEquals(user.getActive(), dbUser.get().isActive());
    	assertEquals(user.getRoles().size(), dbUser.get().getRoles().size());    	
    }
}

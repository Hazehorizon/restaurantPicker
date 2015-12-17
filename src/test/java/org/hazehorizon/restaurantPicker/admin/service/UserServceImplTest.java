package org.hazehorizon.restaurantPicker.admin.service;

import static org.hazehorizon.restaurantPicker.TestUtils.randomId;
import static org.hazehorizon.restaurantPicker.TestUtils.randomString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomUtils;
import org.hazehorizon.restaurantPicker.model.RoleEntity;
import org.hazehorizon.restaurantPicker.model.UserEntity;
import org.hazehorizon.restaurantPicker.model.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServceImplTest {
	@InjectMocks
	private UserServiceImpl testInstance;
	@Mock
	private UserRepository repository;
	
	@Test
	public void testSave() throws Exception
	{
		List<RoleEntity> roles = Stream.generate(()->createRole(null)).limit(RandomUtils.nextInt(0, 5)).collect(Collectors.toList());
		UserEntity user = new UserEntity();
		user.setRoles(roles);
		Map<String, Long> returnedEntities = new HashMap<>();
		
		when(repository.findRoleByCode(anyString())).then(i -> {
			String roleCode = i.getArgumentAt(0, String.class);
			RoleEntity role = createRole(roleCode);
			role.setId(randomId());
			returnedEntities.put(roleCode, role.getId());
			return Optional.of(role);
		});

		testInstance.save(user);
		roles.stream().forEach(r -> assertEquals(returnedEntities.get(r.getCode()) , r.getId()));		
	}
	
	private RoleEntity createRole(String code) {
		RoleEntity role = new RoleEntity();
		role.setCode(null == code ? randomString() : code);
		return role;
	}
}

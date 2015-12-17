package org.hazehorizon.restaurantPicker.settings.rest;

import static org.hazehorizon.restaurantPicker.TestUtils.randomId;
import static org.hazehorizon.restaurantPicker.TestUtils.randomString;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.apache.commons.lang3.RandomUtils;
import org.hazehorizon.restaurantPicker.model.MenuEntity;
import org.hazehorizon.restaurantPicker.settings.rest.dto.MenuDto;
import org.hazehorizon.restaurantPicker.settings.rest.dto.MenuItemDto;
import org.hazehorizon.restaurantPicker.settings.service.MenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MenuControllerTest {
	@InjectMocks
	private MenuController testInstance;
	@Mock
	private MenuService service;
	
	@Test
	public void testCreateMenu() throws Exception {
		MenuDto dto = createDto();
		Long restaurantId = randomId();
		
		when(service.save(any(MenuEntity.class))).then(i -> {
			MenuEntity menu = i.getArgumentAt(0, MenuEntity.class);
			menu.setId(randomId());
			return menu;
		});
		
		testInstance.createMenu(restaurantId, dto);
		
		ArgumentCaptor<MenuEntity> menuCapturer = ArgumentCaptor.forClass(MenuEntity.class);
		verify(service).save(menuCapturer.capture());
		verifyNoMoreInteractions(service);
		
		checkMenu(dto, restaurantId, menuCapturer.getValue());
	}
	
	@Test
	public void testUpdateMenu() throws Exception {
		MenuDto dto = createDto();
		Long restaurantId = randomId();
		MenuEntity menu = new MenuEntity();
		menu.setId(randomId());
		menu.setRestaurantId(restaurantId);
		
		when(service.read(restaurantId, dto.getDate())).thenReturn(menu);
		when(service.save(any(MenuEntity.class))).then(i -> i.getArgumentAt(0, MenuEntity.class));
		
		testInstance.updateMenu(restaurantId, dto.getDate(), dto);
		
		verify(service).save(menu);
		
		checkMenu(dto, restaurantId, menu);
	}
	
	private void checkMenu(MenuDto dto, Long restaurantId, MenuEntity entity) {
		assertEquals(dto.getDate(), entity.getDate());
		assertEquals(dto.getName(), entity.getName());
		assertEquals(restaurantId, entity.getRestaurantId());
		assertEquals(dto.getItems().size(), entity.getItems().size());
		assertEquals(dto.getItems().get(0).getName(), entity.getItems().get(0).getName());
		assertEquals(dto.getItems().get(0).getPrice(), entity.getItems().get(0).getPrice());
		
	}
	
	private MenuDto createDto() {
		MenuDto dto = new MenuDto();
		dto.setName(randomString());
		dto.setDate(LocalDate.now().withDayOfYear(RandomUtils.nextInt(0, 360)));
		MenuItemDto itemDto = new MenuItemDto();
		itemDto.setName(randomString());
		itemDto.setPrice(RandomUtils.nextDouble(0, 100));
		dto.getItems().add(itemDto);
		return dto;
	}
}

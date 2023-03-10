package com.example.demo.controller;

import static com.example.demo.TestUtils.createItem;
import static com.example.demo.TestUtils.createItems;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTest {

	@InjectMocks
	private ItemController itemController;

	@Mock
	private ItemRepository itemRepository;

	@Before
	public void setup() {

		when(itemRepository.findById(1L)).thenReturn(Optional.of(createItem(1)));
		when(itemRepository.findAll()).thenReturn(createItems());
		when(itemRepository.findByName("item")).thenReturn(Arrays.asList(createItem(1), createItem(2)));

	}

	@Test
	public void test_getItems() {
		ResponseEntity<List<Item>> response = itemController.getItems();

		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		List<Item> items = response.getBody();

		assertEquals(createItems(), items);

		verify(itemRepository, times(1)).findAll();
	}

	@Test
	public void test_getItemById() {

		ResponseEntity<Item> response = itemController.getItemById(1L);
		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());

		Item item = response.getBody();
		assertEquals(createItem(1L), item);

		verify(itemRepository, times(1)).findById(1L);
	}

	@Test
	public void test_getItemByIdInvalid() {

		ResponseEntity<Item> response = itemController.getItemById(10L);
		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());

		assertNull(response.getBody());
		verify(itemRepository, times(1)).findById(10L);
	}

	@Test
	public void test_getItemByName() {
		ResponseEntity<List<Item>> response = itemController.getItemsByName("item");

		assertNotNull(response);
		assertEquals(200, response.getStatusCodeValue());
		List<Item> items = Arrays.asList(createItem(1), createItem(2));

		assertEquals(createItems(), items);

		verify(itemRepository, times(1)).findByName("item");
	}

	@Test
	public void test_getItemByNameInvalid() {
		ResponseEntity<List<Item>> response = itemController.getItemsByName("invalid name");

		assertNotNull(response);
		assertEquals(404, response.getStatusCodeValue());

		assertNull(response.getBody());

		verify(itemRepository, times(1)).findByName("invalid name");
	}
}
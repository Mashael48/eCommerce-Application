package com.example.demo.controllers;

import static com.example.demo.common.Constants.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/item")
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		log.info(LOG_FORMAT, "getItems", SUCCESS);
		return ResponseEntity.ok(itemRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		return ResponseEntity.of(itemRepository.findById(id));
	}

	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);

		if (items == null || items.isEmpty()) {
			log.error(LOG_FORMAT, "getItemsByName", FAIL, name);
			return ResponseEntity.notFound().build();
		}

		log.info(LOG_FORMAT, "getItemsByName", SUCCESS, name);
		return ResponseEntity.ok(items);

	}

}

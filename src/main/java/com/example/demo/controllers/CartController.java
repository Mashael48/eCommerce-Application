package com.example.demo.controllers;

import static com.example.demo.common.Constants.*;

import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.persistence.*;
import com.example.demo.model.persistence.repositories.*;
import com.example.demo.model.requests.ModifyCartRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/cart")
public class CartController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ItemRepository itemRepository;

	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {

		User user = userRepository.findByUsername(request.getUsername());
		if (user == null) {
			log.error(LOG_FORMAT, "addTocart", FAIL, request);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Optional<Item> item = itemRepository.findById(request.getItemId());
		if (!item.isPresent()) {
			log.error(LOG_FORMAT, "addTocart", FAIL, request);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity()).forEach(i -> cart.addItem(item.get()));
		cartRepository.save(cart);

		log.info(LOG_FORMAT, "addTocart", SUCCESS, request);
		return ResponseEntity.ok(cart);
	}

	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {

		User user = userRepository.findByUsername(request.getUsername());
		if (user == null) {
			log.error(LOG_FORMAT, "removeFromcart", FAIL, request);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Optional<Item> item = itemRepository.findById(request.getItemId());
		if (!item.isPresent()) {
			log.error(LOG_FORMAT, "removeFromcart", FAIL, request);
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity()).forEach(i -> cart.removeItem(item.get()));
		cartRepository.save(cart);

		log.info(LOG_FORMAT, "removeFromcart", SUCCESS, request);
		return ResponseEntity.ok(cart);
	}

}

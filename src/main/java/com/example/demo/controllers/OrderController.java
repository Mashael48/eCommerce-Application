package com.example.demo.controllers;

import static com.example.demo.common.Constants.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {

		User user = userRepository.findByUsername(username);
		if (user == null) {
			log.error(LOG_FORMAT, "submit", FAIL, username);
			return ResponseEntity.notFound().build();
		}

		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);

		log.info(LOG_FORMAT, "submit", SUCCESS, username);
		return ResponseEntity.ok(order);
	}

	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {

		User user = userRepository.findByUsername(username);
		if (user == null) {
			log.error(LOG_FORMAT, "getOrdersForUser", FAIL, username);
			return ResponseEntity.notFound().build();
		}

		log.info(LOG_FORMAT, "getOrdersForUser", SUCCESS, username);
		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}

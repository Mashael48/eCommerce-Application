package com.example.demo.controllers;

import static com.example.demo.common.Constants.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {

		User user = userRepository.findByUsername(username);

		if (user == null) {
			log.error(LOG_FORMAT, "findByUserName", FAIL, username);
			return ResponseEntity.notFound().build();
		}

		log.info(LOG_FORMAT, "findByUserName", SUCCESS, username);
		return ResponseEntity.ok(user);
	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();
		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);

		if (createUserRequest.getPassword().length() < 7
				|| !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())) {
			log.error(LOG_FORMAT, "createUser", FAIL, createUserRequest);
			return ResponseEntity.badRequest().build();
		}

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		userRepository.save(user);

		log.info(LOG_FORMAT, "createUser", SUCCESS, createUserRequest);
		return ResponseEntity.ok(user);
	}

}

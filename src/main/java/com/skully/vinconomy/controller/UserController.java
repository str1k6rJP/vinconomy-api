package com.skully.vinconomy.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.skully.vinconomy.model.ApiUser;
import com.skully.vinconomy.model.dto.UserRegistration;
import com.skully.vinconomy.service.UserService;

@RestController()
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping("/register")
	public ApiUser registerUser(@RequestBody() UserRegistration user) {
		
		if (StringUtils.isBlank(user.getUsername())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be blank");
		}
		
		if (StringUtils.isBlank(user.getPassword())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password cannot be blank");
		}
		
		if (user.getPassword().length() < 12) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be atleast 12 characters");
		}
		
		if (StringUtils.isBlank(user.getUuid())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UUID cannot be blank");
		}
		
		if (StringUtils.isBlank(user.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name cannot be blank");
		}
		
		return userService.registerAPIUser(user);
	}
		
	@GetMapping()
	//TODO: Disable after testing. Exposes username/roles
	public Authentication getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth;
	}
	
}

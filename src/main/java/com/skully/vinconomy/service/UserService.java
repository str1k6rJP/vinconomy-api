package com.skully.vinconomy.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skully.vinconomy.dao.ApiUserRepository;
import com.skully.vinconomy.enums.ApiRole;
import com.skully.vinconomy.model.ApiUser;
import com.skully.vinconomy.model.dto.UserRegistration;

@Service
public class UserService {

	Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	ApiUserRepository apiRepo;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
	
    public ApiUser saveUser(ApiUser user) {
    	return apiRepo.save(user);
    }
    
	public ApiUser registerAPIUser(UserRegistration reg) {
		return registerAPIUser(reg.getUsername(), reg.getPassword(), ApiRole.USER, reg.getName(), reg.getUuid());
	}
	
	public ApiUser registerAPIUser(String username, String password, ApiRole role, String name, String uuid) {
		if (StringUtils.isBlank(username)) {
			throw new IllegalArgumentException("Username cannot be blank");
		}
		
		if (StringUtils.isBlank(password)) {
			throw new IllegalArgumentException("Password cannot be blank");
		}
		
		List<ApiUser> existingUsers = apiRepo.findByUsernameOrUUID(username, uuid);
		if (existingUsers.size() > 0) {
			ApiUser existingUser = existingUsers.get(0);
			if (existingUser.getUsername().equalsIgnoreCase(username))
				throw new IllegalArgumentException("Username '" + username + "' already taken");
			else
				throw new IllegalArgumentException("UUID '" + uuid + "' already register to a different account");
		}
		
		
		ApiUser user = new ApiUser();
		user.setUsername(username.toLowerCase());
		String encodedPassword = passwordEncoder.encode(password);
		user.setPassword(encodedPassword);
		user.setRole(role);
		user.setName(name);
		user.setUuid(uuid);
		
		user = apiRepo.save(user);
		logger.info("Registered new user: " + user.getUsername());
		return user;
	}
	
	public ApiUser getApiUser(String username) {
		return apiRepo.findByUsername(username.toLowerCase());
	}
	
}

package com.skully.vinconomy.model.dto;

import com.skully.vinconomy.model.ApiUser;

public class UserDTO {
	private String username;
	private String name;
	private String uuid;
	
	
	public UserDTO(ApiUser user) {
		username = user.getUsername();
		name = user.getName();
		uuid = user.getUuid();
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	

}

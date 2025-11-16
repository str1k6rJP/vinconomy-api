package com.skully.vinconomy.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skully.vinconomy.enums.ApiRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ApiUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(unique=true)
	private String username;
	@Enumerated(EnumType.STRING)
	private ApiRole role;
	private String name;
	private String uuid;
	@JsonIgnore
	private String password;
	private boolean disabled;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public ApiRole getRole() {
		return role;
	}
	public void setRole(ApiRole role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	@Override
	public int hashCode() {
		return Objects.hash(disabled, id, name, password, role, username, uuid);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApiUser other = (ApiUser) obj;
		return disabled == other.disabled && id == other.id && Objects.equals(name, other.name)
				&& Objects.equals(password, other.password) && role == other.role
				&& Objects.equals(username, other.username) && Objects.equals(uuid, other.uuid);
	}
	
	
	
}

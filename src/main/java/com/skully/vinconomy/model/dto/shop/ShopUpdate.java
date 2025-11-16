package com.skully.vinconomy.model.dto.shop;

import java.util.List;

public class ShopUpdate {
	public int id;
	
	public String name;
	public String owner;
	public String description;
	
	public boolean removeAll;
	public List<ShopStall> stalls;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<ShopStall> getStalls() {
		return stalls;
	}
	public void setStalls(List<ShopStall> stalls) {
		this.stalls = stalls;
	}
	public boolean isRemoveAll() {
		return removeAll;
	}
	public void setRemoveAll(boolean removeAll) {
		this.removeAll = removeAll;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}

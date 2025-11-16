package com.skully.vinconomy.model.dto.tradenetwork;

import com.skully.vinconomy.enums.AsyncType;
import com.skully.vinconomy.model.TradeNetwork;
import com.skully.vinconomy.model.dto.UserDTO;


public class TradeNetworkDTO {

	private long id;
	private String name;
	private UserDTO owner;
	private String description;
	private String networkAccessKey;
	private boolean autoAcceptRequests;
	private boolean visible;
	private boolean moddedItemsAllowed;
	private AsyncType asyncType;
	
	public TradeNetworkDTO(TradeNetwork t) {
		id = t.getId();
		name = t.getName();
		owner = new UserDTO(t.getOwner());
		description = t.getDescription();
		networkAccessKey = t.getNetworkAccessKey();
		autoAcceptRequests = t.isAutoAcceptRequests();
		visible = t.isVisible();
		moddedItemsAllowed = t.isModdedItemsAllowed();
		asyncType = t.getAsyncType();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserDTO getOwner() {
		return owner;
	}

	public void setOwner(UserDTO owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNetworkAccessKey() {
		return networkAccessKey;
	}

	public void setNetworkAccessKey(String networkAccessKey) {
		this.networkAccessKey = networkAccessKey;
	}

	public boolean isAutoAcceptRequests() {
		return autoAcceptRequests;
	}

	public void setAutoAcceptRequests(boolean autoAcceptRequests) {
		this.autoAcceptRequests = autoAcceptRequests;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isModdedItemsAllowed() {
		return moddedItemsAllowed;
	}

	public void setModdedItemsAllowed(boolean moddedItemsAllowed) {
		this.moddedItemsAllowed = moddedItemsAllowed;
	}

	public AsyncType getAsyncType() {
		return asyncType;
	}

	public void setAsyncType(AsyncType asyncType) {
		this.asyncType = asyncType;
	}
	
	
}

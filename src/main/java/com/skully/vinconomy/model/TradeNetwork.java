package com.skully.vinconomy.model;

import com.skully.vinconomy.enums.AsyncType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class TradeNetwork {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String name;
	
	@ManyToOne
	private ApiUser owner;
	
	private String description;
	
	@Column(unique=true)
	private String networkAccessKey;
	
	private boolean autoAcceptRequests;
	
	private boolean visible;
	
	private boolean moddedItemsAllowed;
	
	@Enumerated(EnumType.STRING)
	private AsyncType asyncType;

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

	public ApiUser getOwner() {
		return owner;
	}

	public void setOwner(ApiUser owner) {
		this.owner = owner;
	}

	public boolean isAutoAcceptRequests() {
		return autoAcceptRequests;
	}

	public void setAutoAcceptRequests(boolean autoAcceptRequests) {
		this.autoAcceptRequests = autoAcceptRequests;
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

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public AsyncType getAsyncType() {
		return asyncType;
	}

	public void setAsyncType(AsyncType asyncType) {
		this.asyncType = asyncType;
	}

	public boolean isModdedItemsAllowed() {
		return moddedItemsAllowed;
	}

	public void setModdedItemsAllowed(boolean moddedItemsAllowed) {
		this.moddedItemsAllowed = moddedItemsAllowed;
	}
	
	
	
}

package com.skully.vinconomy.model.dto.tradenetwork;

import java.sql.Timestamp;

import com.skully.vinconomy.enums.RequestStatus;
import com.skully.vinconomy.model.TradeNetworkRequest;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class TradeNetworkRequestJoinStatus {
	private String serverName;
	private String guid;
	private String hostname;
	private String ip;
	private String owner;
	@Enumerated(EnumType.STRING)
	private RequestStatus status;
	private String message;
	private Timestamp createdAt;
	private Timestamp lastModified;
	private Timestamp lastAccessed;
	
	
	public TradeNetworkRequestJoinStatus(String serverName, String guid, String hostname, String ip, String owner,
			RequestStatus status, String message, Timestamp createdAt, Timestamp lastModified, Timestamp lastAccessed) {
		this.serverName = serverName;
		this.guid = guid;
		this.hostname = hostname;
		this.ip = ip;
		this.owner = owner;
		this.status = status;
		this.message = message;
		this.createdAt = createdAt;
		this.lastModified = lastModified;
		this.lastAccessed = lastAccessed;
	}
	
	public TradeNetworkRequestJoinStatus(TradeNetworkRequest req) {
		this.serverName = req.getNode().getServerName();
		this.guid = req.getNode().getGuid();
		this.hostname = req.getNode().getHostname();
		this.ip = req.getNode().getIp();
		this.owner = req.getNode().getOwner();
		this.status = req.getStatus();
		this.message = req.getMessage();
		this.createdAt = req.getCreatedAt();
		this.lastModified = req.getLastModified();
		this.lastAccessed = req.getNode().getLastAccessed();
	}
	
	public TradeNetworkRequestJoinStatus(){}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public Timestamp getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(Timestamp lastAccessed) {
		this.lastAccessed = lastAccessed;
	}
	
	
	
}

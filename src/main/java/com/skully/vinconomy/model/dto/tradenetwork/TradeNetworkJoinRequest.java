package com.skully.vinconomy.model.dto.tradenetwork;

public class TradeNetworkJoinRequest {
	private String networkAccessKey;
	private Long networkId;
	
	private String serverName;
	private String guid; // Vintage Story Save Game UUID
	private String udpPort;
	
	private String username;
	private String password;
	
	public Long getNetworkId() {
		return networkId;
	}
	public void setNetworkId(Long networkId) {
		this.networkId = networkId;
	}
	public String getNetworkAccessKey() {
		return networkAccessKey;
	}
	public void setNetworkAccessKey(String networkAccessKey) {
		this.networkAccessKey = networkAccessKey;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getUdpPort() {
		return udpPort;
	}
	public void setUdpPort(String udpPort) {
		this.udpPort = udpPort;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}

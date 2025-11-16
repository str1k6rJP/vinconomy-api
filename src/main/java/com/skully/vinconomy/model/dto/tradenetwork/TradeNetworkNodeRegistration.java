package com.skully.vinconomy.model.dto.tradenetwork;

import java.io.Serializable;

public class TradeNetworkNodeRegistration implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String guid;
	private String host;
	private String ip;
	private String username;
	private int udpListenPort;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public int getUdpListenPort() {
		return udpListenPort;
	}
	public void setUdpListenPort(int udpListenPort) {
		this.udpListenPort = udpListenPort;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	
}

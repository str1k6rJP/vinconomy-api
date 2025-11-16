package com.skully.vinconomy.model.dto.tradenetwork;

import com.skully.vinconomy.enums.RequestStatus;

public class TradeNetworkJoinResult {
	private String apiKey;
	private RequestStatus status;
	private String message;
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
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
	
	
}

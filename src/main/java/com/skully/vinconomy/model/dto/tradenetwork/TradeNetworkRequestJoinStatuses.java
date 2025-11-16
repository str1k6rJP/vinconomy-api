package com.skully.vinconomy.model.dto.tradenetwork;

import java.util.List;

public class TradeNetworkRequestJoinStatuses {

	TradeNetworkDTO network;
	List<TradeNetworkRequestJoinStatus> requests;
	
	public TradeNetworkDTO getNetwork() {
		return network;
	}
	public void setNetwork(TradeNetworkDTO network) {
		this.network = network;
	}
	public List<TradeNetworkRequestJoinStatus> getRequests() {
		return requests;
	}
	public void setRequests(List<TradeNetworkRequestJoinStatus> requests) {
		this.requests = requests;
	}
	
	
	
}

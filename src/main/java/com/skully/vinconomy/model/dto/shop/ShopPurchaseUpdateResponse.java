package com.skully.vinconomy.model.dto.shop;

import com.skully.vinconomy.model.ShopTrade;

public class ShopPurchaseUpdateResponse {
	ShopPurchaseUpdate update;
	String error;
	
	public ShopPurchaseUpdateResponse() {}
	
	public ShopPurchaseUpdateResponse(ShopPurchaseUpdate update) {
		this.update = update;
	}
	
	public ShopPurchaseUpdate getUpdate() {
		return update;
	}
	public void setUpdate(ShopPurchaseUpdate update) {
		this.update = update;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}

	public void updatePurchase(ShopTrade trade) {
		if (update != null)
			update.setStatus(trade.getStatus());
	}
	
	
}

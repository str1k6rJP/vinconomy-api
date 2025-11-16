package com.skully.vinconomy.model.dto.tradenetwork;

import java.util.List;

import com.skully.vinconomy.model.ShopProduct;

public class TradeNetworkShop {
	public int id;
	public String nodeId;
	public String name;
	public String serverName;
	public String owner;
	public List<ShopProduct> products;
	public long lastUpdatedTimestamp;
}

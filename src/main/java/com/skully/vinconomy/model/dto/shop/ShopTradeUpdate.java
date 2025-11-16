package com.skully.vinconomy.model.dto.shop;

import java.sql.Timestamp;

import com.skully.vinconomy.enums.TradeStatus;


public class ShopTradeUpdate {
	private long id;
	private long shopId;
	private int x;
	private int y;
	private int z;
	private int stallSlot;
	private TradeStatus status;
	private String requestingNode;
	private String originNode;


	private String name;
	private String playerGuid;
	
	private int amount;

	private Timestamp created;
	private Timestamp modified;
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getShopId() {
		return shopId;
	}
	public void setShopId(long shopId) {
		this.shopId = shopId;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	public int getStallSlot() {
		return stallSlot;
	}
	public void setStallSlot(int stallSlot) {
		this.stallSlot = stallSlot;
	}
	public TradeStatus getStatus() {
		return status;
	}
	public void setStatus(TradeStatus status) {
		this.status = status;
	}
	public String getRequestingNode() {
		return requestingNode;
	}
	public void setRequestingNode(String requestingNode) {
		this.requestingNode = requestingNode;
	}
	public String getOriginNode() {
		return originNode;
	}
	public void setOriginNode(String originNode) {
		this.originNode = originNode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPlayerGuid() {
		return playerGuid;
	}
	public void setPlayerGuid(String playerGuid) {
		this.playerGuid = playerGuid;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Timestamp getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public Timestamp getModified() {
		return modified;
	}
	public void setModified(Timestamp modified) {
		this.modified = modified;
	}


	
}

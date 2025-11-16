package com.skully.vinconomy.model.dto.shop;

import com.skully.vinconomy.enums.TradeStatus;

public class ShopPurchaseUpdate {
	private String nodeGuid;
	private long shopId;
	private int amount;
	private int X;
	private int Y;
	private int Z;
	private int stallSlot;
	private String name;
	private String playerGuid;
	private TradeStatus status;
	
	public String getNodeGuid() {
		return nodeGuid;
	}
	public void setNodeId(String nodeId) {
		this.nodeGuid = nodeId;
	}
	public long getShopId() {
		return shopId;
	}
	public void setShopId(long shopId) {
		this.shopId = shopId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	public int getZ() {
		return Z;
	}
	public void setZ(int z) {
		Z = z;
	}
	public int getStallSlot() {
		return stallSlot;
	}
	public void setStallSlot(int stallSlot) {
		this.stallSlot = stallSlot;
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
	public TradeStatus getStatus() {
		return status;
	}
	public void setStatus(TradeStatus status) {
		this.status = status;
	}
	
}

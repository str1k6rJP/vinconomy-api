package com.skully.vinconomy.model;

import java.sql.Timestamp;

import com.skully.vinconomy.enums.TradeStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class ShopTrade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Enumerated(EnumType.STRING)
	private TradeStatus status;
	@ManyToOne
	private Shop shop;
	@ManyToOne
	private TradeNetworkNode requestingNode; // The network node that requested the trade
	@ManyToOne
	private TradeNetworkNode originNode; // The network node that the requestingNode is buying from
	private int x;
	private int y;
	private int z;
	private int stallSlot;

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
	public TradeStatus getStatus() {
		return status;
	}
	public void setStatus(TradeStatus status) {
		this.status = status;
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
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
	public TradeNetworkNode getRequestingNode() {
		return requestingNode;
	}
	public void setRequestingNode(TradeNetworkNode requestingNode) {
		this.requestingNode = requestingNode;
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
	public TradeNetworkNode getOriginNode() {
		return originNode;
	}
	public void setOriginNode(TradeNetworkNode originNode) {
		this.originNode = originNode;
	}
	
}

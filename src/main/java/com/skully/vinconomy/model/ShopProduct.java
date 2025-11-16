package com.skully.vinconomy.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class ShopProduct {

	@EmbeddedId
	private ShopProductId id;
	private String productName;
	private String productCode;
	private int productQuantity;
    @Lob
    @Column(length = 65535)
	private String productAttributes;
	
	private String currencyName;
	private String currencyCode;
	private int currencyQuantity;
    @Lob
    @Column(length = 65535)
	private String currencyAttributes;
	
	private int totalStock;
	
	@ManyToOne
	@JoinColumns({
	     @JoinColumn(name="node_id", referencedColumnName="node_id", insertable = false, updatable = false),
	     @JoinColumn(name="shop_id", referencedColumnName="shop_id", insertable = false, updatable = false)
	})
	private Shop shop;

	public ShopProductId getId() {
		return id;
	}

	public void setId(ShopProductId id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public int getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(int productQuantity) {
		this.productQuantity = productQuantity;
	}

	public String getProductAttributes() {
		return productAttributes;
	}

	public void setProductAttributes(String productAttributes) {
		this.productAttributes = productAttributes;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public int getCurrencyQuantity() {
		return currencyQuantity;
	}

	public void setCurrencyQuantity(int currencyQuantity) {
		this.currencyQuantity = currencyQuantity;
	}

	public String getCurrencyAttributes() {
		return currencyAttributes;
	}

	public void setCurrencyAttributes(String currencyAttributes) {
		this.currencyAttributes = currencyAttributes;
	}

	public int getTotalStock() {
		return totalStock;
	}

	public void setTotalStock(int totalStock) {
		this.totalStock = totalStock;
	}	
	
	@Override
	public String toString() {
		return id.toString();
	}
}

package com.skully.vinconomy.model.dto.shop;

import java.util.List;

import com.skully.vinconomy.model.dto.Product;

public class ShopStall {
	
	private int x;
	private int y;
	private int z;
	private boolean removeAll;
	private List<Product> products;
	
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
	public boolean isRemoveAll() {
		return removeAll;
	}
	public void setRemoveAll(boolean removeAll) {
		this.removeAll = removeAll;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	
}

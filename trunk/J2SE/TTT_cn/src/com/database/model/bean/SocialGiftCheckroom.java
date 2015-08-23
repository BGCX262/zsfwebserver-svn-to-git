package com.database.model.bean;

public class SocialGiftCheckroom {
	private long id;
	private long master;
	private int goods;
	private int num;
	private boolean validity;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMaster() {
		return master;
	}
	public void setMaster(long master) {
		this.master = master;
	}
	public int getGoods() {
		return goods;
	}
	public void setGoods(int goods) {
		this.goods = goods;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public boolean isValidity() {
		return validity;
	}
	public void setValidity(boolean validity) {
		this.validity = validity;
	}
	
}

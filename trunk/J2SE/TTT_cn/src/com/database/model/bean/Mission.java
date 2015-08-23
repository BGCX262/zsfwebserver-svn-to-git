package com.database.model.bean;

import java.io.Serializable;

public class Mission implements Serializable{
	private static final long serialVersionUID = 3029524781540696910L;
	private int id;
	private long masterID;
	private int product;//怪物goodID
	private long endTime;//完成时间
	private int num;
	
	public int getProduct() {
		return product;
	}
	public void setProduct(int product) {
		this.product = product;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getMasterID() {
		return masterID;
	}
	public void setMasterID(long masterID) {
		this.masterID = masterID;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
}

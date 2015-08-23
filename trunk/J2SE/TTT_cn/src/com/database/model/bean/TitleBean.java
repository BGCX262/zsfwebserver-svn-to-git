package com.database.model.bean;

public class TitleBean {
	private int id;
	private int goodID;
	private int cate;//1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 10道具 11称号
	private String title;
	private String resourcename;
	private int glory;//需要多少荣誉
	private int preTitle;//0不需要前置称号         其他需要前置称号
	private int slaverID;//奴隶ID
	private int coinCost;//被雇佣的花费
	private String comment;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getGlory() {
		return glory;
	}
	public void setGlory(int glory) {
		this.glory = glory;
	}
	public int getPreTitle() {
		return preTitle;
	}
	public void setPreTitle(int preTitle) {
		this.preTitle = preTitle;
	}
	public String getResourcename() {
		return resourcename;
	}
	public void setResourcename(String resourcename) {
		this.resourcename = resourcename;
	}
	public int getSlaverID() {
		return slaverID;
	}
	public void setSlaverID(int slaverID) {
		this.slaverID = slaverID;
	}
	public int getCate() {
		return cate;
	}
	public void setCate(int cate) {
		this.cate = cate;
	}
	public int getGoodID() {
		return goodID;
	}
	public void setGoodID(int goodID) {
		this.goodID = goodID;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getCoinCost() {
		return coinCost;
	}
	public void setCoinCost(int coinCost) {
		this.coinCost = coinCost;
	}
}

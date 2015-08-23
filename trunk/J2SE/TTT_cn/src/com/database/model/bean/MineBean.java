package com.database.model.bean;

public class MineBean {//矿场
	private int id;
	private int goodID;//150001 矿石矿  150002金属矿  150003水晶矿
	private int cate;
	private String name;
	private int level;
	private int product;//产品60001 rock  60002  metal   60003 crystal
	private int produce;//产量
	private int limitTime;//采集限制时间
	private int upgradeNeedCastleLevel;//升级所需城堡等级
	private String resourceName;
	private int upgradeTime;
	private int upgradeNeedRock;
	private int upgradeNeedMetal;
	private int upgradeNeedCrystal;
	private String comment;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGoodID() {
		return goodID;
	}
	public void setGoodID(int goodID) {
		this.goodID = goodID;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getProduct() {
		return product;
	}
	public void setProduct(int product) {
		this.product = product;
	}
	public int getProduce() {
		return produce;
	}
	public void setProduce(int produce) {
		this.produce = produce;
	}
	public int getUpgradeTime() {
		return upgradeTime;
	}
	public void setUpgradeTime(int upgradeTime) {
		this.upgradeTime = upgradeTime;
	}
	public int getUpgradeNeedRock() {
		return upgradeNeedRock;
	}
	public void setUpgradeNeedRock(int upgradeNeedRock) {
		this.upgradeNeedRock = upgradeNeedRock;
	}
	public int getUpgradeNeedMetal() {
		return upgradeNeedMetal;
	}
	public void setUpgradeNeedMetal(int upgradeNeedMetal) {
		this.upgradeNeedMetal = upgradeNeedMetal;
	}
	public int getUpgradeNeedCrystal() {
		return upgradeNeedCrystal;
	}
	public void setUpgradeNeedCrystal(int upgradeNeedCrystal) {
		this.upgradeNeedCrystal = upgradeNeedCrystal;
	}
	public int getCate() {
		return cate;
	}
	public void setCate(int cate) {
		this.cate = cate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getLimitTime() {
		return limitTime;
	}
	public void setLimitTime(int limitTime) {
		this.limitTime = limitTime;
	}
	public int getUpgradeNeedCastleLevel() {
		return upgradeNeedCastleLevel;
	}
	public void setUpgradeNeedCastleLevel(int upgradeNeedCastleLevel) {
		this.upgradeNeedCastleLevel = upgradeNeedCastleLevel;
	}
}

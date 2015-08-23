package com.database.model.bean;

public class SoulTowerBean {
	private int level;//等级
	private int goodID;//商品的goodID
	private int cate;
	private String name;
	private int queueNumLimit;//队列个数限制
	private int upgradeNeedCastleLevel;//升级所需城堡等级
	private int upgradeNeedCoin;//升级所需矿石
	private int upgradeNeedRock;//升级所需矿石
	private int upgradeNeedMetal;//升级所需金属
	private int upgradeNeedCrystal;//升级所需水晶
	private int upgradeNeedSoul;//升级所需灵魂
	private int upgradeTime;//升级时间
	private String resourceName;//资源
	private String comment;//说明
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
	public int getQueueNumLimit() {
		return queueNumLimit;
	}
	public void setQueueNumLimit(int queueNumLimit) {
		this.queueNumLimit = queueNumLimit;
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
	public int getUpgradeTime() {
		return upgradeTime;
	}
	public void setUpgradeTime(int upgradeTime) {
		this.upgradeTime = upgradeTime;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public int getUpgradeNeedSoul() {
		return upgradeNeedSoul;
	}
	public void setUpgradeNeedSoul(int upgradeNeedSoul) {
		this.upgradeNeedSoul = upgradeNeedSoul;
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
	public int getUpgradeNeedCastleLevel() {
		return upgradeNeedCastleLevel;
	}
	public void setUpgradeNeedCastleLevel(int upgradeNeedCastleLevel) {
		this.upgradeNeedCastleLevel = upgradeNeedCastleLevel;
	}
	public int getUpgradeNeedCoin() {
		return upgradeNeedCoin;
	}
	public void setUpgradeNeedCoin(int upgradeNeedCoin) {
		this.upgradeNeedCoin = upgradeNeedCoin;
	}
}

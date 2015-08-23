package com.database.model.bean;

public class PrizeBean {
	private int id;
	private int goodID;
	private int cityLevel;//可以掉落的关
	private double dropRate;//怪物掉落概率
	private double cityPrizeRate; //过关翻牌被翻到的概率
	private double taskPrizeRate;//日常任务被翻到概率
	private double systemCreateRate;//访问好友系统产生物品概率
	private int prizeNum;//奖励数量
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
	public int getCityLevel() {
		return cityLevel;
	}
	public void setCityLevel(int cityLevel) {
		this.cityLevel = cityLevel;
	}
	public double getDropRate() {
		return dropRate;
	}
	public void setDropRate(double dropRate) {
		this.dropRate = dropRate;
	}
	public double getCityPrizeRate() {
		return cityPrizeRate;
	}
	public void setCityPrizeRate(double cityPrizeRate) {
		this.cityPrizeRate = cityPrizeRate;
	}
	public double getTaskPrizeRate() {
		return taskPrizeRate;
	}
	public void setTaskPrizeRate(double taskPrizeRate) {
		this.taskPrizeRate = taskPrizeRate;
	}
	public double getSystemCreateRate() {
		return systemCreateRate;
	}
	public void setSystemCreateRate(double systemCreateRate) {
		this.systemCreateRate = systemCreateRate;
	}
	public int getPrizeNum() {
		return prizeNum;
	}
	public void setPrizeNum(int prizeNum) {
		this.prizeNum = prizeNum;
	}
}

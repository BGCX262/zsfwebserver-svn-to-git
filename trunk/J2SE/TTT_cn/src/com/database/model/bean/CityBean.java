package com.database.model.bean;

public class CityBean {//关
	private int level;//关等级
	private int goodID;//商品goodsID
	private int cate;//1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 10道具 11其他
	private String name;//关名称
	private int lastTimes;//最后一波的编号
	private String mapName;//地图编号
	private String towerBase;//可建塔处tile串
	private String movePath;//怪物路线转折点串
	private int resetNeedEnergy;//重置需要精力值
	private double goldMonsterRate;//刷到黄金怪的概率
	private int openNeedCash;//开启需要金币
	private int openNeedCoin;//开启需要金币
	private int openNeedRock;//开启需要石头
	private int openNeedMetal;//开启需要金属
	private int openNeedCrystal;//开启需要水晶
	private int openNeedCastleLev;//开启需要城堡等级
	private String comment;
	
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}
	public String getTowerBase() {
		return towerBase;
	}
	public void setTowerBase(String towerBase) {
		this.towerBase = towerBase;
	}
	public String getMovePath() {
		return movePath;
	}
	public void setMovePath(String movePath) {
		this.movePath = movePath;
	}
	public int getLastTimes() {
		return lastTimes;
	}
	public void setLastTimes(int lastTimes) {
		this.lastTimes = lastTimes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getGoodID() {
		return goodID;
	}
	public void setGoodID(int goodID) {
		this.goodID = goodID;
	}
	public int getCate() {
		return cate;
	}
	public void setCate(int cate) {
		this.cate = cate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getResetNeedEnergy() {
		return resetNeedEnergy;
	}
	public void setResetNeedEnergy(int resetNeedEnergy) {
		this.resetNeedEnergy = resetNeedEnergy;
	}
	public double getGoldMonsterRate() {
		return goldMonsterRate;
	}
	public void setGoldMonsterRate(double goldMonsterRate) {
		this.goldMonsterRate = goldMonsterRate;
	}
	public int getOpenNeedRock() {
		return openNeedRock;
	}
	public void setOpenNeedRock(int openNeedRock) {
		this.openNeedRock = openNeedRock;
	}
	public int getOpenNeedMetal() {
		return openNeedMetal;
	}
	public void setOpenNeedMetal(int openNeedMetal) {
		this.openNeedMetal = openNeedMetal;
	}
	public int getOpenNeedCrystal() {
		return openNeedCrystal;
	}
	public void setOpenNeedCrystal(int openNeedCrystal) {
		this.openNeedCrystal = openNeedCrystal;
	}
	public int getOpenNeedCastleLev() {
		return openNeedCastleLev;
	}
	public void setOpenNeedCastleLev(int openNeedCastleLev) {
		this.openNeedCastleLev = openNeedCastleLev;
	}
	public int getOpenNeedCoin() {
		return openNeedCoin;
	}
	public void setOpenNeedCoin(int openNeedCoin) {
		this.openNeedCoin = openNeedCoin;
	}
	public int getOpenNeedCash() {
		return openNeedCash;
	}
	public void setOpenNeedCash(int openNeedCash) {
		this.openNeedCash = openNeedCash;
	}
}

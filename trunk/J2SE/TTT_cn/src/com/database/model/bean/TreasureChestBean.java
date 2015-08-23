package com.database.model.bean;

public class TreasureChestBean {//宝箱
	private int id;
	private int goodID;//120001开宝箱 120002礼品盒
	private int level;//宝箱等级
	private boolean needKey;//是否需要钥匙
	private int keyID;//宝箱对应钥匙ID
	private double zeroGoods;//0件物品概率
	private double oneGoods;//1件物品概率
	private double twoGoods;//2件物品概率
	private double threeGoods;//3件物品概率
	private double normalGoods;//一般物品概率
	private double preciousGoods;//贵重物品概率
	private double rareGoods;//稀有物品概率
	private int time;//开宝箱需要时间
	private int cate;
	private int limitLevel;//礼包开启需求城堡等级
	private String name;
	private String resourceName;
	private String presents;//JSONArray格式：[[10002,2],[10003,5],[20001,10]]
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
	public String getPresents() {
		return presents;
	}
	public void setPresents(String presents) {
		this.presents = presents;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isNeedKey() {
		return needKey;
	}
	public void setNeedKey(boolean needKey) {
		this.needKey = needKey;
	}
	public double getZeroGoods() {
		return zeroGoods;
	}
	public void setZeroGoods(double zeroGoods) {
		this.zeroGoods = zeroGoods;
	}
	public double getOneGoods() {
		return oneGoods;
	}
	public void setOneGoods(double oneGoods) {
		this.oneGoods = oneGoods;
	}
	public double getTwoGoods() {
		return twoGoods;
	}
	public void setTwoGoods(double twoGoods) {
		this.twoGoods = twoGoods;
	}
	public double getThreeGoods() {
		return threeGoods;
	}
	public void setThreeGoods(double threeGoods) {
		this.threeGoods = threeGoods;
	}
	public double getNormalGoods() {
		return normalGoods;
	}
	public void setNormalGoods(double normalGoods) {
		this.normalGoods = normalGoods;
	}
	public double getPreciousGoods() {
		return preciousGoods;
	}
	public void setPreciousGoods(double preciousGoods) {
		this.preciousGoods = preciousGoods;
	}
	public double getRareGoods() {
		return rareGoods;
	}
	public void setRareGoods(double rareGoods) {
		this.rareGoods = rareGoods;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getLimitLevel() {
		return limitLevel;
	}
	public void setLimitLevel(int limitLevel) {
		this.limitLevel = limitLevel;
	}
	public int getKeyID() {
		return keyID;
	}
	public void setKeyID(int keyID) {
		this.keyID = keyID;
	}
}

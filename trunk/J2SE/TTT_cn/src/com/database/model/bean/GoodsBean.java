package com.database.model.bean;

public class GoodsBean {//商品总表
	private int id;//商品id
	private String name;//商品名称
	private int cate;//1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 10道具 11称号 12礼品盒 13任务 14灵魂塔 15矿场 16科技塔 17科技树 18促销 19活动
	private int buy;//1 不能购买    2普通购买   3vip购买
	private int level;//购买等级
	private int payment;//0点券 代金券   1点券  2代金券 3游戏币
	private double discount;//折扣
	private int score;//人气
	private int price;//价格
	private int coinPrice;//coin购买价格
	private int vipPrice;//vip购买价格
	private int sellPrice;//卖出价格
	private boolean hot;//是否热销
	private double online;//上线时间
	private double offline;//下线时间
	private String presents;//赠送的物品，JSONArray格式：[[10002,2],[10003,5],[20001,10]]

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCate() {
		return cate;
	}
	public void setCate(int cate) {
		this.cate = cate;
	}
	public int getBuy() {
		return buy;
	}
	public void setBuy(int buy) {
		this.buy = buy;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getPayment() {
		return payment;
	}
	public void setPayment(int payment) {
		this.payment = payment;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getVipPrice() {
		return vipPrice;
	}
	public void setVipPrice(int vipPrice) {
		this.vipPrice = vipPrice;
	}
	public int getSellPrice() {
		return sellPrice;
	}
	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}
	public boolean isHot() {
		return hot;
	}
	public void setHot(boolean hot) {
		this.hot = hot;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public String getPresents() {
		return presents;
	}
	public void setPresents(String presents) {
		this.presents = presents;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getCoinPrice() {
		return coinPrice;
	}
	public void setCoinPrice(int coinPrice) {
		this.coinPrice = coinPrice;
	}
	public double getOnline() {
		return online;
	}
	public void setOnline(double online) {
		this.online = online;
	}
	public double getOffline() {
		return offline;
	}
	public void setOffline(double offline) {
		this.offline = offline;
	}
}

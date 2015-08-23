package com.database.model.bean;

public class IndexBean {
	private int indexID;//goodsID  
	private String name;//名字
	private int cate;//1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 10道具 11称号 12礼品盒 13任务 14灵魂塔 15矿场 16科技塔 17科技树 18促销 19活动
	private boolean canPrize;//是否可以被翻牌
	private int prizeNum;//奖励数量
	private int pileNum;//堆叠数量
	private int level;//物品等级
	
	public int getIndexID() {
		return indexID;
	}
	public void setIndexID(int indexID) {
		this.indexID = indexID;
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
	public boolean isCanPrize() {
		return canPrize;
	}
	public void setCanPrize(boolean canPrize) {
		this.canPrize = canPrize;
	}
	public int getPrizeNum() {
		return prizeNum;
	}
	public void setPrizeNum(int prizeNum) {
		this.prizeNum = prizeNum;
	}
	public int getPileNum() {
		return pileNum;
	}
	public void setPileNum(int pileNum) {
		this.pileNum = pileNum;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}

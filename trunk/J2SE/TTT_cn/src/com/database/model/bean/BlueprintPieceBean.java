package com.database.model.bean;

public class BlueprintPieceBean {
	private int id;//图纸碎片id
	private int goodID;//商品goodsID
	private int cate;//1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 10道具 11其他
	private int race;//种族：1 元素要塞  2科技之都 3水晶城堡 
	private String name;//图纸碎片名称
	private int baseLevel;//炮塔初始等级
	private int type;//图纸碎片类型A B C D
	private double mixSuc;//合成概率
	private int mixerID;//合成后的图纸goodsID
	private String resourceName;//图纸碎片资源编号
	private String comment;
	private int color;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public double getMixSuc() {
		return mixSuc;
	}
	public void setMixSuc(double mixSuc) {
		this.mixSuc = mixSuc;
	}
	public int getMixerID() {
		return mixerID;
	}
	public void setMixerID(int mixerID) {
		this.mixerID = mixerID;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public int getRace() {
		return race;
	}
	public void setRace(int race) {
		this.race = race;
	}
	public int getBaseLevel() {
		return baseLevel;
	}
	public void setBaseLevel(int baseLevel) {
		this.baseLevel = baseLevel;
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
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
}

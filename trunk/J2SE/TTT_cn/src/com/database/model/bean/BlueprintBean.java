package com.database.model.bean;

public class BlueprintBean {// 图纸表

	private int id;// 图纸id
	private int goodID;// 商品goodsID
	private String name;// 图纸名称
	private int cate;// 1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 10道具 11其他
	private int race;// 种族：1 元素要塞 2科技之都 3水晶城堡
	private int baseLevel;// 炮塔初始等级
	private int level;// 图纸等级
	private String resourceName;// 图纸资源编号
	private int product;// 生成炮塔或怪物goodsID
	private int synthesizeSuccessID;// 合成成功后的goodID
	private int synthesizeFailedID;// 合成失败后的goodID
	private double basicRate;// 基础升级概率
	private double extraRate;// 额外材料概率单位
	private int basicID;// 需要的基础材料id
	private int basicNum;// 基础材料数量
	private int maxExtra;// 最大额外材料数量
	private int extraID;// 额外材料ID
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getRace() {
		return race;
	}

	public void setRace(int race) {
		this.race = race;
	}

	public int getProduct() {
		return product;
	}

	public void setProduct(int product) {
		this.product = product;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
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

	public int getSynthesizeSuccessID() {
		return synthesizeSuccessID;
	}

	public void setSynthesizeSuccessID(int synthesizeSuccessID) {
		this.synthesizeSuccessID = synthesizeSuccessID;
	}

	public int getSynthesizeFailedID() {
		return synthesizeFailedID;
	}

	public void setSynthesizeFailedID(int synthesizeFailedID) {
		this.synthesizeFailedID = synthesizeFailedID;
	}

	public double getBasicRate() {
		return basicRate;
	}

	public void setBasicRate(double basicRate) {
		this.basicRate = basicRate;
	}

	public double getExtraRate() {
		return extraRate;
	}

	public void setExtraRate(double extraRate) {
		this.extraRate = extraRate;
	}

	public int getBasicID() {
		return basicID;
	}

	public void setBasicID(int basicID) {
		this.basicID = basicID;
	}

	public int getBasicNum() {
		return basicNum;
	}

	public void setBasicNum(int basicNum) {
		this.basicNum = basicNum;
	}

	public int getMaxExtra() {
		return maxExtra;
	}

	public void setMaxExtra(int maxExtra) {
		this.maxExtra = maxExtra;
	}

	public int getExtraID() {
		return extraID;
	}

	public void setExtraID(int extraID) {
		this.extraID = extraID;
	}
}

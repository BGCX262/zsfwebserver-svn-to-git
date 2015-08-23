package com.database.model.bean;

public class MonsterBean {// 怪物表

	private int id;// 怪物id
	private int goodID;// 商品goodsID
	private int cate;// 1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 10道具 11其他
	private String name;// 怪物名称
	private int level;// 怪物等级
	private int hp;// 血量
	// private int race;//种族：1 元素要塞 2科技之都 3水晶城堡
	private String resourceName;// 怪物资源编号
	private String resourceSWFName;// 怪物SWF资源编号
	private int atk;// 攻击力
	private int asd;// 攻击速度
	private int type;// 0普通 1boss 2道具 3黄金怪
	private boolean canBeCreate;// 是否可以被造
	private int time;//
	private int createNeedCoin;
	private int createNeedRock;
	private int createNeedMetal;
	private int createNeedCrystal;
	private int createNeedSoul;// 造一个怪需要灵魂
	private int createNeedBadge;// 造一個怪需要徽章
	private int createNumLimit;// 造怪个数限制
	private int robTimes;// 掠夺资源倍数
	private int special;// boss专有，减少伤害
	private String miss;// 怪物miss属性
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

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getAtk() {
		return atk;
	}

	public void setAtk(int atk) {
		this.atk = atk;
	}

	public int getAsd() {
		return asd;
	}

	public void setAsd(int asd) {
		this.asd = asd;
	}

	public int getSpecial() {
		return special;
	}

	public void setSpecial(int special) {
		this.special = special;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceSWFName() {
		return resourceSWFName;
	}

	public void setResourceSWFName(String resourceSWFName) {
		this.resourceSWFName = resourceSWFName;
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

	public int getCreateNeedSoul() {
		return createNeedSoul;
	}

	public void setCreateNeedSoul(int createNeedSoul) {
		this.createNeedSoul = createNeedSoul;
	}

	public boolean isCanBeCreate() {
		return canBeCreate;
	}

	public void setCanBeCreate(boolean canBeCreate) {
		this.canBeCreate = canBeCreate;
	}

	public int getCreateNeedRock() {
		return createNeedRock;
	}

	public void setCreateNeedRock(int createNeedRock) {
		this.createNeedRock = createNeedRock;
	}

	public int getCreateNeedMetal() {
		return createNeedMetal;
	}

	public void setCreateNeedMetal(int createNeedMetal) {
		this.createNeedMetal = createNeedMetal;
	}

	public int getCreateNeedCrystal() {
		return createNeedCrystal;
	}

	public void setCreateNeedCrystal(int createNeedCrystal) {
		this.createNeedCrystal = createNeedCrystal;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getCreateNeedCoin() {
		return createNeedCoin;
	}

	public void setCreateNeedCoin(int createNeedCoin) {
		this.createNeedCoin = createNeedCoin;
	}

	public int getCreateNumLimit() {
		return createNumLimit;
	}

	public void setCreateNumLimit(int createNumLimit) {
		this.createNumLimit = createNumLimit;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRobTimes() {
		return robTimes;
	}

	public void setRobTimes(int robTimes) {
		this.robTimes = robTimes;
	}

	public int getCreateNeedBadge() {
		return createNeedBadge;
	}

	public void setCreateNeedBadge(int createNeedBadge) {
		this.createNeedBadge = createNeedBadge;
	}

	public String getMiss() {
		return miss;
	}

	public void setMiss(String miss) {
		this.miss = miss;
	}
}

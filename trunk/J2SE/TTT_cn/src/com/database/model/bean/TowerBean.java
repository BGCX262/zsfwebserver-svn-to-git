package com.database.model.bean;

public class TowerBean {// 炮塔表

	private int id;// 炮塔id
	private int goodID;// 商品ID
	private int cate;// 1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 10道具 11其他
	private String name;// 炮塔名称
	private int baseLevel;// 炮塔初始等级
	private int level;// 炮塔等级
	private int sceneLevel;// 场景炮塔等级
	private int towerType;// 攻击类型：1散射 2弹射 3普通
	private int attackNum;// 攻击数量
	private int shellType;// 炮弹类型：1减速 2暴击 3普通
	private int duration;// 持续时间
	private int buildNeedRock;// 生产需求矿石多少
	private int buildNeedMetal;// 生产需求金属多少
	private int buildNeedCrystal;// 生产需求水晶多少
	private int buildNeedCoin; // 生产需求金币多少
	private int race;// 种族：1 元素要塞 2科技之都 3水晶城堡
	private String bulletResourceName;// 子弹外观
	private String resourceName;// 塔资源外观
	private String resourceSWFName;// 炮塔SWF资源编号
	private int hp;// 血量
	private int atk;// 攻击力
	private double asd;// 攻击速度
	private int ar;// 攻击范围
	private int upTime;// 炮塔升级时间
	private int upNeedRock;// 升级所需矿石
	private int upNeedMetal;// 升级需金属
	private int upNeedCrystal;// 升级需水晶
	private int upNeedFriend;// 升级需好友
	private int time;// 建造时间
	private int towerAttType;// 炮塔类型
	private int feedbackRock;// 返还石头
	private int feedbackMetal;// 返还金属
	private int feedbackCrystal;// 返还水晶

	private int upIndexID;// 场景里升级后的indexID
	private String rate;// 发生概率
	private String effect;// 攻击效果
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

	public int getSceneLevel() {
		return sceneLevel;
	}

	public void setSceneLevel(int sceneLevel) {
		this.sceneLevel = sceneLevel;
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

	public int getTowerAttType() {
		return towerAttType;
	}

	public void setTowerAttType(int towerAttType) {
		this.towerAttType = towerAttType;
	}

	public void setRace(int race) {
		this.race = race;
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

	public double getAsd() {
		return asd;
	}

	public void setAsd(double asd) {
		this.asd = asd;
	}

	public int getAr() {
		return ar;
	}

	public void setAr(int ar) {
		this.ar = ar;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getUpTime() {
		return upTime;
	}

	public int getUpIndexID() {
		return upIndexID;
	}

	public void setUpIndexID(int upIndexID) {
		this.upIndexID = upIndexID;
	}

	public void setUpTime(int upTime) {
		this.upTime = upTime;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public int getUpNeedRock() {
		return upNeedRock;
	}

	public void setUpNeedRock(int upNeedRock) {
		this.upNeedRock = upNeedRock;
	}

	public int getUpNeedMetal() {
		return upNeedMetal;
	}

	public void setUpNeedMetal(int upNeedMetal) {
		this.upNeedMetal = upNeedMetal;
	}

	public int getUpNeedCrystal() {
		return upNeedCrystal;
	}

	public void setUpNeedCrystal(int upNeedCrystal) {
		this.upNeedCrystal = upNeedCrystal;
	}

	public String getBulletResourceName() {
		return bulletResourceName;
	}

	public void setBulletResourceName(String bulletResourceName) {
		this.bulletResourceName = bulletResourceName;
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

	public int getTowerType() {
		return towerType;
	}

	public void setTowerType(int towerType) {
		this.towerType = towerType;
	}

	public int getAttackNum() {
		return attackNum;
	}

	public void setAttackNum(int attackNum) {
		this.attackNum = attackNum;
	}

	public int getShellType() {
		return shellType;
	}

	public void setShellType(int shellType) {
		this.shellType = shellType;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getBuildNeedRock() {
		return buildNeedRock;
	}

	public void setBuildNeedRock(int buildNeedRock) {
		this.buildNeedRock = buildNeedRock;
	}

	public int getBuildNeedMetal() {
		return buildNeedMetal;
	}

	public void setBuildNeedMetal(int buildNeedMetal) {
		this.buildNeedMetal = buildNeedMetal;
	}

	public int getBuildNeedCrystal() {
		return buildNeedCrystal;
	}

	public void setBuildNeedCrystal(int buildNeedCrystal) {
		this.buildNeedCrystal = buildNeedCrystal;
	}

	public int getBuildNeedCoin() {
		return buildNeedCoin;
	}

	public void setBuildNeedCoin(int buildNeedCoin) {
		this.buildNeedCoin = buildNeedCoin;
	}

	public int getUpNeedFriend() {
		return upNeedFriend;
	}

	public void setUpNeedFriend(int upNeedFriend) {
		this.upNeedFriend = upNeedFriend;
	}

	public int getFeedbackRock() {
		return feedbackRock;
	}

	public void setFeedbackRock(int feedbackRock) {
		this.feedbackRock = feedbackRock;
	}

	public int getFeedbackMetal() {
		return feedbackMetal;
	}

	public void setFeedbackMetal(int feedbackMetal) {
		this.feedbackMetal = feedbackMetal;
	}

	public int getFeedbackCrystal() {
		return feedbackCrystal;
	}

	public void setFeedbackCrystal(int feedbackCrystal) {
		this.feedbackCrystal = feedbackCrystal;
	}
}

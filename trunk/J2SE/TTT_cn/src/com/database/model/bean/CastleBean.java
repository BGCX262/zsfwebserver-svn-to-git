package com.database.model.bean;

public class CastleBean {
	private int id;//城堡id
	private int cate;//1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 10道具 11其他
	private int goodID;//商品goodsID
	private int race;//种族：1 元素要塞  2科技之都 3水晶城堡 
	private int level;//城堡等级
	private int hp;//血量
	private int monsterVol;//储怪物数量
	private int soulVol;//储灵魂数量
	private int towerVol;//pvp可建塔数量
	private int spVol;//精力值上限
	//private int upIndexID;//升级后的indexID
	private int upNeedRock;//升级所需矿石
	private int upNeedMetal;//升级需金属
	private int upNeedCrystal;//升级需水晶
	private int upNeedCoin;//升级所需游戏币
	private int upNeedGlory;//升级所需经验
	private int upTime;//升级时间
	private int repairTime;
	private String resourceName;
	private String comment;
	
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
	public int getMonsterVol() {
		return monsterVol;
	}
	public void setMonsterVol(int monsterVol) {
		this.monsterVol = monsterVol;
	}
	public int getSoulVol() {
		return soulVol;
	}
	public void setSoulVol(int soulVol) {
		this.soulVol = soulVol;
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
	public int getUpNeedCoin() {
		return upNeedCoin;
	}
	public void setUpNeedCoin(int upNeedCoin) {
		this.upNeedCoin = upNeedCoin;
	}
	public int getUpTime() {
		return upTime;
	}
	public void setUpTime(int upTime) {
		this.upTime = upTime;
	}
	public int getUpNeedGlory() {
		return upNeedGlory;
	}
	public void setUpNeedGlory(int upNeedGlory) {
		this.upNeedGlory = upNeedGlory;
	}
	public int getGoodID() {
		return goodID;
	}
	public void setGoodID(int goodID) {
		this.goodID = goodID;
	}
	public int getRace() {
		return race;
	}
	public void setRace(int race) {
		this.race = race;
	}
	/*public int getUpIndexID() {
		return upIndexID;
	}
	public void setUpIndexID(int upIndexID) {
		this.upIndexID = upIndexID;
	}*/
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getTowerVol() {
		return towerVol;
	}
	public void setTowerVol(int towerVol) {
		this.towerVol = towerVol;
	}
	public int getRepairTime() {
		return repairTime;
	}
	public void setRepairTime(int repairTime) {
		this.repairTime = repairTime;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public int getSpVol() {
		return spVol;
	}
	public void setSpVol(int spVol) {
		this.spVol = spVol;
	}
}

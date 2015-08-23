package com.database.model.bean;

public class UserCastle {

	private long id;
	private long time;
	private volatile boolean change;
	private int castleID;
	private String name;
	private int race;
	private int maxCity;// 最大关等级
	private boolean vip;// 是不是vip
	private int energyLimit;// 精力值上限
	private int nomalstorageLimit;// 背包上限
	private int slaverLimit;// 奴隶容量
	private int soulLimit;// 储灵魂的容量
	private volatile int currentCity;// 当前所处关等级
	private int state;// 0 正常 1维修 2升级
	private int repairHp;// 修复Hp
	private long endTime;// 升级结束时间
	private int timezoneOffset;// 时区
	private String titles;// 拥有称号JSONArray格式[1,2,3]
	private int currTitle;// 正在使用的称号
	private int pvpWinCount;// pvp战斗胜利场次
	private volatile int hp;// 城堡HP
	private long revertTime;// 精力值上次回复时间
	private int recivePresentLevel;// 礼品包可开启的下一等级
	private boolean sex;// 性别

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCastleID() {
		return castleID;
	}

	public void setCastleID(int castleID) {
		this.castleID = castleID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isChange() {
		return change;
	}

	public void setChange(boolean change) {
		this.change = change;
	}

	public int getTimezoneOffset() {
		return timezoneOffset;
	}

	public void setTimezoneOffset(int timezoneOffset) {
		this.timezoneOffset = timezoneOffset;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getRace() {
		return race;
	}

	public void setRace(int race) {
		this.race = race;
	}

	public boolean isVip() {
		return vip;
	}

	public void setVip(boolean vip) {
		this.vip = vip;
	}

	public String getTitles() {
		return titles;
	}

	public void setTitles(String titles) {
		this.titles = titles;
	}

	public int getCurrTitle() {
		return currTitle;
	}

	public void setCurrTitle(int currTitle) {
		this.currTitle = currTitle;
	}

	public int getPvpWinCount() {
		return pvpWinCount;
	}

	public void setPvpWinCount(int pvpWinCount) {
		this.pvpWinCount = pvpWinCount;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMaxCity() {
		return maxCity;
	}

	public void setMaxCity(int maxCity) {
		this.maxCity = maxCity;
	}

	public int getEnergyLimit() {
		return energyLimit;
	}

	public void setEnergyLimit(int energyLimit) {
		this.energyLimit = energyLimit;
	}

	public int getSlaverLimit() {
		return slaverLimit;
	}

	public void setSlaverLimit(int slaverLimit) {
		this.slaverLimit = slaverLimit;
	}

	public int getSoulLimit() {
		return soulLimit;
	}

	public void setSoulLimit(int soulLimit) {
		this.soulLimit = soulLimit;
	}

	public int getCurrentCity() {
		return currentCity;
	}

	public void setCurrentCity(int currentCity) {
		this.currentCity = currentCity;
	}

	public int getRepairHp() {
		return repairHp;
	}

	public void setRepairHp(int repairHp) {
		this.repairHp = repairHp;
	}

	public int getNomalstorageLimit() {
		return nomalstorageLimit;
	}

	public void setNomalstorageLimit(int nomalstorageLimit) {
		this.nomalstorageLimit = nomalstorageLimit;
	}

	public long getRevertTime() {
		return revertTime;
	}

	public void setRevertTime(long revertTime) {
		this.revertTime = revertTime;
	}

	public int getRecivePresentLevel() {
		return recivePresentLevel;
	}

	public void setRecivePresentLevel(int recivePresentLevel) {
		this.recivePresentLevel = recivePresentLevel;
	}

	public boolean isSex() {
		return sex;
	}

	public void setSex(boolean sex) {
		this.sex = sex;
	}
}

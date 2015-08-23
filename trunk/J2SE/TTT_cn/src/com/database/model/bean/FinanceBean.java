package com.database.model.bean;

public class FinanceBean {
	private long id;
	private volatile int coin;                //1 金币
	private volatile double systemCash;          //2代金券
	private volatile double cash; 				  //3金券
	private volatile int energy;              //4精力值
	private volatile int rock;                //7石头
	private volatile int metal;               //8金属
	private volatile int crystal;             //9水晶
	private volatile int soul;                //10灵魂
	private volatile int glory;               //11荣誉
	private volatile int badge;               //12徽章
	private volatile int maxMmarkID;//物品标志id最大值
	
	//private long persistTime;
	//private long perviousTime;
	private long time;
//	private boolean master;
	private volatile boolean change;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getEnergy() {
		return energy;
	}
	public void setEnergy(int energy) {
		this.energy = energy;
	}
	public int getRock() {
		return rock;
	}
	public void setRock(int rock) {
		this.rock = rock;
	}
	public int getMetal() {
		return metal;
	}
	public void setMetal(int metal) {
		this.metal = metal;
	}
	public int getCrystal() {
		return crystal;
	}
	public void setCrystal(int crystal) {
		this.crystal = crystal;
	}
	public int getSoul() {
		return soul;
	}
	public void setSoul(int soul) {
		this.soul = soul;
	}
	public int getGlory() {
		return glory;
	}
	public void setGlory(int glory) {
		this.glory = glory;
	}
	public int getMaxMmarkID() {
		return maxMmarkID;
	}
	public void setMaxMmarkID(int maxMmarkID) {
		this.maxMmarkID = maxMmarkID;
	}
/*	public long getPersistTime() {
		return persistTime;
	}
	public void setPersistTime(long persistTime) {
		this.persistTime = persistTime;
	}
	public long getPerviousTime() {
		return perviousTime;
	}
	public void setPerviousTime(long perviousTime) {
		this.perviousTime = perviousTime;
	}*/
/*	public boolean isMaster() {
		return master;
	}
	public void setMaster(boolean master) {
		this.master = master;
	}*/
	public boolean isChange() {
		return change;
	}
	public void setChange(boolean change) {
		this.change = change;
	}
	public int getCoin() {
		return coin;
	}
	public void setCoin(int coin) {
		this.coin = coin;
	}
	public double getSystemCash() {
		return systemCash;
	}
	public void setSystemCash(double systemCash) {
		this.systemCash = systemCash;
	}
	public double getCash() {
		return cash;
	}
	public void setCash(double cash) {
		this.cash = cash;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getBadge() {
		return badge;
	}
	public void setBadge(int badge) {
		this.badge = badge;
	}
}

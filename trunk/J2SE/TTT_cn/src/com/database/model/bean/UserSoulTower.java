package com.database.model.bean;

public class UserSoulTower {
	private long id;//主人ID
	private int state;//0正常 1升级
	private boolean change;
	private int soulTowerLevel;//灵魂塔ID
	private long endTime;//结束时间
	private long time;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public boolean isChange() {
		return change;
	}
	public void setChange(boolean change) {
		this.change = change;
	}
	public int getSoulTowerLevel() {
		return soulTowerLevel;
	}
	public void setSoulTowerLevel(int soulTowerLevel) {
		this.soulTowerLevel = soulTowerLevel;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}

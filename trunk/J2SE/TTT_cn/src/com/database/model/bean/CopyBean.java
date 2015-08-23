package com.database.model.bean;

public class CopyBean {
	private int cityID;//副本关ID
	//private Boolean opened;//是否开启
	private long openTime;//开启时间
	private int curTimesNum;//当前波
	public long getOpenTime() {
		return openTime;
	}
	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	public int getCurTimesNum() {
		return curTimesNum;
	}
	public void setCurTimesNum(int curTimesNum) {
		this.curTimesNum = curTimesNum;
	}
/*	public Boolean getOpened() {
		return opened;
	}
	public void setOpened(Boolean opened) {
		this.opened = opened;
	}*/
}

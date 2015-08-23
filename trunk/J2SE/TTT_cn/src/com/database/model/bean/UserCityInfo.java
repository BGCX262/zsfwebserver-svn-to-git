package com.database.model.bean;

import java.io.Serializable;

public class UserCityInfo implements Serializable{
	private static final long serialVersionUID = 7862613076041038000L;
	private long masterID;
	private int cityID;
	private String info;
	
	public long getMasterID() {
		return masterID;
	}
	public void setMasterID(long masterID) {
		this.masterID = masterID;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
}

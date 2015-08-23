package com.database.model.bean;

public class UserProp {
	private long id;
	private long masterID;
	private int propID;
	private long useTime;//使用时间
	private long expiration;
	private volatile int change;//0没改变   1改变:save  2改变 :update  3null值
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getMasterID() {
		return masterID;
	}
	public void setMasterID(long masterID) {
		this.masterID = masterID;
	}
	public int getPropID() {
		return propID;
	}
	public void setPropID(int propID) {
		this.propID = propID;
	}
	public long getUseTime() {
		return useTime;
	}
	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}
	public int getChange() {
		return change;
	}
	public void setChange(int change) {
		this.change = change;
	}
	public long getExpiration() {
		return expiration;
	}
	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}
}

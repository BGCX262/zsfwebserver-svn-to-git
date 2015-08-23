package com.database.model.bean;

public class MemcachePVPListBean {
	private long id;
	private long protectTime;
	private transient int level;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getProtectTime() {
		return protectTime;
	}
	public void setProtectTime(long protectTime) {
		this.protectTime = protectTime;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}

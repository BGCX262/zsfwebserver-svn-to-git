package com.database.model.bean;

public class Enemy {
	private long id;
	private long masterID;
	private long enemyID;
	private long time;
	
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
	public long getEnemyID() {
		return enemyID;
	}
	public void setEnemyID(long enemyID) {
		this.enemyID = enemyID;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}

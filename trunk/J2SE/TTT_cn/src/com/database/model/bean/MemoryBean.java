package com.database.model.bean;

import com.server.cache.Right;

public class MemoryBean extends Right implements Cloneable{
	private long time;
	private UserCastle castle;
	private UserCity city;
	private UserSlavers slavers;
	private UserStorages storages;
	private UserTasks tasks;
	private UserSoulTower soulTower;
	private UserMine userMine;
	private UserProps userProps;
	private UserTechnology userTec;
	private UserCopy copy;
	
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public UserCastle getCastle() {
		return castle;
	}
	public void setCastle(UserCastle castle) {
		this.castle = castle;
	}
	public UserCity getCity() {
		return city;
	}
	public void setCity(UserCity city) {
		this.city = city;
	}
	public UserSlavers getSlavers() {
		return slavers;
	}
	public void setSlavers(UserSlavers slavers) {
		this.slavers = slavers;
	}
	public UserStorages getStorages() {
		return storages;
	}
	public void setStorages(UserStorages storages) {
		this.storages = storages;
	}
	public UserTasks getTasks() {
		return tasks;
	}
	public void setTasks(UserTasks tasks) {
		this.tasks = tasks;
	}
	public UserSoulTower getSoulTower() {
		return soulTower;
	}
	public void setSoulTower(UserSoulTower soulTower) {
		this.soulTower = soulTower;
	}
	public UserMine getUserMine() {
		return userMine;
	}
	public void setUserMine(UserMine userMine) {
		this.userMine = userMine;
	}
	public UserProps getUserProps() {
		return userProps;
	}
	public void setUserProps(UserProps userProps) {
		this.userProps = userProps;
	}
	public UserTechnology getUserTec() {
		return userTec;
	}
	public void setUserTec(UserTechnology userTec) {
		this.userTec = userTec;
	}
	public UserCopy getCopy() {
		return copy;
	}
	public void setCopy(UserCopy copy) {
		this.copy = copy;
	}
	
	public MemoryBean clone(){
		MemoryBean o = null;
		try{
			o = (MemoryBean)super.clone();
		}catch(CloneNotSupportedException e){
			e.printStackTrace();
		}
		return o;
	}
}

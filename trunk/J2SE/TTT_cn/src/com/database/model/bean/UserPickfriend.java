package com.database.model.bean;

public class UserPickfriend {

	private int id;
	private long masterID;
	private long friendID;
	private int pickedNum;
	private long lastPickedTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getMasterID() {
		return masterID;
	}

	public void setMasterID(long masterID) {
		this.masterID = masterID;
	}

	public long getFriendID() {
		return friendID;
	}

	public void setFriendID(long friendID) {
		this.friendID = friendID;
	}

	public int getPickedNum() {
		return pickedNum;
	}

	public void setPickedNum(int pickedNum) {
		this.pickedNum = pickedNum;
	}

	public long getLastPickedTime() {
		return lastPickedTime;
	}

	public void setLastPickedTime(long lastPickedTime) {
		this.lastPickedTime = lastPickedTime;
	}

}

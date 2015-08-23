package com.database.model.bean;


public class UserLoveTask {
	
	private int id;
	private long masterId;
	private long friendId;
	private long time;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public long getMasterId() {
		return masterId;
	}
	
	public void setMasterId(long masterId) {
		this.masterId = masterId;
	}
	
	public long getFriendId() {
		return friendId;
	}
	
	public void setFriendId(long friendId) {
		this.friendId = friendId;
	}
	
	public long getTime() {
		return time;
	}
	
	public void setTime(long time) {
		this.time = time;
	}

}

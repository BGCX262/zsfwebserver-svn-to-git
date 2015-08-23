package com.database.model.bean;

import java.io.Serializable;

public class UserSlaver implements Serializable{
	private static final long serialVersionUID = -4560633835191639375L;
	private Integer id;
	private long masterID;
	private volatile int change;//0没改变   1保存:save  2改变 :update  3null值
	private int slaverID;//0朋友奴隶 
	private long friendID;
	private int state;// 0 闲  1忙  2逃跑 3打工中 4打工结束
	private int workID;//工作于城堡时为0  -2工作于灵魂塔  -3科技馆
	private long endTime;
	private long escapeTime;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public long getMasterID() {
		return masterID;
	}
	public void setMasterID(long masterID) {
		this.masterID = masterID;
	}
	public int getSlaverID() {
		return slaverID;
	}
	public void setSlaverID(int slaverID) {
		this.slaverID = slaverID;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getFriendID() {
		return friendID;
	}
	public void setFriendID(long friendID) {
		this.friendID = friendID;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getChange() {
		return change;
	}
	public void setChange(int change) {
		if((this.change == 1 && change == 2) || 
				(this.change == 3)){
			return;
		}else{
			this.change = change;
		}
	}
	public long getEscapeTime() {
		return escapeTime;
	}
	public void setEscapeTime(long escapeTime) {
		this.escapeTime = escapeTime;
	}
	public int getWorkID() {
		return workID;
	}
	public void setWorkID(int workID) {
		this.workID = workID;
	}
}

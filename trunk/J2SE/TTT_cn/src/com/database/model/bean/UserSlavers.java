package com.database.model.bean;

import java.util.List;

public class UserSlavers{
	private long id;
	private long time;
	private List<UserSlaver> slavers;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public List<UserSlaver> getSlavers() {
		return slavers;
	}
	public void setSlavers(List<UserSlaver> slavers) {
		this.slavers = slavers;
	}
}

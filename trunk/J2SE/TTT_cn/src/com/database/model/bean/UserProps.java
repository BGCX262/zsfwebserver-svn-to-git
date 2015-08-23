package com.database.model.bean;

import java.util.List;

public class UserProps{
	private long id;
	private long time;
	private List<UserProp> usedProps;
	
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
	public List<UserProp> getUsedProps() {
		return usedProps;
	}
	public void setUsedProps(List<UserProp> usedProps) {
		this.usedProps = usedProps;
	}
}

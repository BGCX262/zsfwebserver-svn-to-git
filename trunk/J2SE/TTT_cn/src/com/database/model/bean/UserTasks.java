package com.database.model.bean;

import java.util.List;

public class UserTasks {
	private long id;
	private long time;
	private List<UserTask> tasks;
	
	
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
	public List<UserTask> getTasks() {
		return tasks;
	}
	public void setTasks(List<UserTask> tasks) {
		this.tasks = tasks;
	}
}

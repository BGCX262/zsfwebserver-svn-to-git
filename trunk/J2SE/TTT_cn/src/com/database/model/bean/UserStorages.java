package com.database.model.bean;

import java.util.List;

public class UserStorages{
	private long id;
	private long time;
	//private boolean change;
	private List<UserStorage> storage;
	
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
	public List<UserStorage> getStorage() {
		return storage;
	}
	public void setStorage(List<UserStorage> storage) {
		this.storage = storage;
	}
	/*public boolean isChange() {
		return change;
	}
	public void setChange(boolean change) {
		this.change = change;
	}*/
}

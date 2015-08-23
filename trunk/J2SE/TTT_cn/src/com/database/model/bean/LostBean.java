package com.database.model.bean;

public class LostBean {
	private UserStorage userStorage;
	private Integer markID;
	private Integer boxID;

	public LostBean(UserStorage userStorage, Integer markID, Integer boxID) {
		this.userStorage = userStorage;
		this.markID = markID;
		this.boxID = boxID;
	}

	public UserStorage getUserStorage() {
		return userStorage;
	}

	public void setUserStorage(UserStorage userStorage) {
		this.userStorage = userStorage;
	}

	public Integer getMarkID() {
		return markID;
	}

	public void setMarkID(Integer markID) {
		this.markID = markID;
	}

	public Integer getBoxID() {
		return boxID;
	}

	public void setBoxID(Integer boxID) {
		this.boxID = boxID;
	}
}

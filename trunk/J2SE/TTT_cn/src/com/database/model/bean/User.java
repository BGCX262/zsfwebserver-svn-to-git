package com.database.model.bean;


public class User {
	private long id;
	private int sid;
	private long facebook_user_id;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public long getFacebook_user_id() {
		return facebook_user_id;
	}
	public void setFacebook_user_id(long facebook_user_id) {
		this.facebook_user_id = facebook_user_id;
	}
}

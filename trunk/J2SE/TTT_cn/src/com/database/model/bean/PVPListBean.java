package com.database.model.bean;

import java.io.Serializable;

public class PVPListBean implements Serializable {
	private static final long serialVersionUID = 4600065518088237064L;
	private long id;
	private long protectTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getProtectTime() {
		return protectTime;
	}
	public void setProtectTime(long protectTime) {
		this.protectTime = protectTime;
	}
}

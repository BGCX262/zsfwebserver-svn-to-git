package com.server.log;

import java.util.List;

public class  HandleLogBean{
	private long index;
	private long who;
	private int what;
	private long when;
	private Integer handle;
	private boolean success;
	private List<ThingBean> get;
	private List<ThingBean> lost;
	private String comment;
	
	public HandleLogBean(){}
	public HandleLogBean(long who, int what, long when, Integer handle, boolean success, List<ThingBean> get, List<ThingBean>lost, String comment){
		this.who = who;
		this.what = what;
		this.when = when;
		this.handle = handle;
		this.success = success;
		this.get = get;
		this.lost = lost;
		this.comment = comment;
	}
	public long getIndex() {
		return index;
	}
	public void setIndex(long index) {
		this.index = index;
	}
	public long getWho() {
		return who;
	}
	public void setWho(long who) {
		this.who = who;
	}
	public int getWhat() {
		return what;
	}
	public void setWhat(int what) {
		this.what = what;
	}
	public long getWhen() {
		return when;
	}
	public void setWhen(long when) {
		this.when = when;
	}
	public Integer getHandle() {
		return handle;
	}
	public void setHandle(Integer handle) {
		this.handle = handle;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public List<ThingBean> getGet() {
		return get;
	}
	public void setGet(List<ThingBean> get) {
		this.get = get;
	}
	public List<ThingBean> getLost() {
		return lost;
	}
	public void setLost(List<ThingBean> lost) {
		this.lost = lost;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}

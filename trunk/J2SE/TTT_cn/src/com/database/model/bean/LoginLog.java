package com.database.model.bean;

import java.sql.Date;
import java.sql.Timestamp;

public class LoginLog {

	private long masterID;
	private Date registerTime;
	private int loginCount;
	private int loginAwardCount;// 領獎次數
	private long lastAwardTime;// 上次領取每日獎勵時間
	private Timestamp lastLoginTime;// 上次登錄時間
	private long lastIssueTime;// 上次发布日常任务的时间
	private int inviteCount;// 邀请好友领取SP次数
	private long lastInviteTime;// 最后一次邀请时间
	private int tenAddCount;// 10点补充次数
	private long lastTenAddTime;// 最后一次补充10点时间
	private int fullAddCount;// 点卷补满次数
	private long lastFullAddTime;// 最后一次补满时间

	public long getMasterID() {
		return masterID;
	}

	public void setMasterID(long masterID) {
		this.masterID = masterID;
	}

	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	public int getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(int loginCount) {
		this.loginCount = loginCount;
	}

	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public long getLastIssueTime() {
		return lastIssueTime;
	}

	public void setLastIssueTime(long lastIssueTime) {
		this.lastIssueTime = lastIssueTime;
	}

	public int getLoginAwardCount() {
		return loginAwardCount;
	}

	public void setLoginAwardCount(int loginAwardCount) {
		this.loginAwardCount = loginAwardCount;
	}

	public long getLastAwardTime() {
		return lastAwardTime;
	}

	public void setLastAwardTime(long lastAwardTime) {
		this.lastAwardTime = lastAwardTime;
	}

	public int getInviteCount() {
		return inviteCount;
	}

	public void setInviteCount(int inviteCount) {
		this.inviteCount = inviteCount;
	}

	public long getLastInviteTime() {
		return lastInviteTime;
	}

	public void setLastInviteTime(long lastInviteTime) {
		this.lastInviteTime = lastInviteTime;
	}

	public int getTenAddCount() {
		return tenAddCount;
	}

	public void setTenAddCount(int tenAddCount) {
		this.tenAddCount = tenAddCount;
	}

	public long getLastTenAddTime() {
		return lastTenAddTime;
	}

	public void setLastTenAddTime(long lastTenAddTime) {
		this.lastTenAddTime = lastTenAddTime;
	}

	public int getFullAddCount() {
		return fullAddCount;
	}

	public void setFullAddCount(int fullAddCount) {
		this.fullAddCount = fullAddCount;
	}

	public long getLastFullAddTime() {
		return lastFullAddTime;
	}

	public void setLastFullAddTime(long lastFullAddTime) {
		this.lastFullAddTime = lastFullAddTime;
	}
}

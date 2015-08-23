package com.database.model.bean;

import java.sql.Time;
import java.sql.Timestamp;

public class LoginRecord {

	private long id;
	private boolean style;
	private boolean sound;
	private boolean showLevel;
	private String ip;
	private Time offline;
	private Timestamp online;
	private long todayOnline;
	private int firstLoginPremium;
	private boolean isFirst;
	private boolean initialized;
	private long freashProtectTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isStyle() {
		return style;
	}

	public void setStyle(boolean style) {
		this.style = style;
	}

	public boolean isSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Time getOffline() {
		return offline;
	}

	public void setOffline(Time offline) {
		this.offline = offline;
	}

	public Timestamp getOnline() {
		return online;
	}

	public void setOnline(Timestamp online) {
		this.online = online;
	}

	public int getFirstLoginPremium() {
		return firstLoginPremium;
	}

	public void setFirstLoginPremium(int firstLoginPremium) {
		this.firstLoginPremium = firstLoginPremium;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public boolean isShowLevel() {
		return showLevel;
	}

	public void setShowLevel(boolean showLevel) {
		this.showLevel = showLevel;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public long getFreashProtectTime() {
		return freashProtectTime;
	}

	public void setFreashProtectTime(long freashProtectTime) {
		this.freashProtectTime = freashProtectTime;
	}

	@Override
	public String toString() {
		return "Login record description:\tID = " + getId() + " IP = " + getIp() + " online = " + getOnline()
				+ " offline = " + getOffline();
	}

	public long getTodayOnline() {
		return todayOnline;
	}

	public void setTodayOnline(long todayOnline) {
		this.todayOnline = todayOnline;
	}

}

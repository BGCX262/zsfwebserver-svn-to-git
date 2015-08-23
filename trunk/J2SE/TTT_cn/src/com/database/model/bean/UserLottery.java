package com.database.model.bean;

import java.io.Serializable;

public class UserLottery implements Serializable {

	private long id;
	private int lotteryLevel;
	private int lotteryNum;
	private long lotteryTime;
	private boolean zero;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLotteryLevel() {
		return lotteryLevel;
	}

	public void setLotteryLevel(int lotteryLevel) {
		this.lotteryLevel = lotteryLevel;
	}

	public int getLotteryNum() {
		return lotteryNum;
	}

	public void setLotteryNum(int lotteryNum) {
		this.lotteryNum = lotteryNum;
	}

	public long getLotteryTime() {
		return lotteryTime;
	}

	public void setLotteryTime(long lotteryTime) {
		this.lotteryTime = lotteryTime;
	}

	public boolean isZero() {
		return zero;
	}

	public void setZero(boolean zero) {
		this.zero = zero;
	}
}

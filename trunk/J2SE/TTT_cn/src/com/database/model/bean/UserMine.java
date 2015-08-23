package com.database.model.bean;

public class UserMine {
	private long id;
	private int rockMineLevel;//矿石矿等级
	private int metalMineLevel;//金属矿等级
	private int crystalMineLevel;//水晶矿等级
	private int rockMineState;//矿石矿状态                       0 正常 1升级
	private int metalMineState;//金属矿状态                    0正常 1升级
	private int crystalMineState;//水晶矿状态               0正常 1 升级
	private long rockEndTime;//矿石矿升级结束时间
	private long metalEndTime;//金属矿升级结束时间
	private long crystalEndTime;//水晶矿升级结束时间
	private long nextRockCollectTime;
	private long nextMetalCollectTime;
	private long nextCrystalCollectTime;
	private long time;
	private boolean change;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getRockMineLevel() {
		return rockMineLevel;
	}
	public void setRockMineLevel(int rockMineLevel) {
		this.rockMineLevel = rockMineLevel;
	}
	public int getMetalMineLevel() {
		return metalMineLevel;
	}
	public void setMetalMineLevel(int metalMineLevel) {
		this.metalMineLevel = metalMineLevel;
	}
	public int getCrystalMineLevel() {
		return crystalMineLevel;
	}
	public void setCrystalMineLevel(int crystalMineLevel) {
		this.crystalMineLevel = crystalMineLevel;
	}
	public int getRockMineState() {
		return rockMineState;
	}
	public void setRockMineState(int rockMineState) {
		this.rockMineState = rockMineState;
	}
	public int getMetalMineState() {
		return metalMineState;
	}
	public void setMetalMineState(int metalMineState) {
		this.metalMineState = metalMineState;
	}
	public int getCrystalMineState() {
		return crystalMineState;
	}
	public void setCrystalMineState(int crystalMineState) {
		this.crystalMineState = crystalMineState;
	}
	public long getRockEndTime() {
		return rockEndTime;
	}
	public void setRockEndTime(long rockEndTime) {
		this.rockEndTime = rockEndTime;
	}
	public long getMetalEndTime() {
		return metalEndTime;
	}
	public void setMetalEndTime(long metalEndTime) {
		this.metalEndTime = metalEndTime;
	}
	public long getCrystalEndTime() {
		return crystalEndTime;
	}
	public void setCrystalEndTime(long crystalEndTime) {
		this.crystalEndTime = crystalEndTime;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public boolean isChange() {
		return change;
	}
	public void setChange(boolean change) {
		this.change = change;
	}
	public long getNextRockCollectTime() {
		return nextRockCollectTime;
	}
	public void setNextRockCollectTime(long nextRockCollectTime) {
		this.nextRockCollectTime = nextRockCollectTime;
	}
	public long getNextMetalCollectTime() {
		return nextMetalCollectTime;
	}
	public void setNextMetalCollectTime(long nextMetalCollectTime) {
		this.nextMetalCollectTime = nextMetalCollectTime;
	}
	public long getNextCrystalCollectTime() {
		return nextCrystalCollectTime;
	}
	public void setNextCrystalCollectTime(long nextCrystalCollectTime) {
		this.nextCrystalCollectTime = nextCrystalCollectTime;
	}
}

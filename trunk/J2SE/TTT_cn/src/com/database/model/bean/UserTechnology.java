package com.database.model.bean;

public class UserTechnology {

	private long id;
	private int tecTowerLevel;
	private int rockTechnologyLevel;
	private int metalTechnologyLevel;
	private int crystalTechnologyLevel;
	private int powderTechnologyLevel;
	private int trainTechnologyLevel;
	private int autoAttackTechnologyLevel;

	private boolean upgradeTecTower;
	private boolean upgradeRockTec;
	private boolean upgradeMetalTec;
	private boolean upgradeCrystalTec;
	private boolean upgradePowderTec;
	private boolean upgradeTrainTec;
	private boolean upgradeAutoAttackTec;

	private long tecTowerEndTime;
	private long rockTecEndTime;
	private long metalTecEndTime;
	private long crystalTecEndTime;
	private long powderTecEndTime;
	private long trainTecEndTime;
	private long autoAttackTecEndTime;

	private long time;
	private boolean change;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getTecTowerLevel() {
		return tecTowerLevel;
	}

	public void setTecTowerLevel(int tecTowerLevel) {
		this.tecTowerLevel = tecTowerLevel;
	}

	public int getRockTechnologyLevel() {
		return rockTechnologyLevel;
	}

	public void setRockTechnologyLevel(int rockTechnologyLevel) {
		this.rockTechnologyLevel = rockTechnologyLevel;
	}

	public int getMetalTechnologyLevel() {
		return metalTechnologyLevel;
	}

	public void setMetalTechnologyLevel(int metalTechnologyLevel) {
		this.metalTechnologyLevel = metalTechnologyLevel;
	}

	public int getCrystalTechnologyLevel() {
		return crystalTechnologyLevel;
	}

	public void setCrystalTechnologyLevel(int crystalTechnologyLevel) {
		this.crystalTechnologyLevel = crystalTechnologyLevel;
	}

	public int getPowderTechnologyLevel() {
		return powderTechnologyLevel;
	}

	public void setPowderTechnologyLevel(int powderTechnologyLevel) {
		this.powderTechnologyLevel = powderTechnologyLevel;
	}

	public int getTrainTechnologyLevel() {
		return trainTechnologyLevel;
	}

	public void setTrainTechnologyLevel(int trainTechnologyLevel) {
		this.trainTechnologyLevel = trainTechnologyLevel;
	}

	public boolean isUpgradeTecTower() {
		return upgradeTecTower;
	}

	public void setUpgradeTecTower(boolean upgradeTecTower) {
		this.upgradeTecTower = upgradeTecTower;
	}

	public boolean isUpgradeRockTec() {
		return upgradeRockTec;
	}

	public void setUpgradeRockTec(boolean upgradeRockTec) {
		this.upgradeRockTec = upgradeRockTec;
	}

	public boolean isUpgradeMetalTec() {
		return upgradeMetalTec;
	}

	public void setUpgradeMetalTec(boolean upgradeMetalTec) {
		this.upgradeMetalTec = upgradeMetalTec;
	}

	public boolean isUpgradeCrystalTec() {
		return upgradeCrystalTec;
	}

	public void setUpgradeCrystalTec(boolean upgradeCrystalTec) {
		this.upgradeCrystalTec = upgradeCrystalTec;
	}

	public boolean isUpgradePowderTec() {
		return upgradePowderTec;
	}

	public void setUpgradePowderTec(boolean upgradePowderTec) {
		this.upgradePowderTec = upgradePowderTec;
	}

	public boolean isUpgradeTrainTec() {
		return upgradeTrainTec;
	}

	public void setUpgradeTrainTec(boolean upgradeTrainTec) {
		this.upgradeTrainTec = upgradeTrainTec;
	}

	public long getTecTowerEndTime() {
		return tecTowerEndTime;
	}

	public void setTecTowerEndTime(long tecTowerEndTime) {
		this.tecTowerEndTime = tecTowerEndTime;
	}

	public long getRockTecEndTime() {
		return rockTecEndTime;
	}

	public void setRockTecEndTime(long rockTecEndTime) {
		this.rockTecEndTime = rockTecEndTime;
	}

	public long getMetalTecEndTime() {
		return metalTecEndTime;
	}

	public void setMetalTecEndTime(long metalTecEndTime) {
		this.metalTecEndTime = metalTecEndTime;
	}

	public long getCrystalTecEndTime() {
		return crystalTecEndTime;
	}

	public void setCrystalTecEndTime(long crystalTecEndTime) {
		this.crystalTecEndTime = crystalTecEndTime;
	}

	public long getPowderTecEndTime() {
		return powderTecEndTime;
	}

	public void setPowderTecEndTime(long powderTecEndTime) {
		this.powderTecEndTime = powderTecEndTime;
	}

	public long getTrainTecEndTime() {
		return trainTecEndTime;
	}

	public void setTrainTecEndTime(long trainTecEndTime) {
		this.trainTecEndTime = trainTecEndTime;
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

	public int getAutoAttackTechnologyLevel() {
		return autoAttackTechnologyLevel;
	}

	public void setAutoAttackTechnologyLevel(int autoAttackTechnologyLevel) {
		this.autoAttackTechnologyLevel = autoAttackTechnologyLevel;
	}

	public boolean isUpgradeAutoAttackTec() {
		return upgradeAutoAttackTec;
	}

	public void setUpgradeAutoAttackTec(boolean upgradeAutoAttackTec) {
		this.upgradeAutoAttackTec = upgradeAutoAttackTec;
	}

	public long getAutoAttackTecEndTime() {
		return autoAttackTecEndTime;
	}

	public void setAutoAttackTecEndTime(long autoAttackTecEndTime) {
		this.autoAttackTecEndTime = autoAttackTecEndTime;
	}
}

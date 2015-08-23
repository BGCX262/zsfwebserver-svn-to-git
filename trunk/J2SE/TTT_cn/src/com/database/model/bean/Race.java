package com.database.model.bean;

public class Race {
	private int id;
	private int workPerMin;//每分钟修复多少HP
	private int repairNeedCoin;//修复1HP所需矿石
	private int repairNeedRock;//修复1HP所需矿石
	private int repairNeedMetal;//修复1HP需金属
	private int repairNeedCrystal;//修复1HP需水晶
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRepairNeedRock() {
		return repairNeedRock;
	}
	public void setRepairNeedRock(int repairNeedRock) {
		this.repairNeedRock = repairNeedRock;
	}
	public int getRepairNeedMetal() {
		return repairNeedMetal;
	}
	public void setRepairNeedMetal(int repairNeedMetal) {
		this.repairNeedMetal = repairNeedMetal;
	}
	public int getRepairNeedCrystal() {
		return repairNeedCrystal;
	}
	public void setRepairNeedCrystal(int repairNeedCrystal) {
		this.repairNeedCrystal = repairNeedCrystal;
	}
	public int getRepairNeedCoin() {
		return repairNeedCoin;
	}
	public void setRepairNeedCoin(int repairNeedCoin) {
		this.repairNeedCoin = repairNeedCoin;
	}
	public int getWorkPerMin() {
		return workPerMin;
	}
	public void setWorkPerMin(int workPerMin) {
		this.workPerMin = workPerMin;
	}
}

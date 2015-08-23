package com.database.model.bean;

public class UserTask {
	private long id;
	private volatile int change;//0没改变   1改变:save  2改变 :update  3null值
	private long masterID;//主人ID
	private int taskID;//任务编号
	private int state;//1进行中  2已完成  3已领奖
	private double finishRate;//完成进度 
	private int taskType;//任务类型
	private long acceptTime;//接受时间
	private long finishTime;//完成时间
	private int lucky;//任务幸运度：0普通任务 1紫色任务
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getChange() {
		return change;
	}
	public void setChange(int change) {
		if((this.change == 1 && change == 2) || 
				(this.change == 3 && change == 2)){
			return;
		}else{
			this.change = change;
		}
	}
	public int getTaskID() {
		return taskID;
	}
	public long getMasterID() {
		return masterID;
	}
	public void setMasterID(long masterID) {
		this.masterID = masterID;
	}
	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(long acceptTime) {
		this.acceptTime = acceptTime;
	}
	public long getFinishTime() {
		return finishTime;
	}
	public double getFinishRate() {
		return finishRate;
	}
	public void setFinishRate(double finishRate) {
		this.finishRate = finishRate;
	}
	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
	public int getLucky() {
		return lucky;
	}
	public void setLucky(int lucky) {
		this.lucky = lucky;
	}
}

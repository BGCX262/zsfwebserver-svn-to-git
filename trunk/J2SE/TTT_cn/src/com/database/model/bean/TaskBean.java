package com.database.model.bean;

public class TaskBean {

	private int id;// 任务编号
	private int goodID;
	private int cate;
	private String name;// 任务名称
	private int preTask;// 前置任务
	private int acceptMinLevel;// 限制等级
	private int acceptMaxLevel;// 限制等级
	private int taskType;// 任务类型：1 引导任务 2 日常任务 3成长任务 4事件任务 5挑战任务
	private String target;// 任务目标
	private boolean lead;// 是否引导
	private int targetNum;// 目标次数
	private String targetPara;// 目标参数
	private String prize;// JSONArray格式：[[60006,1],[60001,1]]
	private String leadMsg;// 提示说明
	private String comment;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPreTask() {
		return preTask;
	}

	public void setPreTask(int preTask) {
		this.preTask = preTask;
	}

	public int getTaskType() {
		return taskType;
	}

	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getPrize() {
		return prize;
	}

	public void setPrize(String prize) {
		this.prize = prize;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isLead() {
		return lead;
	}

	public void setLead(boolean lead) {
		this.lead = lead;
	}

	public int getGoodID() {
		return goodID;
	}

	public void setGoodID(int goodID) {
		this.goodID = goodID;
	}

	public int getCate() {
		return cate;
	}

	public void setCate(int cate) {
		this.cate = cate;
	}

	public int getTargetNum() {
		return targetNum;
	}

	public void setTargetNum(int targetNum) {
		this.targetNum = targetNum;
	}

	public String getTargetPara() {
		return targetPara;
	}

	public void setTargetPara(String targetPara) {
		this.targetPara = targetPara;
	}

	public String getLeadMsg() {
		return leadMsg;
	}

	public void setLeadMsg(String leadMsg) {
		this.leadMsg = leadMsg;
	}

	public int getAcceptMinLevel() {
		return acceptMinLevel;
	}

	public void setAcceptMinLevel(int acceptMinLevel) {
		this.acceptMinLevel = acceptMinLevel;
	}

	public int getAcceptMaxLevel() {
		return acceptMaxLevel;
	}

	public void setAcceptMaxLevel(int acceptMaxLevel) {
		this.acceptMaxLevel = acceptMaxLevel;
	}
}

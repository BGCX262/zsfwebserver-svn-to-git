package com.database.model.bean;

public class Message {
	private long id;
	private long accepter;// 主人ID
	private long sender;
	private long attacher; // 附加id
	private boolean old;// 是否已读
	private int senderRace;
	private long time;// 发件日期
	private int messageType;// 消息类型 0好友帮忙捡东西 1敌人攻破了你的城堡 2敌人的怪物闯进了你的城堡 3工人逃跑了
							// 4系统邮件 5好友帮忙复仇邮件
	private String content;// 物品JSONArray格式[[20001,1],[10001,1]]
	private String usedProp;// 用过的道具
	private String lossGoods;// 损失的怪物
	private String reward; // 防守成功礼物
	// private String msg;//信息
	private int change;// //0没改变 1改变:save 2改变 :update 3null值

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getSender() {
		return sender;
	}

	public boolean isOld() {
		return old;
	}

	public void setOld(boolean old) {
		this.old = old;
	}

	public void setSender(long sender) {
		this.sender = sender;
	}

	public long getAccepter() {
		return accepter;
	}

	public void setAccepter(long accepter) {
		this.accepter = accepter;
	}

	public int getSenderRace() {
		return senderRace;
	}

	public void setSenderRace(int senderRace) {
		this.senderRace = senderRace;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public int getChange() {
		return change;
	}

	public void setChange(int change) {
		this.change = change;
	}

	public String getUsedProp() {
		return usedProp;
	}

	public void setUsedProp(String usedProp) {
		this.usedProp = usedProp;
	}

	public String getLossGoods() {
		return lossGoods;
	}

	public void setLossGoods(String lossGoods) {
		this.lossGoods = lossGoods;
	}

	public long getAttacher() {
		return attacher;
	}

	public void setAttacher(Long attacher) {
		this.attacher = attacher;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

}

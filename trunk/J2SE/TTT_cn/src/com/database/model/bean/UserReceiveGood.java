/**
 * ReceiveGoods.java
 */
package com.database.model.bean;

/**
 * 收到的物品
 * 
 * @author zsf
 */
public class UserReceiveGood {

	private int id;
	private long masterID; // 接受者ID
	private int goodID; // 物品ID
	private int num; // 物品数量
	private String createDate; // 创建时间
	private int isReceive; // 玩家是否已经领取
	private Long realTime; // 可用时间
	private Long receiveTime; // 领取时间
	private Integer type; // 0手动补偿 1 怪物捕获 2 送礼

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the isReceive
	 */
	public int getIsReceive() {
		return isReceive;
	}

	/**
	 * @param isReceive
	 *            the isReceive to set
	 */
	public void setIsReceive(int isReceive) {
		this.isReceive = isReceive;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the masterID
	 */
	public long getMasterID() {
		return masterID;
	}

	/**
	 * @param masterID
	 *            the masterID to set
	 */
	public void setMasterID(long masterID) {
		this.masterID = masterID;
	}

	/**
	 * @return the goodID
	 */
	public int getGoodID() {
		return goodID;
	}

	/**
	 * @param goodID
	 *            the goodID to set
	 */
	public void setGoodID(int goodID) {
		this.goodID = goodID;
	}

	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param num
	 *            the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

	public Long getRealTime() {
		return realTime;
	}

	public void setRealTime(Long realTime) {
		this.realTime = realTime;
	}

	public Long getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Long receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

}

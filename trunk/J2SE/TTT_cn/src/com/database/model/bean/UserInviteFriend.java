package com.database.model.bean;

/**
 * 升级炮塔邀请好友
 * @author zsf 2011-12-19 下午02:56:49
 */
public class UserInviteFriend {

	private long id;

	/**
	 * 玩家ID
	 */
	private long masterID;

	/**
	 * 场景ID
	 */
	private int cityID;

	/**
	 * 场景中炮塔ID
	 */
	private int towerID;

	/**
	 * 需要邀请的人数
	 */
	private int needFriend;

	/**
	 * 已达成的人数
	 */
	private int reachedFriend;

	/**
	 * 邀请的人列表
	 */
	private String inviteFriends;

	/**
	 * 已接受的人列表
	 */
	private String reachedFriendsList;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
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
	 * @return the cityID
	 */
	public int getCityID() {
		return cityID;
	}

	/**
	 * @param cityID
	 *            the cityID to set
	 */
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}

	/**
	 * @return the towerID
	 */
	public int getTowerID() {
		return towerID;
	}

	/**
	 * @param towerID
	 *            the towerID to set
	 */
	public void setTowerID(int towerID) {
		this.towerID = towerID;
	}

	/**
	 * @return the needFriend
	 */
	public int getNeedFriend() {
		return needFriend;
	}

	/**
	 * @param needFriend
	 *            the needFriend to set
	 */
	public void setNeedFriend(int needFriend) {
		this.needFriend = needFriend;
	}

	/**
	 * @return the reachedFriend
	 */
	public int getReachedFriend() {
		return reachedFriend;
	}

	/**
	 * @param reachedFriend
	 *            the reachedFriend to set
	 */
	public void setReachedFriend(int reachedFriend) {
		this.reachedFriend = reachedFriend;
	}

	/**
	 * @return the inviteFriends
	 */
	public String getInviteFriends() {
		return inviteFriends;
	}

	/**
	 * @param inviteFriends
	 *            the inviteFriends to set
	 */
	public void setInviteFriends(String inviteFriends) {
		this.inviteFriends = inviteFriends;
	}

	/**
	 * @return the reachedFriendsList
	 */
	public String getReachedFriendsList() {
		return reachedFriendsList;
	}

	/**
	 * @param reachedFriendsList
	 *            the reachedFriendsList to set
	 */
	public void setReachedFriendsList(String reachedFriendsList) {
		this.reachedFriendsList = reachedFriendsList;
	}

}

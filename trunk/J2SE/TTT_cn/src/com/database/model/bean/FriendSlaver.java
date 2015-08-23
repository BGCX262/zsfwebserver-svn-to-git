package com.database.model.bean;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class FriendSlaver implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private long masterID;
	private long friendID;
	private long escapeTime;
	private int speedUp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getMasterID() {
		return masterID;
	}

	public void setMasterID(long masterID) {
		this.masterID = masterID;
	}

	public long getFriendID() {
		return friendID;
	}

	public void setFriendID(long friendID) {
		this.friendID = friendID;
	}

	public long getEscapeTime() {
		return escapeTime;
	}

	public void setEscapeTime(long escapeTime) {
		this.escapeTime = escapeTime;
	}

	public int getSpeedUp() {
		return speedUp;
	}

	public void setSpeedUp(int speedUp) {
		this.speedUp = speedUp;
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof FriendSlaver)) {
			return false;
		}

		FriendSlaver fs = (FriendSlaver) obj;
		return new EqualsBuilder().append(this.id, fs.getId()).append(this.friendID, fs.getFriendID())
				.append(this.escapeTime, fs.getEscapeTime()).isEquals();

	}

	public int hashCode() {
		return new HashCodeBuilder().append(this.id).append(this.friendID).append(this.getEscapeTime()).toHashCode();
	}
}

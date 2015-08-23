package com.mina.pool.response;

import java.util.Iterator;
import java.util.LinkedList;

import com.mina.data.User;

/**
 * 可运行的数据包
 * @author zsf
 * 2011-4-14 下午02:46:07
 */
public class RunnableResponsePacket implements Runnable {

	private LinkedList<User> users;

	private Object data;
	
	public LinkedList<User> getUsers() {
		return users;
	}

	public void setUsers(LinkedList<User> users) {
		this.users = users;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}
	
	@Override
	public void run() {
		for (Iterator<User> iter = users.iterator(); iter.hasNext(); ) {
			iter.next().getSession().write(data);
		}
	}
	
	public RunnableResponsePacket()	 {}
	
	public RunnableResponsePacket(LinkedList<User> users, Object data) {
		this.users = users;
		this.data = data;
	}

}

package com.mina.server.room;

import java.util.LinkedList;
import java.util.Map;

import com.mina.data.User;
import com.mina.framework.AbstractRoom;


/**
 * @author zsf
 * 2011-4-12 上午11:40:21
 */
public class SimpleRoom extends AbstractRoom {
	
	private LinkedList<User> loginUsers = new LinkedList<User>();
	
	private LinkedList<User> registUsers = new LinkedList<User>();

	/**
	 * @return the loginUsers
	 */
	public LinkedList<User> getLoginUsers() {
		return loginUsers;
	}

	/**
	 * @param loginUsers the loginUsers to set
	 */
	public void setLoginUsers(LinkedList<User> loginUsers) {
		this.loginUsers = loginUsers;
	}

	public LinkedList<User> getRegistUsers() {
		return registUsers;
	}

	public void setRegistUsers(LinkedList<User> registUsers) {
		this.registUsers = registUsers;
	}

	@Override
	protected void initStringInvocation() {
		stringInvocationMap.put("1", "com.mina.server.handler.RegistHandler");
		stringInvocationMap.put("2", "com.mina.server.handler.LoginHandler");
		stringInvocationMap.put("3", "com.mina.server.handler.SayHandler");
	}

	@Override
	public void handleInnerEvent(Map<String, Object> args, String event) {
		if (event.equals("user_exit")) {
			User user = (User) args.get("user");
			loginUsers.remove(user);
			System.out.println(user.getName() + "离开了房间");
		}
	}

}

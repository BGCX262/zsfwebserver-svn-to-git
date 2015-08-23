package com.mina.server.handler;

import com.mina.data.User;
import com.mina.data.UserVar;
import com.mina.framework.AbstractRoom;
import com.mina.framework.IStringRequestHandler;
import com.mina.server.room.SimpleRoom;
import com.mina.utils.StringPackageUtil;


public class LoginHandler implements IStringRequestHandler {
	
	private SimpleRoom room = null;

	public void setRoom(AbstractRoom room) {
		this.room = (SimpleRoom) room;
	}

	@Override
	public void onRequest(User user, String content) {
		try {
			
			String [] args = StringPackageUtil.getArgs(content, ",", 2);
			
			boolean flag = true;
			for (User u : room.getRegistUsers()) {
				if (u.getName().equals(args[0]) && u.getUserVar("password").getString().equals(args[1])) {
					flag = false;
					break;
				}
			}
			
			if (flag || room.getLoginUsers().contains(user)) {
				room.sendResponse("5,0", user);
				return;
			}
			
			if (user.getName() == null && user.getUserVar("password") == null) {
				user.setName(args[0]);
				user.addUserVar("password", new UserVar(args[1]));
			}
			
			room.sendResponse("5,1", user);
			room.sendResponse("6," + user.getName() + "进入了房间", room.getLoginUsers());
			
			room.getLoginUsers().add(user);
			System.out.println(user.getName() + "用户进入了房间");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

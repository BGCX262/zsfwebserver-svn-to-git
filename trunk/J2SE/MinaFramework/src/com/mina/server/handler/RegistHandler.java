package com.mina.server.handler;

import com.mina.data.User;
import com.mina.data.UserVar;
import com.mina.framework.AbstractRoom;
import com.mina.framework.IStringRequestHandler;
import com.mina.server.room.SimpleRoom;
import com.mina.utils.StringPackageUtil;


public class RegistHandler implements IStringRequestHandler {
	
	private SimpleRoom room = null;

	public void setRoom(AbstractRoom room) {
		this.room = (SimpleRoom) room;
	}

	@Override
	public void onRequest(User user, String content) {
		try {
			
			String[] args = StringPackageUtil.getArgs(content, ",", 2);
			if (!room.getRegistUsers().contains(user)) {
				user.setName(args[0]);
				user.addUserVar("password", new UserVar(args[1]));
				
				room.getRegistUsers().add(user);
				
				room.sendResponse("4,1", user);
				
			} else {
				room.sendResponse("4,0", user);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

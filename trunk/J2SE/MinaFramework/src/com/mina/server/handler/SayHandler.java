package com.mina.server.handler;

import com.mina.data.User;
import com.mina.framework.AbstractRoom;
import com.mina.framework.IStringRequestHandler;
import com.mina.server.room.SimpleRoom;


public class SayHandler implements IStringRequestHandler {
	
	private SimpleRoom room = null;

	public void setRoom(AbstractRoom room) {
		this.room = (SimpleRoom) room;
	}

	public void onRequest(User user, String content) {
		try {
			
			room.sendResponse("6," + user.getName() + ": " + content, room.getLoginUsers());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

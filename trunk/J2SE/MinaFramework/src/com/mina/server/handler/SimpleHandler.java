package com.mina.server.handler;

import com.mina.data.User;
import com.mina.framework.AbstractRoom;
import com.mina.framework.IStringRequestHandler;
import com.mina.server.room.SimpleRoom;


public class SimpleHandler implements IStringRequestHandler {
	
	private SimpleRoom room = null;

	public void setRoom(AbstractRoom room) {
		this.room = (SimpleRoom) room;
	}

	@Override
	public void onRequest(User user, String content) {
		System.out.println("join~~~~");
		room.hashCode();
	}

}

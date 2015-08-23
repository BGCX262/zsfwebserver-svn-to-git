package com.mina.pool.request;

import java.util.Arrays;

import net.sf.json.JSONObject;

import com.mina.data.User;
import com.mina.utils.session.handler.DefaultSessionHandler;

/**
 * 可运行的数据包
 * @author zsf
 * 2011-4-14 下午02:46:07
 */
public class RunnableRequestPacket implements Runnable {

	private User user;

	private Object data;

	public void setUser(User user) {
		this.user = user;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public User getUser() {
		return user;
	}

	public Object getData() {
		return data;
	}
	
	@Override
	public void run() {
		/* String */
		if (DefaultSessionHandler.PROTOCOL_TYPE.equals("string")) {
			String msg = data.toString();
			int index = msg.indexOf(",");
			String cmd = msg.substring(0, index);
			String content = msg.substring(index + 1);
			DefaultSessionHandler.ROOM.handleStringRequest(cmd, content, getUser());
		}

		/* JSON */
		else if (DefaultSessionHandler.PROTOCOL_TYPE.equals("json")) {
			JSONObject jso = JSONObject.fromObject(data);
			DefaultSessionHandler.ROOM.handleJSONRequest(jso.remove("c").toString(), jso, getUser());
		}
		
		/* byte */
		else if (DefaultSessionHandler.PROTOCOL_TYPE.equals("byte")) {
			byte[] bytes = (byte[]) data;
			byte[] cmd = Arrays.copyOf(bytes, 4);
			DefaultSessionHandler.ROOM.handleByteRequest(cmd, bytes, user);
		}
	}
	
	public RunnableRequestPacket()	 {}
	
	public RunnableRequestPacket(User user, Object data) {
		this.user = user;
		this.data = data;
	}

}

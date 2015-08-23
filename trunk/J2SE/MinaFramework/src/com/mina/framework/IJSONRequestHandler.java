package com.mina.framework;

import com.mina.data.User;

import net.sf.json.JSONObject;


/**
 * 用户请求处理类接口
 * @author zsf
 * 2011-4-12 上午10:25:27
 */
public interface IJSONRequestHandler {
	
	public void setRoom(AbstractRoom room);
	
	public void onRequest(User user, JSONObject json);

}

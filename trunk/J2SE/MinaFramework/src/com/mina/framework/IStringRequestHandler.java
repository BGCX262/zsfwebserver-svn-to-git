package com.mina.framework;

import com.mina.data.User;


/**
 * 用户请求处理类接口
 * @author zsf
 * 2011-4-12 上午10:25:27
 */
public interface IStringRequestHandler {
	
	public void setRoom(AbstractRoom room);
	
	public void onRequest(User user, String content);

}

package com.ttt.handlers;

import com.ttt.cmd.SimpleClient;

import net.sf.json.JSONObject;

/**
 * 处理类集成此方法
 * @author zsf
 * 2011-12-21 上午10:40:38
 */
public abstract class AbstractCallback extends AbstractHandler {
	
	protected SimpleClient client;
	
	public abstract void callback(long id, JSONObject json);
	
	public void setClient(SimpleClient client) {
		this.client = client;
	}

}

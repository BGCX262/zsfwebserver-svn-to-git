package com.ttt.handlers;

import net.sf.json.JSONObject;

import com.ttt.cmd.SimpleClient;
import com.ttt.util.Cmd;


public abstract class AbstractResponseHandler extends AbstractHandler {
	
	protected SimpleClient client;
	
	public abstract JSONObject handlerRequest(long id, Cmd cmd);
	
	public void setClient(SimpleClient client) {
		this.client = client;
	}
	
}

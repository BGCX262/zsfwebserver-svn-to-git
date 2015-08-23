package com.ttt.handlers;

import com.ttt.cmd.SimpleClient;


public abstract class AbstractRequestHandler extends AbstractHandler {
	
	protected SimpleClient client;
	
	public abstract byte[] getMsg(long uid, Object... params);
	
	public void setClient(SimpleClient client) {
		this.client = client;
	}

}

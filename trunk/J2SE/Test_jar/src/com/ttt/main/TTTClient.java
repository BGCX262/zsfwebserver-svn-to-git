package com.ttt.main;

import com.ttt.cmd.SimpleClient;
import com.ttt.util.LoadAllClassUtil;


public class TTTClient extends SimpleClient {
	
	public TTTClient(String ipAddr, int port, long[] uids) {
		super(ipAddr, port, uids);
	}

	@Override
	protected void otherInvocation() {
		LoadAllClassUtil.load(callbackInvocationMap, "/com/ttt/handlers/callback");
	}

}

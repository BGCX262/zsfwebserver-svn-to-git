package com.ttt.handlers.callback;

import net.sf.json.JSONObject;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractCallback;

public class GetTimeCallback extends AbstractCallback {

	@Override
	public void callback(long id, JSONObject json) {
		try {
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getCommand() {
		return Command.GETTIME;
	}

}

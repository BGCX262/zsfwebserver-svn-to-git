package com.ttt.handlers.callback;

import net.sf.json.JSONObject;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractCallback;


public class StartPveCallback extends AbstractCallback {

	@Override
	public void callback(long id, JSONObject json) {
		System.out.println(json);
		client.sendMsg(id, Command.PVE_RESULT, json.getInt("timesId"));
	}

	@Override
	public byte[] getCommand() {
		return Command.START_PVE;
	}

}

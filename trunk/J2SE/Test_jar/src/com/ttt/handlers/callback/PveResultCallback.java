package com.ttt.handlers.callback;

import net.sf.json.JSONObject;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractCallback;


public class PveResultCallback extends AbstractCallback {

	@Override
	public void callback(long id, JSONObject json) {
		System.out.println(json);
	}

	@Override
	public byte[] getCommand() {
		return Command.PVE_RESULT;
	}

}

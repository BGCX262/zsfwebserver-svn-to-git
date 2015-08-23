package com.ttt.handlers.callback;

import net.sf.json.JSONObject;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractCallback;


public class CanLoginCallback extends AbstractCallback {

	@Override
	public void callback(long id, JSONObject json) {
		try {
			
			int code = json.getInt("code");
			if (code == 0) {
				client.sendMsg(id, Command.LOGIN);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getCommand() {
		return Command.CAN_LOGIN;
	}

}

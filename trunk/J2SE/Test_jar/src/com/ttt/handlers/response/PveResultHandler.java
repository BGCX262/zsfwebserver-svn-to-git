package com.ttt.handlers.response;

import net.sf.json.JSONObject;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractResponseHandler;
import com.ttt.util.Cmd;


public class PveResultHandler extends AbstractResponseHandler {

	@Override
	public JSONObject handlerRequest(long id, Cmd cmd) {
		JSONObject json = new JSONObject();
		try {
			json.put("success", cmd.readByte(10) == 0);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}

	@Override
	public byte[] getCommand() {
		return Command.PVE_RESULT;
	}

}

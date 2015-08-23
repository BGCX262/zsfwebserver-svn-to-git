package com.ttt.handlers.response;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractResponseHandler;
import com.ttt.util.Cmd;


public class CanLoginHandler extends AbstractResponseHandler {
	
	private static final Log LOG = LogFactory.getLog(CanLoginHandler.class);
	
	@Override
	public JSONObject handlerRequest(final long id, Cmd cmd) {
		JSONObject jso = new JSONObject();
		try {
			int code = cmd.readByte(10);
			jso.put("code", code);

		} catch (Exception e) {
			LOG.error(e, e);
		}
		return jso;
	}

	@Override
	public byte[] getCommand() {
		return Command.CAN_LOGIN;
	}

}

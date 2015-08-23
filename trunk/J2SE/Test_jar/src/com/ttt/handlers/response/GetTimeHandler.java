package com.ttt.handlers.response;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractResponseHandler;
import com.ttt.util.Cmd;


public class GetTimeHandler extends AbstractResponseHandler {
	
	private static final Log LOG = LogFactory.getLog(GetTimeHandler.class);

	@Override
	public JSONObject handlerRequest(final long id, Cmd cmd) {
		JSONObject jso = new JSONObject();
		try {
			long time = cmd.readLong(10);
			jso.put("time", time);

		} catch (Exception e) {
			LOG.error(e, e);
		}
		return jso;
	}

	@Override
	public byte[] getCommand() {
		return Command.GETTIME;
	}

}

package com.ttt.handlers.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractResponseHandler;
import com.ttt.util.Cmd;


public class ReceiveGoodHandler extends AbstractResponseHandler {
	
	private static final Log LOG = LogFactory.getLog(AbstractResponseHandler.class);
	
	public static long length = 0;
	public static long inCount = 0;
	public static long outCount = 0;

	@Override
	public JSONObject handlerRequest(final long id, Cmd cmd) {
		length += cmd.length();
		inCount++;
		JSONObject jso = new JSONObject();
		try {
			int count = cmd.readInt(10);
			JSONArray goods = new JSONArray();
			for (int i = 0; i < count; i++) {
				JSONObject temp = new JSONObject();
				temp.put("goodId", cmd.readInt());
				temp.put("num", cmd.readInt());
				goods.add(temp);
			}
			jso.put("goods", goods);
		} catch (Exception e) {
			LOG.error(e, e);
		}
		return jso;
	}

	@Override
	public byte[] getCommand() {
		return Command.RECEIVE_GOOD;
	}

}

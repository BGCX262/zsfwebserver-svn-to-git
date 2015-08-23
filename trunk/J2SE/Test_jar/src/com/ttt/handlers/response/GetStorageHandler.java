package com.ttt.handlers.response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractResponseHandler;
import com.ttt.util.Cmd;


public class GetStorageHandler extends AbstractResponseHandler {
	
	private static final Log LOG = LogFactory.getLog(GetStorageHandler.class);
	
	public static long length = 0;
	public static long inCount = 0;
	public static long outCount = 0;

	@Override
	public JSONObject handlerRequest(final long id, Cmd cmd) {
		length += cmd.length();
		inCount++;
		JSONObject jso = new JSONObject();
		try {
			jso.put("coin", cmd.readInt(10));
			jso.put("systemCash", cmd.readLong());
			jso.put("rock", cmd.readInt());
			jso.put("metal", cmd.readInt());
			jso.put("crystal", cmd.readInt());
			jso.put("soul", cmd.readInt());
			jso.put("badge", cmd.readInt());
			jso.put("storageCount", cmd.readInt());
			
			int boxNum = cmd.readInt();
			JSONArray boxs = new JSONArray();
			for (int i = 0; i < boxNum; i++) {
				JSONObject temp = new JSONObject();
				temp.put("boxId", cmd.readInt());
				temp.put("type", cmd.readInt());
				temp.put("validTime", cmd.readLong());
				temp.put("isLocked", cmd.readByte() == 1);
				temp.put("goodId", cmd.readInt());
				int num = cmd.readInt();
				temp.put("num", num);
				JSONArray markIds = new JSONArray();
				for (int j = 0; j < num; j++) {
					markIds.add(cmd.readInt());
				}
				temp.put("markIds", markIds);
				boxs.add(temp);
			}
			jso.put("boxs", boxs);
			
		} catch (Exception e) {
			LOG.error(e, e);
		}
		return jso;
	}

	@Override
	public byte[] getCommand() {
		return Command.GET_STORAGE;
	}

}

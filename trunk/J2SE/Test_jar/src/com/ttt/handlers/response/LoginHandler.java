package com.ttt.handlers.response;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractResponseHandler;
import com.ttt.util.Cmd;
import com.ttt.util.DataFactory;


public class LoginHandler extends AbstractResponseHandler {
	
	private static final Log LOG = LogFactory.getLog(LoginHandler.class);
	
	public static long length = 0;
	public static long inCount = 0;
	public static long outCount = 0;

	@Override
	public JSONObject handlerRequest(final long id, Cmd cmd) {
		length += cmd.length();
		inCount++;
		JSONObject json = new JSONObject();
		try {
			//json.put("code", cmd.readByte(10));
			json.put("isFirst", cmd.readByte() == 1);
			json.put("time", cmd.readLong());
			json.put("pvpTime", cmd.readLong());
			json.put("sound", cmd.readByte());
			json.put("music", cmd.readByte());
			json.put("isShowLevel", cmd.readByte());
			json.put("limitSlaver", cmd.readInt());
			json.put("race", cmd.readInt());
			json.put("energy", cmd.readInt());
			json.put("lastRecEnergyTime", cmd.readLong());
			json.put("energyLimit", cmd.readInt());
			json.put("glory", cmd.readInt());
			json.put("isVip", cmd.readByte());
			json.put("coin", cmd.readInt());
			json.put("systemCash", cmd.readLong());
			json.put("cash", cmd.readLong());
			json.put("rock", cmd.readInt());
			json.put("metal", cmd.readInt());
			json.put("crystal", cmd.readInt());
			json.put("soul", cmd.readInt());
			json.put("badge", cmd.readInt());
			json.put("maxCityLevel", cmd.readInt());
			json.put("nowTitle", cmd.readInt());
			int titleCount = cmd.readInt();
			JSONArray titles = new JSONArray();
			for (int i = 0; i < titleCount; i++) {
				titles.add(cmd.readInt());
			}
			json.put("titles", titles);
			int slaverCount = cmd.readInt();
			JSONArray slavers = new JSONArray();
			for (int i = 0; i < slaverCount; i++) {
				JSONObject temp = new JSONObject();
				temp.put("slaverId", cmd.readInt());
				temp.put("slaverType", cmd.readInt());
				temp.put("friendId", cmd.readLong());
				temp.put("escapeTime", cmd.readLong());
				temp.put("speedUp", cmd.readInt());
				int state = cmd.readInt();
				temp.put("state", state);
				if (state == 1) {
					temp.put("endTime", cmd.readLong());
					temp.put("wordId", cmd.readInt());
				}
				slavers.add(temp);
			}
			json.put("slavers", slavers);
			
		} catch (Exception e) {
			LOG.error(e, e);
		}
		return json;
	}

	@Override
	public byte[] getCommand() {
		return Command.LOGIN;
	}
	
	public static void main(String[] args) {
		String str = "00006072ef164a73420080da6d2e49734201010104000000010000000f00000000b0f2b2474973421e0000003f00000001fb999800000000000000494000000000d0126341f8979800fb979800069898001e00000000000000020000000000000000000000";
		byte[] b = new byte[str.length() / 2];
		for (int i = 0; i < str.length() / 2; i++) {
			b[i] = (byte)Integer.parseInt(new String(new char[] {str.charAt(i * 2), str.charAt(i * 2 + 1)}), 16);
		}
		System.out.println(new LoginHandler().handlerRequest(0, Cmd.getInstance(b)));;
	}

}

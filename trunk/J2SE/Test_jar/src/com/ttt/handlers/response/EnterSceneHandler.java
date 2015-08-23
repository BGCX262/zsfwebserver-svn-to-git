package com.ttt.handlers.response;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractResponseHandler;
import com.ttt.util.Cmd;


public class EnterSceneHandler extends AbstractResponseHandler {
	
	private static final Log LOG = LogFactory.getLog(EnterSceneHandler.class);
	
	public static long length = 0;
	public static long inCount = 0;
	public static long outCount = 0;

	@Override
	public JSONObject handlerRequest(final long id, Cmd cmd) {
		length += cmd.length();
		inCount++;
		JSONObject jso = new JSONObject();
		try {
			int code = cmd.readByte(10);
			jso.put("success", code == 0);
			if (code == 0) {
				jso.put("masterID", cmd.readLong());
				jso.put("isFriendSlaver", cmd.readByte());
				jso.put("storageSurplus", cmd.readInt());
				jso.put("recivePresentLevel", cmd.readInt());
				jso.put("level", cmd.readInt());
				jso.put("name", cmd.readString());
				jso.put("hp", cmd.readInt());
				int state = cmd.readInt();
				jso.put("state", state);
				if (state != 0) {
					jso.put("upgradeEndTime", cmd.readLong());
					jso.put("repairHp", cmd.readInt());
				}
				jso.put("cityLevel", cmd.readInt());
				jso.put("cityId", cmd.readInt());
				jso.put("currentTimes", cmd.readInt());
				int towerCount = cmd.readInt();
				JSONArray towers = new JSONArray();
				for (int i = 0; i < towerCount; i++) {
					JSONObject temp = new JSONObject();
					temp.put("towerId", cmd.readInt());
					temp.put("towerType", cmd.readInt());
					temp.put("towerPos", cmd.readInt());
					temp.put("level", cmd.readInt());
					temp.put("hp", cmd.readInt());
					int tstate = cmd.readInt();
					temp.put("state", tstate);
					if (tstate != 0)
						temp.put("endTime", cmd.readLong());
					temp.put("inviteFriends", cmd.readString());
					towers.add(temp);
				}
				jso.put("towers", towers);
				
				int pkgCount = cmd.readInt();
				JSONArray pkgs = new JSONArray();
				for (int i = 0; i < pkgCount; i++) {
					JSONObject temp = new JSONObject();
					temp.put("packId", cmd.readInt());
					temp.put("type", cmd.readInt());
					temp.put("dropTime", cmd.readLong());
					int goodsCount = cmd.readInt();
					JSONArray goods = new JSONArray();
					for (int j = 0; j < goodsCount; j++) {
						JSONObject t = new JSONObject();
						t.put("goodId", cmd.readInt());
						t.put("num", cmd.readInt());
						goods.add(t);
					}
					temp.put("goods", goods);
					pkgs.add(temp);
				}
				jso.put("packages", pkgs);
			}

		} catch (Exception e) {
			LOG.error(e, e);
		}
		return jso;
	}

	@Override
	public byte[] getCommand() {
		return Command.ENTER_SCENE;
	}

}

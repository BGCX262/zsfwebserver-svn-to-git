package com.ttt.handlers.response;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractResponseHandler;
import com.ttt.util.Cmd;


public class StartPveHandler extends AbstractResponseHandler {
	
	private static final Log LOG = LogFactory.getLog(StartPveHandler.class);

	@Override
	public JSONObject handlerRequest(final long id, Cmd cmd) {
		JSONObject jso = new JSONObject();
		try {
			int code = cmd.readByte(10);
			jso.put("success", code == 0);
			if (code == 0) {
				jso.put("timesId", cmd.readInt());
				int mCount = cmd.readInt();
				JSONArray monsters = new JSONArray();
				for (int i = 0; i < mCount; i++) {
					JSONObject temp = new JSONObject();
					temp.put("id", cmd.readInt());
					temp.put("goodId", cmd.readInt());
					temp.put("hp", cmd.readInt());
					temp.put("glory", cmd.readInt());
					int dropNum = cmd.readInt();
					JSONArray drops = new JSONArray();
					for (int j = 0; j < dropNum; j++) {
						JSONObject drop = new JSONObject();
						drop.put("goodId", cmd.readInt());
						drop.put("num", cmd.readInt());
						drops.add(drop);
					}
					temp.put("drops", drops);
					monsters.add(temp);
				}
				jso.put("monsters", monsters);
			}

		} catch (Exception e) {
			LOG.error(e, e);
		}
		return jso;
	}

	@Override
	public byte[] getCommand() {
		return Command.START_PVE;
	}

}

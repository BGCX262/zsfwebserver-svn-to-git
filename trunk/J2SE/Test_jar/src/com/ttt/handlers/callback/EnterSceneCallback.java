package com.ttt.handlers.callback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractCallback;


public class EnterSceneCallback extends AbstractCallback {
	
	private static final Log LOG = LogFactory.getLog(EnterSceneCallback.class);
	
	@Override
	public void callback(long id, JSONObject json) {
		try {
			
			LOG.info(json);
			client.sendMsg(id, Command.GET_STORAGE);
			//client.sendMsg(id, Command.START_PVE, json.getInt("currentTimes"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getCommand() {
		return Command.ENTER_SCENE;
	}

}

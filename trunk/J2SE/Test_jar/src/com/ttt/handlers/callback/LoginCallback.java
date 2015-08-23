package com.ttt.handlers.callback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractCallback;

public class LoginCallback extends AbstractCallback {
	
	private static final Log LOG = LogFactory.getLog(LoginCallback.class);

	@Override
	public void callback(long id, JSONObject json) {
		try {
			LOG.info(json);
			client.sendMsg(id, Command.ENTER_SCENE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getCommand() {
		return Command.LOGIN;
	}

}

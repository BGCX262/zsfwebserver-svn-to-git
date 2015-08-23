package com.ttt.handlers.callback;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONObject;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractCallback;


public class GetStorageCallback extends AbstractCallback {
	
	private static final Log LOG = LogFactory.getLog(GetStorageCallback.class);

	@Override
	public void callback(long id, JSONObject json) {
		try {
			
			LOG.info(json);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getCommand() {
		return Command.GET_STORAGE;
	}

}

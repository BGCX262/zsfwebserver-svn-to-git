package com.ttt.handlers.request;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractRequestHandler;
import com.ttt.util.Cmd;


public class EnterSceneHandler extends AbstractRequestHandler {
	
	@Override
	public byte[] getMsg(long uid, Object... params) {
		com.ttt.handlers.response.EnterSceneHandler.outCount++;
		Cmd cmd = Cmd.getInstance();
		cmd.appendAll(new byte[] { (byte) 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00 });
		cmd.appendLong(uid);
		return cmd.getResponse();
	}

	@Override
	public byte[] getCommand() {
		return Command.ENTER_SCENE;
	}

}

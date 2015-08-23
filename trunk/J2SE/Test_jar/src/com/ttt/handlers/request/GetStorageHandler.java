package com.ttt.handlers.request;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractRequestHandler;
import com.ttt.util.Cmd;


public class GetStorageHandler extends AbstractRequestHandler {

	@Override
	public byte[] getMsg(long uid, Object... params) {
		com.ttt.handlers.response.GetStorageHandler.outCount++;
		Cmd cmd = Cmd.getInstance();
		cmd.appendAll(new byte[] { (byte) 0x05, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00 });
		return cmd.getResponse();
	}

	@Override
	public byte[] getCommand() {
		return Command.GET_STORAGE;
	}

}

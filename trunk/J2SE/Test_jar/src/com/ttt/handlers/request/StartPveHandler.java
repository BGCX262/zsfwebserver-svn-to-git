package com.ttt.handlers.request;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractRequestHandler;
import com.ttt.util.Cmd;


public class StartPveHandler extends AbstractRequestHandler {

	@Override
	public byte[] getMsg(long uid, Object... params) {
		if (params.length < 1 || !(params[0] instanceof Integer))
			throw new RuntimeException("StartPveHandler params error!! params[0] must a integer");
		Cmd cmd = Cmd.getInstance();
		cmd.appendAll(new byte[] { (byte) 0x13, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00 });
		cmd.appendInt((Integer) params[0]);
		return cmd.getResponse();
	}

	@Override
	public byte[] getCommand() {
		return Command.START_PVE;
	}

}

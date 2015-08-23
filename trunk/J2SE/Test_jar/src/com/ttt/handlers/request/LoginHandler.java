package com.ttt.handlers.request;

import com.ttt.cmd.Command;
import com.ttt.handlers.AbstractRequestHandler;
import com.ttt.util.Cmd;


public class LoginHandler extends AbstractRequestHandler {

	@Override
	public byte[] getMsg(long uid, Object... params) {
		com.ttt.handlers.response.LoginHandler.outCount++;
		Cmd cmd = Cmd.getInstance();
		cmd.appendAll(new byte[] { 0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00 });
		cmd.appendLong(uid);
		cmd.appendString("test");
		cmd.appendByte((byte) 0x00);
		cmd.appendString("http://www.baidu.com/");
		cmd.appendString("e52d3e12ec1ec1aedc7b7a77d5b7dedb+test");
		return cmd.getResponse();
	}

	@Override
	public byte[] getCommand() {
		return Command.LOGIN;
	}

}

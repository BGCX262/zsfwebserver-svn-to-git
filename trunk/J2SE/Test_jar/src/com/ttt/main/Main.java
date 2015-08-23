package com.ttt.main;

import com.ttt.cmd.Command;
import com.ttt.cmd.SimpleClient;

/**
 * 程序入口
 * @author zsf
 * 2011-12-9 下午12:08:18
 */
public class Main {
	
	public static void main(String[] args) {
		/*long[] uids = new long[500];
		for (int i = 0; i < uids.length; i++) {
			uids[i] = 60000000 + i;
		}*/
		long[] uids = new long[] { 11559900 };
		SimpleClient client = new TTTClient("127.0.0.1", 8700, uids);
		client.sendMsg(Command.LOGIN);
	}

}

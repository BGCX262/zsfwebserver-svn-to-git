package com.ttt.cmd;

/**
 * 协议映射
 * @author zsf
 * 2011-12-10 上午10:48:02
 */
public class Command {
	
	public static final byte[] CAN_LOGIN = new byte[] { (byte) 0x86, 0x00 };
	
	public static final byte[] LOGIN = new byte[] { 0x02, 0x00 };

	public static final byte[] GETTIME = new byte[] { (byte) 0xF2, 0x00 };

	public static final byte[] ENTER_SCENE = new byte[] { (byte) 0x04, 0x00 };

	public static final byte[] GET_STORAGE = new byte[] { (byte) 0x06, 0x00 };

	public static final byte[] RECEIVE_GOOD = new byte[] { (byte) 0x72, 0x00 };

	public static final byte[] START_PVE = new byte[] { (byte) 0x14, 0x00 };

	public static final byte[] PVE_RESULT = new byte[] { (byte) 0x79, 0x00 };

}

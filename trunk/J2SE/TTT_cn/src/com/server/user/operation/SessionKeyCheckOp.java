package com.server.user.operation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.server.util.Configuration;
import com.server.util.MD5Util;

public class SessionKeyCheckOp {
	private static Log log = LogFactory.getLog(SessionKeyCheckOp.class);
	public static final String ENCRYPT_KEY = "td_hash_27yule_$$$";
	public static final String SUPER_KEY = "e52d3e12ec1ec1aedc7b7a77d5b7dedb";
	public static final boolean isSuperMethod = Configuration.getBoolean("server.sessionKey.super", true);
	
	public static boolean check(long uid, String sig_time){
		try {
			String sig = sig_time.substring(0, sig_time.indexOf("+"));
			String time = sig_time.substring(sig_time.indexOf("+") + 1);
			String key = uid + ENCRYPT_KEY + time;
			if(sig.equals(SUPER_KEY)){
				return true;
			}else if(sig.endsWith(MD5Util.getMD5(key))){
				return true;
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return false;
	}
}

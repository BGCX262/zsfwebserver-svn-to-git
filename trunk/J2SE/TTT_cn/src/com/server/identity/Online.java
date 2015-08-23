package com.server.identity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.database.hibernate.util.MemcacheUtil;

public class Online {
	private static Log log = LogFactory.getLog(Identityer.class);
	private static IMemcachedCache memCachedClient = MemcacheUtil.getOnlineMemcached();
	private static int expiry = 3600 * 24;
	public static void online(long id){
		try{
			memCachedClient.put("online:" + id, true, expiry);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	public static void offline(long id){
		try{
			memCachedClient.put("online:" + id, false, expiry);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	public static boolean isOnline(long id){
		try{
			Object obj = memCachedClient.get("online:" + id);
			if(obj != null){
				return Boolean.valueOf(obj.toString());
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return false;
	}
}

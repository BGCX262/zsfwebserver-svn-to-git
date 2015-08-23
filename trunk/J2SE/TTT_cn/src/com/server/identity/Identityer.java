package com.server.identity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.database.hibernate.util.MemcacheUtil;

public class Identityer {
	private static Log log = LogFactory.getLog(Identityer.class);
	private static IMemcachedCache memCachedClient = MemcacheUtil.getOnlineMemcached();
	private static int expiry = 3 * 24 * 3600;
	
	public static boolean identity(long id, long identityCode) {
		try{
			String key = String.valueOf("identityCode:" + id);
			Object codeStr = memCachedClient.get(key);
			if(codeStr != null){
				Long code = Long.valueOf(codeStr.toString());
				if(code.equals(identityCode)){
					return true;
				}else{
					return false;
				}
			}else{
				createIdentity(id);
				return true;
			}
		}catch(Exception e){
			log.info(e,e);
			return true;
		}
	}
	
	public static long createIdentity(long id){
		long identityCode = System.nanoTime();
		try{
			String key = String.valueOf("identityCode:" + id);
			long value = identityCode;
			memCachedClient.put(key, value, expiry);
		}catch(Exception e){
			log.info(e,e);
		}
		return identityCode;
	}
}

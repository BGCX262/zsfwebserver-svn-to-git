package com.database.hibernate.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;
import com.server.util.Configuration;

public class MemcacheUtil {

	private static boolean myspaceVersion = Configuration.isMyspaceVersion();
	private static final Log log = LogFactory.getLog(MemcacheUtil.class);
	private static ICacheManager<IMemcachedCache> manager;
	static {
		setUpBeforeClass();
	}

	public static void setUpBeforeClass() {
		try {
			manager = CacheUtil.getCacheManager(IMemcachedCache.class,
					MemcachedCacheManager.class.getName());
			manager.setResponseStatInterval(5 * 1000);
			if (myspaceVersion) {
				manager.setConfigFile("myspace memcached.xml");
			} else {
				manager.setConfigFile("facebook memcached.xml");
			}
			manager.start();
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public static void tearDownAfterClass() throws Exception {
		manager.stop();
	}

	public static IMemcachedCache getOnlineMemcached() {
		return getIMemcachedCacheByName("online");
	}

	public static IMemcachedCache getStealMemcached() {
		return getIMemcachedCacheByName("steal");
	}

	private static IMemcachedCache getIMemcachedCacheByName(String name) {
		if (manager != null && name != null) {
			IMemcachedCache onlineQueue = manager.getCache(name);
			if (onlineQueue != null) {
				return onlineQueue;
			}
		}
		log.error("Name of " + name + " Memcached is not exist or is null");
		return null;
	}
}

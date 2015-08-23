import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.Date;

import com.alisoft.xplatform.asf.cache.ICacheManager;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.alisoft.xplatform.asf.cache.memcached.CacheUtil;
import com.alisoft.xplatform.asf.cache.memcached.MemcachedCacheManager;
import com.danga.MemCached.Logger;
import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;

/**
 * 使用memcached的缓存实用类.
 */
public class TestMemcached {

	// 创建全局的唯一实例
	protected static IMemcachedCache mcc;

	protected static TestMemcached memCached = new TestMemcached();

	// 设置与缓存服务器的连接池
	static {
		try {
			ICacheManager<IMemcachedCache> manager = CacheUtil.getCacheManager(IMemcachedCache.class, 
				MemcachedCacheManager.class.getName());
			manager.setResponseStatInterval(5 * 1000);
			manager.setConfigFile("memcached.xml");
			manager.start();
			mcc = manager.getCache("online");
			Field log = MemCachedClient.class.getDeclaredField("log");
			log.setAccessible(true);
			Logger logs = (Logger) log.get(mcc);
			logs.setLevel(Logger.LEVEL_ERROR);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 服务器列表和其权重
		String[] servers = { "127.0.0.1:11211" };

		// 获取socke连接池的实例对象
		SockIOPool pool = SockIOPool.getInstance();

		// 设置服务器信息
		pool.setServers(servers);

		// 设置初始连接数、最小和最大连接数以及最大处理时间
		pool.setInitConn(5);
		pool.setMinConn(5);
		pool.setMaxConn(250);
		pool.setFailover(true);

		// 设置主线程的睡眠时间
		pool.setMaintSleep(0);

		// 设置TCP的参数，连接超时等
		pool.setNagle(false);
		pool.setSocketTO(3000);
		pool.setAliveCheck(true);

		// 初始化连接池
		//pool.initialize();

		// 压缩设置，超过指定大小（单位为K）的数据都会被压缩
		//mcc.setCompressEnable(true);
		//mcc.setCompressThreshold(64 * 1024);
	}

	/**
	 * 保护型构造方法，不允许实例化！
	 * 
	 */
	protected TestMemcached() {

	}

	/**
	 * 获取唯一实例.
	 * 
	 * @return
	 */
	public static TestMemcached getInstance() {
		return memCached;
	}

	/**
	 * 添加一个指定的值到缓存中.
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Object add(String key, Object value) {
		return mcc.put(key, value);
	}

	public boolean add(String key, Object value, Date expiry) {
		return mcc.add(key, value, expiry);
	}

	public boolean replace(String key, Object value) {
		return mcc.replace(key, value);
	}

	public boolean replace(String key, Object value, Date expiry) {
		return mcc.replace(key, value, expiry);
	}

	public Object delete(String key) {
		return mcc.remove(key);
	}

	/**
	 * 根据指定的关键字获取对象.
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return mcc.get(key);
	}

	public static void main(String[] args) throws Exception {
		long begin = System.currentTimeMillis();
		/*for (int i = 0; i < 10000; i++) {
			mcc.put("i", i);
			if ((Integer) mcc.get("i") != i) {
				System.err.println(i);
			}
		}*/
		BufferedWriter fw = new BufferedWriter(new FileWriter("d:/1.xml"));
		fw.write(new String(Decompress.decompress(Decompress.simpleDecrypt((byte[]) mcc.get("bean2.zip"))), "utf8"));
		fw.close();
		long end = System.currentTimeMillis();
		System.out.println(end - begin + "ms");
	}
}
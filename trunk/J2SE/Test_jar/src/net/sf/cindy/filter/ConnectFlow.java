package net.sf.cindy.filter;

import java.net.SocketAddress;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.cindy.util.Configuration;
import net.sf.cindy.util.InternalCindyTimer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * IP restrict by time alternation request connect count and had connected
 * number.
 * 
 * @author XiaoJun
 * 
 */
public class ConnectFlow {

	private static final String[] superPrivileges = Configuration.getPrivilege() != null ? Configuration.getPrivilege().split(",") : null;
	private static ConcurrentHashMap<String, ConnectFlow> instanceMap = new ConcurrentHashMap<String, ConnectFlow>();
	private static final long CLEAR_TIME = Configuration.geClearTimeAlternation();
	private static final Log log = LogFactory.getLog(ConnectFlow.class);
	private int maxForceoutCount = Configuration.getMaxForceoutCount();
	private static boolean enable = Configuration.isConnectFlow();
	private AtomicInteger forceoutCount;
	private String host;
	private long time;

	static {
		if (enable) {
			long period = Math.max(CLEAR_TIME / 10, 5000);
			InternalCindyTimer.getInstance().schedule(new CheckRemove(), period, period, TimeUnit.MILLISECONDS);
		}
	}

	private static class CheckRemove implements Runnable {
		public void run() {
			try {
				long currentTime = System.currentTimeMillis();
				Iterator<String> ite = instanceMap.keySet().iterator();
				while (ite.hasNext()) {
					ConnectFlow next = instanceMap.get(ite.next());
					long alternation = currentTime - next.time;
					if (alternation > CLEAR_TIME) {
						ite.remove();
					}
				}
			} catch (Exception e) {
				log.error(e, e);
			}
		}
	}

	public static ConnectFlow getInstance(String host) {
		ConnectFlow instance = instanceMap.get(host);
		if (instance != null) {
			return instance;
		} else {
			ConnectFlow value = new ConnectFlow(host);
			ConnectFlow previous = instanceMap.putIfAbsent(host, value);
			return previous == null ? value : previous;
		}
	}

	private void initial() {
		forceoutCount = new AtomicInteger();
		time = System.currentTimeMillis();
	}

	public void resetInitial() {
		forceoutCount.set(0);
		time = System.currentTimeMillis();
	}

	private ConnectFlow(String host) {
		this.host = host;
		initial();
	}

	private ConnectFlow(String ipAddress, int maxForceoutCount) {
		this.host = ipAddress;
		this.maxForceoutCount = maxForceoutCount;
		initial();
	}

	/**
	 * compare two socketAddress's IP whether equal.
	 * 
	 * @param address
	 * @return
	 */
	public String compareSocketAddress(SocketAddress address) {
		String ip = getRelativeAddress(address);
		return compareSocketAddress(ip);
	}

	public static String getRelativeAddress(SocketAddress address) {
		if (address != null) {
			String ip = address.toString().split(":")[0].substring(1);
			if (ip.length() < 7) {
				throw new IllegalStateException("It's error that distill IP from InetSocketAddress" + address);
			}
			return ip;
		}
		return null;
	}

	private String compareSocketAddress(String address) {
		if (address.equals(host)) {
			return address;
		}
		return null;
	}

	private boolean compareSuperPrivilege(SocketAddress address) {
		String ip = getRelativeAddress(address);
		for (int i = 0; i < superPrivileges.length; i++) {
			if (ip.matches(superPrivileges[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This operate will check concurrent connector whether is checked request
	 * connect count is too much and had connected number much more restrict.
	 * 
	 * @param address
	 * @return
	 */
	public boolean addConnector(SocketAddress address) {
		forceoutCount.incrementAndGet();
		int forceout = forceoutCount.get();
		if (superPrivileges != null) {
			if (compareSuperPrivilege(address)) {
				log.info(getHostDesc(forceout) + " has super privilege.");
				return true;
			}
			log.info(getHostDesc(forceout) + " hasn't super privilege.");
			return false;
		} else {
			if (forceout >= maxForceoutCount) {
				log.warn(getHostDesc(forceout) + " request too much,and please pay a attention!");
				return false;
			}
		}
		log.info(getHostDesc(forceout));
		return true;
	}

	public static boolean isReliance(SocketAddress address) {
		String relativeAddress = getRelativeAddress(address);
		if (enable) {
			if (relativeAddress != null) {
				ConnectFlow instance = getInstance(relativeAddress);
				return instance.addConnector(address);
			}
			return false;
		} else {
			log.info(relativeAddress + "[" + 0 + "]");
			return true;
		}
	}

	private String getHostDesc(int currentCount) {
		return host + "[" + currentCount + "]";
	}
}

package com.server.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.connect.instance.Instance;
import com.cindy.run.connect.instance.InstanceFactory;
import com.cindy.run.util.DataFactory;
import com.server.user.operation.MemcacheOp;


public class LoadMemcachedUtil {
	
	private static final Log LOG = LogFactory.getLog(LoadMemcachedUtil.class);
	private static final InstanceFactory FACTORY = InstanceFactory.getFactory();
	private static final byte[] MSG = new byte[] { (byte) 0x99,(byte) 0x99,(byte) 0x99,0x08,0x01,0x00,0x07,0x00,0x0f,0x0f };
	private static final Timer TIMER = new Timer(true);
	private static final long DELAY_TIME = 1000 * 60 * 5;
	
	private static List<Long> load() {
		List<Long> rtnVal = new LinkedList<Long>();
		try {
			String userStr = MemcacheOp.getUsers();
			if (userStr != null) {
				String[] userArr = userStr.split(",");
				for (int i = 0; i < userArr.length; i++) {
					rtnVal.add(Long.valueOf(userArr[i]).longValue());
				}
			}
		} catch (Exception e) {
			LOG.error(e, e);
		}
		return rtnVal;
	}
	
	private static void reset(List<Long> users) {
		try {
			StringBuffer sb = new StringBuffer();
			for (Iterator<Long> iter = users.iterator(); iter.hasNext(); ) {
				sb.append(iter.next() + ",");
			}
			if (sb.length() > 0) {
				MemcacheOp.setUsers(sb.substring(0, sb.length() - 1));
			}
		} catch (Exception e) {
			LOG.error(e, e);
		}
	}
	
	private static List<Long> send(List<Long> users) {
		List<Long> rtnVal = new LinkedList<Long>(users);
		try {
			ConcurrentHashMap<Object, Instance> instances = FACTORY.getInstances();
			for (Iterator<Object> iter = instances.keySet().iterator(); iter.hasNext(); ) {
				Instance next = instances.get(iter.next());
				long uid = DataFactory.doubleBytesToLong((byte[]) next.getAttribute("uid"));
				if (users.contains(uid)) {
					next.send(MSG);
					rtnVal.remove(uid);
				}
			}
		} catch (Exception e) {
			LOG.error(e, e);
		}
		return rtnVal;
	}
	
	public static void init() {
		try {
			TIMER.schedule(new TimerTask() {
				public void run() {
					try {
						List<Long> users = load();
						List<Long> results = send(users);
						reset(results);
					} catch (Exception e) {
						LOG.error(e, e);
					}
				}
			}, DELAY_TIME, DELAY_TIME);
		} catch (Exception e) {
			LOG.error(e, e);
		}
	}
	
}

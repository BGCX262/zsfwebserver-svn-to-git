package com.mina.userMgr;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.apache.mina.util.SynchronizedQueue;

import com.mina.data.User;
import com.mina.utils.Configuration;

/**
 * 用户工厂(池)
 * @author zsf
 * 2011-4-11 上午09:49:07
 */
public class UserFactory {
	
	public static final Logger log = Logger.getLogger(UserFactory.class);
	
	protected static final int INIT_USER_SIZE = Configuration.getInt("factory.default.init.size", 10000);
	
	private static SynchronizedQueue<User> idleUserQueue = new SynchronizedQueue<User>(new ConcurrentLinkedQueue<User>());
	
	private static SynchronizedQueue<User> busyUserQueue = new SynchronizedQueue<User>(new ConcurrentLinkedQueue<User>());
	
	public static final byte[] lock = new byte[0];
	
	private static int user_id = 0;
	
	static {
		for (int i = 0; i < INIT_USER_SIZE; i++) {
			idleUserQueue.offer(new User(user_id++));
		}
	}
	
	/**
	 * 获取空闲用户
	 * @return
	 */
	public static User getIdleUser() {
		User user = null;
		synchronized (lock) {
			user = idleUserQueue.poll();
			if (user == null) {
				user = new User(user_id++);
			}
			busyUserQueue.add(user);
		}
		
		return user.clearData();
	}
	
	/**
	 * 将用户放置到空闲用户中
	 * @param user
	 */
	public static void freeUser(User user) {
		synchronized (lock) {
			boolean flag = busyUserQueue.remove(user);
			if (!flag)
				log.error("free user failed!!");
			else
				idleUserQueue.offer(user);
		}
		
	}

}

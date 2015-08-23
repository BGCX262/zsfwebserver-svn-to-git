package com.database.hibernate.util;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cindy.run.util.ThreadPoolExecutorTimer;
import com.database.model.bean.User;
import com.server.util.Configuration;

public class HibernateUtil {
	private static final ThreadPoolExecutorTimer TIMER = ThreadPoolExecutorTimer.getIntance();
	private static final ConcurrentHashMap<Long, User> DBIndexMap = new ConcurrentHashMap<Long, User>();
	private static HibernateFactory[] hibernates = new HibernateFactory[Configuration.getDbindex()];
	private static final Log log = LogFactory.getLog(HibernateUtil.class);
	private static final int dbNumber;  
	private static List maxDBUesr;
	static {
		for (int i = 0; i < hibernates.length; i++) {
			hibernates[i] = new HibernateFactory("config/hibernate_" + (i + 1) + ".cfg.xml");
		}
		Session session = hibernates[0].currentSession();
		SQLQuery query = session.createSQLQuery("select distinct sid from user");
		dbNumber = query.list().size();
		if (Configuration.getDbindex() - 1 != dbNumber) {//except user index database;
			log.error("Hibernate configuration is reduced,it's no support!");
			System.exit(0);
		}
		query = session.createSQLQuery("select sid from user group by sid order by count(*)");
		maxDBUesr = query.list();
		if (query.list().size() == dbNumber) {
			
		} else if (query.list().size() < dbNumber){
			log.warn("Assert database increase.....");
		} else {
			log.error("Hibernate configuration is reduced,it's no support!");
			System.exit(0);
		}
		closeSession(session);
		try{
			TIMER.getPreciseTimer().scheduleAtFixedRate(new Runnable(){
				@Override
				public void run() {
					try{
						if(DBIndexMap.size() > 100000){
							DBIndexMap.clear();
						}
					}catch(Exception e){
						log.error(e, e);
					}
				}
			}, 100, 12 * 3600000, TimeUnit.MILLISECONDS);
		}catch(Exception e){
			log.error(e, e);
		}
	}

	public static Session currentSession(long facebookId) {
		Session session;
		if (DBIndexMap.containsKey(facebookId)) {
			User user = DBIndexMap.get(facebookId);
			session = hibernates[user.getSid()].currentSession();
		} else {
			session = hibernates[0].currentSession();
			SQLQuery query = session.createSQLQuery("select * from user where facebook_user_id = " + facebookId);
			query.addEntity(User.class);
			User user = (User) query.uniqueResult();
			closeSession(session);
			session = hibernates[user.getSid()].currentSession();
			DBIndexMap.putIfAbsent(facebookId, user);
		}
		return session;
	}

	public static Session getDefaultSession() {
		return hibernates[1].currentSession();
	}
	
	public static Session getOneSession(int sid) {
		return hibernates[sid].currentSession();
	}

	public static Session checkExistCurrentSession(long facebookId) {
		User user = existUser(facebookId);
		if (!DBIndexMap.containsKey(facebookId) && user == null) {
			log.info("Create database index");
			user = creatUser(facebookId);
			Session session = hibernates[user.getSid()].currentSession();
			DBIndexMap.putIfAbsent(facebookId, user);
			return session;
		} else {
			if (user != null) {
				DBIndexMap.putIfAbsent(facebookId, user);
			}
			return currentSession(facebookId);
		}
	}
	
	
	private static User creatUser(long facebookId) {
		Random random = new Random();
		Session session = hibernates[0].currentSession();
		int n = Math.max(0, hibernates.length - 1);
		int index = (int) (random.nextBoolean() ? 0 : random.nextInt(n));
		User user = new User();
		user.setFacebook_user_id(facebookId);
		user.setSid((Integer) maxDBUesr.get(index));
		Transaction trancation = session.beginTransaction();
		session.save(user);
		trancation.commit();
		closeSession(session);
		return user;
	}

	private static User existUser(long facebookId) {
		Session session = hibernates[0].currentSession();
		SQLQuery query = session.createSQLQuery("select * from user where facebook_user_id = " + facebookId);
		query.addEntity(User.class);
		User user = (User) query.uniqueResult();
		closeSession(session);
		if (user != null) {
			return user;
		}
		return null;
	}

	public static void closeSession(Session session) throws HibernateException {
		if (session != null){
			for (int i = 0; i < hibernates.length; i++) {
				hibernates[i].closeSession(session);
			}
		}
	}
}


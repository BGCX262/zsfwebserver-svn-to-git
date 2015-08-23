package com.database.hibernate.util;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.database.model.bean.UserStorage;


public class HibernateFactory {

	public final ThreadLocal<Session> sessions = new ThreadLocal<Session>();
	private static final Log log = LogFactory.getLog(HibernateFactory.class);
	public SessionFactory sessionFactory;
	
	
	
	public HibernateFactory(String fileName) {
		fileName = fileName == null ? "config/hibernate.cfg.xml" : fileName;
		try {
			File configFile = new File(fileName);
			log.info("Init hibernate accord " + configFile.getAbsolutePath());
			Configuration configuration = new Configuration().configure(configFile);
			sessionFactory = configuration.buildSessionFactory();
		} catch (Throwable ex) {
			log.error("Initial SessionFactory creation failed.", ex);
			throw new ExceptionInInitializerError(ex);
			
		}
	}
	

	public Session currentSession() throws HibernateException {
		long time = System.currentTimeMillis();
		Session session = (Session) sessions.get();
		if (session == null) {
			session = sessionFactory.openSession();
			sessions.set(session);
		}
		long spendTime = System.currentTimeMillis() - time;
		if (spendTime > 50) {
			log.info("Get database connect time: " + spendTime);
		}
		return session;
	}

	public void closeSession(Session session) throws HibernateException {
		Session localSession = (Session) sessions.get();
		if (localSession != null && session != null && session.equals(localSession)){
			session.close();
		}
		sessions.set(null);
	}
}


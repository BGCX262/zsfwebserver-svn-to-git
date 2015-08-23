package com.server.gameDate.statistics;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.database.hibernate.gameDate.util.HibernateGameDateFactory;
import com.database.hibernate.gameDate.util.HibernateGameLogFactory;
import com.database.hibernate.util.HibernateUtil;

public class DBOperateUtil {
	private static Log log = LogFactory.getLog(DBOperateUtil.class);
	
	public static void execute(String executeQuery) throws Exception {
		Session session = null;
		try {
			session = HibernateGameDateFactory.currentSession();
			Transaction tr = session.beginTransaction();
			SQLQuery query = session.createSQLQuery(executeQuery);
			query.executeUpdate();
			tr.commit();
		} catch (Exception e) {
			log.error(e,e);
		}
		HibernateGameDateFactory.closeSession(session);
	}
	
	public static Object getObject(String executeQuery) throws Exception  {
		Object obj = null;
		Session session = null;
		try {
			session = HibernateGameDateFactory.currentSession();
			SQLQuery query = session.createSQLQuery(executeQuery);
			obj = query.uniqueResult();
		} catch (Exception e) {
			log.error(e,e);
		}
		HibernateGameDateFactory.closeSession(session);
		return obj;
	}
	
	public static void execute(long master,String executeQuery) throws Exception {
		Session session = null;
		try {
			session = HibernateUtil.currentSession(master);
			Transaction tr = session.beginTransaction();
			SQLQuery query = session.createSQLQuery(executeQuery);
			query.executeUpdate();
			tr.commit();
		} catch (Exception e) {
			log.error(e,e);
		}finally{
			HibernateUtil.closeSession(session);
		}
	}
	
	public static Object getObject(long master,String executeQuery) throws Exception  {
		Object obj = null;
		Session session = null;
		try {
			session = HibernateUtil.currentSession(master);
			SQLQuery query = session.createSQLQuery(executeQuery);
			obj = query.uniqueResult();
		} catch (Exception e) {
			log.error(e,e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return obj;
	}
	
	public static Object getEntity(long master,String executeQuery,Class clazz) throws Exception  {
		Object obj = null;
		Session session = null;
		try {
			session = HibernateUtil.currentSession(master);
			SQLQuery query = session.createSQLQuery(executeQuery).addEntity(clazz);
			obj = query.uniqueResult();
		} catch (Exception e) {
			log.error(e,e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return obj;
	}
	
	public static List<Object> getList(long master,String executeQuery,Class clazz) throws Exception  {
		List<Object> list = null;
		Session session = null;
		try {
			session = HibernateUtil.currentSession(master);
			SQLQuery query = session.createSQLQuery(executeQuery).addEntity(clazz);
			list = query.list();
		} catch (Exception e) {
			log.error(e,e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return list;
	}
	
	public static void executeLog(String executeQuery) throws Exception {
		Session session = null;
		try {
			session = HibernateGameLogFactory.currentSession();
			Transaction tr = session.beginTransaction();
			SQLQuery query = session.createSQLQuery(executeQuery);
			query.executeUpdate();
			tr.commit();
		} catch (Exception e) {
			log.error(e,e);
			throw new Exception(e.getMessage());
		}finally{
			HibernateGameLogFactory.closeSession(session);
		}
	}
}

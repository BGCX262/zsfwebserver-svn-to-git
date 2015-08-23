package com.server.util;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.database.hibernate.util.HibernateUtil;

public class DBUtil {
	private static Log log = LogFactory.getLog(DBUtil.class);
	
	private static void saveUnit(long id, Object obj) throws Exception{
		if(obj != null){
			Session session = null;
			Transaction tr = null;
			try{
				session = HibernateUtil.currentSession(id);
				tr = session.beginTransaction(); 
				session.save(obj);
				tr.commit();
			}catch(Exception e){
				tr.rollback();
				throw new Exception("save exception");
			}finally{
				HibernateUtil.closeSession(session);
			}
		}
	}
	
	public static void save(long id, Object obj){
		try{
			saveUnit(id, obj);
		}catch(Exception e){
			merge(id, obj);
		}
	}
	
	public static Serializable saveAndReturn(long id, Object obj) {
		Serializable rtnVal = null;
		if(obj != null){
			Session session = null;
			Transaction tr = null;
			try{
				session = HibernateUtil.currentSession(id);
				tr = session.beginTransaction(); 
				rtnVal = session.save(obj);
				tr.commit();
			}catch(Exception e){
				tr.rollback();
				log.error(e, e);
			}finally{
				HibernateUtil.closeSession(session);
			}
		}
		return rtnVal;
	}
	
	public static void update(long id, Object obj){
		try{
			updateUnit(id, obj);
		}catch(Exception e){
			merge(id, obj);
		}
	}
	
	public static void saveOrUpdate(long id, Object obj){
		try{
			if(obj != null){
				Session session = null;
				Transaction tr = null;
				try{
					session = HibernateUtil.currentSession(id);
					tr = session.beginTransaction(); 
					session.saveOrUpdate(obj);
					tr.commit();
				}catch(Exception e){
					tr.rollback();
					throw new Exception("update exception");
				}finally{
					HibernateUtil.closeSession(session);
				}
			}
		}catch(Exception e){
			merge(id, obj);
		}
	}
	
	private static void updateUnit(long id, Object obj) throws Exception{
		if(obj != null){
			Session session = null;
			Transaction tr = null;
			try{
				session = HibernateUtil.currentSession(id);
				tr = session.beginTransaction(); 
				session.update(obj);
				tr.commit();
			}catch(Exception e){
				tr.rollback();
				throw new Exception("update exception");
			}finally{
				HibernateUtil.closeSession(session);
			}
		}
	}
	
	public static void merge(long id, Object obj){
		if(obj != null){
			Session session = null;
			Transaction tr = null;
			try{
				session = HibernateUtil.currentSession(id);
				tr = session.beginTransaction(); 
				session.merge(obj);
				tr.commit();
			}catch(Exception e){
				tr.rollback();
				log.error(e, e);
			}finally{
				HibernateUtil.closeSession(session);
			}
		}
	}
	
	public static Object checkGet(long id, Class clazz){
		Object obj = null;
		Session session = null;
		try{
			session = HibernateUtil.checkExistCurrentSession(id);
			obj = session.get(clazz, id);
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return obj;
	}
	
	public static Object get(long id, Class clazz){
		Object obj = null;
		Session session = null;
		try{
			session = HibernateUtil.currentSession(id);
			obj = session.get(clazz, id);
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return obj;
	}
	
	public static Object get(long id, Object obj){
		Object rtn = null;
		Session session = null;
		try{
			session = HibernateUtil.currentSession(id);
			rtn = session.get(obj.getClass(), (Serializable) obj);
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return rtn;
	}
	
	public static Object doGet(long id, long fromId, Class clazz){
		Object obj = null;
		Session session = null;
		try{
			session = HibernateUtil.currentSession(id);
			obj = session.get(clazz, fromId);
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return obj;
	}
	
	public static void delete(long id, Object obj){
		Session session = null;
		Transaction tr = null;
		try{
			session = HibernateUtil.currentSession(id);
			tr = session.beginTransaction();
			session.delete(obj);
			tr.commit();
		}catch(Exception e){
			tr.rollback();
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
	}
	
	public static void executeUpdate(long id, String sql){
		Session session = null;
		try{
			session = HibernateUtil.currentSession(id);
			Transaction tr = session.beginTransaction();
			session.createSQLQuery(sql).executeUpdate();
			tr.commit();
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
	}
	
	public static List query(long id, String sql){
		List List = null;
		Session session = null;
		try{
			session = HibernateUtil.currentSession(id);
			List = session.createSQLQuery(sql).list();
			HibernateUtil.closeSession(session);
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return List;
	}
	
	public static List namedQuery(long id, String sql){
		List List = null;
		Session session = null;
		try{
			session = HibernateUtil.currentSession(id);
			List = session.createQuery(sql).list();
			HibernateUtil.closeSession(session);
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return List;
	}
}

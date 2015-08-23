/**
 * @author zsf
 * 2011-9-20 下午04:51:38
 */
package com.server.database;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Database Helper
 */
public abstract class DBHelperImpl extends HibernateDaoSupport implements IDBHelper {

	/*
	 * (non-Javadoc)
	 * @see com.server.database.IDBHelper#save(java.lang.Object)
	 */
	public Serializable save(Object obj) {
		return getHibernateTemplate().save(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see com.server.database.IDBHelper#saveOrUpdate(java.lang.Object)
	 */
	public void saveOrUpdate(Object obj) {
		getHibernateTemplate().saveOrUpdate(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see com.server.database.IDBHelper#update(java.lang.Object)
	 */
	public void update(Object obj) {
		getHibernateTemplate().update(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see com.server.database.IDBHelper#delete(java.lang.Object)
	 */
	public void delete(Object obj) {
		getHibernateTemplate().delete(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see com.server.database.IDBHelper#deleteAll(java.util.List)
	 */
	public void deleteAll(List<?> list) {
		getHibernateTemplate().deleteAll(list);
	}

	/*
	 * (non-Javadoc)
	 * @see com.server.database.IDBHelper#get(java.lang.Class,
	 * java.io.Serializable)
	 */
	public Object get(Class<?> entityClass, Serializable id) {
		return getHibernateTemplate().get(entityClass, id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.server.database.IDBHelper#query(java.lang.String)
	 */
	public List<?> query(String hql) {
		return getHibernateTemplate().findByNamedQuery(hql);
	}

	/*
	 * (non-Javadoc)
	 * @see com.server.database.IDBHelper#pageQuery(java.lang.String, int, int)
	 */
	public List<?> pageQuery(final String hql, final int offset, final int length) {
		return getHibernateTemplate().executeFind(new HibernateCallback<Object>() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				List<?> list2 = session.createQuery(hql).setFirstResult(offset).setMaxResults(length).list();
				return list2;
			}
		});
	}

}

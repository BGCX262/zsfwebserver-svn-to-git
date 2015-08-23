package com.server.cache;

import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.MemoryBean;
import com.database.model.bean.UserStorage;
import com.database.model.bean.UserStorages;
import com.server.util.Configuration;

public class Storage implements Cache {
	private static Log log = LogFactory.getLog(Storage.class);
	private static final int STORAGE_CACHE_TIME = Configuration.getStoCacTime();
	private static final int FRIEND_STORAGE_CACHE_TIME = Configuration
			.getFriStoCacTime();
	private static final int MASTER_STORAGE_CACHE_TIME = Configuration
			.getMasStoCacTime();

	public static void saveOrUpdate(MemoryBean bean) {
		if (bean != null && bean.canWrite()) {
			UserStorages storages = bean.getStorages();
			if (storages != null) {
				Session session = HibernateUtil
						.currentSession(storages.getId());
				Transaction tr = session.beginTransaction();
				try {
					List<UserStorage> list = storages.getStorage();
					int count = 0;
					Iterator<UserStorage> ite = list.iterator();
					while (ite.hasNext()) {
						UserStorage storage = ite.next();
						if (storage != null) {
							if (storage.getChange() == 1) {
								session.save(storage);
								count++;
							} else if (storage.getChange() == 2) {
								session.saveOrUpdate(storage);
								count++;
							} else if (storage.getChange() == 3) {
								session.delete(storage);
								ite.remove();
								count++;
							}
						}
						if (count > 0 && count % 20 == 0) {
							session.flush();
							session.clear();
						}
					}
					tr.commit();
					Iterator<UserStorage> ite2 = list.iterator();
					while (ite2.hasNext()) {
						UserStorage storage = ite2.next();
						if (storage != null) {
							storage.setChange(0);
						}
					}
				} catch (Exception e) {
					log.error(e, e);
					tr.rollback();
				} finally {
					try {
						HibernateUtil.closeSession(session);
					} catch (Exception e) {
						log.error(e, e);
					}
				}

			}
		}
	}

	@Override
	public void clean(MemoryBean bean) {
		if(bean != null){
			List<UserStorage> storageList = bean.getStorages().getStorage();
			if(storageList != null){
				Iterator<UserStorage> ite = storageList.iterator();
				while(ite.hasNext()){
					UserStorage storage = ite.next();
					if(storage.getValidTime() < System.currentTimeMillis()){
						storage.setChange(3);
					}
					if(storage.getChange() != 3 && storage.getNum() != JSONArray.fromObject(storage.getMarkIDs()).size()){
						storage.setNum(JSONArray.fromObject(storage.getMarkIDs()).size());
						storage.setChange(2);
					}
				}
			}
		}
	}

	@Override
	public Object gerFromDB(long id, Object para) {
		return null;
	}

	@Override
	public Object getFromDB(long id) {
		UserStorages storages = null;
		Session session = null;
		try {
			session = HibernateUtil.currentSession(id);
			SQLQuery query = session
					.createSQLQuery("select * from userstorage where masterID =:id");
			query.addEntity(UserStorage.class);
			query.setLong("id", id);
			List<UserStorage> list = query.list();
			if (list != null && list.size() > 0) {
				storages = new UserStorages();
				storages.setId(id);
				storages.setStorage(list);
				storages.setTime(System.currentTimeMillis());
			}
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			HibernateUtil.closeSession(session);
		}
		return storages;
	}

	@Override
	public void persist(MemoryBean bean) {
		try {
			if (bean != null) {
				UserStorages storage = bean.getStorages();
				if (storage != null) {
					if (System.currentTimeMillis() - storage.getTime() > STORAGE_CACHE_TIME) {
						saveOrUpdate(bean);
					}
					if (System.currentTimeMillis() - storage.getTime() > MASTER_STORAGE_CACHE_TIME
							&& bean.isMaster()) {
						saveOrUpdate(bean);
						bean.setStorages(null);
					}
					if (System.currentTimeMillis() - storage.getTime() > FRIEND_STORAGE_CACHE_TIME
							&& !bean.isMaster()) {
						bean.setStorages(null);
					}
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	@Override
	public void update(MemoryBean bean) {
		saveOrUpdate(bean);
	}
}

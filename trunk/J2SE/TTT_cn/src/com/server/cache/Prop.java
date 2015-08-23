package com.server.cache;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.MemoryBean;
import com.database.model.bean.UserProp;
import com.database.model.bean.UserProps;

public class Prop implements Cache{
	private static Log log = LogFactory.getLog(Prop.class);
	private static final int PROP_CACHE_TIME = 15 * 60000;
	private static final int MASTER_PROP_CACHE_TIME = 30 * 60000;
	
	@Override
	public void clean(MemoryBean bean) {
		if(bean != null){
			UserProps props = bean.getUserProps();
			if(props != null){
				List<UserProp> propList = props.getUsedProps();
				if(propList != null){
					Iterator<UserProp> ite = propList.iterator();
					while(ite.hasNext()){
						UserProp prop = ite.next();
						if(prop.getExpiration() < System.currentTimeMillis()){
							prop.setChange(3);
						}
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
		UserProps props = null;
		Session session = null;
		try{
			session = HibernateUtil.currentSession(id);
			SQLQuery query = session.createSQLQuery("select * from userprop where masterID =:id");
			query.addEntity(UserProp.class);
			query.setLong("id", id);
			List<UserProp> list = query.list();
			if(list != null && list.size() > 0){
				props = new UserProps();
				props.setId(id);
				props.setUsedProps(list);
				props.setTime(System.currentTimeMillis());
			}
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return props;
	}

	@Override
	public void persist(MemoryBean bean) {
		try{
			if(bean != null){
				UserProps props = bean.getUserProps();
				if(props != null){
					if(System.currentTimeMillis() - props.getTime() > PROP_CACHE_TIME){
						update(bean);
					}
					if(System.currentTimeMillis() - props.getTime() > MASTER_PROP_CACHE_TIME
							&& bean.isMaster()){
						update(bean);
						bean.setUserProps(null);
					}
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
	}

	@Override
	public void update(MemoryBean bean) {

		if (bean != null && bean.canWrite()) {
			UserProps props = bean.getUserProps();
			if(props != null){
				Session session = HibernateUtil.currentSession(props.getId());
				Transaction tr = session.beginTransaction();
				try {
					List<UserProp> list = props.getUsedProps();
					int count = 0;
					Iterator<UserProp> ite = list.iterator();
					while(ite.hasNext()){
						UserProp prop = ite.next();
						if(prop != null){
							if(prop.getChange() == 1){
								session.save(prop);
								count++;
							}else if(prop.getChange() == 2){
								session.update(prop);
								count++;
							}else if(prop.getChange() == 3){
								session.delete(prop);
								ite.remove();
								count++;
							}
							
						}
						if(count > 0 && count % 20 == 0){
							session.flush();
					        session.clear();
						}
					}
					tr.commit();
					Iterator<UserProp> ite2 = list.iterator();
					while(ite2.hasNext()){
						UserProp prop = ite2.next();
						if(prop != null){
							prop.setChange(0);
						}
					}
				} catch (Exception e) {
					log.error(e, e);
					tr.rollback();
				} finally {
					try{
						HibernateUtil.closeSession(session);
					}catch(Exception e){
						log.error(e, e);
					}
				}
			
			}
		}
	
	}
}

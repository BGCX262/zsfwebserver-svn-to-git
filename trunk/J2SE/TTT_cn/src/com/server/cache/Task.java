package com.server.cache;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.MemoryBean;
import com.database.model.bean.TaskBean;
import com.database.model.bean.UserTask;
import com.database.model.bean.UserTasks;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.user.operation.TaskOp;
import com.server.util.Configuration;

public class Task implements Cache{
	private static Log log = LogFactory.getLog(Task.class);
	private static final int TASK_CACHE_TIME = Configuration.getTaskCacheTime();
	private static final int MASTER_TASK_CACHE_TIME = Configuration.getMasterTaskCacheTime();
	private static final int FRIEND_TASK_CACHE_TIME = Configuration.getFriendTaskCacheTime();
	
	@Override
	public void clean(MemoryBean bean) {}

	@Override
	public Object gerFromDB(long id, Object para) {
		return null;
	}

	@Override
	public Object getFromDB(long id) {
		UserTasks tasks = new UserTasks();
		tasks.setId(id);
		Session session = null;
		List<UserTask> list =  null;
		try{
			session = HibernateUtil.currentSession(id);
			SQLQuery query = session.createSQLQuery("select * from usertask where masterID = " + id);
			query.addEntity(UserTask.class);
			list = query.list();
			if(list == null){
				list = new LinkedList<UserTask>();
			}
			tasks.setTasks(list);
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		//TaskOp.addFllowTask(id, list);
		return tasks;
	}

	@Override
	public void persist(MemoryBean bean) {
		try{
			if(bean != null){
				UserTasks tasks = bean.getTasks();
				if(tasks != null){
					if(System.currentTimeMillis() - tasks.getTime() > TASK_CACHE_TIME){
						update(bean);
					}
					if(System.currentTimeMillis() - tasks.getTime() > MASTER_TASK_CACHE_TIME
							&& bean.isMaster()){
						update(bean);
						bean.setTasks(null);
					}
					if(System.currentTimeMillis() - tasks.getTime() > FRIEND_TASK_CACHE_TIME
							&& !bean.isMaster()){
						bean.setTasks(null);
					}
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		
	}

	@Override
	public void update(MemoryBean bean) {
		if(bean != null){
			UserTasks tasks = bean.getTasks();
			if(tasks != null){
				Session session = HibernateUtil.currentSession(tasks.getId());
				Transaction tr = session.beginTransaction();
				try {
					List<UserTask> list = tasks.getTasks();
					int count = 0;
					Iterator<UserTask> ite = list.iterator();
					while(ite.hasNext()){
						UserTask task = ite.next();
						if(task != null){
							if(task.getChange() == 1){
								session.save(task);
								count++;
							}else if(task.getChange() == 2){
								session.saveOrUpdate(task);
								count++;
							}else if(task.getChange() == 3){
								session.delete(task);
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
					Iterator<UserTask> ite2 = list.iterator();
					while(ite2.hasNext()){
						UserTask storage = ite2.next();
						storage.setChange(0);
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

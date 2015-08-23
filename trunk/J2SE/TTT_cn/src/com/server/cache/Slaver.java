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
import com.database.model.bean.DropGoodsBean;
import com.database.model.bean.MemoryBean;
import com.database.model.bean.SlaverBean;
import com.database.model.bean.UserSlaver;
import com.database.model.bean.UserSlavers;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.user.operation.MessageOp;
import com.server.util.Configuration;

public class Slaver implements Cache{
	private static Log log = LogFactory.getLog(Slaver.class);
	private static final int SLAVER_CACHE_TIME = Configuration.getSlaCacTime();
	private static final int FRIEND_SLAVER_CACHE_TIME = Configuration.getFriSlaCacTime();
	private static final int MASTER_SLAVER_CACHE_TIME = Configuration.getMasSlaCacTime();
	
	@Override
	public void persist(MemoryBean bean){
		try{
			if(bean != null){
				UserSlavers slavers = bean.getSlavers();
				if(slavers != null){
					if(System.currentTimeMillis() - slavers.getTime() > SLAVER_CACHE_TIME){
						saveOrUpdate(bean);
					}
					if(System.currentTimeMillis() - slavers.getTime() > MASTER_SLAVER_CACHE_TIME
							&& bean.isMaster()){
						saveOrUpdate(bean);
						bean.setSlavers(null);
					}
					if(System.currentTimeMillis() - slavers.getTime() > FRIEND_SLAVER_CACHE_TIME
							&& !bean.isMaster()){
						bean.setSlavers(null);
					}
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
	
	}

	public static void saveOrUpdate(MemoryBean bean) {
		if (bean != null && bean.canWrite()) {
			UserSlavers slavers = bean.getSlavers();
			if (slavers != null) {
				Session session = HibernateUtil.currentSession(slavers.getId());;
				Transaction tr = session.beginTransaction();
				try {
					List<UserSlaver> list = slavers.getSlavers();
					int count = 0;
					Iterator<UserSlaver> ite = list.iterator();
					while (ite.hasNext()) {
						UserSlaver slaver = ite.next();
						if (slaver != null) {
							if (slaver.getChange() == 1) {
								slaver.setId(null);
								session.save(slaver);
								count++;
							} else if (slaver.getChange() == 2) {
								session.saveOrUpdate(slaver);
								count++;
							} else if (slaver.getChange() == 3) {
								session.delete(slaver);
								ite.remove();
								count++;
							}
						}
						if (count % 20 == 0) {
							session.flush();
							session.clear();
						}
					}
					tr.commit();
					Iterator<UserSlaver> ite2 = list.iterator();
					while (ite2.hasNext()) {
						UserSlaver slaver = ite2.next();
						slaver.setChange(0);
					}
				} catch (Exception e) {
					log.error(e, e);
					tr.rollback();
				} finally {
					HibernateUtil.closeSession(session);
				}

			}
		}
	}
	
	@Override
	public void clean(MemoryBean bean) {
		if(bean != null){
			UserSlavers slavers = bean.getSlavers();
			if(slavers != null){
				List<UserSlaver> slaverList = slavers.getSlavers();
				if(slaverList != null){
					Iterator<UserSlaver> slaverIte = slaverList.iterator();
					long currentTime = System.currentTimeMillis();
					List<DropGoodsBean> escapeSlavers = null;
					while(slaverIte.hasNext()){
						UserSlaver slaver = slaverIte.next();
						if(slaver.getState() == 1 && slaver.getEndTime() < currentTime){
							slaver.setState(0);
							slaver.setChange(2);
						} else if (slaver.getState() == 3 && slaver.getEndTime() < currentTime) {
							slaver.setState(4);
							slaver.setChange(2);
						}
						if(slaver.getState() == 0 && slaver.getEscapeTime() < currentTime){
							SlaverBean slaverBean = (SlaverBean) Goods.getById(GoodsCate.SLAVERBEAN, slaver.getSlaverID());
							slaver.setState(2);
							slaver.setChange(3);
							if(escapeSlavers == null){
								escapeSlavers = new LinkedList<DropGoodsBean>();
							}
							addEscapeSlaver(escapeSlavers, slaverBean.getGoodID());
						}
					}
					if(escapeSlavers != null && escapeSlavers.size() > 0){
						MessageOp.createMessage(0, slavers.getId(), 0, 0, 3, escapeSlavers, null, null,null);
					}
				}
			}
		}
	}
	
	public static void addEscapeSlaver(List<DropGoodsBean> escapeSlavers, int goodID){
		boolean isExist = false;
		if(escapeSlavers != null){
			Iterator<DropGoodsBean> ite = escapeSlavers.iterator();
			while(ite.hasNext()){
				DropGoodsBean bean = ite.next();
				if(bean != null && bean.getGoodsID() == goodID){
					bean.setNum(bean.getNum() + 1);
					isExist = true;
					break;
				}
			}
		}
		
		if(!isExist){
			DropGoodsBean escapeSlaver = new DropGoodsBean(goodID, 1);
			escapeSlavers.add(escapeSlaver);
		}
	}

	@Override
	public Object gerFromDB(long id, Object para) {
		return null;
	}

	@Override
	public void update(MemoryBean bean) {
		saveOrUpdate(bean);
	}

	@Override
	public Object getFromDB(long id) {
		UserSlavers slavers = null;
		Session session = null;
		try{
			session = HibernateUtil.currentSession(id);
			SQLQuery query = session.createSQLQuery("select * from userslaver where masterID =" + id);
			query.addEntity(UserSlaver.class);
			List<UserSlaver> list = query.list();
			if(list != null && list.size() > 0){
				slavers = new UserSlavers();
				slavers.setId(id);
				slavers.setSlavers(list);
				slavers.setTime(System.currentTimeMillis());
			}
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return slavers;
	}
}

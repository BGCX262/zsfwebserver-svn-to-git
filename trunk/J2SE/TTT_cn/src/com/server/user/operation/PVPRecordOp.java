package com.server.user.operation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.PVPRecordBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserStorages;
import com.server.cache.Castle;
import com.server.cache.UserMemory;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.util.DBUtil;

public class PVPRecordOp {
	private static Log log = LogFactory.getLog(PVPRecordOp.class);
	private static Finance financeImpl = FinanceImpl.instance();
	
	public static PVPRecordBean getPVPRecord(long id){
		PVPRecordBean record = null;
		record = MemcacheOp.getPVPRecord(id);
		if(record == null){
			record = getPvpRecordFromDB(id);
		}
		if(record == null){
			record = new PVPRecordBean();
			record.setId(id);
			record.setLossGoods(new LinkedList<Integer>());
			record.setUsedProps(new LinkedList<Integer>());
		}
		return record;
	}
	
	private static PVPRecordBean getPvpRecordFromDB(long id) {
		PVPRecordBean record = null;
		try{
			record = (PVPRecordBean)DBUtil.get(id, PVPRecordBean.class);
		}catch(Exception e){
			log.error(e, e);
		}
		return record;
	}

	public static void modifyPVPRecord(PVPRecordBean record){
		try{
			if(record != null){
				MemcacheOp.setPvpRecord(record);
				DBUtil.update(record.getId(), record);
			}
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static void initPVPRecord(long id){
		Session session = null;
		try{
			PVPRecordBean record = new PVPRecordBean();
			record.setId(id);
			session = HibernateUtil.currentSession(record.getId());
			Transaction tr = session.beginTransaction();
			session.update(record);
			tr.commit();
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
	}
	
	public static void sysData(long id){
		try{
			UserCastle castle = UserMemory.getCastle(id);
			PVPRecordBean record = getPVPRecord(id);
			if(record != null){
				int hp = record.getHp();
				if(hp != 0){
					if(castle.getHp() + hp > 0){
						castle.setHp(castle.getHp() + hp);
					}else{
						castle.setHp(0);
					}
					castle.setChange(true);
					Castle.updateImmediately(id);
				}
				
				int coin = record.getCoin();
				int rock = record.getRock();
				int crystal = record.getCrystal();
				int metal = record.getMetal();
				if(coin != 0 || rock != 0 || metal != 0 || crystal != 0){
					FinanceBean bean = new FinanceBean();
					bean.setId(id);
					bean.setCoin(coin);
					bean.setRock(rock);
					bean.setCrystal(crystal);
					bean.setMetal(metal);
					financeImpl.consume(bean);
				}
				
				List<Integer> marks = record.getLossGoods();
				if (marks != null && marks.size() > 0) {
					UserStorages storages = UserMemory.getStorages(id);
					if (storages != null) {
						if (marks != null) {
							Iterator<Integer> ite = marks.iterator();
							while (ite.hasNext()) {
								Integer mark = ite.next();
								StorageOp.removeGoods(id, mark);
								ite.remove();
							}
						}
					}
				}
			
				record.setCoin(0);
				record.setRock(0);
				record.setMetal(0);
				record.setCrystal(0);
				record.setHp(0);
				modifyPVPRecord(record);
			}
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static int sysHp(long id){//查看同步后的玩家血量
		UserMemory.createFriendMem(id);
		int hp = 0;
		try{
			Session session = null;
			try{
				session = HibernateUtil.currentSession(id);
				SQLQuery createSQLQuery = session.createSQLQuery("select hp from usercastle where id = " + id);
				Object uniqueResult = createSQLQuery.uniqueResult();
				if (uniqueResult != null)
					hp = (Integer) uniqueResult;
				HibernateUtil.closeSession(session);
			}catch(Exception e){
				log.error(e, e);
			}finally{
				HibernateUtil.closeSession(session);
			}
			PVPRecordBean record = getPVPRecord(id);
			if(record != null){
				hp += record.getHp();		
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return hp;
	}
}

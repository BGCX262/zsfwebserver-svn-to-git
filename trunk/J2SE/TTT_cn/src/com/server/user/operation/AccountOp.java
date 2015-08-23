package com.server.user.operation;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cindy.run.util.ThreadPoolExecutorTimer;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.CancelAccount;
import com.server.util.Configuration;

public class AccountOp {
	private static Log log = LogFactory.getLog(AccountOp.class);
	private static ConcurrentHashMap<Long, Long> cancelAccount = new ConcurrentHashMap<Long, Long>();
	private static final ThreadPoolExecutorTimer TIMER = ThreadPoolExecutorTimer.getIntance();
	private static final int TIME = Configuration.getTime();
	
	public AccountOp(){
		TIMER.getPreciseTimer().scheduleAtFixedRate(new RefreshCancelAcc(), 10, 5 * 60000, TimeUnit.MILLISECONDS);
	}
	
	private class RefreshCancelAcc implements Runnable{
		public void run() {
			try{
				Session session = HibernateUtil.getDefaultSession();
				SQLQuery query = session.createSQLQuery("select * from cancelaccount where type = 0");
				query.addEntity(CancelAccount.class);
				List<CancelAccount> cancelAccList =  query.list();
				HibernateUtil.closeSession(session);
				cancelAccount.clear();
				if(cancelAccList != null){
					Iterator<CancelAccount> cancelAccIte = cancelAccList.iterator();
					while(cancelAccIte.hasNext()){
						CancelAccount acc = cancelAccIte.next();
						cancelAccount.putIfAbsent(acc.getId(), acc.getId());
					}
				}
			}catch(Exception e){
				log.error(e, e);
			}
		}
	}
	
	public static boolean isLock(long id){
		 return cancelAccount.containsKey(id);
	}
	
	public static void unLock(long id){
		try{
			cancelAccount.remove(id);
			Session session = HibernateUtil.getDefaultSession();
			Transaction tr = session.beginTransaction();
			CancelAccount acc = new CancelAccount();
			acc.setId(id);
			session.delete(acc);
			tr.commit();
			HibernateUtil.closeSession(session);
		}catch(Exception e){
			log.error(e, e);
		}
	}
}

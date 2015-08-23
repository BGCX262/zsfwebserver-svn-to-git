package com.server.gameDate.statistics;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.util.ThreadPoolExecutorTimer;
import com.database.model.bean.SocialGiftCheckroom;

public class SocialGiftReward {
	private static long period = 3600;
	private static int MIN_SIZE = 100;
	private static int MAX_SIZE = 5000;
	private static long initialDelay = 1800;
	private static Log log = LogFactory.getLog(SocialGiftReward.class);
	private static ConcurrentLinkedQueue<Long> receivedQuery = new ConcurrentLinkedQueue<Long>();
	private static ThreadPoolExecutorTimer threadPoolExecutorTimer = ThreadPoolExecutorTimer.getIntance();
	private static SocialGiftReward instance = new SocialGiftReward();

	public static SocialGiftReward getInstance(){
		return instance;
	}
	
	public SocialGiftReward () {
		threadPoolExecutorTimer.getPreciseTimer().scheduleAtFixedRate(new PersistClear(), initialDelay, period, TimeUnit.SECONDS);
	}
	
	private class PersistClear implements Runnable{
		public void run() {
			try {
				if (receivedQuery.size() > MIN_SIZE) {
					clear();
				}else {
					log.info("query size less than 100.not to be clear.");
				}
			} catch (Exception e) {
				log.error(e, e);
			}
		}
	}
	
	public JSONArray getReward(long master){
		JSONArray jsonArray = new JSONArray();
		if (!receivedQuery.contains(master)) {
			try {
				Object obj = selectSocialReward(master);
				if (obj != null) {
					jsonArray.add(new Object[]{Integer.parseInt(obj.toString()),1});
				}
				offer(master);
			} catch (Exception e) {
				log.error(e, e);
			}
		}
		return jsonArray;
	}
	
	private void offer(long master) {
		if (receivedQuery.size() > MAX_SIZE) {
			log.info("query is overflow. need to clear.");
			clear();
		}
		receivedQuery.offer(master);
	}

	private void clear() {
		receivedQuery.clear();
	}

	private Object selectSocialReward(long master) throws Exception{
		String executeQuery = "select goods from t_socialgiftreward where day = current_date and master = " + master + " and validity = 1";
		Object obj = DBOperateUtil.getObject(master, executeQuery);
		if (obj != null) {
			executeQuery = "update t_socialgiftreward set validity = 0 where day = current_date and master = " + master;
			DBOperateUtil.execute(master, executeQuery);
		}
		return obj;
	}
	
	public JSONArray getSocialGiftCheck(long master){
		JSONArray jsonArray = new JSONArray();
		try {
			List<Object> list = selectSocialCheck(master);
			if (list != null) {
				Iterator<Object> ite = list.iterator();
				while (ite.hasNext()) {
					SocialGiftCheckroom check = (SocialGiftCheckroom) ite.next();
					jsonArray.add(new Object[]{check.getGoods(),check.getNum()});
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return jsonArray;
	}
	
	public boolean updateSocialGiftCheck(long master,int goods,int num){
		boolean result = false;
		String executeQuery = "select * from t_socialgiftcheckroom where master = " + master + " and goods = " + goods; 
		try {
			SocialGiftCheckroom bean = (SocialGiftCheckroom) DBOperateUtil.getEntity(master, executeQuery, SocialGiftCheckroom.class);
			if (bean != null && bean.isValidity()) {
				if (bean.getNum() >= num) {
					executeQuery = "update t_socialgiftcheckroom set num = num - " + num + " where master = " + master + " and goods = " + goods;
					if (bean.getNum() == num) {
						executeQuery = "update t_socialgiftcheckroom set num = num - " + num + ",validity = 0 where master = " + master + " and goods = " + goods;
					}
					DBOperateUtil.execute(master, executeQuery);
					result = true;
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return result;
	}
	
	private List<Object> selectSocialCheck(long master) throws Exception {
		String executeQuery = "select * from t_socialgiftcheckroom where master = " + master + " and validity = 1";
		List<Object> list = DBOperateUtil.getList(master, executeQuery,SocialGiftCheckroom.class);
		return list;
	}
}

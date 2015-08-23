package com.server.gameDate.statistics;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.database.model.bean.UserStorage;
import com.server.util.Configuration;

public class GameDateOperate implements GameDateOperateInterf {

	private static final ExecutorService NEW_FIXED_THREAD_POOL = Executors.newFixedThreadPool(10);
	private static final boolean DEBUG = Configuration.getIsWriteGameDate();
	private static Log log = LogFactory.getLog(GameDateOperate.class);
	private static final String TABLENAME_COST_FORMAT = "yyyyMMdd";
	private static final String TABLE_COST_PREFIX = "t_cost_";
	private static final String TABLE_PVPLOG = "t_pvplog";
	private static final int MAX_SIZE = 5000;
	public static ConcurrentHashMap<Long, Integer> sourceMap = new ConcurrentHashMap<Long, Integer>();
	private static GameLogOperate logOperate = GameLogOperate.getInstance();
	private static GameDateOperate instance = new GameDateOperate();
	
	public static GameDateOperate getInstance(){
		return instance;
	}
	
	private void offerPVPLOG(long master,long time, int type,List<UserStorage> list) throws Exception{
		String tableName = TABLE_PVPLOG;
		String t = type == 1 ? "in" : "out";
		String executeQuery = "insert into " + tableName + " values('" + new Timestamp(time).toString() + "'," + master + ",'" + t + "','" + list.toString() + "')";
		DBOperateUtil.execute(executeQuery);
	}
	
	@Override
	public void collectPvpLog(final long master, final long time, final int type, final List<UserStorage> list) {
		if (DEBUG) {
			NEW_FIXED_THREAD_POOL.execute(new Runnable(){
				public void run() {
					try {
						offerPVPLOG(master, time, type, list);
					} catch (Exception e) {
						log.error(e, e);
					}
				}
			});
		}
	}

	public void collectCashCost(final long master, final double cost,final int goods,final int num,final int type) {
		if (DEBUG) {
			NEW_FIXED_THREAD_POOL.execute(new Runnable(){
				public void run() {
					try {
						String tableName = TABLE_COST_PREFIX + getStringDateShort(TABLENAME_COST_FORMAT);
						String executeQuery = "insert into " + tableName + " values(default," + master + "," +cost + "," + goods +
						",default," + type + ")";
						DBOperateUtil.execute(executeQuery);
						logOperate.logCashCost(master, cost, goods, num);
					} catch (Exception e) {
						log.error(e, e);
					}
					return;
				}
			});
		}
	}
	
	@Override
	public void collectGoodsCost(final int costType, final int goods, final int num) {
		if (DEBUG) {
			NEW_FIXED_THREAD_POOL.execute(new Runnable(){
				public void run() {
					try {
						PersistGoodsCost.getInstance().increase(goods, costType, num);
					} catch (Exception e) {
						log.error(e, e);
					}
				}
			});
		}
	}

	public void collectFreeCash(final long master, final double cost) {
		if (DEBUG) {
			NEW_FIXED_THREAD_POOL.execute(new Runnable(){
				public void run() {
					try {
						logOperate.logFreeCash(master, cost, 2);
					} catch (Exception e) {
						log.error(e, e);
					}
				}
			});
		}
	}
	
	private int getFidSource(long master,int kind){
		if (sourceMap.containsKey(master)) {
			return sourceMap.get(master);
		}else {
			try {
				String executeQuery = "select kind from t_fidsource where master = " + master;
				Object obj = DBOperateUtil.getObject(executeQuery);
				if (obj == null) {
					executeQuery = "insert into t_fidsource values(" + master + "," + kind +",default)";
					DBOperateUtil.execute(executeQuery);
				}else {
					kind = new Integer(obj.toString());
				}
				put(master, kind);
			} catch (Exception e) {
				log.error(e, e);
			}
			return kind;
		}
	}
	
	private void put(long master,int kind){
		if (sourceMap.size() > MAX_SIZE) {
			sourceMap.clear();
		}
		sourceMap.put(master, kind);
	}
	
	private String getStringDateShort(String format){
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(new Date());
		return dateString;
	}
	
	private void jsonToString(String methodName,String error) {
		JSONObject json = new JSONObject();
		json.put("methodName", methodName);
		json.put("error", error);
		log.info(json.toString());
	}
} 

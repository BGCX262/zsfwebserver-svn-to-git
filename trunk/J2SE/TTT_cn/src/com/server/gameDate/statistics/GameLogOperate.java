package com.server.gameDate.statistics;

import java.sql.Timestamp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.server.util.Configuration;

public class GameLogOperate {
	
	private static Log log = LogFactory.getLog(GameLogOperate.class);
	private static final boolean DEBUG = Configuration.getWriteGameLog();
	private static GameLogOperate instance = new GameLogOperate();
	
	public static GameLogOperate getInstance(){
		return instance;
	}
	
	public void logCashCost(long master, double cost,int goods,int num){ 
		if (DEBUG) {
			try {
				String execute = "call sp_add_item_log(" + master+ ",'" + goods+ "'," + num + ",0, " + cost + ",0,'" + new Timestamp(System.currentTimeMillis()) + "')";
				DBOperateUtil.executeLog(execute);
			} catch (Exception e) {
				log.error(e, e);
			}
		}
	}
	
	public void logFreeCash(long master, double cost,int coin_type){ 
		if (DEBUG) {
			try {
				String execute = "call sp_add_case_log(" + master+ ",0," + coin_type + "," + cost + ",0,'','" + new Timestamp(System.currentTimeMillis()) + "')";
				DBOperateUtil.executeLog(execute);
			} catch (Exception e) {
				log.error(e, e);
			}
		}
	}
	
	public void offerLogin(long master,String ip,Timestamp loginTime,Timestamp outTime){
		if (DEBUG) {
			try {
				String execute = "call sp_add_login_log(0," + master+ ",'" + ip + "','" + loginTime + "')";
				DBOperateUtil.executeLog(execute);
				execute = "call sp_add_login_log(1," + master+ ",'" + ip + "','" + outTime + "')";
				DBOperateUtil.executeLog(execute);
			} catch (Exception e) {
				log.error(e, e);
			}
		}
	}
}

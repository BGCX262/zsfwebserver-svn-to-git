package com.server.user.operation;

import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.LoginLog;
import com.database.model.bean.TreasureChestBean;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class LoginAwardOp {
	private static Log log = LogFactory.getLog(LoginAwardOp.class);
	private static final int LOGIN_AWARD_GOODID = 120003;
	private static final long DAY = Configuration.getDay();
	private static final long BASE_TIME = 1293120000171l;
	public static boolean isApartNDay(Date beforeDate, Date afterDate, int n){
      /* Calendar c = Calendar.getInstance();
       c.setTime(beforeDate);
       int bd = c.get(Calendar.DAY_OF_YEAR);
       c.setTime(afterDate);
       int ad = c.get(Calendar.DAY_OF_YEAR);
       return ad - bd == n ? true : false;*/
		long bd = (beforeDate.getTime() - BASE_TIME) / DAY;
		long ad = (afterDate.getTime() - BASE_TIME) / DAY;
		return ad - bd == n ? true : false;
	}
	
	public static int getAwardDate(long id){
		LoginLog loginLog = (LoginLog) DBUtil.get(id, LoginLog.class);
		int date = loginLog.getLoginAwardCount();
		if(loginLog != null){
			long lastAwardTime = loginLog.getLastAwardTime();
			long nowTime = System.currentTimeMillis();
			Date nowDate = new Date(nowTime);
			Date lastAwardDate = new Date(lastAwardTime);
			if(isApartNDay(lastAwardDate, nowDate, 0)){
				return 0;
			}
			if(date == 0){
				date = 1;
			}
		}
		return date;
	}
	
	public static void setSeqLogin(long id, LoginLog loginLog){
		try{
			Timestamp lastLoginDate = loginLog.getLastLoginTime();
			long nowTime = System.currentTimeMillis();
			Date nowDate = new Date(nowTime);
			if(lastLoginDate == null){
				loginLog.setLoginAwardCount(1);
				return;
			}else if(!isApartNDay(lastLoginDate, nowDate, 0)){
				if(isApartNDay(lastLoginDate, nowDate, 1)){
					loginLog.setLoginAwardCount(loginLog.getLoginAwardCount() % 6 + 1);
				}else{
					loginLog.setLoginAwardCount(1);
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static byte[] getLoginAwardInfo(long id){
		int date = getAwardDate(id);
		byte[] re = DataFactory.getbyte(date);
		return re;
	}
	
	public static byte[] award(long id){
		byte[] re = new byte[]{0x01};
		try{
			int date = getAwardDate(id);
			if(date != 0){
				TreasureChestBean loginAwardBean = (TreasureChestBean) Goods.getByGoodIDAndLevel(GoodsCate.TREASURECHESTBEAN, LOGIN_AWARD_GOODID, date);
				if(loginAwardBean != null){
					List<ThingBean> get = new LinkedList<ThingBean>();
					JSONArray ja = JSONArray.fromObject(loginAwardBean.getPresents());
					for(int i = 0; i < ja.size(); i++){
						JSONArray jaa = ja.getJSONArray(i);
						List<ThingBean> getThing = StorageOp.storeGoods(id, jaa.getInt(0), jaa.getInt(1));
						if(getThing != null){
							get.addAll(getThing);
						}
					}
					boolean suc = false;
					if(get != null && get.size() > 0){
						suc = true;
						DBUtil.executeUpdate(id, "update loginlog set lastAwardTime = " + System.currentTimeMillis() + " where masterID = " + id);
					}
					GameLog.createLog(id, 33, null, suc, get, null, "get login award");
					re = new byte[]{0x00};
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return re;
	}
}

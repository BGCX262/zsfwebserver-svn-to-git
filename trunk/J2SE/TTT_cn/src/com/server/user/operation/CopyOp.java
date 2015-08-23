package com.server.user.operation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.CastleBean;
import com.database.model.bean.CityBean;
import com.database.model.bean.CopyBean;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserCity;
import com.database.model.bean.UserCopy;
import com.server.cache.UserMemory;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;

public class CopyOp {
	private static Log log = LogFactory.getLog(CopyOp.class);
	private static Finance financeImpl = FinanceImpl.instance();
	public static final int COPY_START_NUM = 101;//副本开始关卡号
	public static final int COPY_START_TIMES_NUM = 20001;//副本开始战斗波次
	
	public static byte[] getCopy(long id)throws Exception{
		byte[] re = null;
		re = DataFactory.getbyte(0);
		UserCopy copy = UserMemory.getCopy(id);
		if(copy != null){
			List<CopyBean> copyList = copy.getCopyList();
			if(copyList != null){
				int count = 0;
				byte[] temp = new byte[]{};
				Iterator<CopyBean> ite = copyList.iterator();
				while(ite.hasNext()){
					CopyBean tempCopy = ite.next();
					if(tempCopy != null){
						temp = DataFactory.addByteArray(temp, DataFactory.getbyte(tempCopy.getCityID()));
						temp = DataFactory.addByteArray(temp, DataFactory.getbyte(tempCopy.getCurTimesNum()));
						count++;
					}
				}
				DataFactory.replace(re, 0, DataFactory.getbyte(count));
				re = DataFactory.addByteArray(re, temp);
			}
		}
		return re;
	}
	
	public static byte[] openCopy(long id, byte[] information)throws Exception{
		byte[] re = new byte[]{0x01};
		int cityID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int type = DataFactory.getInt(DataFactory.get(information, 14, 1));
		CityBean cityBean = (CityBean) Goods.getById(GoodsCate.CITYBEAN, cityID);
		if(cityBean != null){
			UserCastle castle = UserMemory.getCastle(id);
			CityBean lastCityBean = (CityBean) Goods.getById(GoodsCate.CITYBEAN, cityID-1);
			CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
			if((cityID == COPY_START_NUM  || lastCityBean != null) && castleBean != null && castleBean.getLevel() >= cityBean.getOpenNeedCastleLev()){
				FinanceBean cost = new FinanceBean();
				cost.setId(id);
				if (type == 0) {
					cost.setRock(-cityBean.getOpenNeedRock());
					cost.setMetal(-cityBean.getOpenNeedMetal());
					cost.setCrystal(-cityBean.getOpenNeedCrystal());
					cost.setCoin(-cityBean.getOpenNeedCoin());
				} else {
					cost.setCash(-cityBean.getOpenNeedCash());
				}
				UserCopy copy = UserMemory.getCopy(id);
				if(copy != null && financeImpl.charge(cost)){
					CopyBean copyBean = new CopyBean();
					copyBean.setCityID(cityID);
					if(cityID == COPY_START_NUM){
						copyBean.setCurTimesNum(COPY_START_TIMES_NUM);
					}else{
						copyBean.setCurTimesNum(lastCityBean.getLastTimes() + 1);
					}
					copyBean.setOpenTime(System.currentTimeMillis());
					copy.setResetTime(System.currentTimeMillis());
					copy.getCopyList().add(copyBean);
					copy.setChange(true);
					re = new byte[]{0x00};
					List<ThingBean> lost = new LinkedList<ThingBean>();
					lost.add(new ThingBean(7, 7,60001, cityBean.getOpenNeedRock(), null));
					lost.add(new ThingBean(7, 8, 60002, cityBean.getOpenNeedMetal(), null));
					lost.add(new ThingBean(7, 9, 60003, cityBean.getOpenNeedCrystal(), null));
					lost.add(new ThingBean(7, 1, 60006, cityBean.getOpenNeedCoin(), null));
					lost.add(new ThingBean(7, 2, 60007, cityBean.getOpenNeedCash(), null));
					GameLog.createLog(id, 34, null, true, null, lost, null);
					
				}else{
					re = new byte[]{0x03};
				}
			}else{
				re = new byte[]{0x02};
			}
		}
		return re;
	}
	
	public  static boolean resetCopy(long id, Session session){
		session.createSQLQuery("delete from usercopy where id = " + id).executeUpdate();
		return true;
	}
	
	public static CopyBean getCopyBean(long id, int cityID){
		UserCopy copy = UserMemory.getCopy(id);
		if(copy != null){
			List<CopyBean> copyList = copy.getCopyList();
			if(copyList != null){
				Iterator<CopyBean> ite = copyList.iterator();
				while(ite.hasNext()){
					CopyBean temp = ite.next();
					if(temp.getCityID() == cityID){
						return temp;
					}
				}
			}
		}
		return null;
	}
	
	public static void setCurTimesNum(long id, UserCity city, int curTimesNum){
		try{
			UserCopy copy = UserMemory.getCopy(id);
			if(copy != null && city != null && city.getCityID() >= COPY_START_NUM){
				CopyBean copyBean = getCopyBean(id, city.getCityID());
				if(copyBean != null){
					copyBean.setCurTimesNum(curTimesNum);
					copy.setChange(true);
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static boolean canEnterCopy(long id, int cityID){
		try{
			UserCopy copy = UserMemory.getCopy(id);
			if(copy != null){
				List<CopyBean> copyList = copy.getCopyList();
				if(copyList != null){
					Iterator<CopyBean> ite = copyList.iterator();
					while(ite.hasNext()){
						CopyBean copyBean = ite.next();
						if(copyBean != null && copyBean.getCityID() == cityID){
							return true;
						}
					}
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return false;
	}
}

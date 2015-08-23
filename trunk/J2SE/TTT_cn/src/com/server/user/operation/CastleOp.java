package com.server.user.operation;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cindy.run.connect.instance.Instance;
import com.cindy.run.util.DataFactory;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.CastleBean;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.LoginLog;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserLoveTask;
import com.server.cache.UserMemory;
import com.server.dispose.TDDispose;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.Cmd;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class CastleOp {

	private static Log log = LogFactory.getLog(CastleOp.class);
	private static Finance financeImpl = FinanceImpl.instance();
	public static final int REPAIR_COST_COIN = Configuration.getRepairCastleCostCoin();
	public static final int REPAIR_COST_ROCK = Configuration.getRepairCastleCostRock();
	public static final int REPAIR_COST_METAL = Configuration.getRepairCastleCostMetal();
	public static final int REPAIR_COST_CRYSTAL = Configuration.getRepairCastleCostCrystal();
	public static final int NORMAL_STORAGE_LIMIT = Configuration.getNormalStorageLimit();

	public static byte[] getCastle(long id) {
		byte[] re = null;
		try {
			UserCastle castle = UserMemory.getCastle(id);
			re = DataFactory.getbyte(castle.getCastleID());

		} catch (Exception e) {
			log.error(e, e);
		}
		return re;
	}

	public static UserCastle initUserCastle(long id, int timezone, String name, Instance role) {
		UserCastle castle = new UserCastle();
		CastleBean bean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, 1);
		castle.setId(id);
		castle.setCastleID(1);
		castle.setRace(1);
		castle.setSex((Boolean) role.getAttribute("sex"));
		castle.setCurrentCity(1);
		castle.setMaxCity(1);
		castle.setTimezoneOffset(timezone);
		castle.setSlaverLimit(4);
		castle.setEnergyLimit(bean.getSpVol());
		castle.setSoulLimit(100);
		castle.setNomalstorageLimit(NORMAL_STORAGE_LIMIT);
		castle.setHp(20);
		castle.setRevertTime(System.currentTimeMillis());
		castle.setRecivePresentLevel(1);
		try {
			castle.setName(name/*
								 * + new String("的城堡".getBytes("UTF-8"),
								 * "UTF-8")
								 */);
			DBUtil.save(id, castle);
		} catch (Exception e) {
			log.error(e, e);
		}
		if (castle != null) {
			castle.setTime(System.currentTimeMillis());
		}
		return castle;
	}

	public static boolean
			resetCastle(long id, UserCastle castle, CastleBean castleBean, int targetRace, Session session) {
		castle.setState(0);
		castle.setCastleID(castleBean.getId());
		castle.setRace(targetRace);
		castle.setCurrentCity(1);
		castle.setMaxCity(1);
		castle.setSlaverLimit(5);
		castle.setEnergyLimit(castleBean.getSpVol());
		castle.setSoulLimit(100);
		castle.setHp(20);
		castle.setRevertTime(System.currentTimeMillis());
		session.update(castle);
		return true;
	}

	public static void demage(long id, int hp) {
		UserCastle castle = UserMemory.getCastle(id);
		int newHp = 0;
		if (castle.getHp() + hp > 0) {
			newHp = castle.getHp() + hp;
		}
		castle.setHp(newHp);
		castle.setChange(true);
	}

	public static byte[] repairCastle(long id, byte[] information) throws Exception {
		byte[] re = new byte[] { 0x04 };
		int hp = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int num = DataFactory.getInt(DataFactory.get(information, 14, 4));
		long[] sla = new long[num];
		for (int i = 0; i < num; i++) {
			sla[i] = DataFactory.doubleBytesToLong(DataFactory.get(information, 18 + 8 * i, 8));
		}
		if (!SlaverOp.isFree(id, sla)) {
			log.warn("Game_Warning:no slaver work for this");
			return re;
		}
		UserCastle castle = UserMemory.getCastle(id);
		if (castle != null/* && castle.getState() != 2*/) {
			CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
			if (castleBean != null) {
				castle.setRepairHp(hp);
				FinanceBean temp = new FinanceBean();
				temp.setId(id);
				temp.setCoin(-hp * REPAIR_COST_COIN);
				temp.setCrystal(-hp * REPAIR_COST_CRYSTAL);
				temp.setRock(-hp * REPAIR_COST_ROCK);
				temp.setMetal(-hp * REPAIR_COST_METAL);
				boolean suc = financeImpl.charge(temp);
				if (suc) {
					castle.setHp(castle.getHp() + hp);
					castle.setChange(true);
					List<ThingBean> lost = new LinkedList<ThingBean>();
					lost.add(new ThingBean(7, 7, 60001, hp * REPAIR_COST_ROCK, null));
					lost.add(new ThingBean(7, 8, 60002, hp * REPAIR_COST_METAL, null));
					lost.add(new ThingBean(7, 9, 60003, hp * REPAIR_COST_CRYSTAL, null));
					lost.add(new ThingBean(7, 1, 60006, hp * REPAIR_COST_COIN, null));
					GameLog.createLog(id, 10, null, true, null, lost, null);
					re = new byte[] { 0x00 };
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(System.currentTimeMillis()));
					TaskOp.doTask(id, 20006, 1);
				} else {
					log.warn("Game_Warning:the material is not enough");
				}
			}
		}
		return re;
	}

	public static byte[] upgradCastle(long id, byte[] information) throws Exception {
		byte[] re = new byte[] { 0x03 };
		int num = DataFactory.getInt(DataFactory.get(information, 10, 4));
		long[] sla = new long[num];
		for (int i = 0; i < num; i++) {
			sla[i] = DataFactory.doubleBytesToLong(DataFactory.get(information, 14 + 8 * i, 8));
		}
		if (!SlaverOp.isFree(id, sla)) {
			log.warn("Game_Warning:no slaver work for this");
			return re;
		}
		UserCastle castle = UserMemory.getCastle(id);
		if (castle != null) {
			CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
			FinanceBean financeBean = financeImpl.getFinance(id);
			if (castleBean != null && financeBean != null && financeBean.getGlory() >= castleBean.getUpNeedGlory()) {
				FinanceBean temp = new FinanceBean();
				temp.setId(id);
				temp.setCoin(-castleBean.getUpNeedCoin());
				temp.setCrystal(-castleBean.getUpNeedCrystal());
				temp.setRock(-castleBean.getUpNeedRock());
				temp.setMetal(-castleBean.getUpNeedMetal());
				boolean suc = financeImpl.charge(temp);
				if (suc) {
					long endTime = SlaverOp.upgradeCastle(id, sla);
					castle.setEndTime(endTime);
					castle.setState(2);
					castle.setChange(true);
					List<ThingBean> lost = new LinkedList<ThingBean>();
					lost.add(new ThingBean(7, 7, 60001, castleBean.getUpNeedRock(), null));
					lost.add(new ThingBean(7, 8, 60002, castleBean.getUpNeedMetal(), null));
					lost.add(new ThingBean(7, 9, 60003, castleBean.getUpNeedCrystal(), null));
					lost.add(new ThingBean(7, 1, 60006, castleBean.getUpNeedCoin(), null));
					GameLog.createLog(id, 11, null, true, null, lost, null);
					re = new byte[] { 0x00 };
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
					
					TaskOp.doTask(id, 10006, 1);
					//TaskOp.checkDoTask(id, castleBean.getLevel(), 1, true);
				} else {
					log.warn("Game_Warning:the material is not enough");
				}
			} else {
				log.warn("Game_Warning:the glory is too little");
			}
		}
		return re;
	}

	public static void modifyRaceAndName(long id, byte[] information) throws Exception {
		try {
			int race = DataFactory.getInt(DataFactory.get(information, 10, 4));
			int nameLength =
					DataFactory.getInt(DataFactory.addByteArray(DataFactory.get(information, 14, 2), new byte[] { 0x00,
							0x00 }));
			String name = new String(DataFactory.get(information, 16, nameLength), "UTF-8");
			UserCastle castle = UserMemory.getCastle(id);
			castle.setRace(race);
			castle.setName(name);
			castle.setChange(true);
			if (castle.getCastleID() <= 1) {
				StorageOp.initStorage(id);
				CityOp.initTower(id, 0);
				CityOp.initTower(id, 1);
			}
			DBUtil.executeUpdate(id, "update loginrecord set initialized = true where id = " + id);
		} catch (Exception e) {
			log.error(e, e);
			throw new Exception();
		}

	}

	public static CastleBean findByRaceLevel(List<CastleBean> castleBeanList, int race, int level) {
		if (castleBeanList != null) {
			Iterator<CastleBean> ite = castleBeanList.iterator();
			while (ite.hasNext()) {
				CastleBean temp = ite.next();
				if (temp.getRace() == race && temp.getLevel() == level) {
					return temp;
				}
			}
		}
		return null;
	}

	public static long speedup(long id, int propType, double speedupTime) {
		UserCastle castle = UserMemory.getCastle(id);
		if (castle != null && castle.getState() == 0) {
			return 0;
		}
		long endTime = 0;
		if (propType == 1) {
			endTime = castle.getEndTime() - (int) speedupTime;
		} else if (propType == 2) {
			endTime = castle.getEndTime() - (long) ((castle.getEndTime() - System.currentTimeMillis()) * speedupTime);
		} else {
			endTime = System.currentTimeMillis();
		}
		castle.setEndTime(endTime);
		castle.setChange(true);
		SlaverOp.speedup(id, 0, endTime);
		return endTime;
	}

	public static boolean cancel(long id, double returnRate) {
		UserCastle castle = UserMemory.getCastle(id);
		if (castle != null && castle.getState() != 0) {
			castle.setState(0);
			castle.setChange(true);
			SlaverOp.cancelWork(id, 0);
			return true;
		}
		return false;
	}

	public static int getCastleLevel(long id) {
		UserCastle castle = UserMemory.getCastle(id);
		CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
		return castleBean.getLevel();
	}

	/**
	 * 增加SP
	 * @param id
	 * @param information
	 * @return
	 */
	public static byte[] addSp(long id, byte[] information) {
		try {
			Cmd req = Cmd.getInstance(information);
			Cmd res = Cmd.getInstance();
			int state = 0;
			int cash = 0;

			int num = req.readInt(10);
			int type = req.readInt();

			LoginLog loginLog = (LoginLog) DBUtil.get(id, LoginLog.class);
			if (type == 1) {
				/* 邀请好友补充SP */
				if (loginLog != null) {
					/* 判断最后一次使用该功能是否同一天 */
					long oneDay = 1000 * 60 * 60 * 24;
					if (loginLog.getLastInviteTime() / oneDay != System.currentTimeMillis() / oneDay) {
						loginLog.setLastInviteTime(System.currentTimeMillis());
						loginLog.setInviteCount(0);
					}
					if (loginLog.getInviteCount() < 1) {
						FinanceBean temp = new FinanceBean();
						temp.setId(id);
						temp.setEnergy(num);
						if (financeImpl.charge(temp)) {
							loginLog.setInviteCount(loginLog.getInviteCount() + 1);
						}
					} else
						state = 1;
					DBUtil.update(id, loginLog);
				}
			} else if (type == 2) {
				/* 判断SP是否需要补充 */
				int nowSp = financeImpl.getFinance(id).getEnergy();
				int maxSp = UserMemory.getCastle(id).getEnergyLimit();
				/* 判断最后一次使用该功能是否同一天 */
				long oneDay = 1000 * 60 * 60 * 24;
				if (loginLog.getLastFullAddTime() / oneDay != System.currentTimeMillis() / oneDay) {
					loginLog.setLastFullAddTime(System.currentTimeMillis());
					loginLog.setFullAddCount(0);
				}
				if (nowSp >= maxSp) {
					state = 3;
					num = 0;
				} else {
					if (loginLog.getFullAddCount() < 3) {
						cash = num * 2;
						FinanceBean temp = new FinanceBean();
						temp.setId(id);
						temp.setEnergy(num);
						temp.setCash(-cash);
						if (financeImpl.charge(temp)) {
							TDDispose.statisticLog.collectCashCost(id, cash, -1, num, 1);
							loginLog.setFullAddCount(loginLog.getFullAddCount() + 1);
							state = 0;
							DBUtil.update(id, loginLog);
						} else {
							state = 2;
							cash = 0;
						}
					} else {
						state = 4;
						num = 0;
					}
				}
			}

			res.appendByte((byte) state);
			res.appendInt(cash);
			return res.getResponse();
		} catch (Exception e) {
			log.error(e, e);
		}

		return null;
	}

	/**
	 * 根据性别和数量获取玩家id
	 * @param id
	 * @param information
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static byte[] getPeoplesBySex(long id, byte[] information) {
		Cmd req = Cmd.getInstance(information);
		Cmd res = Cmd.getInstance();
		
		try {
			int sex = req.readByte(10);
			int count = req.readInt();
			
			long oneDay = 1000 * 60 * 60 * 24;
			long thisDay = System.currentTimeMillis() / oneDay * oneDay;
			/* 获取已送的玩家 */
			DBUtil.executeUpdate(id, "delete from userlovetask where masterId="+id+" and time<"+thisDay);
			List<BigInteger> sendeds = DBUtil.query(id, "select friendId from userlovetask ult where ult.masterId="+id+" and ult.time>"+thisDay+";");
			String sendedStr = "";
			if (sendeds != null && sendeds.size() > 0) {
				String temp = "(";
				for (Iterator<BigInteger> iter = sendeds.iterator(); iter.hasNext(); ) {
					temp += iter.next().longValue() + ",";
				}
				temp = temp.substring(0, temp.length() - 1);
				temp += ")";
				sendedStr = " and uc.id not in "+temp;
			}
			/* 获取玩家列表 */
			List<BigInteger> namedQuery = DBUtil.query(id, "select id from UserCastle uc where uc.sex="+sex
					+sendedStr+" order by rand() limit "+count+";");
			if (namedQuery != null && namedQuery.size() > 0) {
				res.appendInt(namedQuery.size());
				for (Iterator<BigInteger> iter = namedQuery.iterator(); iter.hasNext(); ) {
					BigInteger next = (BigInteger) iter.next();
					res.appendLong(next.longValue());
				}
				return res.getResponse();
			} else {
				res.appendInt(0);
				return res.getResponse();
			}
			
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return DataFactory.getbyte(0);
	}
	
	/**
	 * 发送情缘任务
	 * @param id
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public static void sendLove(long id, Cmd request) {
		long friendId = request.readLong(10);
		List<UserLoveTask> results = DBUtil.namedQuery(id, "from UserLoveTask ult where ult.masterId="+id+" and friendId="+friendId);
		UserLoveTask userLoveTask = null;
		if (results != null && results.size() > 0) {
			userLoveTask = results.get(0);
			userLoveTask.setTime(System.currentTimeMillis());
			DBUtil.update(id, userLoveTask);
		} else {
			userLoveTask = new UserLoveTask();
			userLoveTask.setMasterId(id);
			userLoveTask.setFriendId(friendId);
			userLoveTask.setTime(System.currentTimeMillis());
			DBUtil.save(id, userLoveTask);
		}
	}
	
	/**
	 * 获取已发送情缘礼包的玩家
	 * @param id
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Cmd getSendedLoves(long id, Cmd request) {
		Cmd res = Cmd.getInstance();
		try {
			long oneDay = 1000 * 60 * 60 * 24;
			long thisDay = System.currentTimeMillis() / oneDay * oneDay;
			List<BigInteger> sendeds = DBUtil.query(id, "select friendId from userlovetask ult where ult.masterId="+id+" and ult.time>"+thisDay+";");
			if (sendeds != null && sendeds.size() > 0) {
				res.appendInt(sendeds.size());
				for (Iterator<BigInteger> iter = sendeds.iterator(); iter.hasNext(); ) {
					BigInteger next = (BigInteger) iter.next();
					res.appendLong(next.longValue());
				}
				return res;
			} else {
				res.appendInt(0);
				return res;
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return Cmd.getInstance(DataFactory.getbyte(0));
	}

	/**
	 * 增加SP上限
	 * @param id
	 * @param cmd
	 * @return
	 */
	public static Cmd addMaxSp(long id, Cmd cmd) {
		Cmd res = Cmd.getInstance();
		res.appendByte((byte) 1);
		try {
			FinanceBean bean = new FinanceBean();
			UserCastle uc = UserMemory.getCastle(id);
			if (uc.getEnergyLimit() >= 30)
				return res;
			bean.setId(id);
			int cash = (uc.getEnergyLimit() - 20) * 5 + 10;
			bean.setCash(-cash);
			if (financeImpl.charge(bean)) {
				TDDispose.statisticLog.collectCashCost(id, cash, -2, 1, 1);
				uc.setEnergyLimit(uc.getEnergyLimit() + 1);
				uc.setChange(true);
				bean.setEnergy(1);
				bean.setCash(0);
				financeImpl.charge(bean);
				res.clear();
				res.appendByte((byte) 0);
				res.appendInt(cash);
				res.appendInt(uc.getEnergyLimit());
			}
			
		} catch (Exception e) {
			log.error(e, e);
		}
		return res;
	}
}

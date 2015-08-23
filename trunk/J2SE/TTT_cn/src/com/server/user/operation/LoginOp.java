package com.server.user.operation;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cindy.run.connect.instance.Instance;
import com.cindy.run.util.DataFactory;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.CastleBean;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.LoginLog;
import com.database.model.bean.LoginRecord;
import com.database.model.bean.MemoryBean;
import com.database.model.bean.PVPRecordBean;
import com.database.model.bean.PickedFriends;
import com.database.model.bean.TitleBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserStorage;
import com.server.cache.UserMemory;
import com.server.dispose.TDDispose;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class LoginOp {

	private static final Log log = LogFactory.getLog(LoginOp.class);
	private static final int PERIOD = Configuration.getDebugTime();
	private static Finance financeImpl = FinanceImpl.instance();

	private static LoginRecord saveLogRecord(long id, String ip) {
		Session session = null;
		LoginRecord record = null;
		try {
			session = HibernateUtil.checkExistCurrentSession(id);
			Transaction tr = session.beginTransaction();
			record = (LoginRecord) session.get(LoginRecord.class, id);
			if (record == null) {
				record = new LoginRecord();
				record.setId(id);
				record.setFirst(true);
				record.setIp(ip);
				record.setOnline(new Timestamp(System.currentTimeMillis()));
				record.setOffline(new Time(System.currentTimeMillis()));
				record.setFreashProtectTime(System.currentTimeMillis() + PVPListOp.FRESH_USER_PVP_PROTECT_TIME);
				session.save(record);
			} else {
				record.setIp(ip);
				Timestamp online = new Timestamp(System.currentTimeMillis());
				long oneDay = 1000 * 60 * 60 * 24;
				if (online.getTime() / oneDay != record.getOnline().getTime() / oneDay)
					record.setTodayOnline(0);
				record.setOnline(online);
				session.update(record);
			}
			tr.commit();
		} catch (Exception e) {
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return record;
	}

	public static void saveLoginLog(LoginRecord loginRecord) {
		try {
			if (loginRecord != null) {
				Timestamp now = new Timestamp(System.currentTimeMillis());
				java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
				long id = loginRecord.getId();
				LoginLog loginLog = (LoginLog) DBUtil.get(id, LoginLog.class);
				if (loginRecord.isFirst() || loginLog == null) {
					loginLog = new LoginLog();
					loginLog.setMasterID(loginRecord.getId());
					loginLog.setRegisterTime(date);
					loginLog.setLoginCount(loginLog.getLoginCount() + 1);
					LoginAwardOp.setSeqLogin(loginRecord.getId(), loginLog);
					loginLog.setLastLoginTime(now);
					DBUtil.save(id, loginLog);
				} else {
					loginLog.setLoginCount(loginLog.getLoginCount() + 1);
					LoginAwardOp.setSeqLogin(loginRecord.getId(), loginLog);
					loginLog.setLastLoginTime(now);
					DBUtil.update(id, loginLog);
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public static void preOnline(long id) {
		MemoryBean bean = UserMemory.get(id);
		if (bean == null || (bean != null && !bean.isMaster())) {
			UserMemory.createMasterMem(id);
			bean = UserMemory.get(id);
		}
	}

	public static LoginRecord online(long id, String ip, int timezone, String name, int source, Instance role) {
		long time = System.currentTimeMillis();
		LoginRecord record = saveLogRecord(id, ip);
		preOnline(id);
		UserCastle us = UserMemory.getCastle(id);
		if (record.isFirst()) {
			if (us == null || us.getCastleID() <= 1) {
				initUser(id, timezone, name, record, role);
			} else {
				record.setInitialized(true);
				log.warn("init user error!!!!!!!!!!!!!!! id: " + id + ", castle: " + us + ", level: " + us.getCastleID());
			}
		}
		saveLoginLog(record);
		PVPRecordOp.sysData(id);
		TaskOp.issueDailyTask(id);
		long spend = System.currentTimeMillis() - time;
		if (spend > PERIOD) {
			log.info("Online spend time :" + spend);
		}
		return record;
	}

	public static void offline(long id) {
		try {
			LoginRecord record = (LoginRecord) DBUtil.get(id, LoginRecord.class);
			FightOp.offline(id);
			UserMemory.offline(id);
			// Online.offline(id);
			financeImpl.offline(id);
			if (record != null) {
				long now = System.currentTimeMillis();
				long oneDay = 1000 * 60 * 60 * 24;
				record.setOffline(new Time(now));
				
				long lastOnline = record.getOnline().getTime();
				if (lastOnline / oneDay != now / oneDay) {
					lastOnline /= oneDay;
					lastOnline *= oneDay;
				}
				record.setTodayOnline(record.getTodayOnline() + now - lastOnline);
				DBUtil.update(id, record);
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	private static void update(long id, String sql) {
		try {
			DBUtil.executeUpdate(id, sql);
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public static void setStyle(long id, int style) {
		update(id, "update loginrecord set style = " + style + " where id = " + id);
	}

	public static void setSe(long id, int tone) {
		update(id, "update loginrecord set tone = " + tone + " where id = " + id);
	}

	public static void setSound(long id, int sound) {
		update(id, "update loginrecord set sound = " + sound + " where id = " + id);
	}

	private static void initUser(long id, int timezone, String name, LoginRecord record, Instance role) {
		try {
			FinanceImpl.initFinance(id);
			UserMemory.initUser(id, timezone, name, role);
			FightOp.initPVPRecord(id);
			FightOp.initPVEFightBean(id);
			PVPRecordOp.initPVPRecord(id);
			PVPListOp.initPVPListBean(id, record.getFreashProtectTime());
			MessageOp.createMessage(0, id, 0, 0, 4, null, null, null, null);
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public static byte[] loginReturn(long id, LoginRecord loginRecord) {
		byte[] loReturn = DataFactory.getCurrentTime();
		try {
			loReturn =
					DataFactory.addByteArray(loReturn,
							DataFactory.doubleToXiaoTouByte(loginRecord.getFreashProtectTime()));
			loReturn = DataFactory.addByteArray(loReturn, getBasicSetting(loginRecord));
			loReturn = DataFactory.addByteArray(loReturn, getFinanceBean(id));
			loReturn = DataFactory.addByteArray(loReturn, SlaverOp.getSlavers(id));
		} catch (Exception e) {
			log.error(e, e);
		}
		return loReturn;
	}

	private static byte[] getBasicSetting(LoginRecord loginRecord) {
		byte[] re = null;
		try {
			if (loginRecord != null) {
				if (loginRecord.isStyle()) {
					re = new byte[] { 0x00 };
				} else {
					re = new byte[] { 0x01 };
				}
				if (loginRecord.isSound()) {
					re = DataFactory.addByteArray(re, new byte[] { 0x00 });
				} else {
					re = DataFactory.addByteArray(re, new byte[] { 0x01 });
				}
				if (loginRecord.isShowLevel()) {
					re = DataFactory.addByteArray(re, new byte[] { 0x00 });
				} else {
					re = DataFactory.addByteArray(re, new byte[] { 0x01 });
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return re;
	}

	private static byte[] getFinanceBean(long id) {
		byte[] re = null;
		try {
			FinanceBean bean = financeImpl.getFinance(id);
			UserCastle castle = UserMemory.getCastle(id);
			re = DataFactory.getbyte(castle.getSlaverLimit());
			re = DataFactory.addByteArray(re, DataFactory.getbyte(castle.getRace()));
			EnergyOp.revert(id);
			re = DataFactory.addByteArray(re, DataFactory.getbyte(bean.getEnergy()));
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(castle.getRevertTime()));
			re = DataFactory.addByteArray(re, DataFactory.getbyte(castle.getEnergyLimit()));
			// re = DataFactory.addByteArray(re,
			// DataFactory.getbyte(bean.getExperience()));
			re = DataFactory.addByteArray(re, DataFactory.getbyte(bean.getGlory()));
			if (castle.isVip()) {
				re = DataFactory.addByteArray(re, new byte[] { 0x00 });
			} else {
				re = DataFactory.addByteArray(re, new byte[] { 0x01 });
			}
			re = DataFactory.addByteArray(re, DataFactory.getbyte(bean.getCoin()));
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(bean.getSystemCash()));
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(bean.getCash()));
			re = DataFactory.addByteArray(re, DataFactory.getbyte(bean.getRock()));
			re = DataFactory.addByteArray(re, DataFactory.getbyte(bean.getMetal()));
			re = DataFactory.addByteArray(re, DataFactory.getbyte(bean.getCrystal()));
			re = DataFactory.addByteArray(re, DataFactory.getbyte(bean.getSoul()));
			re = DataFactory.addByteArray(re, DataFactory.getbyte(bean.getBadge()));
			re = DataFactory.addByteArray(re, DataFactory.getbyte(castle.getMaxCity()));
			if (castle.getCurrTitle() != 0) {
				TitleBean title = (TitleBean) Goods.getById(GoodsCate.TITLEBEAN, castle.getCurrTitle());
				re = DataFactory.addByteArray(re, DataFactory.getbyte(title.getGoodID()));
			} else {
				re = DataFactory.addByteArray(re, DataFactory.getbyte(0));
			}
			JSONArray ja = null;
			if (castle.getTitles() != null) {
				ja = JSONArray.fromObject(castle.getTitles());
			}
			int titleNum = 0;
			if (ja != null) {
				titleNum = ja.size();
				re = DataFactory.addByteArray(re, DataFactory.getbyte(titleNum));
				for (int i = 0; i < ja.size(); i++) {
					TitleBean titleBean = (TitleBean) Goods.getById(GoodsCate.TITLEBEAN, ja.getInt(i));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(titleBean.getGoodID()));
				}
			} else {
				re = DataFactory.addByteArray(re, DataFactory.getbyte(titleNum));
			}

		} catch (Exception e) {
			log.error(e, e);
		}
		return re;
	}

	public static byte[] getCastle(long id) throws Exception {
		byte[] re = null;
		PVPRecordBean record = PVPRecordOp.getPVPRecord(id);
		int hp = 0;
		if(record != null){
			hp += record.getHp();		
		}
		UserCastle castle = UserMemory.getCastle(id);
		CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
		int level = castleBean.getLevel();
		re = DataFactory.getbyte(castle.getRecivePresentLevel());
		re = DataFactory.addByteArray(re, DataFactory.getbyte(level));
		byte[] castleName = castle.getName().getBytes("UTF-8");
		int length = castleName.length;
		re = DataFactory.addByteArray(re, new byte[] { (byte) (length), (byte) (length >> 8) });
		re = DataFactory.addByteArray(re, castle.getName().getBytes("UTF-8"));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(castle.getHp() + hp));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(castle.getState()));
		if (castle.getState() != 0) {
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(castle.getEndTime()));
			re = DataFactory.addByteArray(re, DataFactory.getbyte(castle.getRepairHp()));
		}
		return re;
	}

	public static byte[] enterScene(long id, long sceneID) {
		byte[] re = new byte[] { 0x00 };
		try {
			UserMemory.createFriendMem(sceneID);

			UserCastle castle = UserMemory.getCastle(sceneID);
			if (id == sceneID && castle.getCurrentCity() >= CopyOp.COPY_START_NUM
					&& !CopyOp.canEnterCopy(sceneID, castle.getCurrentCity())) {
				int maxCity = castle.getMaxCity();
				if (maxCity > CityOp.MAX_PVE_CITY) {
					maxCity = CityOp.MAX_PVE_CITY;
				}
				castle.setCurrentCity(maxCity);
				castle.setChange(true);
			}
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(sceneID));
			if (SlaverOp.getFriendSlaver(id, sceneID) != null) {
				re = DataFactory.addByteArray(re, new byte[] { 0x01 });
			} else {
				re = DataFactory.addByteArray(re, new byte[] { 0x00 });
			}

			List<UserStorage> normalStorageList = StorageOp.getNomalStorage(sceneID);

			int remainVol = castle.getNomalstorageLimit() - normalStorageList.size();

			re = DataFactory.addByteArray(re, DataFactory.getbyte(remainVol));

			re = DataFactory.addByteArray(re, getCastle(sceneID));

			re = DataFactory.addByteArray(re, CityOp.getCity(sceneID, castle.getCurrentCity()));

			PickedFriends pickedFriends = PickOp.getPickedFriends(id);

			if (PickOp.canPickMaterialBox(id, sceneID, pickedFriends)) {
				re = DataFactory.addByteArray(re, new byte[] { 0x00 });
			} else {
				re = DataFactory.addByteArray(re, new byte[] { 0x01 });
			}
			
			re = DataFactory.addByteArray(re, new byte[] { (byte) (CityOp.canPickFriends(id, sceneID) ? 0x00 : 0x01) });
			
			PVPListOp.addPVPListBeanToMemcache(sceneID);
		} catch (Exception e) {
			log.error(e, e);
			re[0] = 0x01;
		}
		return re;
	}
}

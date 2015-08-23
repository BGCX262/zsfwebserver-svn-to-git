package com.server.user.operation;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.CastleBean;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.Mission;
import com.database.model.bean.MonsterBean;
import com.database.model.bean.SoulTowerBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserSoulTower;
import com.server.cache.UserMemory;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.DBUtil;

public class SoulTowerOp {
	private static Log log = LogFactory.getLog(SoulTowerOp.class);
	private static Finance financeImpl = FinanceImpl.instance();
	
	public static byte[] getUserSoulTower(long id){
		byte[] re = new byte[]{0x01};
		UserSoulTower userSoulTower = UserMemory.getSoulTower(id);
		if(userSoulTower != null){
			re = DataFactory.addByteArray(re, DataFactory.getbyte(userSoulTower.getSoulTowerLevel()));
			if(userSoulTower.getState() == 0){
				re = DataFactory.addByteArray(re, new byte[]{0x00});
			}else{
				re = DataFactory.addByteArray(re, new byte[]{0x01});
			}
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userSoulTower.getEndTime()));
			DataFactory.replace(re, 0, new byte[]{0x00});
		}
		return re;
	}
	
	public static UserSoulTower initSoulTower(long id){
		UserSoulTower userSoulTower = new UserSoulTower();
		userSoulTower.setId(id);
		SoulTowerBean soulTowerBean = (SoulTowerBean) Goods.getById(GoodsCate.SOULTOWERBEAN, 1);
		userSoulTower.setSoulTowerLevel(soulTowerBean.getLevel());
		DBUtil.merge(id, userSoulTower);
		if(userSoulTower != null){
			userSoulTower.setTime(System.currentTimeMillis());
		}
		return userSoulTower;
	}
	
	public static boolean resetSoulTower(long id, Session session){
		UserSoulTower userSoulTower = new UserSoulTower();
		userSoulTower.setId(id);
		SoulTowerBean soulTowerBean = (SoulTowerBean) Goods.getById(GoodsCate.SOULTOWERBEAN, 1);
		userSoulTower.setSoulTowerLevel(soulTowerBean.getLevel());
		userSoulTower.setTime(System.currentTimeMillis());
		session.update(userSoulTower);
		return true;
	}
	
	public static byte[] upgrade(long id, byte[] information){
		byte[] re = new byte[]{0x01};
		int slaverNum = DataFactory.getInt(DataFactory.get(information, 10, 4));
		long[] slaverID = new long[slaverNum];
		for(int i = 0; i < slaverNum; i++){
			slaverID[i] = DataFactory.doubleBytesToLong(DataFactory.get(information, 14 + i * 8, 8));
		}
		
		if(!SlaverOp.isFree(id, slaverID)){
			log.warn("Game_Warning:no slaver work for this");
			re = new byte[]{0x03};
			return re;
		}
		
		UserSoulTower userSoulTower = UserMemory.getSoulTower(id);
		if(userSoulTower != null){
			UserCastle castle = UserMemory.getCastle(id);
			CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
			SoulTowerBean soulTowerBean = (SoulTowerBean) Goods.getById(GoodsCate.SOULTOWERBEAN, userSoulTower.getSoulTowerLevel());
			SoulTowerBean upGradeSoulTowerBean = (SoulTowerBean) Goods.getById(GoodsCate.SOULTOWERBEAN, userSoulTower.getSoulTowerLevel() + 1);
			if(soulTowerBean.getUpgradeNeedCastleLevel() <= castleBean.getLevel()){
				if(soulTowerBean != null && upGradeSoulTowerBean != null){
					FinanceBean cost = new FinanceBean();
					cost.setId(id);
					cost.setCoin(-soulTowerBean.getUpgradeNeedCoin());
					cost.setRock(-soulTowerBean.getUpgradeNeedRock());
					cost.setMetal(-soulTowerBean.getUpgradeNeedMetal());
					cost.setCrystal(-soulTowerBean.getUpgradeNeedCrystal());
					if(financeImpl.charge(cost)){
						long endTime = SlaverOp.upgradeSoulTower(id, slaverID);
						userSoulTower.setEndTime(endTime);
						userSoulTower.setState(1);
						userSoulTower.setChange(true);
						re = new byte[]{0x00};
						re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
						List<ThingBean> lost = new LinkedList<ThingBean>();
						lost.add(new ThingBean(7, 7, 60001, soulTowerBean.getUpgradeNeedRock(), null));
						lost.add(new ThingBean(7, 8, 60002, soulTowerBean.getUpgradeNeedMetal(), null));
						lost.add(new ThingBean(7, 9, 60003,soulTowerBean.getUpgradeNeedCrystal(), null));
						lost.add(new ThingBean(7, 1, 60006, soulTowerBean.getUpgradeNeedCoin(), null));
						GameLog.createLog(id, 19, null, true, null, lost, null);
						TaskOp.doTask(id, 10009, 1);
					}else{
						re = new byte[]{0x02};
					}
				}
			}else{
				re = new byte[]{0x01};
			}
		}
		return re;
	}
	
	public static boolean cancel(long id){
		UserSoulTower userSoulTower = UserMemory.getSoulTower(id);
		if(userSoulTower != null && userSoulTower.getState() == 1){
			userSoulTower.setState(0);
			userSoulTower.setChange(true);
			/*
			SoulTowerBean soulTowerBean = (SoulTowerBean) Goods.getById(GoodsCate.SOULTOWERBEAN, userSoulTower.getSoulTowerLevel());
			FinanceBean backMoney = new FinanceBean();
			backMoney.setId(id);
			backMoney.setCoin((int)(soulTowerBean.getUpgradeNeedCoin() * 0.8));
			backMoney.setRock((int)(soulTowerBean.getUpgradeNeedRock() * 0.8));
			backMoney.setMetal((int)(soulTowerBean.getUpgradeNeedMetal() * 0.8));
			backMoney.setCrystal((int)(soulTowerBean.getUpgradeNeedCrystal() * 0.8));
			financeImpl.consume(backMoney);
			List<ThingBean> get = new LinkedList<ThingBean>();
			get.add(new ThingBean(7, 7, 60001, (int)(soulTowerBean.getUpgradeNeedRock() * 0.8), null));
			get.add(new ThingBean(7, 8, 60002, (int)(soulTowerBean.getUpgradeNeedMetal() * 0.8), null));
			get.add(new ThingBean(7, 9, 60003, (int)(soulTowerBean.getUpgradeNeedCrystal() * 0.8), null));
			get.add(new ThingBean(7, 1, 60006, (int)(soulTowerBean.getUpgradeNeedCoin() * 0.8), null));
			GameLog.createLog(id, 20, null, true, get, null, null);*/
			SlaverOp.cancelWork(id, -2);
			return true;
		}
		return false;
	}
	
	public static byte[] createMonster(long id, byte[] information)throws Exception{
		byte[] re = new byte[]{0x01};
		int monsterGoodID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int num = DataFactory.getInt(DataFactory.get(information, 14, 4));
		UserSoulTower userSoulTower = UserMemory.getSoulTower(id);
		SoulTowerBean soulTowerBean = (SoulTowerBean) Goods.getById(GoodsCate.SOULTOWERBEAN, userSoulTower.getSoulTowerLevel());
		MonsterBean monsterBean = (MonsterBean) Goods.getSingleByGoodID(GoodsCate.MONSTERBEAN, monsterGoodID);
		if(userSoulTower != null && soulTowerBean != null && monsterBean != null && monsterBean.getLevel() <= soulTowerBean.getLevel()){
			List<Mission> missionList = MissionOp.getMissionQueue(id);
			int missionQueueSize = 0;
			if(missionList != null){
				missionQueueSize = missionList.size();
			}
			if(missionQueueSize + 1 <= soulTowerBean.getQueueNumLimit()){
				int monsterCount = StorageOp.getMonsterCount(id);
				UserCastle castle = UserMemory.getCastle(id);
				CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
				if(monsterCount + num <= castleBean.getMonsterVol()){
					Mission mission = new Mission();
					int key = MissionOp.getMaxID(missionList) + 1;
					mission.setId(key);
					mission.setMasterID(id);
					mission.setProduct(monsterBean.getGoodID());
					mission.setEndTime(System.currentTimeMillis() + monsterBean.getTime() * num);
					mission.setNum(num);
					FinanceBean bean = new FinanceBean();
					bean.setId(id);
					bean.setCrystal(-monsterBean.getCreateNeedCrystal()* num);
					bean.setMetal(-monsterBean.getCreateNeedMetal() * num);
					bean.setRock(-monsterBean.getCreateNeedRock() * num);
					bean.setSoul(-monsterBean.getCreateNeedSoul() * num);
					bean.setCoin(-monsterBean.getCreateNeedCoin() * num);
					bean.setBadge(-monsterBean.getCreateNeedBadge() * num);
					boolean suc = financeImpl.charge(bean);
					if(suc){
						MissionOp.save(mission);
						DataFactory.replace(re, 0,new byte[]{0x00});
						re = DataFactory.addByteArray(re, DataFactory.getbyte(key));
						List<ThingBean> get = new LinkedList<ThingBean>();
						List<ThingBean> lost = new LinkedList<ThingBean>();
						get.add(new ThingBean(5, mission.getId(), monsterBean.getGoodID(), num, null));
						lost.add(new ThingBean(7, 7, 60001, monsterBean.getCreateNeedRock(), null));
						lost.add(new ThingBean(7, 8, 60002, monsterBean.getCreateNeedMetal(), null));
						lost.add(new ThingBean(7, 9, 60003, monsterBean.getCreateNeedCrystal(), null));
						lost.add(new ThingBean(7, 10, 60004, monsterBean.getCreateNeedSoul(), null));
						lost.add(new ThingBean(7, 1, 60006, monsterBean.getCreateNeedCoin(), null));
						lost.add(new ThingBean(7, 12, 60009, monsterBean.getCreateNeedBadge(), null));
						GameLog.createLog(id, 8, null, true, get, lost, null);
						TaskOp.doTask(id, 10010, 1);
						
					}else{
						re = new byte[]{0x03};
					}
				
				}else{
					re = new byte[]{0x04};
				}
			}else{
				re = new byte[]{0x01};
			}
		}else{
			re = new byte[]{0x02};
		}
		return re;
	}
	
	public static void cancelQueue(long id, byte[] information)throws Exception{
		int queueID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		Mission mission = new Mission();
		mission.setMasterID(id);
		mission.setId(queueID);
		MissionOp.delete(mission);
		GameLog.createLog(id, 12, null, true, null, null, "queueID:" + queueID);
	}
	
	public static long speedup(long id, int propType, double speedupTime){
		UserSoulTower userSoulTower = UserMemory.getSoulTower(id);
		if(userSoulTower != null && userSoulTower.getState() != 1){
			return 0;
		}
		long endTime = 0;
		if(propType == 1){
			endTime = userSoulTower.getEndTime() - (int)speedupTime;
		}else if(propType == 2){
			endTime = userSoulTower.getEndTime() - (long)((userSoulTower.getEndTime() - System.currentTimeMillis()) * speedupTime);
		}else{
			endTime = System.currentTimeMillis();
		}
		userSoulTower.setEndTime(endTime);
		userSoulTower.setChange(true);
		SlaverOp.speedup(id, -2, endTime);
		return UserMemory.getSoulTower(id).getEndTime();
	}
	
	public static long speedUpCreate(long id, int propType, double speedupTime, int queueID){
		Mission mission = MissionOp.getMission(id, queueID);
		if(mission != null){
			long endTime = 0;
			if(propType == 1){
				endTime = mission.getEndTime() - (int)speedupTime;
			}else if(propType == 2){
				endTime = mission.getEndTime() - (long)((mission.getEndTime() - System.currentTimeMillis()) * speedupTime);	
			}else{
				endTime = System.currentTimeMillis();
			}
			mission.setEndTime(endTime);
			DBUtil.update(id, mission);
			return mission.getEndTime();
		}
		return 0;
	}
}

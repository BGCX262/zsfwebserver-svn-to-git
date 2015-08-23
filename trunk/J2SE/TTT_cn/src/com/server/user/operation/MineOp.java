package com.server.user.operation;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.CastleBean;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.MineBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserMine;
import com.server.cache.Mine;
import com.server.cache.Technology;
import com.server.cache.UserMemory;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.DBUtil;

public class MineOp {
	private static Log log = LogFactory.getLog(MineOp.class);
	private static Finance financeImpl = FinanceImpl.instance();
	
	public static byte[] getMine(long id) throws Exception{
		byte[] re = null;
		UserMine userMine = UserMemory.getUserMine(id);
		re = DataFactory.getbyte(userMine.getRockMineLevel());
		re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userMine.getNextRockCollectTime()));
		if(userMine.getRockMineState() == 1){
			re = DataFactory.addByteArray(re, new byte[]{0x01});
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userMine.getRockEndTime()));
		}else{
			re = DataFactory.addByteArray(re, new byte[]{0x00});
		}
		
		re = DataFactory.addByteArray(re, DataFactory.getbyte(userMine.getMetalMineLevel()));
		re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userMine.getNextMetalCollectTime()));
		if(userMine.getMetalMineState() == 1){
			re = DataFactory.addByteArray(re, new byte[]{0x01});
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userMine.getMetalEndTime()));
		}else{
			re = DataFactory.addByteArray(re, new byte[]{0x00});
		}
		
		re = DataFactory.addByteArray(re, DataFactory.getbyte(userMine.getCrystalMineLevel()));
		re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userMine.getNextCrystalCollectTime()));
		if(userMine.getCrystalMineState() == 1){
			re = DataFactory.addByteArray(re, new byte[]{0x01});
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userMine.getCrystalEndTime()));
		}else{
			re = DataFactory.addByteArray(re, new byte[]{0x00});
		}
		return re;
	}

	public static byte[] collect(long id, byte[] information) throws Exception{
		byte[] re = new byte[]{0x01};
		byte type = DataFactory.get(information, 10, 1)[0];
		UserMine userMine = UserMemory.getUserMine(id);
		long threeHours = 1000 * 60 * 60 * 3;
		if(type == 1 || type == 2 || type == 3){
			FinanceBean financeBean = new FinanceBean();
			financeBean.setId(id);
			List<ThingBean> get = new LinkedList<ThingBean>();
			if(type == 1){
				MineBean rockMineBean = (MineBean) Goods.getByGoodIDAndLevel(GoodsCate.MINEBEAN, Mine.ROCK_GOODID, userMine.getRockMineLevel());
				if(userMine.getNextRockCollectTime() < System.currentTimeMillis()){
					long count = (System.currentTimeMillis() - userMine.getNextRockCollectTime()) / rockMineBean.getLimitTime();
					count = Math.min(2, count);
					userMine.setNextRockCollectTime(System.currentTimeMillis() - (count - 1) * rockMineBean.getLimitTime());
					int produce = (int)((double)rockMineBean.getProduce() * (1 + TechnologyOp.technologyATK(id, Technology.ROCK_TEC_GOODID)));
					financeBean.setRock(produce);
					if(financeImpl.consume(financeBean)){
						userMine.setChange(true);
						get.add(new ThingBean(7, 7, 60001, rockMineBean.getProduce(), null));
						re = new byte[]{0x00};
						re = DataFactory.addByteArray(re, DataFactory.getbyte(produce));
						re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userMine.getNextRockCollectTime()));
					}
				}
			}else if(type == 2 ){
				MineBean metalMineBean = (MineBean) Goods.getByGoodIDAndLevel(GoodsCate.MINEBEAN, Mine.METAL_GOODID, userMine.getMetalMineLevel());
				if(userMine.getNextMetalCollectTime() < System.currentTimeMillis()){
					long count = (System.currentTimeMillis() - userMine.getNextMetalCollectTime()) / metalMineBean.getLimitTime();
					count = Math.min(2, count);
					userMine.setNextMetalCollectTime(System.currentTimeMillis() - (count - 1) * metalMineBean.getLimitTime());
					int produce = (int)((double)metalMineBean.getProduce() * (1 + TechnologyOp.technologyATK(id, Technology.METAL_TEC_GOODID)));
					financeBean.setMetal(produce);
					if(financeImpl.consume(financeBean)){
						userMine.setChange(true);
						get.add(new ThingBean(7, 8, 60002, metalMineBean.getProduce(), null));		
						re = new byte[]{0x00};
						re = DataFactory.addByteArray(re, DataFactory.getbyte(produce));
						re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userMine.getNextMetalCollectTime()));
					}
				}
			}else if(type == 3){
				MineBean crystalMineBean = (MineBean) Goods.getByGoodIDAndLevel(GoodsCate.MINEBEAN, Mine.CRYSTAL_GOODID, userMine.getCrystalMineLevel());
				if(userMine.getNextCrystalCollectTime() < System.currentTimeMillis()){
					long count = (System.currentTimeMillis() - userMine.getNextCrystalCollectTime()) / crystalMineBean.getLimitTime();
					count = Math.min(2, count);
					userMine.setNextCrystalCollectTime(System.currentTimeMillis() - (count - 1) * crystalMineBean.getLimitTime());
					int produce = (int)((double)crystalMineBean.getProduce() * (1 + TechnologyOp.technologyATK(id, Technology.CRYSTAL__TEC_GOODID)));
					financeBean.setCrystal(produce);
					if(financeImpl.consume(financeBean)){
						userMine.setChange(true);
						get.add(new ThingBean(7, 9, 60003, crystalMineBean.getProduce(), null));
						re = new byte[]{0x00};
						re = DataFactory.addByteArray(re, DataFactory.getbyte(produce));
						re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userMine.getNextCrystalCollectTime()));
					}
				}
			}
			GameLog.createLog(id, 21, null, true, get, null, null);
			
			TaskOp.doTask(id, 10005, 1);
			TaskOp.doTask(id, 40002, 1);
			TaskOp.doTask(id, 50004, 1);
			
		}
		return re;
	}
	
	public static byte[] upgrade(long id, byte[] information) throws Exception{
		byte[] re = new byte[]{0x01};
		byte type = DataFactory.get(information, 10, 1)[0];
		UserMine userMine = UserMemory.getUserMine(id);
		long endTime = 0;
		if(type == 1 || type == 2 || type == 3){
			FinanceBean cost = new FinanceBean();
			cost.setId(id);
			UserCastle castle = UserMemory.getCastle(id);
			CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
			if(type == 1){
				MineBean rockMineBean = (MineBean) Goods.getByGoodIDAndLevel(GoodsCate.MINEBEAN, Mine.ROCK_GOODID, userMine.getRockMineLevel());
				cost.setRock(-rockMineBean.getUpgradeNeedRock());
				cost.setMetal(-rockMineBean.getUpgradeNeedMetal());
				cost.setCrystal(-rockMineBean.getUpgradeNeedCrystal());
				if(rockMineBean.getUpgradeNeedCastleLevel() <= castleBean.getLevel() && financeImpl.charge(cost)){
					endTime = System.currentTimeMillis() + rockMineBean.getUpgradeTime();
					userMine.setRockEndTime(endTime);
					userMine.setRockMineState(1);
					userMine.setChange(true);
					re = new byte[]{0x00};
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
				}
			}else if(type == 2){
				MineBean metalMineBean = (MineBean) Goods.getByGoodIDAndLevel(GoodsCate.MINEBEAN, Mine.METAL_GOODID, userMine.getMetalMineLevel());
				cost.setRock(-metalMineBean.getUpgradeNeedRock());
				cost.setMetal(-metalMineBean.getUpgradeNeedMetal());
				cost.setCrystal(-metalMineBean.getUpgradeNeedCrystal());
				if(metalMineBean.getUpgradeNeedCastleLevel() <= castleBean.getLevel() && financeImpl.charge(cost)){
					endTime = System.currentTimeMillis() + metalMineBean.getUpgradeTime();
					userMine.setMetalEndTime(endTime);
					userMine.setMetalMineState(1);
					userMine.setChange(true);
					re = new byte[]{0x00};
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
					TaskOp.doTask(id, 10008, 1);
				}
			}else if(type == 3){
				MineBean crystalMineBean = (MineBean) Goods.getByGoodIDAndLevel(GoodsCate.MINEBEAN, Mine.CRYSTAL_GOODID, userMine.getCrystalMineLevel());
				cost.setRock(-crystalMineBean.getUpgradeNeedRock());
				cost.setMetal(-crystalMineBean.getUpgradeNeedMetal());
				cost.setCrystal(-crystalMineBean.getUpgradeNeedCrystal());
				if(crystalMineBean.getUpgradeNeedCastleLevel() <= castleBean.getLevel() && financeImpl.charge(cost)){
					endTime = System.currentTimeMillis() + crystalMineBean.getUpgradeTime();
					userMine.setCrystalEndTime(endTime);
					userMine.setCrystalMineState(1);
					userMine.setChange(true);
					re = new byte[]{0x00};
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
				}
			}
			TaskOp.doUpMineTask(id);
		}
		return re;
	}
	
	public static UserMine initMine(long id){
		UserMine userMine = new UserMine();
		userMine.setId(id);
		userMine.setRockMineLevel(1);
		userMine.setMetalMineLevel(1);
		userMine.setCrystalMineLevel(1);
		userMine.setNextRockCollectTime(System.currentTimeMillis());
		userMine.setNextMetalCollectTime(System.currentTimeMillis());
		userMine.setNextCrystalCollectTime(System.currentTimeMillis());
		userMine.setChange(true);
		DBUtil.merge(id, userMine);
		if(userMine != null){
			userMine.setTime(System.currentTimeMillis());
		}
		return userMine;
	}
	
	public static boolean resetMine(long id, Session session){
		UserMine userMine = new UserMine();
		userMine.setId(id);
		userMine.setRockMineLevel(1);
		userMine.setMetalMineLevel(1);
		userMine.setCrystalMineLevel(1);
		userMine.setTime(System.currentTimeMillis());
		session.update(userMine);
		return true;
	}
	
	public static long speedup(long id, int propType, double speedupTime, int type){
		UserMine userMine = UserMemory.getUserMine(id);
		if(userMine != null){
			if(type == 3 && userMine.getRockMineState() == 1){
				if(propType == 1){
					userMine.setRockEndTime(userMine.getRockEndTime() - (int)speedupTime);
				}else if(propType == 2){
					userMine.setRockEndTime(userMine.getRockEndTime() - (long)((userMine.getRockEndTime() - System.currentTimeMillis()) * speedupTime));
				}else{
					userMine.setRockEndTime(System.currentTimeMillis());
					//userMine.setRockMineState(0);
				}
				userMine.setChange(true);
				return UserMemory.getUserMine(id).getRockEndTime();
			}else if(type == 4 && userMine.getMetalMineState() == 1){
				if(propType == 1){
					userMine.setMetalEndTime(userMine.getMetalEndTime() - (int)speedupTime);
				}else if(propType == 2){
					userMine.setMetalEndTime(userMine.getMetalEndTime() - (long)((userMine.getMetalEndTime() - System.currentTimeMillis()) * speedupTime));
				}else{
					userMine.setMetalEndTime(System.currentTimeMillis());
					//userMine.setMetalMineState(0);
				}
				userMine.setChange(true);
				return UserMemory.getUserMine(id).getMetalEndTime();
			}else if(type == 5 && userMine.getCrystalMineState() == 1){
				if(propType == 1){
					userMine.setCrystalEndTime(userMine.getCrystalEndTime() - (int)speedupTime);
				}else if(propType == 2){
					userMine.setCrystalEndTime(userMine.getCrystalEndTime() - (long)((userMine.getCrystalEndTime() - System.currentTimeMillis()) * speedupTime));
				}else{
					userMine.setCrystalEndTime(System.currentTimeMillis());
				}
				userMine.setChange(true);
				return UserMemory.getUserMine(id).getCrystalEndTime();
			}
		}
		return 0;
	}
	
	public static boolean cancel(long id, int type){
		UserMine userMine = UserMemory.getUserMine(id);
		if(type == 1){
			if(userMine.getRockMineState() == 1){
				userMine.setRockMineState(0);
				userMine.setChange(true);
				return true;
			}
		}else if(type == 2){
			if(userMine.getMetalMineState() == 1){
				userMine.setMetalMineState(0);
				userMine.setChange(true);
				return true;
			}
		}else if(type == 3){
			if(userMine.getCrystalMineState() == 1){
				userMine.setCrystalMineState(0);
				userMine.setChange(true);
				return true;
			}
		}
		return false;
	}
}

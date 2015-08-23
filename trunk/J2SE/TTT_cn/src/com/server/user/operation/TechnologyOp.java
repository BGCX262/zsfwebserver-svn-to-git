package com.server.user.operation;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.CastleBean;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.TechnologyTowerBean;
import com.database.model.bean.TechnologyTreeBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserTechnology;
import com.server.cache.Technology;
import com.server.cache.UserMemory;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.DBUtil;

public class TechnologyOp {
	private static Finance financeImpl = FinanceImpl.instance();
	private static Log log = LogFactory.getLog(TechnologyOp.class);
	
	public static UserTechnology initTechnology(long id){
		UserTechnology userTec = new UserTechnology();
		userTec.setId(id);
		userTec.setTecTowerLevel(1);
		userTec.setRockTechnologyLevel(0);
		userTec.setMetalTechnologyLevel(0);
		userTec.setCrystalTechnologyLevel(0);
		userTec.setPowderTechnologyLevel(0);
		userTec.setTrainTechnologyLevel(0);
		userTec.setAutoAttackTechnologyLevel(0);
		DBUtil.save(id, userTec);
		if(userTec != null){
			userTec.setTime(System.currentTimeMillis());
		}
		return userTec;
	}
	
	public static boolean resetTechnology(long id, Session session){
		UserTechnology userTec = new UserTechnology();
		userTec.setId(id);
		userTec.setTecTowerLevel(1);
		userTec.setRockTechnologyLevel(0);
		userTec.setMetalTechnologyLevel(0);
		userTec.setCrystalTechnologyLevel(0);
		userTec.setPowderTechnologyLevel(0);
		userTec.setTrainTechnologyLevel(0);
		userTec.setAutoAttackTechnologyLevel(0);
		userTec.setTime(System.currentTimeMillis());
		session.update(userTec);
		return true;
	}
	
	public static byte[] getUserTec(long id) throws Exception{
		byte[] re = null;
		UserTechnology userTec = UserMemory.getUserTec(id);
		re = DataFactory.getbyte(userTec.getTecTowerLevel());
		if(userTec.isUpgradeTecTower()){
			re = DataFactory.addByteArray(re, new byte[]{0x00});
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userTec.getTecTowerEndTime()));
		}else{
			re = DataFactory.addByteArray(re, new byte[]{0x01});
		}
		
		int num = 6;
		re = DataFactory.addByteArray(re, DataFactory.getbyte(num));
		
		re = DataFactory.addByteArray(re, DataFactory.getbyte(Technology.AUTO_ATTACK_GOODID));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(userTec.getAutoAttackTechnologyLevel()));
		if(userTec.isUpgradeAutoAttackTec()){
			re = DataFactory.addByteArray(re, new byte[]{0x00});
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userTec.getAutoAttackTecEndTime()));
		}else{
			re = DataFactory.addByteArray(re, new byte[]{0x01});
		}
		
		re = DataFactory.addByteArray(re, DataFactory.getbyte(Technology.ROCK_TEC_GOODID));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(userTec.getRockTechnologyLevel()));
		if(userTec.isUpgradeRockTec()){
			re = DataFactory.addByteArray(re, new byte[]{0x00});
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userTec.getRockTecEndTime()));
		}else{
			re = DataFactory.addByteArray(re, new byte[]{0x01});
		}
		
		re = DataFactory.addByteArray(re, DataFactory.getbyte(Technology.METAL_TEC_GOODID));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(userTec.getMetalTechnologyLevel()));
		if(userTec.isUpgradeMetalTec()){
			re = DataFactory.addByteArray(re, new byte[]{0x00});
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userTec.getMetalTecEndTime()));
		}else{
			re = DataFactory.addByteArray(re, new byte[]{0x01});
		}
		
		re = DataFactory.addByteArray(re, DataFactory.getbyte(Technology.CRYSTAL__TEC_GOODID));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(userTec.getCrystalTechnologyLevel()));
		if(userTec.isUpgradeCrystalTec()){
			re = DataFactory.addByteArray(re, new byte[]{0x00});
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userTec.getCrystalTecEndTime()));
		}else{
			re = DataFactory.addByteArray(re, new byte[]{0x01});
		}
		
		re = DataFactory.addByteArray(re, DataFactory.getbyte(Technology.POWDER_TEC_GOODID));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(userTec.getPowderTechnologyLevel()));
		if(userTec.isUpgradePowderTec()){
			re = DataFactory.addByteArray(re, new byte[]{0x00});
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userTec.getPowderTecEndTime()));
		}else{
			re = DataFactory.addByteArray(re, new byte[]{0x01});
		}
		
		re = DataFactory.addByteArray(re, DataFactory.getbyte(Technology.TRAIN_TEC_GOODID));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(userTec.getTrainTechnologyLevel()));
		if(userTec.isUpgradeTrainTec()){
			re = DataFactory.addByteArray(re, new byte[]{0x00});
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(userTec.getTrainTecEndTime()));
		}else{
			re = DataFactory.addByteArray(re, new byte[]{0x01});
		}
		
		return re;
	}
	
	public static byte[] upgradeTecTower(long id, byte[] information) throws Exception{
		byte[] re = new byte[]{0x01};
		int num = DataFactory.getInt(DataFactory.get(information, 10, 4));
		long[] sla = new long[num];
		for(int i = 0; i < num; i++){
			sla[i] = DataFactory.doubleBytesToLong(DataFactory.get(information, 14 + 8 * i, 8));
		}
		if(!SlaverOp.isFree(id, sla)){
			log.warn("Game_Warning:no slaver work for this");
			re = new byte[]{0x03};
			return re;
		}
		UserTechnology userTec = UserMemory.getUserTec(id);
		TechnologyTowerBean tecTowerBean = (TechnologyTowerBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTOWERBEAN, Technology.TEC_TOWER_GOODID, userTec.getTecTowerLevel());
		UserCastle castle = UserMemory.getCastle(id);
		CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
		if(userTec != null && tecTowerBean != null && castleBean.getLevel() >= tecTowerBean.getUpgradeNeedCastleLevel()){
			FinanceBean financeBean = new FinanceBean();
			financeBean.setId(id);
			financeBean.setCoin(-tecTowerBean.getUpgradeNeedCoin());
			financeBean.setRock(-tecTowerBean.getUpgradeNeedRock());
			financeBean.setMetal(-tecTowerBean.getUpgradeNeedMetal());
			financeBean.setCrystal(-tecTowerBean.getUpgradeNeedCrystal());
			if(financeImpl.charge(financeBean)){
				long endTime = SlaverOp.upgradeTecTower(id, sla);
				userTec.setTecTowerEndTime(endTime);
				userTec.setUpgradeTecTower(true);
				userTec.setChange(true);
				List<ThingBean> lost = new LinkedList<ThingBean>();
				lost.add(new ThingBean(7, 1, 60006, tecTowerBean.getUpgradeNeedCoin(), null));
				lost.add(new ThingBean(7, 7,60001, tecTowerBean.getUpgradeNeedRock(), null));
				lost.add(new ThingBean(7, 8, 60002, tecTowerBean.getUpgradeNeedMetal(), null));
				lost.add(new ThingBean(7, 9, 60003, tecTowerBean.getUpgradeNeedCrystal(), null));
				//lost.add(new ThingBean(null, null, 60005, castleBean.getUpNeedGlory(), null));
				GameLog.createLog(id, 25, null, true, null, lost, null);
				re = new byte[]{0x00};
				re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
			}else{
				re = new byte[]{0x02};
			}
		}
		return re;
	}
	
	public static byte[] upgradeTecTree(long id, byte[] information) throws Exception{
		byte[] re = new byte[]{0x01};
		int goodID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		TechnologyTreeBean tecTreeBean = null;
		UserTechnology userTec = UserMemory.getUserTec(id);
		if(goodID == Technology.ROCK_TEC_GOODID){
			tecTreeBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, goodID, userTec.getRockTechnologyLevel()); 
		}else if(goodID == Technology.METAL_TEC_GOODID){
			tecTreeBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, goodID, userTec.getMetalTechnologyLevel()); 
		}else if(goodID == Technology.CRYSTAL__TEC_GOODID){
			tecTreeBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, goodID, userTec.getCrystalTechnologyLevel()); 
		}else if(goodID == Technology.POWDER_TEC_GOODID){
			tecTreeBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, goodID, userTec.getPowderTechnologyLevel()); 
		}else if(goodID == Technology.TRAIN_TEC_GOODID){
			tecTreeBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, goodID, userTec.getTrainTechnologyLevel()); 
		}else if(goodID == Technology.AUTO_ATTACK_GOODID){
			tecTreeBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, goodID, userTec.getAutoAttackTechnologyLevel()); 
		}
		
		if(userTec.getTecTowerLevel() < tecTreeBean.getUpgradeNeedTecTowerLevel()){
			return re;
		}
		
		FinanceBean financeBean = new FinanceBean();
		financeBean.setId(id);
		financeBean.setCoin(-tecTreeBean.getUpgradeNeedCoin());
		financeBean.setRock(-tecTreeBean.getUpgradeNeedRock());
		financeBean.setMetal(-tecTreeBean.getUpgradeNeedMetal());
		financeBean.setCrystal(-tecTreeBean.getUpgradeNeedCrystal());
		boolean suc = financeImpl.charge(financeBean);
		if(suc){
			long endTime = System.currentTimeMillis() + tecTreeBean.getUpgradeTime();
			if(goodID == Technology.ROCK_TEC_GOODID){
				userTec.setRockTecEndTime(endTime);
				userTec.setUpgradeRockTec(true);
				userTec.setChange(true);
				re = new byte[]{0x00};
				re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
			}else if(goodID == Technology.METAL_TEC_GOODID){
				userTec.setMetalTecEndTime(endTime);
				userTec.setUpgradeMetalTec(true);
				userTec.setChange(true);
				re = new byte[]{0x00};
				re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
			}else if(goodID == Technology.CRYSTAL__TEC_GOODID){
				userTec.setCrystalTecEndTime(endTime);
				userTec.setUpgradeCrystalTec(true);
				userTec.setChange(true);
				re = new byte[]{0x00};
				re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
			}else if(goodID == Technology.POWDER_TEC_GOODID){
				userTec.setPowderTecEndTime(endTime);
				userTec.setUpgradePowderTec(true);
				userTec.setChange(true);
				re = new byte[]{0x00};
				re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
			}else if(goodID == Technology.TRAIN_TEC_GOODID){
				userTec.setTrainTecEndTime(endTime);
				userTec.setUpgradeTrainTec(true);
				userTec.setChange(true);
				re = new byte[]{0x00};
				re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
			}else if(goodID == Technology.AUTO_ATTACK_GOODID){
				userTec.setAutoAttackTecEndTime(endTime);
				userTec.setUpgradeAutoAttackTec(true);
				userTec.setChange(true);
				re = new byte[]{0x00};
				re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
				TaskOp.doTask(id, 10011, 1);
			}
			
			if(re[0] == 0x00){
				List<ThingBean> lost = new LinkedList<ThingBean>();
				lost.add(new ThingBean(7, 1, 60006, tecTreeBean.getUpgradeNeedCoin(), null));
				lost.add(new ThingBean(7, 7,60001, tecTreeBean.getUpgradeNeedRock(), null));
				lost.add(new ThingBean(7, 8, 60002, tecTreeBean.getUpgradeNeedMetal(), null));
				lost.add(new ThingBean(7, 9, 60003, tecTreeBean.getUpgradeNeedCrystal(), null));
				//lost.add(new ThingBean(null, null, 60005, castleBean.getUpNeedGlory(), null));
				GameLog.createLog(id, 26, null, true, null, lost, null);
			}
		}else {
			re = new byte[]{0x02};
		}
		return re;
	}
	
	public static long speedup(long id, int propType, double speedupTime){
		long endTime = 0;
		UserTechnology userTec = UserMemory.getUserTec(id);
		if(userTec != null && userTec.isUpgradeTecTower()){
			if(propType == 1){
				endTime = userTec.getTecTowerEndTime() - (long)speedupTime;
			}else if(propType == 2){
				endTime = userTec.getTecTowerEndTime() - (long)((userTec.getTecTowerEndTime() - System.currentTimeMillis()) * speedupTime);
			}else{
				endTime = System.currentTimeMillis();
			}
			userTec.setTecTowerEndTime(endTime);
			userTec.setChange(true);
			SlaverOp.speedup(id, -3, endTime);
		}
		return endTime;
	}
	
	public static double technologyATK(long id, int goodID){
		UserTechnology userTec = UserMemory.getUserTec(id);
		TechnologyTreeBean tecTreeBean = null; 
		if(goodID == Technology.ROCK_TEC_GOODID){
			tecTreeBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, goodID, userTec.getRockTechnologyLevel());
		}else if(goodID == Technology.METAL_TEC_GOODID){
			tecTreeBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, goodID, userTec.getMetalTechnologyLevel());
		}else if(goodID == Technology.CRYSTAL__TEC_GOODID){
			tecTreeBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, goodID, userTec.getCrystalTechnologyLevel());
		}else if(goodID == Technology.POWDER_TEC_GOODID){
			tecTreeBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, goodID, userTec.getPowderTechnologyLevel());
		}else if(goodID == Technology.TRAIN_TEC_GOODID){
			tecTreeBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, goodID, userTec.getTrainTechnologyLevel());
		}else if(goodID == Technology.AUTO_ATTACK_GOODID){
			tecTreeBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, goodID, userTec.getAutoAttackTechnologyLevel());
		}
		return tecTreeBean.getEffectPara();
	}
	
	public static boolean cancel(long id, double returnRate){
		UserTechnology tecTower = UserMemory.getUserTec(id);
		if(tecTower != null && tecTower.isUpgradeTecTower()){
			tecTower.setUpgradeTecTower(false);
			tecTower.setChange(true);
			SlaverOp.cancelWork(id, -3);
			return true;
		}
		return false;
	}
	
	public static long speedupTecTree(long id, int propType, double speedupTime, int tecTreeGoodID ){
		UserTechnology userTec = UserMemory.getUserTec(id);
		long endTime = 0;
		if(tecTreeGoodID == Technology.ROCK_TEC_GOODID && userTec.isUpgradeRockTec()){
			endTime = userTec.getRockTecEndTime();
			if(propType == 1){
				endTime = endTime - (int)speedupTime;
			}else if(propType == 2){
				endTime = endTime - (long)((endTime - System.currentTimeMillis()) * speedupTime);
			}else{
				endTime = System.currentTimeMillis();
			}
			userTec.setRockTecEndTime(endTime);
			userTec.setChange(true);
		}else if(tecTreeGoodID == Technology.METAL_TEC_GOODID && userTec.isUpgradeMetalTec()){
			endTime = userTec.getMetalTecEndTime();
			if(propType == 1){
				endTime = endTime - (int)speedupTime;
			}else if(propType == 2){
				endTime = endTime - (long)((endTime - System.currentTimeMillis()) * speedupTime);
			}else{
				endTime = System.currentTimeMillis();
			}
			userTec.setMetalTecEndTime(endTime);
			userTec.setChange(true);
		}else if(tecTreeGoodID == Technology.CRYSTAL__TEC_GOODID && userTec.isUpgradeCrystalTec()){
			endTime = userTec.getCrystalTecEndTime();
			if(propType == 1){
				endTime = endTime - (int)speedupTime;
			}else if(propType == 2){
				endTime = endTime - (long)((endTime - System.currentTimeMillis()) * speedupTime);
			}else{
				endTime = System.currentTimeMillis();
			}
			userTec.setCrystalTecEndTime(endTime);
			userTec.setChange(true);
		}else if(tecTreeGoodID == Technology.POWDER_TEC_GOODID && userTec.isUpgradePowderTec()){
			endTime = userTec.getPowderTecEndTime();
			if(propType == 1){
				endTime = endTime - (int)speedupTime;
			}else if(propType == 2){
				endTime = endTime - (long)((endTime - System.currentTimeMillis()) * speedupTime);
			}else{
				endTime = System.currentTimeMillis();
			}
			userTec.setPowderTecEndTime(endTime);
			userTec.setChange(true);
		}else if(tecTreeGoodID == Technology.TRAIN_TEC_GOODID && userTec.isUpgradeTrainTec()){
			endTime = userTec.getTrainTecEndTime();
			if(propType == 1){
				endTime = endTime - (int)speedupTime;
			}else if(propType == 2){
				endTime = endTime - (long)((endTime - System.currentTimeMillis()) * speedupTime);
			}else{
				endTime = System.currentTimeMillis();
			}
			userTec.setTrainTecEndTime(endTime);
			userTec.setChange(true);
		}else if(tecTreeGoodID == Technology.AUTO_ATTACK_GOODID && userTec.isUpgradeAutoAttackTec()){
			endTime = userTec.getAutoAttackTecEndTime();
			if(propType == 1){
				endTime = endTime - (int)speedupTime;
			}else if(propType == 2){
				endTime = endTime - (long)((endTime - System.currentTimeMillis()) * speedupTime);
			}else{
				endTime = System.currentTimeMillis();
			}
			userTec.setAutoAttackTecEndTime(endTime);
			userTec.setChange(true);
		}
		
		return endTime;
	}
	
	public static boolean cancelTecTree(long id, double returnRate, int tecTreeGoodID){
		UserTechnology userTec = UserMemory.getUserTec(id);
		if(tecTreeGoodID == Technology.ROCK_TEC_GOODID && userTec.isUpgradeRockTec()){
			userTec.setUpgradeRockTec(false);
			userTec.setChange(true);
			return true;
		}else if(tecTreeGoodID == Technology.METAL_TEC_GOODID && userTec.isUpgradeMetalTec()){
			userTec.setUpgradeMetalTec(false);
			userTec.setChange(true);
			return true;
		}else if(tecTreeGoodID == Technology.CRYSTAL__TEC_GOODID && userTec.isUpgradeCrystalTec()){
			userTec.setUpgradeCrystalTec(false);
			userTec.setChange(true);
			return true;
		}else if(tecTreeGoodID == Technology.POWDER_TEC_GOODID && userTec.isUpgradePowderTec()){
			userTec.setUpgradePowderTec(false);
			userTec.setChange(true);
			return true;
		}else if(tecTreeGoodID == Technology.TRAIN_TEC_GOODID && userTec.isUpgradeTrainTec()){
			userTec.setUpgradeTrainTec(false);
			userTec.setChange(true);
			return true;
		}else if(tecTreeGoodID == Technology.AUTO_ATTACK_GOODID && userTec.isUpgradeAutoAttackTec()){
			userTec.setUpgradeAutoAttackTec(false);
			userTec.setChange(true);
			return true;
		}
		return false;
	}
}

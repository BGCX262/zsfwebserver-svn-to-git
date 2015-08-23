package com.server.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.database.model.bean.MemoryBean;
import com.database.model.bean.TechnologyTowerBean;
import com.database.model.bean.TechnologyTreeBean;
import com.database.model.bean.UserTechnology;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class Technology implements Cache {
	private static Log log = LogFactory.getLog(Technology.class);
	private static final int CACHE_TIME = Configuration.getTecCacheTime();
	private static final int MASTER_CACHE_TIME = Configuration.getMasterTecCacheTime();
	public static final int TEC_TOWER_GOODID  = 160001;//科技塔
	public static final int ROCK_TEC_GOODID = 170001;//石头科技
	public static final int METAL_TEC_GOODID = 170002;//金属科技
	public static final int CRYSTAL__TEC_GOODID = 170003;//水晶科技
	public static final int POWDER_TEC_GOODID = 170004;//火药科技
	public static final int TRAIN_TEC_GOODID = 170005;//抗击科技
	public static final int AUTO_ATTACK_GOODID = 170006;//抗击科技
	@Override
	public void clean(MemoryBean bean) {
		if(bean != null){
			UserTechnology userTec = bean.getUserTec();
			if(userTec.isUpgradeTecTower() && userTec.getTecTowerEndTime() < System.currentTimeMillis()){
				TechnologyTowerBean upgradeTecTowerBean = (TechnologyTowerBean) Goods.getById(GoodsCate.TECHNOLOGYTOWERBEAN, userTec.getTecTowerLevel() + 1);
				if(upgradeTecTowerBean != null){
					userTec.setTecTowerLevel(upgradeTecTowerBean.getLevel());
				}
				userTec.setUpgradeTecTower(false);
				userTec.setChange(true);
			}
			
			if(userTec.isUpgradeRockTec() && userTec.getRockTecEndTime() < System.currentTimeMillis()){
				TechnologyTreeBean upgradeRockTecBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, ROCK_TEC_GOODID, userTec.getRockTechnologyLevel() + 1);
				if(upgradeRockTecBean != null){
					userTec.setRockTechnologyLevel(upgradeRockTecBean.getLevel());
				}
				userTec.setUpgradeRockTec(false);
				userTec.setChange(true);
			}
			
			if(userTec.isUpgradeMetalTec() && userTec.getMetalTecEndTime() < System.currentTimeMillis()){
				TechnologyTreeBean upgradeMetalTecBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, METAL_TEC_GOODID, userTec.getMetalTechnologyLevel() + 1);
				if(upgradeMetalTecBean != null){
					userTec.setMetalTechnologyLevel(upgradeMetalTecBean.getLevel());
				}
				userTec.setUpgradeMetalTec(false);
				userTec.setChange(true);
			}
			
			if(userTec.isUpgradeCrystalTec() && userTec.getCrystalTecEndTime() < System.currentTimeMillis()){
				TechnologyTreeBean upgradeCrystalTecBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, CRYSTAL__TEC_GOODID, userTec.getCrystalTechnologyLevel() + 1);
				if(upgradeCrystalTecBean != null){
					userTec.setCrystalTechnologyLevel(upgradeCrystalTecBean.getLevel());
				}
				userTec.setUpgradeCrystalTec(false);
				userTec.setChange(true);
			}
			
			if(userTec.isUpgradePowderTec() && userTec.getPowderTecEndTime() < System.currentTimeMillis()){
				TechnologyTreeBean upgradePowderTecBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, POWDER_TEC_GOODID, userTec.getPowderTechnologyLevel() + 1);
				if(upgradePowderTecBean != null){
					userTec.setPowderTechnologyLevel(upgradePowderTecBean.getLevel());
				}
				userTec.setUpgradePowderTec(false);
				userTec.setChange(true);
			}
			
			if(userTec.isUpgradeTrainTec() && userTec.getTrainTecEndTime() < System.currentTimeMillis()){
				TechnologyTreeBean upgradeTrainTecBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, TRAIN_TEC_GOODID, userTec.getTrainTechnologyLevel() + 1);
				if(upgradeTrainTecBean != null){
					userTec.setTrainTechnologyLevel(upgradeTrainTecBean.getLevel());
				}
				userTec.setUpgradeTrainTec(false);
				userTec.setChange(true);
			}
			
			if(userTec.isUpgradeAutoAttackTec() && userTec.getAutoAttackTecEndTime() < System.currentTimeMillis()){
				TechnologyTreeBean upgradeAutoAttackTecBean = (TechnologyTreeBean) Goods.getByGoodIDAndLevel(GoodsCate.TECHNOLOGYTREEBEAN, AUTO_ATTACK_GOODID, userTec.getAutoAttackTechnologyLevel() + 1);
				if(upgradeAutoAttackTecBean != null){
					userTec.setAutoAttackTechnologyLevel(upgradeAutoAttackTecBean.getLevel());
				}
				userTec.setUpgradeAutoAttackTec(false);
				userTec.setChange(true);
			}
		}
	}

	@Override
	public Object gerFromDB(long id, Object para) {
		return DBUtil.get(id, UserTechnology.class);
	}

	@Override
	public Object getFromDB(long id) {
		return DBUtil.get(id, UserTechnology.class);
	}

	@Override
	public void persist(MemoryBean bean) {
		if(bean != null){
			UserTechnology userTec = bean.getUserTec();
			if(userTec != null){
				if(System.currentTimeMillis() - userTec.getTime() > CACHE_TIME){
					update(bean);
				}else if(System.currentTimeMillis() - userTec.getTime() > MASTER_CACHE_TIME){
					update(bean);
					bean.setUserTec(null);
				}
			}
		}
	}

	@Override
	public void update(MemoryBean bean) {
		if(bean != null){
			UserTechnology userTec = bean.getUserTec();
			if(userTec != null && userTec.isChange()){
				DBUtil.update(userTec.getId(), userTec);
				userTec.setChange(false);
			}
		}
	}

}

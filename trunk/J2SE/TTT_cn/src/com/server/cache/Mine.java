package com.server.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.database.model.bean.MemoryBean;
import com.database.model.bean.MineBean;
import com.database.model.bean.UserMine;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class Mine implements Cache {
	private static Log log = LogFactory.getLog(Mine.class);
	private static final int MINE_CACHE_TIME = Configuration.getMineCacheTime();
	private static final int MASTER_MINE_CACHE_TIME = Configuration.getMasterMineCacheTime();
	public static final int ROCK_GOODID = 150001;
	public static final int METAL_GOODID = 150002;
	public static final int CRYSTAL_GOODID = 150003;
	@Override
	public void clean(MemoryBean bean) {
		if(bean != null){
			UserMine userMine = bean.getUserMine();
			if(userMine.getRockMineState() == 1 && userMine.getRockEndTime() < System.currentTimeMillis()){
				MineBean upgradeRockMineBean = (MineBean) Goods.getByGoodIDAndLevel(GoodsCate.MINEBEAN, ROCK_GOODID, userMine.getRockMineLevel() + 1);
				if(upgradeRockMineBean != null){
					userMine.setRockMineLevel(upgradeRockMineBean.getLevel());
				}
				userMine.setRockMineState(0);
				userMine.setChange(true);
			}
			
			if(userMine.getMetalMineState() == 1 && userMine.getMetalEndTime() < System.currentTimeMillis()){
				MineBean upgradeMetalMineBean = (MineBean) Goods.getByGoodIDAndLevel(GoodsCate.MINEBEAN, METAL_GOODID, userMine.getMetalMineLevel() + 1);
				if(upgradeMetalMineBean != null){
					userMine.setMetalMineLevel(upgradeMetalMineBean.getLevel());
				}
				userMine.setMetalMineState(0);
				userMine.setChange(true);
			}
			
			if(userMine.getCrystalMineState() == 1 && userMine.getCrystalEndTime() < System.currentTimeMillis()){
				MineBean upgradeCrystalMineBean = (MineBean) Goods.getByGoodIDAndLevel(GoodsCate.MINEBEAN, CRYSTAL_GOODID, userMine.getCrystalMineLevel() + 1);
				if(upgradeCrystalMineBean != null){
					userMine.setCrystalMineLevel(upgradeCrystalMineBean.getLevel());
				}
				userMine.setCrystalMineState(0);
				userMine.setChange(true);
			}
		}
	}

	@Override
	public Object gerFromDB(long id, Object para) {
		return DBUtil.get(id, UserMine.class);
	}

	@Override
	public Object getFromDB(long id) {
		return DBUtil.get(id, UserMine.class);
	}

	@Override
	public void persist(MemoryBean bean) {
		if(bean != null){
			UserMine userMine = bean.getUserMine();
			if(userMine != null){
				if(System.currentTimeMillis() - userMine.getTime() > MINE_CACHE_TIME){
					update(bean);
				}else if(System.currentTimeMillis() - userMine.getTime() > MASTER_MINE_CACHE_TIME){
					update(bean);
					bean.setUserMine(null);
				}
			}
		}
	}

	@Override
	public void update(MemoryBean bean) {
		if(bean != null){
			UserMine userMine = bean.getUserMine();
			if(userMine != null && userMine.isChange()){
				DBUtil.update(userMine.getId(), userMine);
				userMine.setChange(false);
			}
		}
	}

}

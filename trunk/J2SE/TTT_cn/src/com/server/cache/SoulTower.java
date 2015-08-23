package com.server.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.database.model.bean.MemoryBean;
import com.database.model.bean.SoulTowerBean;
import com.database.model.bean.UserSoulTower;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class SoulTower implements Cache{
	private static Log log = LogFactory.getLog(SoulTower.class);
	private static final int SOULTOWER_CACHE_TIME = Configuration.getSoulCacheTime();
	private static final int MASTER_SOULTOWER_CACHE_TIME = Configuration.getMasterSoulCacheTime();
	@Override
	public void clean(MemoryBean bean) {
		if(bean != null){
			UserSoulTower userSoulTower = bean.getSoulTower();
			if(userSoulTower.getState() == 1 && userSoulTower.getEndTime() < System.currentTimeMillis()){
				SoulTowerBean upgradeSoulTowerBean = (SoulTowerBean) Goods.getById(GoodsCate.SOULTOWERBEAN, userSoulTower.getSoulTowerLevel() + 1);
				if(upgradeSoulTowerBean != null){
					userSoulTower.setSoulTowerLevel(upgradeSoulTowerBean.getLevel());
				}
				userSoulTower.setState(0);
				userSoulTower.setChange(true);
			}
		}
	}

	@Override
	public Object gerFromDB(long id, Object para) {
		return (UserSoulTower)DBUtil.get(id, UserSoulTower.class);
	}

	@Override
	public Object getFromDB(long id) {
		return (UserSoulTower)DBUtil.get(id, UserSoulTower.class);
	}

	@Override
	public void persist(MemoryBean bean) {
		if(bean != null){
			UserSoulTower userSoulTower = bean.getSoulTower();
			if(userSoulTower != null){
				if(System.currentTimeMillis() - userSoulTower.getTime() > SOULTOWER_CACHE_TIME){
					update(bean);
				}else if(System.currentTimeMillis() - userSoulTower.getTime() > MASTER_SOULTOWER_CACHE_TIME){
					update(bean);
					bean.setSoulTower(null);
				}
			}
		}
	}

	@Override
	public void update(MemoryBean bean) {
		if(bean != null){
			UserSoulTower userSoulTower = bean.getSoulTower();
			if(userSoulTower != null && userSoulTower.isChange()){
				DBUtil.update(userSoulTower.getId(), userSoulTower);
				userSoulTower.setChange(false);
			}
		}
	}
}

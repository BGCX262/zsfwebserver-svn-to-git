package com.server.cache;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.MemoryBean;
import com.database.model.bean.TowerBean;
import com.database.model.bean.UserCity;
import com.database.model.bean.UserCityInfo;
import com.database.model.bean.UserSceneGoods;
import com.database.model.bean.UserTower;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.util.Configuration;

public class City  implements Cache {
	private static Log log = LogFactory.getLog(City.class);
	private static final int CITY_CACHE_TIME = Configuration.getCitCacTime();
	private static final int FRIEND_CITY_CACHE_TIME = Configuration.getFriCitCacTime();
	private static final int MASTER_CITY_CACHE_TIME = Configuration.getMasCitCacTime();
	//private static final int DROPGOODS_DISAPPEAR_TIME = 12 * 3600000;
	
	@Override
	public Object getFromDB(long id){
		List<UserCityInfo> list = null;
		Session session = null;
		try{
			session = HibernateUtil.currentSession(id);
			SQLQuery query = session.createSQLQuery("select * from usercityinfo where masterID = " + id);
			query.addEntity(UserCityInfo.class);
			list = query.list();
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return list;
	}
	
	@Override
	public Object gerFromDB(long id, Object para){
		UserCity city = null;
		Session session = null;
		try{
			session = HibernateUtil.currentSession(id);
			SQLQuery query = session.createSQLQuery("select * from usercityinfo where masterID = " + id + " and cityID = " + Integer.valueOf(para.toString()));
			query.addEntity(UserCityInfo.class);
			UserCityInfo cityInfo = null;
			List<UserCityInfo> list = query.list();
			if(list != null && list.size() > 0){
				cityInfo = list.get(0);
			}
			if(cityInfo != null){
				city = UserCity.decodeFromJA(cityInfo);
			}
			//cleanSceneGoods(id, city);
			//cleanHp(id, city);
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return city;
	}
	
	private static void cleanSceneGoods(long id, UserCity city){
		if(city != null){/*
			List<UserSceneGoods> goodsList = city.getGoods();
			Iterator<UserSceneGoods> goodsIte = goodsList.iterator();
			while(goodsIte.hasNext()){
				UserSceneGoods goods = goodsIte.next();
				if((TempBoxOp.getBox(city.getId(), tempBoxList, goods.getId()) != null)){
					goodsIte.remove();
					city.setChange(true);
				}
			}
		*/}
	}
	
	private void cleanHp(UserCity city, UserTower tower){
		if(System.currentTimeMillis() - tower.getTime() > 3600000 && tower.getHp() > 0){
			long num = System.currentTimeMillis() / 3600000 - tower.getTime() / 3600000;
			int hp = (int)(tower.getHp() - 1 * num);
			if(hp > 0){
				tower.setHp(hp);
			}else{
				tower.setHp(0);
			}
			tower.setTime(tower.getTime() + num * 3600000);
			city.setChange(true);
		}
	}
	
	@Override
	public void clean(MemoryBean bean){
		if(bean != null){
			UserCity city = bean.getCity();
			if(city != null){
				List<UserTower> towers = city.getTowers();
				if(towers != null){
					Iterator<UserTower> ite = towers.iterator();
					while(ite.hasNext()){
						UserTower tower = ite.next();
						cleanHp(city, tower);
						if(tower.getEndTime() < System.currentTimeMillis()){
							if(tower.getState() == 1){
								tower.setState(0);
								city.setChange(true);
							}else if(tower.getState() == 2){
								TowerBean towerBean = (TowerBean)Goods.getById(GoodsCate.TOWERBEAN, tower.getTowerID());
								tower.setState(0);
								tower.setTowerID(((TowerBean)Goods.getById(GoodsCate.TOWERBEAN, tower.getTowerID())).getUpIndexID());
								tower.setHp(towerBean.getHp());
								city.setChange(true);
							}else if(tower.getState() == 3 ){
								TowerBean towerBean = (TowerBean)Goods.getById(GoodsCate.TOWERBEAN, tower.getTowerID());
								tower.setHp(towerBean.getHp());
								tower.setState(0);
								city.setChange(true);
							}
						}
					}
				}
			}
		}
		
	}
	
	
	@Override
	public void update(MemoryBean bean) {
		if (bean != null && bean.canWrite()) {
			UserCity city = bean.getCity();
			if(city != null && city.isChange()){
				Session session = null;
				Transaction tr = null;
				try {
					UserCityInfo info = new UserCityInfo();
					info.setMasterID(city.getId());
					info.setCityID(city.getCityID());
					info.setInfo(city.encode2Ja());
					session = HibernateUtil.currentSession(city.getId());
					tr = session.beginTransaction();
					session.createSQLQuery("update usercityinfo set info = '" + info.getInfo() +  "' where masterID = " + info.getMasterID() + " and cityID = " + info.getCityID()).executeUpdate();
					tr.commit();
					city.setChange(false);
				} catch (Exception e) {
					tr.rollback();
					log.error(e, e);
				} finally {
					HibernateUtil.closeSession(session);
				}
			
			}
		}
	}
	
	public void persist(MemoryBean bean){
		try{
			if(bean != null){
				UserCity city = bean.getCity();
				if(city != null){
					if(System.currentTimeMillis() - city.getTime() > CITY_CACHE_TIME){
						update(bean);
					}
					if(System.currentTimeMillis() - city.getTime() > MASTER_CITY_CACHE_TIME
							&& bean.isMaster()){
						update(bean);
						bean.setCity(null);
					}
					if(System.currentTimeMillis() - city.getTime() > FRIEND_CITY_CACHE_TIME
							&& !bean.isMaster()){
						bean.setCity(null);
					}
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
	}
}

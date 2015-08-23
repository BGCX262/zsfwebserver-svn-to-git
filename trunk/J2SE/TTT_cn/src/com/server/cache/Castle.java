package com.server.cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.CastleBean;
import com.database.model.bean.MemoryBean;
import com.database.model.bean.UserCastle;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.user.operation.PVPListOp;
import com.server.util.Configuration;

public class Castle implements Cache{
	private static Log log = LogFactory.getLog(Castle.class);
	private static final int CASTLE_CACHE_TIME = Configuration.getCasCacTime();
	private static final int FRIEND_CASTLE_CACHE_TIME = Configuration.getFriCasCacTime();
	private static final int MASTER_CASTLE_CACHE_TIME = Configuration.getMasCasCacTime();
	@Override
	public void update(MemoryBean bean){
		saveOrUpdate(bean, 2);
	}
	
	private static void saveOrUpdate(MemoryBean bean, int type){
		if(bean != null && bean.canWrite()){
			UserCastle castle = bean.getCastle();
			if(castle != null){
				Session session = null;
				Transaction tr = null;
				try{
					if(castle != null){
						session = HibernateUtil.currentSession(castle.getId());
						tr = session.beginTransaction();
						if(type == 1){
							session.save(castle);
						}if(type == 2){
							 if(castle.isChange()){
								 session.saveOrUpdate(castle);
							 }
						}
						tr.commit();
						castle.setChange(false);
					}
				}catch(Exception e){
					tr.rollback();
					log.error(e, e);
				}finally{
					HibernateUtil.closeSession(session);
				}
			}
		}
	}
	
	public void clean(MemoryBean bean){
		if(bean != null){
			UserCastle castle = bean.getCastle();
			if(castle != null){
				long currentTime = System.currentTimeMillis(); 
				if(castle.getState() != 0 && castle.getEndTime() < currentTime){
					if(castle.getState() == 1){
						castle.setHp(castle.getHp() + castle.getRepairHp());
						castle.setState(0);
						castle.setChange(true);
					}else if(castle.getState() == 2){
						CastleBean oldCcastleBean = (CastleBean)Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
						CastleBean castleBean = (CastleBean)Goods.getByGoodIDAndLevel(GoodsCate.CASTLEBEAN, oldCcastleBean.getGoodID(), oldCcastleBean.getLevel() + 1);
						castle.setSoulLimit(castleBean.getSoulVol());
						castle.setCastleID(castleBean.getId());
						castle.setHp(castleBean.getHp());
						//castle.setEnergyLimit(castleBean.getSpVol());//增加精力值上限
						castle.setState(0);
						castle.setChange(true);
						if(bean.isMaster()){
							if(castleBean.getLevel() == PVPListOp.PVP_LEVEL){
								PVPListOp.initPVPListBean(castle.getId(), System.currentTimeMillis() + PVPListOp.FRESH_USER_PVP_PROTECT_TIME);
							}else if(castleBean.getLevel() > PVPListOp.PVP_LEVEL){
								PVPListOp.upgradeModify(castle.getId());
							}
						}
					}
				}
			}
		}
	}
	
	public static void updateImmediately(long id){
		MemoryBean bean = UserMemory.get(id);
		saveOrUpdate(bean, 2);
	}
	
	public void persist(MemoryBean bean){
		try{
			if(bean != null){
				UserCastle castle = bean.getCastle();
				if(castle != null){
					if(System.currentTimeMillis() - castle.getTime() > CASTLE_CACHE_TIME){
						update(bean);
					}
					if(System.currentTimeMillis() - castle.getTime() > MASTER_CASTLE_CACHE_TIME
							&& bean.isMaster()){
						update(bean);
						bean.setCastle(null);
					}
					if(System.currentTimeMillis() - castle.getTime() > FRIEND_CASTLE_CACHE_TIME
							&& !bean.isMaster()){
						bean.setCastle(null);
					}
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	@Override
	public Object getFromDB(long id) {
		UserCastle castle = null;
		Session session = null;
		try{
			session = HibernateUtil.currentSession(id);
			castle = (UserCastle) session.get(UserCastle.class, id);
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return castle;
	}

	@Override
	public Object gerFromDB(long id, Object para) {
		return null;
	}
}

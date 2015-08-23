package com.server.user.operation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.CastleBean;
import com.database.model.bean.MemcachePVPListBean;
import com.database.model.bean.PVPListBean;
import com.database.model.bean.PropBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserStorage;
import com.server.cache.UserMemory;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.util.Configuration;


public class ResetOp {
	private static Log log = LogFactory.getLog(ResetOp.class);
	private static Finance financeImpl = FinanceImpl.instance();
	private static final int RESET_LIMIT_LEVEL = Configuration.getResetLimitLevel();
	
	public static byte[] reset(long id, UserStorage propStorage, PropBean propBean, int markID){
		byte[] re = new byte[]{0x01};
		UserCastle castle = UserMemory.getCastle(id);
		CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
		if(propStorage != null && castleBean.getLevel() >= RESET_LIMIT_LEVEL){
			int targetRace = Integer.valueOf(propBean.getResult());
			boolean suc = reset(id, targetRace);
			if(suc){
				UserMemory.createMasterMem(id);
				StorageOp.removeGoods(id, markID);
				re = new byte[]{0x00};
			}
		}
		return re;
	}
	
	public static boolean reset(long id, int targetRace){
		boolean suc = false;
		UserMemory.offline(id);
		Session session = null;
		Transaction tr = null;
		try{
			session = HibernateUtil.currentSession(id);
			tr = session.beginTransaction();
			UserCastle castle = (UserCastle) session.get(UserCastle.class, id);
			CastleBean castleBean = CastleOp.findByRaceLevel(Goods.getGoodsByCate(GoodsCate.CASTLEBEAN), targetRace, 1);
			
			int oldCastleLevel = castleBean.getLevel();
			int newCastleLevel = 1;
			
			CastleOp.resetCastle(id, castle, castleBean, targetRace, session);
			
			CityOp.resetCity(id, targetRace, session);
			
			MineOp.resetMine(id, session);
			
			SoulTowerOp.resetSoulTower(id, session);
			
			TechnologyOp.resetTechnology(id, session);
			
			TaskOp.resetTask(id, session);
			
			MemcachePVPListBean memPVPListBean = PVPListOp.resetPVPListBean(id, oldCastleLevel, newCastleLevel, session);
			
			StorageOp.resetStorage(id, targetRace, session);
			
			SlaverOp.resetSlaver(id, session);
			
			FinanceImpl.resetFinance(id, session);
			
			CopyOp.resetCopy(id, session);
			tr.commit();
			MemcacheOp.setIssueTime(id, 0l, 24 * 3600);
			
			MemcacheOp.removePVPListBean(oldCastleLevel, id);
			PVPListBean pvpListBean = new PVPListBean();
			pvpListBean.setId(id);
			pvpListBean.setProtectTime(memPVPListBean.getProtectTime());
			MemcacheOp.setPVPListBean(newCastleLevel, pvpListBean);
			suc = true;
		}catch(Exception e){
			log.error(e, e);
			tr.rollback();
		}finally{
			HibernateUtil.closeSession(session);
		}
		return suc;
	}
}

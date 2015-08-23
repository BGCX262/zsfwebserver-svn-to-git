package com.server.user.operation;

import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.database.model.bean.CastleBean;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.UserCastle;
import com.server.cache.UserMemory;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.Configuration;

public class EnergyOp {
	private static Log log = LogFactory.getLog(EnergyOp.class);
	private static Finance financeImpl = FinanceImpl.instance();
	private static final int REVERT_TIME = 15 * 60000;
	private static final int SP_PVE_CONSUME = Configuration.getSpPveConsume();
	private static final int SP_PVP_CONSUME = Configuration.getSpPvpConsume();
	private static final int SP_PVN_CONSUME = Configuration.getSpPvnConsume();
	private static final int SP_CITY_RESETING_CONSUME = Configuration.getSpCityResetConsume();
	private static final int SP_TASK_CHARGING_CONSUME = Configuration.getSpTaskChargeConsume();
	public static boolean consume(long id, int type){//1 pve花费  2重置关卡花费 3挑战任务4pvp花费5pvn花费
		revert(id);
		try{
			FinanceBean bean = new FinanceBean();
			bean.setId(id);
			int num = SP_PVE_CONSUME;
			int what = 23;
			if(type == 2){
				num = SP_CITY_RESETING_CONSUME;
				what = 24;
			}else if(type == 3){
				num = SP_TASK_CHARGING_CONSUME;
				what = 28;
			}else if(type == 4){
				num = SP_PVP_CONSUME;
				what = 32;
			}else if(type ==5){
				num = SP_PVN_CONSUME;
				what = 32;
			}
			bean.setEnergy(-num);
			if(financeImpl.charge(bean)){
				List<ThingBean> lost = new LinkedList<ThingBean>();
				lost.add(new ThingBean(7, 4, 60005, num, null));
				GameLog.createLog(id, what, null, true, null, lost, null);
				return true;
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return false;
	}
	
	public static void revert(long id){
		UserCastle castle = UserMemory.getCastle(id);
		//CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
		UserCastle us = UserMemory.getCastle(id);
		long leaveTime = System.currentTimeMillis() - castle.getRevertTime();
		if(castle.getRevertTime() == 0){
			castle.setRevertTime(System.currentTimeMillis());
			castle.setChange(true);
		}
		if(leaveTime > REVERT_TIME){
			try{
				long revertTime = System.currentTimeMillis();
				FinanceBean financeBean = financeImpl.getFinance(id);
				if(financeBean.getEnergy() < us.getEnergyLimit()){
					FinanceBean bean = new FinanceBean();
					bean.setId(id);
					int energy = (int)(leaveTime / REVERT_TIME);
					energy = energy > us.getEnergyLimit() - financeBean.getEnergy()? us.getEnergyLimit() - financeBean.getEnergy() : energy;
					revertTime = castle.getRevertTime() + energy * REVERT_TIME;
					bean.setEnergy(energy);
					financeImpl.charge(bean);
				}
				castle.setRevertTime(revertTime);
				castle.setChange(true);
			}catch(Exception e){
				log.error(e, e);
			}
		}
	}
}

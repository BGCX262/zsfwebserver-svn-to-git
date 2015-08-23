package com.server.gameDate.statistics;

import java.sql.Timestamp;
import java.util.List;

import com.database.model.bean.UserStorage;

public interface GameDateOperateInterf {
	/**
	 * 记录每一笔cash消费
	 * @param master
	 * @param cost cash消费数量
	 * @param goods 消费物品
	 * @param type 1点券 2代金券
	 */
	public void collectCashCost(long master,double cost,int goods, int num,int type);
	
	/**
	 * 
	 * @param costType点卷，代金卷，金币
	 * @param goods 物品ID
	 * @param num 购买数量
	 */
	public void collectGoodsCost(int costType,int goods,int num);
	
	/**
	 * 记录每一笔赠送给玩家的点卷，代金卷
	 * @param master
	 * @param cost 赠送金额
	 * @param coin_type 金额类型，点卷，代金卷
	 */
	public void collectFreeCash(long master, double cost);

	/**
	 * 记录每一次PVP战斗
	 * @param costType
	 * @param goods
	 * @param num
	 */
	public void collectPvpLog(long master, long time,int type,List<UserStorage> list);
}

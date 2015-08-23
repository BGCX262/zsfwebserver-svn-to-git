package com.server.user.operation;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.GoodsBean;
import com.server.cache.UserMemory;
import com.server.dispose.TDDispose;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;

public class ShopOp {
	private static Log log = LogFactory.getLog(ShopOp.class);
	private static Finance financeImpl = FinanceImpl.instance();
	public static byte[] buyGoods(long id, byte[] information) throws Exception {
		byte[] re = null;
		int goodsID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int num = DataFactory.getInt(DataFactory.get(information, 14, 4));
		int coinType = DataFactory.getInt(DataFactory.get(information, 18, 1));
		int buyType = DataFactory.getInt(DataFactory.get(information, 19, 1));
		GoodsBean goodsBean = (GoodsBean) Goods.getSingleByGoodID(GoodsCate.GOODSBEAN, goodsID);
		if(goodsBean != null){
			if(goodsBean.getBuy() == 1){
				log.warn("Game_Warning:the goods:" + goodsID + " can't be buy now");
				re = new byte[]{0x04};
			}else{
				List<ThingBean> lost = new LinkedList<ThingBean>();
				List<ThingBean> get = new LinkedList<ThingBean>();
				FinanceBean bean = new FinanceBean();
				bean.setId(id);
				if(buyType == 0){
					if(coinType == 0){
						int cost = (int)(-goodsBean.getPrice() * goodsBean.getDiscount() / 10 * num);
						bean.setCoin(cost);
						lost.add(new ThingBean(7, 1, 60006, -cost, null));
					}else if(coinType == 1){
						double cost = -goodsBean.getPrice() * goodsBean.getDiscount() / 10 * num;
						bean.setCash(cost);
						lost.add(new ThingBean(7, 3, 60007, -(int)cost, null));
					}else if(coinType == 2){
						double cost = -goodsBean.getPrice() * goodsBean.getDiscount() / 10 * num;
						bean.setSystemCash(cost);
						lost.add(new ThingBean(7, 2, 60008, -(int)cost, null));
					}
				}else if(buyType == 1){
					if(coinType == 0){
						int cost = (int)(-goodsBean.getPrice() * goodsBean.getDiscount() / 10 * num);
						bean.setCoin(cost);
						lost.add(new ThingBean(7, 1, 60006, -cost, null));
					}else if(coinType == 1){
						double cost = -goodsBean.getVipPrice() * goodsBean.getDiscount() / 10 * num;
						bean.setCash(cost);
						lost.add(new ThingBean(7, 3, 60007, -(int)cost, null));
					}else if(coinType == 2){
						double cost = -goodsBean.getVipPrice() * goodsBean.getDiscount() / 10 * num;
						bean.setSystemCash(cost);
						lost.add(new ThingBean(7, 2, 60008, -(int)cost, null));
					}
				}
				FinanceBean augend = financeImpl.getFinance(id);
				if(financeImpl.afford(augend, bean)){
					List<ThingBean> getThing = StorageOp.storeGoods(id, goodsID, num);
					if(getThing != null && financeImpl.charge(bean)){
						// 点券
						if(bean.getCash() < 0){
							TDDispose.statisticLog.collectCashCost(id, -bean.getCash(), goodsID, num,1);
						}else if(bean.getSystemCash()<0){
							//代金券
							TDDispose.statisticLog.collectCashCost(id, -bean.getSystemCash(), goodsID, num,2);
						}
						TDDispose.statisticLog.collectGoodsCost(coinType, goodsID, num);
						re = new byte[]{0x00};
						re = DataFactory.addByteArray(re, DataFactory.getbyte(augend.getCoin()));
						re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(augend.getCash()));
						re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(augend.getSystemCash()));
						get.addAll(getThing);
						GameLog.createLog(id, 17, null, true, get, lost, "buy goods");
						
						if (goodsID / 10000 == 7)
							TaskOp.doTask(id, 20007, 1);
						
					}else{
						re = new byte[]{0x03};
					}
				}else{
					re = new byte[]{0x01};
				}
			}
		}else{
			log.warn("Game_Warning:the goods:" + goodsID + " is not exist");
			re = new byte[]{0x02};
		}
		return re;
	}
	
}

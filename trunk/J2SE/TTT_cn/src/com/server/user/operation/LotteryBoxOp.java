package com.server.user.operation;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.LoginRecord;
import com.database.model.bean.TreasureChestBean;
import com.database.model.bean.UserLottery;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class LotteryBoxOp {
	public static final int LOTTERY_BOX_GOODID = 120001;
	private static final int LOTTERY_NUM = Configuration.getLotteryNum();
	private static final int VALIDTIME = Configuration.getLotterValidTime();
	private static final int DAY = Configuration.getDay();
	
	public static UserLottery getLottery(long id){
		UserLottery userLottery = null;
		//userLottery = MemcacheOp.getLottery(id);
		if(userLottery == null){
			userLottery = (UserLottery) DBUtil.get(id, UserLottery.class);
		}
		if(userLottery == null || new Date(System.currentTimeMillis()).getDate() != new Date(userLottery.getLotteryTime()).getDate()
				|| System.currentTimeMillis() - userLottery.getLotteryTime() > DAY){
			userLottery = new UserLottery();
			userLottery.setId(id);
			userLottery.setLotteryLevel(1);
			userLottery.setLotteryNum(0);
			userLottery.setLotteryTime(System.currentTimeMillis());
			setLottery(id, userLottery);
		}
		return userLottery;
	}
	
	public static void setLottery(long id, UserLottery userLottery){
		MemcacheOp.setLottery(id, userLottery, DAY);
		DBUtil.saveOrUpdate(id, userLottery);
	}
	
	public static byte[] operLottery(long id) throws Exception{
		byte[] re = new byte[]{0x01};
		UserLottery userLottery = getLottery(id);
		if(userLottery != null){
			re = new byte[]{0x00};
			re = DataFactory.addByteArray(re, DataFactory.getbyte(userLottery.getLotteryLevel()));
			re = DataFactory.addByteArray(re, new byte[] { (byte) (userLottery.isZero() ? 1 : 0) });
		}
		return re;
	}
	
	public static byte[] getLotteryPrize(long id, byte[] information)  throws Exception{
		byte[] re = new byte[]{0x01};
		UserLottery userLottery = getLottery(id);
		int level = DataFactory.getInt(DataFactory.get(information, 10, 4));
		TreasureChestBean lotteryBean = (TreasureChestBean) Goods.getByGoodIDAndLevel(GoodsCate.TREASURECHESTBEAN, LOTTERY_BOX_GOODID, level);
		if(userLottery.getLotteryNum() < LOTTERY_NUM || level == 0){
			LoginRecord record = (LoginRecord) DBUtil.get(id, LoginRecord.class);
			if(record.getTodayOnline() + (System.currentTimeMillis() - record.getOnline().getTime()) > lotteryBean.getTime()){
				
				long oneDay = 1000 * 60 * 60 * 24;
				if (level == 0 && userLottery.isZero() && userLottery.getLotteryTime() / oneDay == System.currentTimeMillis() / oneDay) {
					return re;
				}
				
				JSONArray ja = JSONArray.fromObject(lotteryBean.getPresents());
				if(ja != null){
					List<ThingBean> get = new LinkedList<ThingBean>();
					
					/* 检查背包是否足够 */
					if (!ChestOp.isStorageSizeEnough(id, 2))
						return new byte[] {0x02};
					
					for(int i = 0; i < ja.size(); i++){
						JSONArray jaa = ja.getJSONArray(i);
						List<ThingBean> getThing = StorageOp.storeGoods(id, jaa.getInt(0), jaa.getInt(1));
						if(getThing != null){
							get.addAll(getThing);
						}
					}
					GameLog.createLog(id, 27, null, true, get, null, "open lottery box");
					re = new byte[]{0x00};
					TreasureChestBean treasureChestBean = (TreasureChestBean) Goods.getByGoodIDAndLevel(GoodsCate.TREASURECHESTBEAN, LOTTERY_BOX_GOODID, userLottery.getLotteryLevel()+ 1);
					if(treasureChestBean != null || lotteryBean.getLevel() == LOTTERY_NUM || level == 0){
						if (level != 0) {
							userLottery.setLotteryLevel(userLottery.getLotteryLevel() + 1);
							userLottery.setLotteryNum(userLottery.getLotteryNum() + 1);
						} else 
							userLottery.setZero(true);
						userLottery.setLotteryTime(System.currentTimeMillis());
						setLottery(id, userLottery);
					}
				}
			}
		}
		return re;
	}
}

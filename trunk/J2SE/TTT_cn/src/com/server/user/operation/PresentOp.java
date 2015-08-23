package com.server.user.operation;

import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.CastleBean;
import com.database.model.bean.TreasureChestBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserStorage;
import com.database.model.bean.UserStorages;
import com.server.cache.UserMemory;
import com.server.gameDate.statistics.SocialGiftReward;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;

public class PresentOp {
	public static final int PRESENT_PACK_GOODID = 120002;
	public static SocialGiftReward social = SocialGiftReward.getInstance(); 
	public static byte[] openPresentsPack(long id){
		byte[] re = new byte[]{0x01};
		UserCastle castle = UserMemory.getCastle(id);
		CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
		TreasureChestBean presentPackBean = (TreasureChestBean) Goods.getByGoodIDAndLevel(GoodsCate.TREASURECHESTBEAN, PRESENT_PACK_GOODID, castle.getRecivePresentLevel());
		if(presentPackBean != null && presentPackBean.getLimitLevel() <= castleBean.getLevel()){
			JSONArray ja = JSONArray.fromObject(presentPackBean.getPresents());
			UserStorage storage = StorageOp.getBoxByGoodID(id, PRESENT_PACK_GOODID);
			if(storage == null){
				storage = StorageOp.createBox(id, PRESENT_PACK_GOODID);
			}
			
			if(ja != null){
				if(StorageOp.canStore(id, ja)){
					List<ThingBean> get = new LinkedList<ThingBean>();
					for(int i = 0; i < ja.size(); i++){
						JSONArray jaa = ja.getJSONArray(i);
						if(jaa != null){
							List<ThingBean> getThing = StorageOp.storeGoods(id, jaa.getInt(0), jaa.getInt(1));
							if(getThing != null){
								get.addAll(getThing);
							}
						}
					}
					GameLog.createLog(id, 29, null, true, get, null, "open lottery box");
					if(get.size() > 0){
						castle.setRecivePresentLevel(castle.getRecivePresentLevel() + 1);
						castle.setChange(true);
						re = new byte[]{0x00};
					}
					TaskOp.doTask(id, 10014, 1);
				}else{
					re = new byte[]{0x02};
				}
			}
		}
		return re;
	}
	
	
	public static byte[] recivedPresents(long id, byte[] information){
		byte[] re = DataFactory.get(information, 0, 10);
		re[0] = 0x72;
		JSONArray friendSend = social.getSocialGiftCheck(id);
		JSONArray sendReward = social.getReward(id);
		int num = 0;
		if(friendSend != null){
			re = DataFactory.addByteArray(re, DataFactory.getbyte(friendSend.size()));
			List<ThingBean> get = new LinkedList<ThingBean>();
			for(int i = 0; i < friendSend.size(); i++){
				JSONArray temp = friendSend.getJSONArray(i);
				List<ThingBean> getThing = StorageOp.storeGoods(id, temp.getInt(0), temp.getInt(1));
				social.updateSocialGiftCheck(id, temp.getInt(0), temp.getInt(1));
				if(getThing != null){
					num++;
					re = DataFactory.addByteArray(re, DataFactory.getbyte(temp.getInt(0)));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(temp.getInt(1)));
					get.addAll(getThing);
				}else{
					break;
				}
			}
			GameLog.createLog(id, 30, null, true, get, null, "friend send gift");
		}
		if(sendReward != null){
			List<ThingBean> get = new LinkedList<ThingBean>();
			for(int i = 0; i < sendReward.size(); i++){
				JSONArray temp = sendReward.getJSONArray(i);
				List<ThingBean> getThing = StorageOp.storeGoods(id, temp.getInt(0), temp.getInt(1));
				if(getThing != null){
					num++;
					re = DataFactory.addByteArray(re, DataFactory.getbyte(temp.getInt(0)));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(temp.getInt(1)));
					get.addAll(getThing);
				}else{
					break;
				}
			}
			DataFactory.replace(re, 10, DataFactory.getbyte(num));
			GameLog.createLog(id, 31, null, true, get, null, "system award");
		}
		return re;
	}
}

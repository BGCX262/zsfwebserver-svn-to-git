package com.server.user.operation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.CastleBean;
import com.database.model.bean.DropGoodsBean;
import com.database.model.bean.PickedFriends;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserSceneGoods;
import com.server.cache.UserMemory;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class PickOp {
	private static Log log = LogFactory.getLog(PickOp.class);
	private static final int LIMIT_TIME = 8 * 3600 * 1000;
	private static final int PICK_COIN = Configuration.getPickCoin();
	private static final int PICK_ROCK = Configuration.getPickRock();
	private static final int PICK_METAL = Configuration.getPickMetal();
	private static final int PICK_CRYSTAL = Configuration.getPickCrystal();
	private static final int PICK_INCREMENT = Configuration.getPickIncrment();
	public static PickedFriends getPickedFriends(long id){
		PickedFriends pickedFriends = null;
		pickedFriends = MemcacheOp.getPickedFriends(id);
		if(pickedFriends == null){
			pickedFriends = (PickedFriends) DBUtil.get(id, PickedFriends.class);
		}
		if(pickedFriends != null){
			pickedFriends.unwrap();
		}
		if(pickedFriends == null){
			pickedFriends = new PickedFriends();
			pickedFriends.setId(id);
			pickedFriends.setPicked(new LinkedList<Long>());
		}
		return pickedFriends;
	}
	
	
	public static void setPickedFriends(long id, PickedFriends pickedFriends){
		pickedFriends.wrap();
		MemcacheOp.setPickedFriends(id, pickedFriends, 8 * 3600);
		DBUtil.update(id, pickedFriends);
	}
	
	public static void addPickedFriends(long id, long friend, PickedFriends pickedFriends){
		List<Long> friends = pickedFriends.getPicked();
		if(System.currentTimeMillis() - pickedFriends.getTime() > LIMIT_TIME){
			friends.clear(); 
			pickedFriends.setTime(System.currentTimeMillis());
		}
		friends.add(friend);
		setPickedFriends(id, pickedFriends);
	}
	
	public static boolean canPickMaterialBox(long id, long sceneID, PickedFriends pickedFriends){
		try{
			if(id == sceneID){
				return false;
			}
			if(pickedFriends != null){
				if(System.currentTimeMillis() - pickedFriends.getTime() < LIMIT_TIME){
					if(pickedFriends.getPicked() != null && pickedFriends.getPicked().size() >= 20){
						return false;
					}else{
						List<Long> friends = pickedFriends.getPicked();
						if(friends != null){
							Iterator<Long> ite = friends.iterator();
							while(ite.hasNext()){
								if(ite.next() == sceneID){
									return false;
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return true;
	}

	public static UserSceneGoods createMaterialBox(long id, long sceneID){
		UserCastle castle = UserMemory.getCastle(id);
		CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
		UserSceneGoods materialBox = new UserSceneGoods();
		List<DropGoodsBean> goodsList = new LinkedList<DropGoodsBean>();
		int increment = (castleBean.getLevel() - 1) * PICK_INCREMENT;
		DropGoodsBean dropCoin = new DropGoodsBean();
		dropCoin.setGoodsID(60006);
		dropCoin.setNum(PICK_COIN + increment);
		goodsList.add(dropCoin);
		
		/*DropGoodsBean dropRock = new DropGoodsBean();
		dropRock.setGoodsID(60001);
		dropRock.setNum(PICK_ROCK + increment);
		goodsList.add(dropRock);
		
		DropGoodsBean dropMetal = new DropGoodsBean();
		dropMetal.setGoodsID(60002);
		dropMetal.setNum(PICK_METAL + increment);
		goodsList.add(dropMetal);
		
		DropGoodsBean dropCrystal = new DropGoodsBean();
		dropCrystal.setGoodsID(60003);
		dropCrystal.setNum(PICK_CRYSTAL + increment);
		goodsList.add(dropCrystal);*/
		
		materialBox.setDropGoods(goodsList);
		return materialBox;
	}
	
	public static byte[] pickMaterialBox(long id, long sceneID, byte[] information){
		byte[] re = new byte[]{0x01};
		PickedFriends pickedFriends = getPickedFriends(id);
		if(pickedFriends != null && canPickMaterialBox(id, sceneID, pickedFriends)){
			UserSceneGoods materialBox = createMaterialBox(id, sceneID);
			List<DropGoodsBean> goodsList = materialBox.getDropGoods();
			if(goodsList != null){
				List<ThingBean> get = new LinkedList<ThingBean>();
				re = DataFactory.addByteArray(re, DataFactory.getbyte(goodsList.size()));
				Iterator<DropGoodsBean> ite = goodsList.iterator();
				while(ite.hasNext()){
					DropGoodsBean goods = ite.next();
					re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getGoodsID()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getNum()));
					get.addAll(StorageOp.storeGoods(id, goods.getGoodsID(), goods.getNum()));
				}
				addPickedFriends(id, sceneID, pickedFriends);
				DataFactory.replace(re, 0, new byte[]{0x00});
				//UserCastle castle = UserMemory.getCastle(id);
				//MessageOp.createMessage(id, sceneID, 0, castle.getRace() , 0, goodsList, null, null,null);
				GameLog.createLog(id, 18, null, true, get, null, null);
			}else{
				re = DataFactory.getbyte(0);
			}
		}
		return re;
	}
}

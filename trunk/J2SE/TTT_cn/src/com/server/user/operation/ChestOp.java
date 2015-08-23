package com.server.user.operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.DropGoodsBean;
import com.database.model.bean.TreasureChestBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserStorage;
import com.server.cache.UserMemory;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;

/**
 * 宝箱操作
 * @author zsf
 */
public class ChestOp {
	
	public static final int OPENCHEST_NEEDSIZE = 4;

	/**
	 * 开启宝箱
	 * @param id 			用户ID
	 * @return				返回协议
	 */
	public static byte[] openChest(long id, byte[] information) throws Exception {
		byte[] re = new byte[] { 0x02 };
		int chestBoxID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int chestMarkID = DataFactory.getInt(DataFactory.get(information, 14, 4));
		//int keyBoxID = DataFactory.getInt(DataFactory.get(information, 18, 4));
		int keyMarkID = DataFactory.getInt(DataFactory.get(information, 22, 4));
		UserStorage chestStor = StorageOp.getBoxByIDAndMarkID(id, chestBoxID, chestMarkID);
		if(chestStor == null){
			return re;
		}
		
		/* 获得宝箱实例 */
		TreasureChestBean chest = (TreasureChestBean) Goods.getSingleByGoodID(GoodsCate.TREASURECHESTBEAN, chestStor.getGoodID());
		if (chest == null) {
			return re;
		}
		
		re = new byte[] { 0x01 };
		/* 检查仓库里是否有对应的钥匙 */
		UserStorage chestKey = StorageOp.getBoxByGoodID(id, chest.getKeyID());
		if (chestKey != null) {
			re = new byte[] { 0x03 };
			
			/* 取得开启宝箱获得的奖励 */
			List<DropGoodsBean> props = SackOp.getFinalGoods(chest, UserMemory.getCastle(id).getRace());
			/* 过滤已有的图纸碎片 */
			props = FightOp.removeUnmustGoods(id, props);
			int needSize = 0;
			for (Iterator<DropGoodsBean> iter = props.iterator(); iter.hasNext(); ) {
				DropGoodsBean bean = iter.next();
				if (bean.getGoodsID() / 10000 != 6) {
					needSize += bean.getNum();
				}
			}
			needSize = Math.max(needSize, OPENCHEST_NEEDSIZE);
			
			/* 检查仓库空间是否足够 */
			if (isStorageSizeEnough(id, needSize)) {
				/* 处理，并记录日志 */
				ThingBean lossChest = StorageOp.removeGoods(chestStor, chestMarkID);
				ThingBean lossChestKey = StorageOp.removeGoods(chestKey, keyMarkID);
				List<ThingBean> lossList = new LinkedList<ThingBean>();
				lossList.add(lossChest);
				lossList.add(lossChestKey);

				/* 处理返回协议 */
				List<ThingBean> get = new LinkedList<ThingBean>();
				re = DataFactory.getbyte(props.size());
				for (Iterator<DropGoodsBean> iter = props.iterator(); iter.hasNext(); ) {
					DropGoodsBean bean = iter.next();
					re = DataFactory.addByteArray(re, DataFactory.getbyte(bean.getGoodsID()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(bean.getNum()));
					
					List<ThingBean> getThing = StorageOp.storeGoods(id, bean.getGoodsID(), bean.getNum());
					if(getThing != null){
						get.addAll(getThing);
					}
				}
				GameLog.createLog(id, 35, null, true, get, lossList, "open chest box");
				re = DataFactory.addByteArray(new byte[] { 0x00 }, re);
				
				TaskOp.doTask(id, 20002, 1);
			}
		}
		
		return re;
	}
	
	/**
	 * 获得根据概率计算后的奖励物品
	 * @param goodID		宝箱goodID
	 * @return				奖励物品的集合
	 */
	public static List<DropGoodsBean> getFinalGoods(TreasureChestBean chest) {
		List<DropGoodsBean> list = new ArrayList<DropGoodsBean>();
		
		if (chest != null) {
			/* 获得开启宝箱获得的物品 */
			JSONArray jso = JSONArray.fromObject(chest.getPresents());
			
			/* 获得应有多少个物品 */
			int count = getPropsCountByChest(chest);
			
			/* 随机一个物品并放进奖励列表 */
			List<Integer> accounts = new ArrayList<Integer>();
			int size = jso.size();
			if (jso != null) {
				for (int i = 0; i < count; i++) {
					int random = (int) (Math.random() * size);
					while (accounts.contains(random)) {
						random = (int) (Math.random() * size);
					}
					accounts.add(random);
					
					/* 生成物品 */
					JSONArray array = jso.getJSONArray(random);
					DropGoodsBean bean = new DropGoodsBean();
					
					/* 判断是否有子物品 */
					if (array.size() > 2) {
						array = array.getJSONArray((int) (Math.random() * array.size()));
					}
					bean.setGoodsID(array.getInt(0));
					bean.setNum(array.getInt(1));
					
					list.add(bean);
					
				}
			}
			
		}
		
		return list;
	}
	
	/**
	 * 判断仓库剩余空间是否足够
	 * @param id			用户ID
	 * @param needSize		需要空间
	 * @return				判断结果
	 */
	public static boolean isStorageSizeEnough(long id, int needSize) {
		UserCastle castle = UserMemory.getCastle(id);
		int limitSize = castle.getNomalstorageLimit();
		
		List<UserStorage> storage = StorageOp.getNomalStorage(id);
		int usedSize = StorageOp.getUsedStorageNum(storage);
		
		if (usedSize + needSize <= limitSize) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 随机获得宝箱应该有多少个奖励物品
	 * @param chest
	 * @return
	 */
	private static int getPropsCountByChest(TreasureChestBean chest) {
		
		int zeroGoods = (int) (chest.getZeroGoods() * 100);
		int oneGoods = (int) (chest.getOneGoods() * 100);
		int twoGoods = (int) (chest.getTwoGoods() * 100);
		int threeGoods = (int) (chest.getThreeGoods() * 100);
		
		int random = (int) (Math.random() * 100 + 1);
		
		int propsCount = 0;
		if (random <= threeGoods)
			propsCount = 4;
		else if (random <= twoGoods + threeGoods)
			propsCount = 3;
		else if (random <= oneGoods + twoGoods + threeGoods)
			propsCount = 2;
		else if (random <= zeroGoods + oneGoods + twoGoods + threeGoods)
			propsCount = 1;
		
		return propsCount;
		
	}

}

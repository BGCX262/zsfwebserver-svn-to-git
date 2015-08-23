package com.server.user.operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.DropGoodsBean;
import com.database.model.bean.TreasureChestBean;
import com.database.model.bean.UserStorage;
import com.server.cache.UserMemory;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;

/**
 * 宝石袋子相关操作
 * @author zsf
 */
public class SackOp {
	
	/**
	 * 开启宝石袋子
	 * @param id
	 * @param information
	 * @return
	 */
	public static byte[] openSack(long id, byte[] information) {
		byte[] re = new byte[] { 0x01 };
		int boxID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int markID = DataFactory.getInt(DataFactory.get(information, 14, 4));
		
		/* 检测宝石袋子是否存在 */
		UserStorage storage = StorageOp.getBoxByIDAndMarkID(id, boxID, markID);
		if (storage == null) {
			return re;
		}
		
		/* 获得宝石袋子实例 */
		TreasureChestBean singleByGoodID = (TreasureChestBean) Goods.getSingleByGoodID(GoodsCate.TREASURECHESTBEAN, storage.getGoodID());
		if (singleByGoodID == null) {
			return re;
		}
		
		re = new byte[] { 0x02 };
		
		/* 判断背包空间是否足够 */
		List<DropGoodsBean> list = getFinalGoods(singleByGoodID, UserMemory.getCastle(id).getRace());
		/* 过滤已有的图纸碎片 */
		list = FightOp.removeUnmustGoods(id, list);
		int needSize = 0;
		for (Iterator<DropGoodsBean> iter = list.iterator(); iter.hasNext(); ) {
			DropGoodsBean bean = iter.next();
			if (bean.getGoodsID() / 10000 != 6) {
				needSize += bean.getNum();
			}
		}
		
		if (ChestOp.isStorageSizeEnough(id, needSize)) {
			/* 处理，并记录日志 */
			ThingBean lossSack = StorageOp.removeGoods(storage, markID);
			List<ThingBean> lossList = new LinkedList<ThingBean>();
			lossList.add(lossSack);

			/* 处理返回协议 */
			List<ThingBean> get = new LinkedList<ThingBean>();
			re = DataFactory.getbyte(list.size());
			for (Iterator<DropGoodsBean> iter = list.iterator(); iter.hasNext(); ) {
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
		}
		
		return re;
	}
	
	/**
	 * 获取最终掉落物品
	 * @param sack
	 * @param race 
	 * @return
	 */
	public static List<DropGoodsBean> getFinalGoods(TreasureChestBean sack, int race) {
		List<DropGoodsBean> list = new ArrayList<DropGoodsBean>();
		
		JSONArray jso = JSONArray.fromObject(sack.getPresents());
		double random = Math.random();
		
		int size = jso.size();
		for (int i = 0; i < size; i++) {
			JSONArray child = jso.getJSONArray(i);
			double proba = child.getDouble(1);
			
			/* 判断是否达到概率 */
			if (proba < random) continue;
			
			JSONArray goods = child.getJSONArray(0);
			int goodsSize = goods.size();
			
			/* 奖励物品的下标 */
			int getIndex = (int) (Math.random() * goodsSize);
			JSONArray node = goods.getJSONArray(getIndex);
			DropGoodsBean bean = new DropGoodsBean();
			/* 判断是否有子物品 */
			if (node.size() > 2) {
				/* 判断是否仍有子物品（图纸碎片） */
				if (node.getJSONArray(0).size() > 2) {
					node = node.getJSONArray(race - 1);
				}
				node = node.getJSONArray((int) (Math.random() * node.size()));
			}
			bean.setGoodsID(node.getInt(0));
			bean.setNum(node.getInt(1));
			
			list.add(bean);
		}
		
		return list;
	}

}

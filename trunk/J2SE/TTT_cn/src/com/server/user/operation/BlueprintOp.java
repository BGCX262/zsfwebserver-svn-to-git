package com.server.user.operation;

import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.BlueprintBean;
import com.database.model.bean.BlueprintPieceBean;
import com.database.model.bean.PropBean;
import com.database.model.bean.StoneBean;
import com.database.model.bean.UserStorage;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.Cmd;
import com.server.util.ToolUtil;
import com.sun.swing.internal.plaf.basic.resources.basic;

public class BlueprintOp {
	private static Log log = LogFactory.getLog(BlueprintOp.class);
	private static final int MIX_NUM = 4;
	private static Finance financeImpl = FinanceImpl.instance();
	
	private static List<BlueprintPieceBean> getPiecesBean(List<UserStorage> storageList) {
		List<BlueprintPieceBean> list = new LinkedList<BlueprintPieceBean>();
		for(int i = 0; i < storageList.size(); i++){
			list.add((BlueprintPieceBean)Goods.getSingleByGoodID(GoodsCate.BLUEPRINTPIECEBEAN, storageList.get(i).getGoodID()));
		}
		return list;
	}
	
	private static boolean canMix(List<BlueprintPieceBean> bpps){
		boolean[] con1 = new boolean[4];
		int goodID = bpps.get(0).getMixerID();
		if(bpps.size() == MIX_NUM){
			for(int i = 0; i < bpps.size(); i++){
				for(int j = 0 ; j < 4; j++){
					if(bpps.get(i).getType() == j + 1){
						con1[i] = true;
					}
				}
				if(goodID != bpps.get(i).getMixerID()){
					return false;
				}
			}
		}else{
			return false;
		}
		
		for(int i = 0; i < con1.length; i++){
			if(con1[i] == false){
				return false;
			}
		}
		return true;
	}
	
	public static byte[] mixBlueprint(long id, byte[] information) throws Exception{
		byte[] re = new byte[]{0x01};
		int num = DataFactory.getInt(DataFactory.get(information, 10, 4));
		List<UserStorage> storageList = new LinkedList<UserStorage>();
		for(int i = 0; i < num; i++){
			int boxID = DataFactory.getInt(DataFactory.get(information, 14 + i * 8, 4));
			int markID = DataFactory.getInt(DataFactory.get(information, 18 + i * 8, 4));
			UserStorage temp = StorageOp.getBoxByIDAndMarkID(id, boxID, markID);
			storageList.add(temp);
		}
		List<BlueprintPieceBean> bpps = getPiecesBean(storageList);
		if(canMix(bpps)){
			int mixerGoodID = bpps.get(0).getMixerID();
			ThingBean to = StorageOp.storeHidenStorage(id, mixerGoodID);
			DataFactory.replace(re, 0, new byte[]{0x00});
			List<ThingBean> get = new LinkedList<ThingBean>();
			get.add(to);
			GameLog.createLog(id, 1, null, true, get, null, "mix blueprint");
			TaskOp.doTask(id, 10022, 1);
		}
		return re;
	}
	
	
	public static byte[] upBlueprint(long id, byte[] information) throws Exception{
		byte[] re = new byte[]{0x03};
//		int bpBoxID = DataFactory.getInt(DataFactory.get(information, 10, 4));
//		int bpMarkID = DataFactory.getInt(DataFactory.get(information, 14, 4));
//		int stoneBoxID = DataFactory.getInt(DataFactory.get(information, 18, 4));
//		int stoneMarkID = DataFactory.getInt(DataFactory.get(information, 22, 4));
//		UserStorage bpStorage = StorageOp.getBoxByIDAndMarkID(id, bpBoxID, bpMarkID);
//		UserStorage stoneStorage = StorageOp.getBoxByIDAndMarkID(id, stoneBoxID, stoneMarkID);
//		if(bpStorage == null || stoneStorage == null || bpStorage.getChange() == 3){
//			re = new byte[]{0x02};
//			return re;
//		}
//		BlueprintBean bpBean = (BlueprintBean) Goods.getSingleByGoodID(GoodsCate.BLUEPRINTBEAN, bpStorage.getGoodID());
//		StoneBean stoneBean = (StoneBean) Goods.getSingleByGoodID(GoodsCate.STONEBEAN, stoneStorage.getGoodID());
//		List<ThingBean> get = new LinkedList<ThingBean>();
//		List<ThingBean> loss = new LinkedList<ThingBean>();
//		String comment = null;
//		if(bpBean != null && stoneBean != null){
//			double r = ToolUtil.getRandom().nextDouble();
//			double sucRate = bpBean.getPrimaryStoneUpRate();
//			if(stoneBean.getLevel() == 2){
//				sucRate = bpBean.getSeniorStoneUpRate();
//			} else if (stoneBean.getLevel() == 3) {
//				sucRate = bpBean.getSeniorStoneUpRate();
//				re = new byte[]{0x03};
//			} else if (stoneBean.getLevel() == 4) {
//				sucRate = Double.valueOf(ToolUtil.getLowFormat()
//						.format(bpBean.getSeniorStoneUpRate() * 1.8)).doubleValue();
//				re = new byte[]{0x03};
//			} else if (stoneBean.getLevel() == 5) {
//				sucRate = 1;
//				re = new byte[]{0x03};
//			}
//			if(r < sucRate){//升级成功
//				get.add(StorageOp.storeHidenStorage(id, bpBean.getUpID()));
//				re = new byte[]{0x00};
//			}else{
//				if (stoneBean.getLevel() <= 2) {
//					if(stoneBean.getLevel() == 1){
//						get.add(StorageOp.storeHidenStorage(id, bpBean.getPrimaryStoneDeID()));
//					}else{
//						get.add(StorageOp.storeHidenStorage(id, bpBean.getSeniorStoneDeID()));
//					}
//				}
//			}
//			if (stoneBean.getLevel() <= 2 || r < sucRate)
//				if(bpBean.getPrimaryStoneDeID() != bpStorage.getGoodID()){
//					loss.add(StorageOp.removeGoods(bpStorage, bpMarkID));
//				}
//			StorageOp.removeGoods(stoneStorage, stoneMarkID);
//			GameLog.createLog(id, 2, null, true, get, loss, comment);
//			
//
//		}
		return re;
	}	
	
	/**
	 * 合成图纸 （升级）
	 * @param id
	 * @param information
	 * @return
	 */
	public static byte[] synthesizeBlueprint(long id, byte[] information) {
		byte[] re = new byte[] {4};
		try {
			Cmd req = Cmd.getInstance(information);
			
			/* 取图纸数据 */
			int blueprintBoxID = req.readInt(10);
			int blueprintMarkID = req.readInt();
			UserStorage blueprintStor = StorageOp.getBoxByIDAndMarkID(id, blueprintBoxID, blueprintMarkID);
			BlueprintBean blueprintBean = (BlueprintBean) Goods.getSingleByGoodID(GoodsCate.BLUEPRINTBEAN, blueprintStor.getGoodID());
			BlueprintBean successBean = (BlueprintBean) Goods.getSingleByGoodID(GoodsCate.BLUEPRINTBEAN, blueprintBean.getSynthesizeSuccessID());
			
			/* 判断图纸是否存在 */
//			if (blueprintStor == null || blueprintBean.getLevel() < 10) {
//				log.warn("id:" + id + "synthesize blueprint error");
//				return re;
//			}
			
			/* 取主材料数据 */
			re = new byte[] {2};
			int basicNum = req.readInt();
			int[] basicBoxIds = new int[basicNum];
			int[] basicMarkIds = new int[basicNum];
			for (int i = 0; i < basicNum; i++) {
				basicBoxIds[i] = req.readInt();
				basicMarkIds[i] = req.readInt();
			}
			List<UserStorage> storages = new LinkedList<UserStorage>();
			
			/* 判断主材料是否存在 */
			if (!checkStorage(id, basicBoxIds, basicMarkIds, storages, 0) && basicNum != blueprintBean.getBasicNum())
				return re;
			
			/* 辅材料数据 */
			re = new byte[] {3};
			int extraNum = req.readInt();
			int[] extraBoxIds = new int[extraNum];
			int[] extraMarkIds = new int[extraNum];
			for (int i = 0; i < extraNum; i++) {
				extraBoxIds[i] = req.readInt();
				extraMarkIds[i] = req.readInt();
			}
			
			/* 判断辅材料是否存在 */
			if (!checkStorage(id, extraBoxIds, extraMarkIds, storages, blueprintBean.getExtraID()) && extraNum <= blueprintBean.getMaxExtra())
				return re;
			
			/* 计算概率 */
			boolean flag = true;
			double allPro = blueprintBean.getBasicRate() + blueprintBean.getExtraRate() * extraNum;
			
			/* 如果总数超出限制 */
			if (extraNum > blueprintBean.getMaxExtra()) {
				return re;
			}
			/* 如果等级为2以上的宝石数量大于红宝石和蓝宝石的数量，则不掉级 */
			if (extraNum > 0)
				flag = false;
			
			List<ThingBean> get = new LinkedList<ThingBean>();
			List<ThingBean> loss = new LinkedList<ThingBean>();
			
			/* 判断升级情况 */
			double r = ToolUtil.getRandom().nextDouble();
			if (r < allPro) {	// 升级成功
				get.add(StorageOp.storeHidenStorage(id, blueprintBean.getSynthesizeSuccessID()));
				loss.add(StorageOp.removeGoods(blueprintStor, blueprintMarkID));
				re = new byte[] { 0x00 };
				re = DataFactory.addByteArray(re, DataFactory.getbyte(blueprintBean.getSynthesizeSuccessID()));
				
				if (successBean != null && successBean.getBaseLevel() == 1) {
					TaskOp.doTask(id, 10015, 1);
				}
				log.info("player:"+id+" up blurprint success~ before goodId: " + blueprintStor.getGoodID() + ", success goodId: "+blueprintBean.getSynthesizeSuccessID());
			} else {
				re = new byte[] { 0x01 };
				if (flag) {
					get.add(StorageOp.storeHidenStorage(id, blueprintBean.getSynthesizeFailedID()));
					loss.add(StorageOp.removeGoods(blueprintStor, blueprintMarkID));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(blueprintBean.getSynthesizeFailedID()));
					log.info("player:"+id+" up blurprint failed~ before goodId: " + blueprintStor.getGoodID() + ", success goodId: "+blueprintBean.getSynthesizeFailedID());
				} else {
					re = DataFactory.addByteArray(re, DataFactory.getbyte(blueprintBean.getGoodID()));
					log.info("player:"+id+" up blurprint failed~ before goodId: " + blueprintStor.getGoodID() + ", not drop!!");
				}
			}
			
			/* 移除材料 */
			for (UserStorage stor : storages) {
				loss.add(StorageOp.removeGoods(stor, JSONArray.fromObject(stor.getMarkIDs()).getInt(0)));
			}
			GameLog.createLog(id, 36, null, true, get, loss, null);
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return re;
	}
	
	/**
	 * 判断物品是否存在，如果存在则存到storage中
	 * @param id
	 * @param basicBoxIds
	 * @param basicMarkIds
	 * @param storages
	 * @return
	 */
	private static boolean checkStorage(long id, int[] basicBoxIds, int[] basicMarkIds, List<UserStorage> storages, int goodID) {

		for (int i = 0; i < basicBoxIds.length; i++) {
			UserStorage storage = StorageOp.getBoxByIDAndMarkID(id, basicBoxIds[i], basicMarkIds[i]);
			/* 判断物品是否存在 */
			if (storage != null && (goodID == 0 ? true : goodID == storage.getGoodID()))
				storages.add(storage);
			else
				return false;
		}
		
		return true;
	}
}

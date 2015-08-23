package com.server.user.operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.IndexBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserStorage;
import com.database.model.bean.UserStorages;
import com.server.cache.UserMemory;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.gameDate.statistics.GameDateOperate;
import com.server.gameDate.statistics.GameDateOperateInterf;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.Configuration;

public class StorageOp {
	private static Log log = LogFactory.getLog(StorageOp.class);
	private static Finance financeImpl = FinanceImpl.instance();
	public static JSONArray INITELEMENTBAG = Configuration.getInitElementBag();
	private static GameDateOperateInterf gdo = GameDateOperate.getInstance();
	
	public static byte[] getStorage(long id) throws Exception {
		UserStorages storages = UserMemory.getStorages(id);
		UserCastle castle = UserMemory.getCastle(id);
		FinanceBean financeBean = financeImpl.getFinance(id);
		
		byte[] re = DataFactory.getbyte(financeBean.getCoin());
		re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(financeBean.getSystemCash()));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(financeBean.getRock()));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(financeBean.getMetal()));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(financeBean.getCrystal()));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(financeBean.getSoul()));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(financeBean.getBadge()));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(castle.getNomalstorageLimit()));
		if (storages != null) {
			List<UserStorage> storageList = storages.getStorage();
			int count = 0;
			byte[] temp = DataFactory.getbyte(storageList.size());
			if (storageList != null) {
				Iterator<UserStorage> storageIte = storageList.iterator();
				while (storageIte.hasNext()) {
					UserStorage storage = storageIte.next();
					if(storage != null && storage.getChange() != 3){
						count++;
						temp = DataFactory.addByteArray(temp, DataFactory.getbyte(storage.getBoxID()));
						temp = DataFactory.addByteArray(temp, DataFactory.getbyte(storage.getType()));
						temp =  DataFactory.addByteArray(temp, DataFactory.doubleToXiaoTouByte(storage.getValidTime()));
						if (storage.isLocked()) {
							temp = DataFactory.addByteArray(temp, new byte[] { 0x01 });
						} else {
							temp = DataFactory.addByteArray(temp, new byte[] { 0x00 });
						}
						temp = DataFactory.addByteArray(temp, DataFactory.getbyte(storage.getGoodID()));
						temp = DataFactory.addByteArray(temp, DataFactory.getbyte(storage.getNum()));
						JSONArray ja = JSONArray.fromObject(storage.getMarkIDs());
						for (int i = 0; i < storage.getNum(); i++) {
							temp = DataFactory.addByteArray(temp, DataFactory.getbyte(ja.getInt(i)));
						}
					}
				}
			}
			DataFactory.replace(temp, 0, DataFactory.getbyte(count));
			re = DataFactory.addByteArray(re, temp);
		}
		return re;
	}

	private static int createMarkID(long id){
		FinanceBean bean = new FinanceBean();
		bean.setId(id);
		bean.setMaxMmarkID(1);
		financeImpl.charge(bean);
		int markID = financeImpl.getFinance(id).getMaxMmarkID();
		return markID;
	}
	
	public static UserStorage getBoxByID(long id, int boxID){
		UserStorages storageBean = UserMemory.getStorages(id);
		List<UserStorage> storageList = storageBean.getStorage();
		if(storageList != null){
			Iterator<UserStorage> storageIte = storageList.iterator();
			while(storageIte.hasNext()){
				UserStorage sto = storageIte.next();
				if(sto.getChange() != 3 && sto.getBoxID() == boxID && sto.getNum() > 0){
					return sto;
				}
			}
		}
		return null;
	}
	
	public static UserStorage getBoxByIDAndMarkID(long id, int boxID, int markID){
		UserStorage storage = getBoxByID(id, boxID);
		if(storage != null){
			JSONArray ja = JSONArray.fromObject(storage.getMarkIDs());
			for(int i = 0; i < ja.size(); i++){
				if(markID == ja.getInt(i)){
					return storage;
				}
			}
		}
		return null;
	}

	public static boolean removeGoods(long id, int markID) {
		UserStorages storages = UserMemory.getStorages(id);
		if(storages != null){
			List<UserStorage> storageList = storages.getStorage();
			if(storageList != null){
				Iterator<UserStorage> ite = storageList.iterator();
				while(ite.hasNext()){
					UserStorage sto = ite.next();
					List<Integer> marks = JSONArray.toList(JSONArray.fromObject(sto.getMarkIDs()));
					if(marks != null){
						Iterator<Integer> markIte = JSONArray.fromObject(sto.getMarkIDs()).iterator();
						while(markIte.hasNext()){
							int mark = markIte.next();
							if(mark == markID){
								markIte.remove();
								sto.setMarkIDs(JSONArray.fromObject(marks).toString());
								sto.setNum(sto.getNum() - 1);
								if(sto.getNum() == 0){
									sto.setChange(3);
								}else{
									sto.setChange(2);
								}
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public static ThingBean removeGoods(UserStorage sto, int markID) {
		ThingBean from = null;
		if(sto != null){
			if(sto.getNum() > 0){
				sto.setNum(sto.getNum() - 1);
				if(sto.getNum() == 0){
					JSONArray ja = new JSONArray();
					sto.setMarkIDs(ja.toString());
					sto.setChange(3);
				}else{
					JSONArray ja = JSONArray.fromObject(sto.getMarkIDs());
					for(int i = 0; i < ja.size(); i++){
						if(ja.getInt(i) == markID){
							ja.remove(i);
						}
					}
					sto.setMarkIDs(ja.toString());
					sto.setChange(2);
				}
				from = new ThingBean();
				from.setPos(1);
				from.setPosID(sto.getBoxID());
				from.setNum(1);
				from.setGoodID(sto.getGoodID());
				from.setMarks(JSONArray.fromObject("[" + markID + "]"));
				return from;
			}else if(sto.getNum() <= 0){
				log.warn("remove storage goods error: the num <= 0");
				sto.setChange(3);
			}
			
		}
		return from;
	}

	public static void lock(long id, byte[] information) throws Exception{
		int boxID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		UserStorage storage = getBoxByID(id, boxID);
		if(storage != null){
			storage.setLocked(true);
			storage.setChange(2);
			log.info("Game_Info:id:" + id + " lock storage:" + boxID);
		}else{
			throw new Exception("Game_Warning:the storage:" + boxID + " is not exist");
		}
	}

	public static void delete(long id, byte[] information) throws Exception{
		int boxID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int num = DataFactory.getInt(DataFactory.get(information, 14, 4));
		List<Integer> markIds = new LinkedList<Integer>();
		for (int i = 0; i < num; i++) {
			markIds.add(DataFactory.getInt(DataFactory.get(information, 18 + i * 4, 4)));
		}
		UserStorage storage = getBoxByID(id, boxID);
		if(storage != null){
			List<ThingBean> lost = new LinkedList<ThingBean>();
			lost.add(new ThingBean(1, storage.getBoxID(), storage.getGoodID(), num, JSONArray.fromObject(markIds)));
			JSONArray jso = JSONArray.fromObject(storage.getMarkIDs());
			jso.removeAll(markIds);
			storage.setMarkIDs(jso.toString());
			storage.setNum(storage.getNum() - num);
			if (storage.getNum() <= 0)
				storage.setChange(3);
			else
				storage.setChange(2);
			GameLog.createLog(id, 20, null, true, null, lost, null);
		}else{
			log.warn("Game_Warning:the storage is not exist");
		}
	}
	
/*	public static List<UserStorage> getUnfullBoxByGoodsID(long id, int goodsID){
		List<UserStorage> list = new ArrayList<UserStorage>();
		UserStorages storages = UserMemory.getStorages(id);
		if(storages != null){
			List<UserStorage> storageList = storages.getStorage();
			if(storageList != null){
				Iterator<UserStorage> ite = storageList.iterator();
				while(ite.hasNext()){
					UserStorage temp = ite.next();
					if(temp.getGoodID() == goodsID && !temp.isFull()){
						list.add(temp);
					}
				}
			}
		}
		return list;
	}*/
	
	public static List<UserStorage> getNomalStorage(long id){
		return typeStorage(id, 1);
	}
	
	public static List<UserStorage> getTempStorage(long id){
		return typeStorage(id, 2);
	}
	
	public static List<UserStorage> getHidenStorage(long id){
		return typeStorage(id, 3);
	}
	
	public static List<UserStorage> getUnlimitedStorage(long id){
		return typeStorage(id, 4);
	}
	
	public static List<UserStorage> typeStorage(long id, int type){
		List<UserStorage> list = new ArrayList<UserStorage>();
		UserStorages storages = UserMemory.getStorages(id);
		if(storages != null){
			List<UserStorage> storageList = storages.getStorage();
			if(storageList != null){
				Iterator<UserStorage> ite = storageList.iterator();
				while(ite.hasNext()){
					UserStorage storage = ite.next();
					if(storage != null && storage.getType() == type){
						list.add(storage);
					}
				}
			}
		}
		return list;
	}
	
	private static int getMaxBoxID(long id){
		int max = 0;
		UserStorages storages = UserMemory.getStorages(id);
		List<UserStorage> storageList = storages.getStorage();
		if(storageList != null){
			Iterator<UserStorage> ite = storageList.iterator();
			while(ite.hasNext()){
				UserStorage temp = ite.next();
				if(temp.getBoxID() > max){
					max = temp.getBoxID();
				}
			}
		}
		return max;
	}
	
	public static UserStorage getBoxByGoodID(long id, int goodID){//1存储在普通仓库  2存储在暂存仓库 3 隐形背包 4 无限背包
		List<UserStorage> storageList = null;
		int type = typeGoods(goodID);
		if(type == 1){
			storageList = getNomalStorage(id);
		}else if(type == 3){
			storageList = getHidenStorage(id);
		}else if(type == 4){
			storageList = getUnlimitedStorage(id);
		}
		
		if(storageList != null){
			Iterator<UserStorage> ite = storageList.iterator();
			while(ite.hasNext()){
				UserStorage temp = ite.next();
				if(temp.getChange() != 3 && temp.getGoodID() == goodID && temp.getNum() > 0){
					return temp;
				}
			}
		}
		return null;
	}
	
	public static UserStorage getBoxByGoodIDAndValidtime(long id, int goodID, long validtime, int num){//1存储在普通仓库  2存储在暂存仓库 3 隐形背包 4 无限背包
		List<UserStorage> storageList = null;
		int type = typeGoods(goodID);
		if(type == 1){
			storageList = getNomalStorage(id);
		}else if(type == 3){
			storageList = getHidenStorage(id);
		}else if(type == 4){
			storageList = getUnlimitedStorage(id);
		}
		
		if(storageList != null){
			Iterator<UserStorage> ite = storageList.iterator();
			while(ite.hasNext()){
				UserStorage temp = ite.next();
				if(temp.getChange() != 3 && temp.getGoodID() == goodID && temp.getNum() > 0 && 
						temp.getValidTime() == validtime && temp.getNum() < num){
					return temp;
				}
			}
		}
		return null;
	}
	
	public static UserStorage createBox(long id, int goodsID){//1
		return createBox(id, goodsID, Long.MAX_VALUE);
	}
	
	private static UserStorage createBox(long id, int goodsID, long validTime){//1
		IndexBean indexBean = (IndexBean) Goods.getById(GoodsCate.INDEXBEAN, goodsID); 
		if(indexBean != null){
			UserStorage storage = getBoxByGoodIDAndValidtime(id, goodsID, validTime, 99);
			if (storage == null) {
				storage = new UserStorage();
				storage.setMasterID(id);
				storage.setGoodID(goodsID);
				storage.setValidTime(validTime);
				storage.setCate(Goods.getCate(goodsID));
				storage.setLocked(false);
				storage.setNum(1);
				JSONArray ja = new JSONArray();
				ja.add(createMarkID(id));
				storage.setMarkIDs(ja.toString());
				int type = typeGoods(goodsID);
				storage.setType(type);
				storage.setBoxID(getMaxBoxID(id) + 1);
				storage.setChange(1);
				UserMemory.getStorages(id).getStorage().add(storage);
			} else {
				storage.setNum(storage.getNum() + 1);
				JSONArray jso = JSONArray.fromObject(storage.getMarkIDs());
				jso.add(createMarkID(id));
				storage.setMarkIDs(jso.toString());
				if (storage.getChange() == 0)
					storage.setChange(2);
			}
			return storage;
		}else{
			log.error("Game_Error:the goods:" + goodsID + " is not exist");
		}
		return null;
	}
	
	public static int getUsedStorageNum(List<UserStorage> storageList){
		int num = 0;
		if(storageList != null){
			Iterator<UserStorage> ite = storageList.iterator();
			while(ite.hasNext()){
				UserStorage sto = ite.next();
				if(sto.getChange() != 3){
					num++;
				}
			}
		}
		return num;
	}
	
/*	private static boolean storeNomalOrTempStorage(long id, int goodsID, int type){//1 normal  2 temp
		UserCastle castle = UserMemory.getCastle(id);
		if(castle != null){
			List<UserStorage> storageList = getNomalStorage(id);
			int sizeLimit = castle.getNomalstorageLimit();
			if(type ==2){
				//sizeLimit = castle.getTempStorageLimit();
				storageList = getTempStorage(id);
			}
			int usedNum = getUsedStorageNum(storageList);
			if(usedNum < sizeLimit){
				UserStorage storage = null;
				storage = createBox(id, goodsID, type);
				if(storage != null){
					UserMemory.getStorages(id).getStorage().add(storage);
				}
				return true;
			}
		}
		return false;
	}*/
	
	
/*	public static boolean storeNomalStorage(long id, int goodsID){
		return storeNomalOrTempStorage(id, goodsID, 1);
	}*/
	
	/*public static boolean storeTempStorage(long id, int goodsID){
		return storeNomalOrTempStorage(id, goodsID, 2);
	}*/
	
	public static ThingBean storeHidenStorage(long id, int goodsID){
		ThingBean to = null;
		if(isHidenGoods(goodsID)){
			boolean isExist = false;
			List<UserStorage> hidenStorageList = getHidenStorage(id);
			Iterator<UserStorage> ite = hidenStorageList.iterator();
			to = new ThingBean();
			to.setPos(1);
			to.setGoodID(goodsID);
			to.setNum(1);
			while(ite.hasNext()){ 
				UserStorage storage = ite.next();
				if(storage.getGoodID() == goodsID && storage.getChange() != 3){
					isExist = true;
					to.setPosID(storage.getBoxID());
					to.setMarks(JSONArray.fromObject(storage.getMarkIDs()));
				}
			}
			if(!isExist){
				UserStorage storage = createBox(id, goodsID);
				if(storage != null){
					to.setPosID(storage.getBoxID());
					to.setMarks(JSONArray.fromObject(storage.getMarkIDs()));
				}
			}
		}
		return to;
	}
	
	public static ThingBean storeUnLimitedStorage(long id, int goodsID){
		ThingBean to = new ThingBean();
		to.setPos(1);
		to.setGoodID(goodsID);
		to.setNum(1);
		boolean suc = false;
		List<UserStorage> storageList = getUnlimitedStorage(id);
		if(storageList != null && storageList.size() > 0){
			Iterator<UserStorage> ite = storageList.iterator();
			while(ite.hasNext()){
				UserStorage storage = ite.next();
				if(storage != null && storage.getGoodID() == goodsID){
					int num = storage.getNum() + 1;
					storage.setNum(num);
					JSONArray ja = JSONArray.fromObject(storage.getMarkIDs());
					int mark = createMarkID(id);
					ja.add(mark);
					storage.setMarkIDs(ja.toString());
					if(storage.getChange() != 1){
						storage.setChange(2);
					}
					to.setPosID(storage.getBoxID());
					to.setMarks(JSONArray.fromObject("[" + mark + "]"));
					suc = true;
					break;
				}
			}
		}
		if(!suc){
			UserStorage storage = createBox(id, goodsID);
			if(storage != null){
				to.setPosID(storage.getBoxID());
				to.setMarks(JSONArray.fromObject(storage.getMarkIDs()));
			}
		}
		return to;
	}
	
/*	public static boolean store(long id, int goodsID){
		int type = typeGoods(goodsID);
		if(type == 1){
			return storeNomalStorage(id, goodsID);
			if(!suc){
				suc = storeTempStorage(id, goodsID);
			}
		}else if(type == 3){
			storeHidenStorage(id, goodsID);
			return true;
		}else if(type == 4){
			storeUnLimitedStorage(id, goodsID);
			return true;
		}else{
			return storeTempStorage(id, goodsID);
		}
	}*/
	
	public static boolean isHidenGoods(int goodsID){
		if(goodsID > 20000 && goodsID < 40000){
			return true;
		}
		return false;
	}
	
	/*public static void transferBox(long id, byte[] information){
		int boxID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		UserStorage tempStorage = getBoxByID(id, boxID);
		if(tempStorage != null){
			storeGoods(id, tempStorage.getGoodID(), tempStorage.getNum());
			tempStorage.setChange(3);
		}else{
			log.info("the temp box:" + boxID + " is not exist!");
		}
	}*/
	
	public static ThingBean storeMaterial(long id, int goodsID, int num){
		ThingBean to = new ThingBean();
		to.setPos(7);
		to.setNum(num);
		to.setGoodID(goodsID);
		int cate = Goods.getCate(goodsID);
		if(cate == 6){
			FinanceBean bean = new FinanceBean();
			bean.setId(id);
			if(Goods.isCoin(goodsID)){
				bean.setCoin(num);
				to.setPosID(1);
			}else if(Goods.isRock(goodsID)){
				bean.setRock(num);
				to.setPosID(7);
			}else if(Goods.isMetal(goodsID)){
				bean.setMetal(num);
				to.setPosID(8);
			}else if(Goods.isCrystal(goodsID)){
				bean.setCrystal(num);
				to.setPosID(9);
			}else if(Goods.isSoul(goodsID)){
				bean.setSoul(num);
				to.setPosID(10);
			}else if(Goods.isCash(goodsID)){
				bean.setCash(num);
				to.setPosID(3);
			}else if(Goods.isSystemCash(goodsID)){
				bean.setSystemCash(num);
				to.setPosID(2);
				gdo.collectFreeCash(id, num);
			}else if(Goods.isBadge(goodsID)){
				bean.setBadge(num);
				to.setPosID(12);
			}
			financeImpl.consume(bean);
		}
		return to;
	}
	

	/**
	 * return 1 normal storage 2 temp storage 3 hiden storage 4 unlimited storage
	 */
	public static int typeGoods(int goodsID){
		int cate = Goods.getCate(goodsID);
		if(cate == 1 || cate == 10 || cate == 12){
			return 1;
		}else if(cate == 2 || cate == 3){
			return 3;
		}else if(cate == 4 || cate == 5){
			return 4;
		}else if(cate == 7){
			return 5;//奴隶
		}else  if(cate == 6){
			return 6;
		}
		return 2;
	}
	
	public static List<ThingBean> storeGoods(long id, int goodsID, int num){//1存储在普通仓库  2存储在暂存仓库 3 隐形背包 4 无限背包
		return storeGoods(id, goodsID, num, Long.MAX_VALUE);
	}
	
	public static List<ThingBean> storeGoods(long id, int goodsID, int num, long validTime){//1存储在普通仓库  2存储在暂存仓库 3 隐形背包 4 无限背包
		List<ThingBean> get = null;
		int type = typeGoods(goodsID);
		if(type == 6){
			if(get == null){
				 get = new LinkedList<ThingBean>();
			}
			get.add(storeMaterial(id, goodsID, num));
		}else if(type == 5){
			UserCastle castle = UserMemory.getCastle(id);
			if(SlaverOp.getSlaverSize(id) + num <= castle.getSlaverLimit()){
				List<ThingBean> temp = validTime == Long.MAX_VALUE ? 
						SlaverOp.addSlaver(id, goodsID, num) : 
							SlaverOp.addSlaver(id, goodsID, num, validTime);
				if(temp != null){
					if(get == null){
						 get = new LinkedList<ThingBean>();
					}
					get.addAll(temp);
				}
			}
		}else{
			UserCastle castle = UserMemory.getCastle(id);
			if(type == 1){
				int normalLimit = castle.getNomalstorageLimit();
				List<UserStorage> normalStorage = getNomalStorage(id);
				int usedNum = getUsedStorageNum(normalStorage);
				if(usedNum + num <= normalLimit){
					for(int i = 0; i < num; i++){
						UserStorage goods = createBox(id, goodsID, validTime);
						if(goods != null){
							if(get == null){
								 get = new LinkedList<ThingBean>();
							}
							get.add(new ThingBean(1, goods.getBoxID(), goods.getGoodID(), 1, JSONArray.fromObject(goods.getMarkIDs())));
						}
					}
				}else{
					usedNum = getUsedStorageNum(normalStorage);
				}
			}else if(type == 3){
				if(get == null){
					 get = new LinkedList<ThingBean>();
				}
				for(int i = 0; i < num; i++){
					get.add(storeHidenStorage(id, goodsID));
				}
			}else if(type == 4){
				if(get == null){
					 get = new LinkedList<ThingBean>();
				}
				for(int i = 0; i < num; i++){
					get.add(storeUnLimitedStorage(id, goodsID));
				}
			}
		}
		return get;
	}
	
	public static UserStorages initStorage(long id){
		storeGoods(id, PresentOp.PRESENT_PACK_GOODID, 1);
		JSONArray ja = INITELEMENTBAG;
		if(ja != null && ja.size() > 0){
			for(int i = 0; i < ja.size(); i++){
				try{
					JSONArray jaa = ja.getJSONArray(i);
					storeGoods(id, jaa.getInt(0), jaa.getInt(1));
				}catch(Exception e){
					continue;
				}
			}
		}
		return null;
	}
	
	public static int getMonsterCount(long id){
		int count = 0;
		List<UserStorage> unlimitStorage = getUnlimitedStorage(id);
		if(unlimitStorage != null){
			Iterator<UserStorage> ite = unlimitStorage.iterator();
			while(ite.hasNext()){
				UserStorage storage = ite.next();
				if(storage.getCate() == 5){
					count += storage.getNum();
				}
			}
		}
		return count;
	}
	
	public static boolean canStore(long id, JSONArray ja){
		int num = 0;
		if(ja != null){
			for(int i = 0; i < ja.size(); i++){
				JSONArray jaa = ja.getJSONArray(i);
				if(jaa != null){
					int type = typeGoods(jaa.getInt(0));
					if(type == 1){
						num += jaa.getInt(1);
					}
				}
			}
		}
		int usedNum = getUsedStorageNum(getNomalStorage(id));
		if(num + usedNum <= UserMemory.getCastle(id).getNomalstorageLimit()){
			return true;
		}else{
			return false;
		}
	}
	
	public static byte[] expandStorage(long id) throws Exception{
		byte[] re = new byte[]{0x01};
		UserCastle castle = UserMemory.getCastle(id);
		FinanceBean financeBean = new FinanceBean();
		financeBean.setId(id);
		if(castle.getNomalstorageLimit() <= 81){
			financeBean.setCoin(-2000);
			if(financeImpl.charge(financeBean)){
				castle.setNomalstorageLimit(90);
				castle.setChange(true);
				re = new byte[]{0x00};
			}
		}else if(castle.getNomalstorageLimit() > 81 && castle.getNomalstorageLimit() <= 90){
			financeBean.setCash(-10);
			if(financeImpl.charge(financeBean)){
				castle.setNomalstorageLimit(99);
				castle.setChange(true);
				re = new byte[]{0x00};
			}
		}else if(castle.getNomalstorageLimit() > 90 && castle.getNomalstorageLimit() <= 99){
			financeBean.setCash(-30);
			if(financeImpl.charge(financeBean)){
				castle.setNomalstorageLimit(108);
				castle.setChange(true);
				re = new byte[]{0x00};
			}
		}
		return re;
	}
	
	public static UserStorage createBoxWithNoCache(long id, int goodsID, int boxID, int markID, int num, long validTime){
		UserStorage storage = new UserStorage();
		storage.setMasterID(id);
		storage.setGoodID(goodsID);
		storage.setValidTime(validTime);
		storage.setCate(Goods.getCate(goodsID));
		storage.setLocked(false);
		storage.setNum(num);
		JSONArray ja = new JSONArray();
		ja.add(markID);
		storage.setMarkIDs(ja.toString());
		int type = typeGoods(goodsID);
		storage.setType(type);
		storage.setBoxID(boxID);
		storage.setChange(1);
		return storage;
	}

	public static void resetStorage(long id, int targetRace, Session session) {
		session.createSQLQuery("delete from userstorage where masterID = " + id + " and (cate = 2 or cate = 3 or cate = 5)").executeUpdate();
		int boxID = (Integer)session.createSQLQuery("select max(boxID) from userstorage where masterID = " + id).uniqueResult();
		JSONArray ja = INITELEMENTBAG;
		
		FinanceBean bean = (FinanceBean)session.get(FinanceBean.class, id);
		int markID = bean.getMaxMmarkID();
		if(ja != null && ja.size() > 0){
			for(int i = 0; i < ja.size(); i++){
				try{
					JSONArray jaa = ja.getJSONArray(i);
					int type = typeGoods(jaa.getInt(0));
					if(type == 2 || type == 3){
						UserStorage storage = createBoxWithNoCache(id, jaa.getInt(0), ++boxID, ++markID, jaa.getInt(1), Long.MAX_VALUE);
						if(storage != null){
							session.save(storage);
						}
					}
				}catch(Exception e){
					continue;
				}
			}
		}
	}
}

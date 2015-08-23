package com.server.user.operation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.sf.json.JSONArray;

import org.hibernate.Session;

import com.cindy.run.util.DataFactory;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.CastleBean;
import com.database.model.bean.CityBean;
import com.database.model.bean.CopyBean;
import com.database.model.bean.DropGoodsBean;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.LoginLog;
import com.database.model.bean.LostBean;
import com.database.model.bean.PropBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserCity;
import com.database.model.bean.UserCopy;
import com.database.model.bean.UserStorage;
import com.server.cache.UserMemory;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.DBUtil;

public class PropOp {

	private static Finance financeImpl = FinanceImpl.instance();

	public static byte[] use(long id, byte[] information) throws Exception {
		byte[] re = new byte[] { 0x01 };
		int boxID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int markID = DataFactory.getInt(DataFactory.get(information, 14, 4));
		int goodID = DataFactory.getInt(DataFactory.get(information, 18, 4));
		UserStorage propStorage = StorageOp.getBoxByIDAndMarkID(id, boxID, markID);
		if (propStorage == null || goodID != propStorage.getGoodID()) {
			return re;
		}
		PropBean propBean = (PropBean) Goods.getSingleByGoodID(GoodsCate.PROPBEAN, goodID);
		if (propBean.getType() == 1) {
			re = speedup(id, propStorage, information);
		} else if (propBean.getType() == 2) {
			re = addSp(id, propStorage, markID);
		} else if (propBean.getType() == 3 || propBean.getType() == 10) {
			re = usePveOrPvpProp(id, propStorage, markID);
			TaskOp.doTask(id, 20001, 1);
		} else if (propBean.getType() == 5 || propBean.getType() == 6 || propBean.getType() == 7
				|| propBean.getType() == 8 || propBean.getType() == 9 || propBean.getType() == 12) {
			re = addMaterial(id, propStorage, markID, propBean.getType());
		} else if (propBean.getType() == 11) {
			re = ResetOp.reset(id, propStorage, propBean, markID);
		} else if (propBean.getType() == 17) {
			re = monsterCard(id, propStorage, propBean, information);
		}

		return re;
	}

	public static byte[] monsterCard(long id, UserStorage propStorage, PropBean propBean, byte[] information)
			throws Exception {
		byte[] re = new byte[] { 0x01 };
		int markID = DataFactory.getInt(DataFactory.get(information, 14, 4));

		String[] split = propBean.getResult().split(",");
		List<ThingBean> get = new LinkedList<ThingBean>();
		get.addAll(StorageOp.storeGoods(id, Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue()));

		re = new byte[] { 0x00 };
		List<ThingBean> lossList = new LinkedList<ThingBean>();
		lossList.add(StorageOp.removeGoods(propStorage, markID));
		GameLog.createLog(id, 22, null, true, get, lossList, "monster card");

		return re;
	}

	public static byte[] speedup(long id, UserStorage propStorage, byte[] information) throws Exception {
		byte[] re = new byte[] { 0x01 };
		// int boxID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int markID = DataFactory.getInt(DataFactory.get(information, 14, 4));
		int goodID = DataFactory.getInt(DataFactory.get(information, 18, 4));
		int obj = DataFactory.getInt(DataFactory.get(information, 22, 4));
		int para = DataFactory.getInt(DataFactory.get(information, 26, 4));
		PropBean propBean = (PropBean) Goods.getSingleByGoodID(GoodsCate.PROPBEAN, goodID);
		int propType = 1;
		double speedupTime = 0;
		long endTime = 0;
		ThingBean loss = null;
		if (propBean.getResult().equals("0")) {
			propType = 3;
		} else if (Double.parseDouble(propBean.getResult()) < 1) {
			propType = 2;
			speedupTime = Double.parseDouble(propBean.getResult());
		} else {
			speedupTime = Double.parseDouble(propBean.getResult());
		}
		if (obj == 1) {
			endTime = CastleOp.speedup(id, propType, speedupTime);
			TaskOp.doTask(id, 10007, 1);
		} else if (obj == 2) {
			endTime = SoulTowerOp.speedup(id, propType, speedupTime);
		} else if (obj == 3 || obj == 4 || obj == 5) {
			endTime = MineOp.speedup(id, propType, speedupTime, obj);
		} else if (obj == 6) {
			endTime = SoulTowerOp.speedUpCreate(id, propType, speedupTime, para);
		} else if (obj == 7) {
			endTime = TechnologyOp.speedup(id, propType, speedupTime);
		} else if (obj == 8) {
			endTime = TechnologyOp.speedupTecTree(id, propType, speedupTime, para);
		}
		if (endTime > 0) {
			loss = StorageOp.removeGoods(propStorage, markID);
			re = new byte[] { 0x00 };
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
			List<ThingBean> lossList = new LinkedList<ThingBean>();
			lossList.add(loss);
			GameLog.createLog(id, 22, null, true, null, lossList, null);
		}

		return re;
	}

	public static byte[] addSp(long id, UserStorage propStorage, int markID) throws Exception {
		byte[] re = new byte[] { 0x01 };
		PropBean propBean = (PropBean) Goods.getSingleByGoodID(GoodsCate.PROPBEAN, propStorage.getGoodID());
		int sp = 0;
		LoginLog loginLog = (LoginLog) DBUtil.get(id, LoginLog.class);
		long oneDay = 1000 * 60 * 60 * 24;
		if (loginLog.getLastTenAddTime() / oneDay != System.currentTimeMillis() / oneDay) {
			loginLog.setLastTenAddTime(System.currentTimeMillis());
			loginLog.setTenAddCount(0);
		}
		if (loginLog.getTenAddCount() < 3 || propBean.getGoodID() == 100042 || propBean.getGoodID() == 100043) {
			UserCastle castle = UserMemory.getCastle(id);
			CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
			if (propBean.getResult().equals("0")) {
				sp = castleBean.getSpVol() - financeImpl.getFinance(id).getEnergy();
			} else {
				sp = Integer.valueOf(propBean.getResult());
			}
			FinanceBean financeBean = financeImpl.getFinance(id);
			sp = sp > castle.getEnergyLimit() - financeBean.getEnergy() ? castle.getEnergyLimit() - financeBean.getEnergy()
							: sp;
			FinanceBean temp = new FinanceBean();
			temp.setId(id);
			temp.setEnergy(sp);
			if (financeImpl.charge(temp)) {
				if (propBean.getGoodID() != 100042 && propBean.getGoodID() != 100043)
					loginLog.setTenAddCount(loginLog.getTenAddCount() + 1);
				ThingBean loss = null;
				loss = StorageOp.removeGoods(propStorage, markID);
				re = new byte[] { 0x00 };
				List<ThingBean> lossList = new LinkedList<ThingBean>();
				lossList.add(loss);
				GameLog.createLog(id, 22, null, true, null, lossList, null);
				DBUtil.update(id, loginLog);
			}
		} else 
			return new byte[] { 0x03 };
		return re;
	}

	public static byte[] usePveOrPvpProp(long id, UserStorage propStorage, int markID) throws Exception {
		byte[] re = new byte[] { 0x01 };
		if (propStorage != null) {
			ThingBean loss = null;
			loss = StorageOp.removeGoods(propStorage, markID);
			re = new byte[] { 0x00 };
			List<ThingBean> lossList = new LinkedList<ThingBean>();
			lossList.add(loss);
			UserCity city = UserMemory.getCurrentCity(id);
			if (city.getCityID() > 100)
				TaskOp.doTask(id, 50003, 1);
			GameLog.createLog(id, 22, null, true, null, lossList, null);
		}
		return re;
	}

	public static byte[] addMaterial(long id, UserStorage propStorage, int markID, int propType) {
		byte[] re = new byte[] { 0x01 };
		PropBean propBean = (PropBean) Goods.getSingleByGoodID(GoodsCate.PROPBEAN, propStorage.getGoodID());
		FinanceBean temp = new FinanceBean();
		temp.setId(id);
		if (propBean.getType() == 5) {
			temp.setCoin(Integer.valueOf(propBean.getResult()));
		} else if (propBean.getType() == 6) {
			temp.setRock(Integer.valueOf(propBean.getResult()));
		} else if (propBean.getType() == 7) {
			temp.setMetal(Integer.valueOf(propBean.getResult()));
		} else if (propBean.getType() == 8) {
			temp.setCrystal(Integer.valueOf(propBean.getResult()));
		} else if (propBean.getType() == 9) {
			temp.setSoul(Integer.valueOf(propBean.getResult()));
		} else if (propBean.getType() == 12) {
			temp.setBadge(Integer.valueOf(propBean.getResult()));
		}
		if (financeImpl.charge(temp)) {
			ThingBean loss = null;
			loss = StorageOp.removeGoods(propStorage, markID);
			re = new byte[] { 0x00 };
			List<ThingBean> lossList = new LinkedList<ThingBean>();
			lossList.add(loss);
			GameLog.createLog(id, 22, null, true, null, lossList, null);
		}
		return re;
	}

	/**
	 * 幸运大转盘
	 */
	public static byte[] turntable(long id, byte[] information) {
		/* 取数据 */
		int index = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int num = DataFactory.getInt(DataFactory.get(information, 14, 4));

		/* 处理 */
		List<ThingBean> lossList = new LinkedList<ThingBean>();
		UserStorage userStorage = null;
		for (int i = 0; i < num; i++) {
			int boxID = DataFactory.getInt(DataFactory.get(information, 18 + i * 8, 4));
			int markID = DataFactory.getInt(DataFactory.get(information, 22 + i * 8, 4));
			UserStorage storage = StorageOp.getBoxByIDAndMarkID(id, boxID, markID);

			if (storage == null)
				return new byte[] { 0x01 };
			if (userStorage == null)
				userStorage = storage;
			lossList.add(StorageOp.removeGoods(storage, markID));

		}

		PropBean bean = (PropBean) Goods.getSingleByGoodID(GoodsCate.PROPBEAN, userStorage.getGoodID());
		String[] strArr = bean.getResult().split(",");
		List<ThingBean> storeGoods = StorageOp.storeGoods(id, Integer.valueOf(strArr[index]).intValue(), 1);

		GameLog.createLog(id, 40, null, true, storeGoods, lossList, "turntable");

		byte[] re = new byte[] { 0x00 };
		re = DataFactory.addByteArray(re, DataFactory.getbyte(Integer.valueOf(strArr[index]).intValue()));
		/* 返回 */
		return re;
	}

	/**
	 * 重置副本
	 * 
	 * @param id
	 * @param information
	 * @return
	 */
	public static byte[] resetFB(long id, byte[] information) {

		int cityID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int boxID = DataFactory.getInt(DataFactory.get(information, 14, 4));
		int markID = DataFactory.getInt(DataFactory.get(information, 18, 4));

		/* 判断物品是否存在 */
		UserStorage storage = StorageOp.getBoxByIDAndMarkID(id, boxID, markID);
		if (storage == null) {
			return new byte[] { 0x01 };
		}

		/* 清理副本信息 */
		UserCopy userCopy = UserMemory.getCopy(id);
		for (Iterator<CopyBean> iter = userCopy.getCopyList().iterator(); iter.hasNext();) {
			CopyBean bean = iter.next();
			if (bean.getCityID() == cityID) {
				/* 判断是否以通关 */
				CityBean cityBean = (CityBean) Goods.getById(GoodsCate.CITYBEAN, cityID);
				if (bean.getCurTimesNum() <= cityBean.getLastTimes())
					return new byte[] { 0x02 };

				iter.remove();
				if (userCopy.getCopyList().size() > 0)
					userCopy.setChange(true);
				else {
					DBUtil.delete(id, userCopy);
				}
				DBUtil.executeUpdate(id, "delete from usercityinfo where masterID = " + id + " and cityID = " + cityID);
				DBUtil.executeUpdate(id, "delete from userinvitefriend where masterID = " + id + " and cityID = "
						+ cityID);
				List<ThingBean> lossList = new LinkedList<ThingBean>();
				lossList.add(StorageOp.removeGoods(storage, markID));
				GameLog.createLog(id, 41, null, true, null, lossList, "resetFB");
				return new byte[] { 0x00 };
			}
		}

		return new byte[] { 0x01 };
	}

	/**
	 * 用道具打开道具
	 */
	public static byte[] openPropsBox(long id, byte[] information) {
		byte[] re = new byte[] { 0x00 };
		Random random = new Random();
		// 需要开启的宝箱
		int boxID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int bmarkID = DataFactory.getInt(DataFactory.get(information, 14, 4));
		UserStorage boxStorage = StorageOp.getBoxByIDAndMarkID(id, boxID, bmarkID);
		if (boxStorage == null) {
			re = new byte[] { 0x02 };
			return re;
		}

		int num = DataFactory.getInt(DataFactory.get(information, 18, 4));

		// 得到宝箱道具对象
		PropBean propBean = (PropBean) Goods.getSingleByGoodID(GoodsCate.PROPBEAN, boxStorage.getGoodID());
		String[] keyIDs = propBean.getKeyIDs().split(",");
		// 得到开启传递的 道具
		int match = 0;
		List<LostBean> lostProps = new LinkedList<LostBean>();
		for (int n = 0; n < num; n++) {
			int _boxID = DataFactory.getInt(DataFactory.get(information, 22 + n * 8, 4));
			int _markID = DataFactory.getInt(DataFactory.get(information, 26 + n * 8, 4));
			//
			UserStorage storage = StorageOp.getBoxByIDAndMarkID(id, _boxID, _markID);
			if (storage == null) {
				re = new byte[] { 0x01 };
				return re;
			} else {

				for (int index = 0; index < keyIDs.length; index++) {
					if (storage.getGoodID() == Integer.parseInt(keyIDs[index])) {
						match++;
						break;
					}
				}
				lostProps.add(new LostBean(storage, _markID, _boxID));
			}
		}

		if (keyIDs.length != match) {
			re = new byte[] { 0x01 };
			return re;
		}
		// 解析宝箱开启后的 奖励
		int needplace = 0;
		JSONArray jarray = JSONArray.fromObject(propBean.getResult());
		List<DropGoodsBean> rewards = new ArrayList<DropGoodsBean>();
		for (int i = 0; i < jarray.size(); i++) {
			JSONArray subArray = jarray.getJSONArray(i);
			Double hit = subArray.getDouble(subArray.size() - 1);
			if (random.nextInt(100) <= hit * 100) {
				int ranIndex = random.nextInt(subArray.size() - 1);
				JSONArray json = subArray.getJSONArray(ranIndex);
				if (json.getInt(0) / 10000 != 6 && json.getInt(0) / 10000 != 5)
					needplace++;
				rewards.add(new DropGoodsBean(json.getInt(0), json.getInt(1)));
			}
		}

		/* 检查背包是否足够 */
		if (!ChestOp.isStorageSizeEnough(id, needplace))
			return new byte[] { 0x02 };

		/* 返回宝箱中的物品 */
		List<ThingBean> get = new LinkedList<ThingBean>();
		re = DataFactory.addByteArray(re, DataFactory.getbyte(rewards.size()));
		Iterator<DropGoodsBean> iter = rewards.iterator();
		while (iter.hasNext()) {
			DropGoodsBean dropGoods = (DropGoodsBean) iter.next();
			re = DataFactory.addByteArray(re, DataFactory.getbyte(dropGoods.getGoodsID()));
			re = DataFactory.addByteArray(re, DataFactory.getbyte(dropGoods.getNum()));
			// 更新到背包中
			List<ThingBean> getThing = StorageOp.storeGoods(id, dropGoods.getGoodsID(), dropGoods.getNum());
			if (getThing != null) {
				get.addAll(getThing);
			}
		}
		/* 删除用到的物品 */
		if (rewards.size() > 0) {
			List<ThingBean> loss = new LinkedList<ThingBean>();
			loss.add(StorageOp.removeGoods(boxStorage, bmarkID));
			Iterator<LostBean> iterator = lostProps.iterator();
			while (iterator.hasNext()) {
				LostBean lost = iterator.next();
				loss.add(StorageOp.removeGoods(lost.getUserStorage(), lost.getMarkID()));
			}
			GameLog.createLog(id, 22, null, true, get, loss, "open lucky box");
		}
		return re;
	}
}

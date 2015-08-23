package com.server.user.operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.BlueprintBean;
import com.database.model.bean.CityBean;
import com.database.model.bean.DropGoodsBean;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.PropBean;
import com.database.model.bean.TowerBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserCity;
import com.database.model.bean.UserCityInfo;
import com.database.model.bean.UserInviteFriend;
import com.database.model.bean.UserPickfriend;
import com.database.model.bean.UserSceneGoods;
import com.database.model.bean.UserStorage;
import com.database.model.bean.UserTower;
import com.server.cache.UserMemory;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.goods.PrizeType;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.Cmd;
import com.server.util.Configuration;
import com.server.util.DBUtil;
import com.server.util.ToolUtil;

@SuppressWarnings("unchecked")
public class CityOp {

	private static Log log = LogFactory.getLog(CityOp.class);
	private static final int MAX_SCENE_GOODS_NUM = Configuration.getSceneGoodsNum();
	public static final int MAX_PVE_CITY = Configuration.getMaxPveCity();
	public static final Random RANDOM = new Random();
	private static Finance financeImpl = FinanceImpl.instance();
	private static TowerBean towerBean1 = null;
	private static TowerBean towerBean1Level2 = null;
	private static TowerBean towerBean2 = null;
	private static TowerBean towerBean2Level2 = null;
	private static TowerBean towerBean3 = null;
	private static TowerBean towerBean3Level2 = null;

	static {
		List<TowerBean> towerList = Goods.getGoodsByCate(GoodsCate.TOWERBEAN);
		if (towerList != null) {
			Iterator<TowerBean> ite = towerList.iterator();
			while (ite.hasNext()) {
				TowerBean towerBean = ite.next();
				if (towerBean != null && towerBean.getRace() == 1 && towerBean.getLevel() == 1
						&& towerBean.getBaseLevel() == 1 && towerBean.getSceneLevel() == 1) {
					towerBean1 = towerBean;
				}
				if (towerBean != null && towerBean.getLevel() == 11
						&& towerBean.getBaseLevel() == 9 && towerBean.getSceneLevel() == 1) {
					towerBean2 = towerBean;
				}
				if (towerBean != null && towerBean.getRace() == 3 && towerBean.getLevel() == 1
						&& towerBean.getBaseLevel() == 1 && towerBean.getSceneLevel() == 1) {
					towerBean3 = towerBean;
				}

				if (towerBean != null && towerBean.getRace() == 1 && towerBean.getLevel() == 2
						&& towerBean.getBaseLevel() == 1 && towerBean.getSceneLevel() == 1) {
					towerBean1Level2 = towerBean;
				}
				if (towerBean != null && towerBean.getRace() == 2 && towerBean.getLevel() == 2
						&& towerBean.getBaseLevel() == 1 && towerBean.getSceneLevel() == 1) {
					towerBean2Level2 = towerBean;
				}
				if (towerBean != null && towerBean.getRace() == 3 && towerBean.getLevel() == 2
						&& towerBean.getBaseLevel() == 1 && towerBean.getSceneLevel() == 1) {
					towerBean3Level2 = towerBean;
				}
			}
		}
	}

	public static void initTower(UserCity city, int cityID, int race) {
		CityBean cityBean = (CityBean) Goods.getById(GoodsCate.CITYBEAN, cityID);
		List<UserTower> towerList = city.getTowers();
		if (cityID == 0 || cityID == 1) {
			TowerBean towerBean = null;
			TowerBean towerBeanLevel2 = null;
			if (race == 1) {
				towerBean = towerBean1;
				towerBeanLevel2 = towerBean1Level2;
			} else if (race == 2) {
				towerBean = towerBean2;
				towerBeanLevel2 = towerBean2Level2;
			} else {
				towerBean = towerBean3;
				towerBeanLevel2 = towerBean3Level2;
			}
			String[] pos = cityBean.getTowerBase().split(",");
			if (cityID == 0) {
				UserTower tower1 = new UserTower();
				tower1.setId(1);
				tower1.setHp(towerBean.getHp());
				tower1.setTowerID(towerBean.getId());
				int posID = Integer.parseInt(pos[0]);
				tower1.setPosID(posID);
				tower1.setTime(System.currentTimeMillis());
				towerList.add(tower1);
				city.setChange(true);
			} else if (cityID == 1) {
				UserTower tower1 = new UserTower();
				tower1.setId(1);
				tower1.setHp(towerBean.getHp());
				tower1.setTowerID(towerBean.getId());
				int posID = Integer.parseInt(pos[0]);
				tower1.setPosID(posID);
				tower1.setTime(System.currentTimeMillis());
				towerList.add(tower1);

				UserTower tower2 = new UserTower();
				tower2.setId(2);
				tower2.setHp(0);
				tower2.setTowerID(towerBean.getId());
				tower2.setPosID(Integer.parseInt(pos[1]));
				tower2.setTime(System.currentTimeMillis());
				towerList.add(tower2);

				UserTower tower3 = new UserTower();
				tower3.setId(3);
				tower3.setHp(towerBeanLevel2.getHp());
				tower3.setTowerID(towerBeanLevel2.getId());
				tower3.setPosID(Integer.parseInt(pos[2]));
				tower3.setTime(System.currentTimeMillis());
				towerList.add(tower3);
				
				UserTower tower4 = new UserTower();
				tower4.setId(4);
				tower4.setHp(towerBean2.getHp());
				tower4.setTowerID(towerBean2.getId());
				tower4.setPosID(Integer.parseInt(pos[8]));
				tower4.setTime(System.currentTimeMillis());
				towerList.add(tower4);
				city.setChange(true);
			}
		}
	}

	public static void initTower(long id, int cityID) {
		UserCity city = UserMemory.getCity(id, cityID);
		UserCastle castle = UserMemory.getCastle(id);
		initTower(city, cityID, castle.getRace());
	}

	public static UserCity initUserCity(long id, int cityID) {
		UserCity bean = new UserCity();
		try {
			UserCityInfo info = null;
			try {
				UserCityInfo temp = new UserCityInfo();
				temp.setMasterID(id);
				temp.setCityID(cityID);
				info = (UserCityInfo) DBUtil.get(id, temp);
			} catch (Exception e) {
				log.error(e, e);
			}
			if (info == null) {
				int cityNo = cityID;
				CityBean cityBean = (CityBean) Goods.getById(GoodsCate.CITYBEAN, cityNo);
				if (cityBean != null) {
					CityBean lastCityBean = (CityBean) Goods.getById(GoodsCate.CITYBEAN, cityNo - 1);
					bean.setId(id);
					bean.setCityID(cityID);
					bean.setCityNo(cityNo);
					if (cityID == CopyOp.COPY_START_NUM) {
						bean.setCurrTimesNum(CopyOp.COPY_START_TIMES_NUM);
					} else {
						if (cityID > 1 && lastCityBean != null) {
							bean.setCurrTimesNum(lastCityBean.getLastTimes() + 1);
						} else if (cityID == 1) {
							bean.setCurrTimesNum(1);
						} else if (cityID <= 0) {
							bean.setCurrTimesNum(0);
						}
					}
					bean.setGoods(new LinkedList<UserSceneGoods>());
					List<UserTower> towerList = new LinkedList<UserTower>();
					bean.setTowers(towerList);
					UserCityInfo cityInfo = new UserCityInfo();
					cityInfo.setCityID(cityID);
					cityInfo.setMasterID(id);
					cityInfo.setInfo(bean.encode2Ja());
					DBUtil.save(id, cityInfo);
				} else {
					log.error("init user city error:cityID=" + cityID + " doesn't exist!");
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		if (bean != null) {
			bean.setTime(System.currentTimeMillis());
		}
		return bean;
	}

	public static void dropSceneGoods(long id, UserSceneGoods goods) {
		UserCity city = UserMemory.getCurrentCity(id);
		List<UserSceneGoods> goodsList = city.getGoods();
		if (goodsList == null) {
			goodsList = new LinkedList<UserSceneGoods>();
			city.setGoods(goodsList);
			goodsList.add(goods);
			city.setChange(true);
		} else if (goodsList != null && goodsList.size() <= MAX_SCENE_GOODS_NUM && goods != null) {
			goodsList.add(goods);
			city.setChange(true);
		}
	}

	public static byte[] pickupGoods(long id, byte[] information) throws Exception {
		byte[] re = new byte[] { 0x01 };
		long sceneID = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));
		int boxID = DataFactory.getInt(DataFactory.get(information, 18, 4));
		int type = DataFactory.getInt(DataFactory.get(information, 22, 4));
		boolean isMaster = false;
		if (id == sceneID) {
			isMaster = true;
		}
		UserCity city = UserMemory.getCurrentCity(sceneID);
		List<UserSceneGoods> sceneGoodsList = city.getGoods();
		if (sceneGoodsList != null) {
			List<ThingBean> get = new LinkedList<ThingBean>();
			Iterator<UserSceneGoods> sceneGoodsIte = sceneGoodsList.iterator();
			while (sceneGoodsIte.hasNext()) {
				UserSceneGoods sceneGoods = sceneGoodsIte.next();
				if (sceneGoods.getId() == boxID && type == sceneGoods.getType()) {
					List<DropGoodsBean> dropGoodsList = sceneGoods.getDropGoods();
					if (dropGoodsList != null && dropGoodsList.size() > 0) {
						Iterator<DropGoodsBean> dropGoodsIte = dropGoodsList.iterator();
						while (dropGoodsIte.hasNext()) {
							DropGoodsBean dropGoods = dropGoodsIte.next();
							List<ThingBean> temp = StorageOp.storeGoods(id, dropGoods.getGoodsID(), dropGoods.getNum());
							if (temp != null) {
								get.addAll(temp);
							}
							if (dropGoods.getGoodsID() == 10001) 
								TaskOp.doTask(id, 40010, 1);
						}
					}
					if (isMaster) {
						sceneGoodsIte.remove();
						city.setChange(true);
					}
					GameLog.createLog(id, 3, null, true, get, null, null);
					re[0] = 0x00;
					re = DataFactory.addByteArray(re, new byte[] { 0x00 });
					return re;
				}
			}
		} else {
			log.warn("the goods you want to pick is not exist!");
		}
		return re;
	}

	/**
	 * 批量拾取
	 * @param id
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public static byte[] batchPickupGoods(long id, byte[] information) throws Exception {
		byte[] re = new byte[] { 0x01 };
		Cmd cmd = Cmd.getInstance(information);
		int count = cmd.readInt(10);
		boolean canDoTask = true;
		int[] bags = new int[count];
		for (int i = 0; i < count; i++) {
			bags[i] = cmd.readInt();
		}
		UserCity city = UserMemory.getCurrentCity(id);
		List<UserSceneGoods> sceneGoodsList = city.getGoods();
		List<UserSceneGoods> tempList = new LinkedList<UserSceneGoods>();
		if (sceneGoodsList != null) {
			List<ThingBean> get = new LinkedList<ThingBean>();
			Iterator<UserSceneGoods> sceneGoodsIte = sceneGoodsList.iterator();
			while (sceneGoodsIte.hasNext()) {
				UserSceneGoods sceneGoods = sceneGoodsIte.next();
				for (int bag : bags) {
					if (sceneGoods.getId() == bag) {
						tempList.add(sceneGoods);
					}
				}
			}

			for (Iterator<UserSceneGoods> iter = tempList.iterator(); iter.hasNext();) {
				UserSceneGoods sceneGoods = iter.next();
				List<DropGoodsBean> dropGoodsList = sceneGoods.getDropGoods();
				if (dropGoodsList != null && dropGoodsList.size() > 0) {
					Iterator<DropGoodsBean> dropGoodsIte = dropGoodsList.iterator();
					while (dropGoodsIte.hasNext()) {
						DropGoodsBean dropGoods = dropGoodsIte.next();
						List<ThingBean> temp = StorageOp.storeGoods(id, dropGoods.getGoodsID(), dropGoods.getNum());
						if (temp != null) {
							get.addAll(temp);
						}
						if (dropGoods.getGoodsID() == 10001 && canDoTask) {
							TaskOp.doTask(id, 40010, 1);
							canDoTask = false;
						}
					}
					sceneGoodsList.remove(sceneGoods);
				}
			}
			city.setChange(true);
			GameLog.createLog(id, 3, null, true, get, null, null);
			re[0] = 0x00;
			re = DataFactory.addByteArray(re, new byte[] { 0x00 });
			return re;
		} else {
			log.warn("the goods you want to pick is not exist!");
		}
		return re;
	}

	public static byte[] buildTower(long id, byte[] information) throws Exception {
		byte[] re = new byte[] { 0x01 };
		int boxID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int markID = DataFactory.getInt(DataFactory.get(information, 14, 4));
		int goodsID = DataFactory.getInt(DataFactory.get(information, 18, 4));
		int posID = DataFactory.getInt(DataFactory.get(information, 22, 4));
		int num = DataFactory.getInt(DataFactory.get(information, 26, 4));
		long[] slavers = new long[num];
		for (int i = 0; i < num; i++) {
			slavers[i] = DataFactory.doubleBytesToLong(DataFactory.get(information, 30 + 8 * i, 8));
		}
		if (slavers.length == 0 || !SlaverOp.isFree(id, slavers)) {
			log.info("no slaver work for this");
			return re;
		}
		UserStorage storage = StorageOp.getBoxByIDAndMarkID(id, boxID, markID);
		if (storage == null) {
			return re;
		}
		if (storage != null && storage.getGoodID() == goodsID) {
			BlueprintBean bpBean =
					(BlueprintBean) Goods.getSingleByGoodID(GoodsCate.BLUEPRINTBEAN, storage.getGoodID());
			TowerBean towerBean = (TowerBean) Goods.getByGoodIDAndLevel(GoodsCate.TOWERBEAN, bpBean.getProduct(), 1);
			if (towerBean != null) {
				UserCity city = UserMemory.getCurrentCity(id);
				List<UserTower> towersList = city.getTowers();
				UserTower tower = new UserTower();
				tower.setId(getMaxTowerID(towersList) + 1);
				tower.setPosID(posID);
				tower.setTowerID(towerBean.getId());
				tower.setState(1);
				tower.setHp(towerBean.getHp());
				tower.setTime(System.currentTimeMillis());
				tower.setEndTime(System.currentTimeMillis() + SlaverOp.workForTower(id, slavers, tower, 0));
				FinanceBean financeBean = new FinanceBean();
				financeBean.setId(id);
				financeBean.setCoin(-towerBean.getBuildNeedCoin());
				financeBean.setCrystal(-towerBean.getBuildNeedCrystal());
				financeBean.setMetal(-towerBean.getBuildNeedMetal());
				financeBean.setRock(-towerBean.getBuildNeedRock());
				if (financeImpl.charge(financeBean)) {
					towersList.add(tower);
					city.setChange(true);
					List<ThingBean> lost = new LinkedList<ThingBean>();
					lost.add(new ThingBean(7, 1, 60006, towerBean.getBuildNeedCoin(), null));
					lost.add(new ThingBean(7, 7, 60001, towerBean.getBuildNeedRock(), null));
					lost.add(new ThingBean(7, 8, 60002, towerBean.getBuildNeedMetal(), null));
					lost.add(new ThingBean(7, 9, 60003, towerBean.getBuildNeedCrystal(), null));
					GameLog.createLog(id, 4, null, true, null, lost, null);
					DataFactory.replace(re, 0, new byte[] { 0x00 });
					re = DataFactory.addByteArray(re, DataFactory.getbyte(tower.getId()));

					if (towerBean.getBaseLevel() == 1) {
						TaskOp.doTask(id, 10001, 1);
						if (towerBean.getLevel() > 1) {
							TaskOp.doTask(id, 10018, 1);
							TaskOp.doTask(id, 10023, 1);
						}
					} else if (towerBean.getBaseLevel() == 6) {
						TaskOp.doTask(id, 10025, 1);
					}
					if (city.getCityID() == 0) {
						TaskOp.doTask(id, 10012, 1);
						TaskOp.doTask(id, 20008, 1);
					}
				}
			}
		}
		return re;
	}

	private static int getMaxTowerID(List<UserTower> towersList) {
		int max = 0;
		if (towersList != null) {
			Iterator<UserTower> ite = towersList.iterator();
			while (ite.hasNext()) {
				UserTower tower = ite.next();
				if (tower.getId() > max) {
					max = tower.getId();
				}
			}
		}
		return max;
	}

	private static UserTower getTower(long id, int indexID) {
		UserCity city = UserMemory.getCurrentCity(id);
		if (city != null) {
			List<UserTower> towerList = city.getTowers();
			if (towerList != null) {
				Iterator<UserTower> towerIte = towerList.iterator();
				while (towerIte.hasNext()) {
					UserTower tower = towerIte.next();
					if (tower.getId() == indexID) {
						return tower;
					}
				}
			}
		}
		return null;
	}

	public static byte[] upgradeTower(long id, byte[] information) throws Exception {
		byte[] re = new byte[] { 0x01 };
		try {
			int indexID = DataFactory.getInt(DataFactory.get(information, 10, 4));
			int num = DataFactory.getInt(DataFactory.get(information, 14, 4));
			long[] sla = new long[num];
			for (int i = 0; i < num; i++) {
				sla[i] = DataFactory.doubleBytesToLong(DataFactory.get(information, 18 + 8 * i, 8));
			}
			if (/* sla.length == 0 || */!SlaverOp.isFree(id, sla)) {
				log.warn("Game_Warning:there are no slaver work for this" + sla[0]);
				return re;
			}
			UserTower tower = getTower(id, indexID);
			UserInviteFriend invite = getUserInviteFriend(id, tower.getId());
			TowerBean towerBean = (TowerBean) Goods.getById(GoodsCate.TOWERBEAN, tower.getTowerID());
			if (tower != null && tower.getState() == 0 && towerBean != null && SlaverOp.isFree(id, sla)) {
				if ((invite == null && towerBean.getUpNeedFriend() > 0)
						|| (invite != null && towerBean.getUpNeedFriend() > invite.getReachedFriend()))
					return new byte[] { 0x02 };
				UserCity city = UserMemory.getCurrentCity(id);
				tower.setTime(System.currentTimeMillis());
				FinanceBean bean = new FinanceBean();
				bean.setId(id);
				bean.setCrystal(-towerBean.getUpNeedCrystal());
				bean.setRock(-towerBean.getUpNeedRock());
				bean.setMetal(-towerBean.getUpNeedMetal());
				boolean suc = financeImpl.charge(bean);
				if (suc) {
					tower.setEndTime(System.currentTimeMillis() + SlaverOp.workForTower(id, sla, tower, 1));
					tower.setState(2);
					city.setChange(true);
					List<ThingBean> lost = new LinkedList<ThingBean>();
					lost.add(new ThingBean(7, 7, 60001, towerBean.getUpNeedRock(), null));
					lost.add(new ThingBean(7, 8, 60002, towerBean.getUpNeedMetal(), null));
					lost.add(new ThingBean(7, 9, 60003, towerBean.getUpNeedCrystal(), null));
					GameLog.createLog(id, 5, null, true, null, lost,
							"posID:" + tower.getPosID() + " towerID:" + tower.getTowerID() + " upgrad");
					re = new byte[] { 0x00 };
					if (invite != null)
						removeUserInviteFriend(id, city, indexID);

					TaskOp.doTask(id, 10003, 1);
					int taskId = 0;
					taskId = towerBean.getSceneLevel() == 4 ? 40003 : taskId;
					taskId = towerBean.getSceneLevel() == 9 ? 40004 : taskId;
					taskId = towerBean.getSceneLevel() == 14 ? 40005 : taskId;
					if (towerBean.getSceneLevel() == 14)
						TaskOp.doTask(id, 50005, 1);
					TaskOp.doTask(id, taskId, 1);
					if (towerBean.getBaseLevel() == 1) {
						if (towerBean.getSceneLevel() == 3)
							TaskOp.doTask(id, 10020, 1);
						if (towerBean.getSceneLevel() == 2)
							TaskOp.doTask(id, 10019, 1);
					}
					if (city.getCityID() == 0) {
						TaskOp.doTask(id, 10013, 1);
					}
					
				} else {
					log.warn("Game_Warning:the material is not enough");
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return re;
	}

	/**
	 * 获取玩家已邀请的好友
	 * @param id
	 * @param towerId
	 * @return
	 */
	private static UserInviteFriend getUserInviteFriend(long id, int towerId) {
		UserInviteFriend userInviteFriend = null;
		try {
			int cityID = UserMemory.getCurrentCity(id).getCityID();
			List<?> query =
					DBUtil.namedQuery(id, "from UserInviteFriend as uif where uif.masterID=" + id + " and uif.cityID="
							+ cityID + " and uif.towerID=" + towerId);
			if (query.size() > 0) {
				userInviteFriend = (UserInviteFriend) query.get(0);
			}

		} catch (Exception e) {
			log.error(e, e);
		}
		return userInviteFriend;
	}

	/**
	 * 邀请好友
	 * @param id
	 * @param information
	 */
	public static void inviteFriends(long id, byte[] information) {
		try {
			Cmd req = Cmd.getInstance(information);

			int cityId = UserMemory.getCurrentCity(id).getCityNo();
			int posId = req.readInt(10);
			int towerId = getTower(id, posId).getId();
			int count = req.readInt();
			long[] friends = new long[count];
			String friendsStr = "";

			for (int i = 0; i < count; i++) {
				friends[i] = req.readLong();
				friendsStr += friends[i] + ",";
			}
			friendsStr = friendsStr.endsWith(",") ? friendsStr.substring(0, friendsStr.length() - 1) : friendsStr;

			freshInviteFriends(id, cityId, towerId, posId, count, friendsStr);

		} catch (Exception e) {
			log.error(e, e);
		}
	}

	/**
	 * 用点卷补齐邀请好友
	 * @param id
	 * @param information
	 * @return
	 */
	public static byte[] useCashInviteFriend(long id, byte[] information) {
		Cmd req = Cmd.getInstance(information);
		try {
			int cityId = UserMemory.getCurrentCity(id).getCityNo();
			int posId = req.readInt(10);
			int towerId = getTower(id, posId).getId();
			int num = req.readInt();
			int cash = req.readInt();
			String friendsStr = "";
			for (int i = 0; i < num; i++) {
				friendsStr += "0,";
			}
			friendsStr = friendsStr.endsWith(",") ? friendsStr.substring(0, friendsStr.length() - 1) : friendsStr;

			if (cash == num) {
				FinanceBean bean = new FinanceBean();
				bean.setId(id);
				bean.setCash(-cash);
				if (financeImpl.charge(bean)) {
					freshInviteFriends(id, cityId, towerId, posId, num, friendsStr);
					return new byte[] { 0x00 };
				}
			}

		} catch (Exception e) {
			log.error(e, e);
		}

		return new byte[] { 0x01 };
	}

	private static void freshInviteFriends(long id, int cityId, int towerId, int posId, int count, String friendsStr) {
		if (count > 0) {
			List<?> query =
					DBUtil.namedQuery(id, "from UserInviteFriend as uif where uif.masterID=" + id + " and uif.cityID="
							+ cityId + " and uif.towerID=" + posId);
			if (query.size() > 0) {
				UserInviteFriend userInviteFriend = (UserInviteFriend) query.get(0);
				userInviteFriend.setReachedFriend(userInviteFriend.getReachedFriend() + count);
				userInviteFriend.setInviteFriends(userInviteFriend.getInviteFriends() + "," + friendsStr);
				DBUtil.update(id, userInviteFriend);
			} else {
				UserInviteFriend uif = new UserInviteFriend();
				uif.setMasterID(id);
				uif.setCityID(cityId);
				uif.setTowerID(posId);
				TowerBean bean = (TowerBean) Goods.getById(GoodsCate.TOWERBEAN, towerId);
				uif.setNeedFriend(bean.getUpNeedFriend());
				uif.setReachedFriend(count);
				uif.setInviteFriends(friendsStr);
				DBUtil.save(id, uif);
			}
		}
	}

	private static void removeUserInviteFriend(long id, UserCity city, int towerId) {
		try {
			int cityID = city.getCityID();
			DBUtil.executeUpdate(id, "delete from userinvitefriend where masterID=" + id + " and cityID=" + cityID
					+ " and towerID=" + towerId);
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	@SuppressWarnings("unused")
	public static byte[] repairTower(long id, byte[] information) throws Exception {
		byte[] re = new byte[] { 0x02 };
		int indexID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int num = DataFactory.getInt(DataFactory.get(information, 14, 4));
		long[] sla = new long[num];
		for (int i = 0; i < num; i++) {
			sla[i] = DataFactory.getInt(DataFactory.get(information, 18 + 8 * i, 8));
		}
		if (!SlaverOp.isFree(id, sla)) {
			log.warn("Game_Warning:there are no slaver work for this" + Arrays.asList(sla));
			return re;
		}
		UserTower tower = getTower(id, indexID);
		TowerBean towerBean = (TowerBean) Goods.getById(GoodsCate.TOWERBEAN, tower.getTowerID());
		if (tower != null && towerBean != null && SlaverOp.isFree(id, sla)) {
			UserCity city = UserMemory.getCurrentCity(id);
			int hp = towerBean.getHp() - tower.getHp();
			tower.setTime(System.currentTimeMillis());
			FinanceBean bean = new FinanceBean();
			bean.setId(id);
			// bean.setCoin(-hp * CastleOp.REPAIR_COST_COIN);
			// bean.setCrystal(-hp * CastleOp.REPAIR_COST_CRYSTAL);
			// bean.setRock(-hp * CastleOp.REPAIR_COST_ROCK);
			// bean.setMetal(-hp * CastleOp.REPAIR_COST_METAL);
			bean.setCoin(-1);
			bean.setCrystal(-1);
			bean.setRock(-1);
			bean.setMetal(-1);

			boolean suc = financeImpl.charge(bean);
			if (suc) {
				// tower.setEndTime(System.currentTimeMillis() +
				// SlaverOp.workForTower(id, sla, tower, 2));
				tower.setEndTime(System.currentTimeMillis() + 1000);
				tower.setState(3);
				city.setChange(true);
				List<ThingBean> lost = new LinkedList<ThingBean>();
				// lost.add(new ThingBean(7, 7, 60001, hp *
				// CastleOp.REPAIR_COST_ROCK, null));
				// lost.add(new ThingBean(7, 8, 60002, hp *
				// CastleOp.REPAIR_COST_METAL, null));
				// lost.add(new ThingBean(7, 9, 60003, hp *
				// CastleOp.REPAIR_COST_CRYSTAL, null));
				// lost.add(new ThingBean(7, 1, 60006, hp *
				// CastleOp.REPAIR_COST_COIN, null));
				lost.add(new ThingBean(7, 7, 60001, 1, null));
				lost.add(new ThingBean(7, 8, 60002, 1, null));
				lost.add(new ThingBean(7, 9, 60003, 1, null));
				lost.add(new ThingBean(7, 1, 60006, 1, null));
				GameLog.createLog(id, 6, null, true, null, lost, null);
				re = new byte[] { 0x00 };
			} else {
				log.warn("Game_Warning:the material is not enough");
				return re;
			}
		}
		return re;
	}

	public static void removeTower(long id, byte[] information) throws Exception {
		int indexID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		UserCity city = UserMemory.getCurrentCity(id);
		if (city != null) {
			List<UserTower> towerList = city.getTowers();
			if (towerList != null) {
				Iterator<UserTower> towerIte = towerList.iterator();
				while (towerIte.hasNext()) {
					UserTower tower = towerIte.next();
					if (tower.getId() == indexID) {
						if (tower.getState() != 0) {
							SlaverOp.cancelWork(id, tower.getId());
						}
						towerIte.remove();
						removeUserInviteFriend(id, city, tower.getId());
						city.setChange(true);
						List<ThingBean> lost = new LinkedList<ThingBean>();
						TowerBean towerBean = (TowerBean) Goods.getById(GoodsCate.TOWERBEAN, tower.getTowerID());
						FinanceBean bean = new FinanceBean();
						bean.setId(id);
						bean.setRock(towerBean.getFeedbackRock());
						bean.setMetal(towerBean.getFeedbackMetal());
						bean.setCrystal(towerBean.getFeedbackCrystal());
						financeImpl.charge(bean);
						lost.add(new ThingBean(4, indexID, towerBean.getGoodID(), 1, null));
						GameLog.createLog(id, 7, null, true, null, lost, null);
					}
				}
			}
		}
	}

	/**
	 * 炮塔加速
	 * 
	 * @param id
	 * @param information
	 * @return
	 */
	public static byte[] towerSpeedUp(long id, byte[] information) {
		int boxID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		int markID = DataFactory.getInt(DataFactory.get(information, 14, 4));
		int towerID = DataFactory.getInt(DataFactory.get(information, 18, 4));

		UserStorage storage = StorageOp.getBoxByIDAndMarkID(id, boxID, markID);
		if (storage == null)
			return new byte[] { 0x01 };

		PropBean speedUp = (PropBean) Goods.getSingleByGoodID(GoodsCate.PROPBEAN, storage.getGoodID());

		if (speedUp == null || speedUp.getType() != 1)
			return new byte[] { 0x01 };

		UserTower tower = getTower(id, towerID);
		if (tower != null && tower.getState() == 0) {
			return new byte[] { 0x01 };
		}
		long endTime = 0;
		int propType = 1;
		double speedupTime = 0;
		if (speedUp.getResult().equals("0")) {
			propType = 3;
		} else if (Double.parseDouble(speedUp.getResult()) < 1) {
			propType = 2;
			speedupTime = Double.parseDouble(speedUp.getResult());
		} else {
			speedupTime = Double.parseDouble(speedUp.getResult());
		}
		if (propType == 1) {
			endTime = tower.getEndTime() - (int) speedupTime;
		} else if (propType == 2) {
			endTime = tower.getEndTime() - (long) ((tower.getEndTime() - System.currentTimeMillis()) * speedupTime);
		} else {
			endTime = System.currentTimeMillis();
		}
		List<ThingBean> lost = new ArrayList<ThingBean>();
		lost.add(StorageOp.removeGoods(storage, markID));
		tower.setEndTime(endTime);
		SlaverOp.speedup(id, tower.getId(), endTime);
		UserMemory.getCurrentCity(id).setChange(true);
		GameLog.createLog(id, 40, 0, true, null, lost, "speed up tower");
		byte[] re = new byte[] { 0x00 };
		re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(endTime));
		return re;
	}

	private static boolean canEnter(long id, int cityID) throws Exception {
		UserCastle castle = UserMemory.getCastle(id);
		if (cityID >= 0 && cityID <= castle.getMaxCity() || cityID >= CopyOp.COPY_START_NUM) {
			return true;
		} else if (cityID > castle.getMaxCity()) {
			UserCity city = UserMemory.getCity(id, castle.getMaxCity());
			if (FightOp.isLastTimes(id, city.getCurrTimesNum(), city)) {
				FightOp.enterNextCity(id, city);
				return true;
			}
		}
		return false;
	}

	public static byte[] getCity(long id, int cityID) throws Exception {
		byte[] re = null;
		if (canEnter(id, cityID)) {
			UserCity city = UserMemory.getCity(id, cityID);
			if (city == null) {
				city = initUserCity(id, cityID);
			}
			re = DataFactory.getbyte(cityID);
			re = DataFactory.addByteArray(re, DataFactory.getbyte(city.getCityNo()));
			re = DataFactory.addByteArray(re, DataFactory.getbyte(city.getCurrTimesNum()));
			List<UserTower> towerList = city.getTowers();
			re = DataFactory.addByteArray(re, DataFactory.getbyte(towerList.size()));
			Iterator<UserTower> towerIte = towerList.iterator();
			while (towerIte.hasNext()) {
				UserTower tower = towerIte.next();
				TowerBean towerBean = (TowerBean) Goods.getById(GoodsCate.TOWERBEAN, tower.getTowerID());
				if (towerBean != null) {
					re = DataFactory.addByteArray(re, DataFactory.getbyte(tower.getId()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(towerBean.getGoodID()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(tower.getPosID()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(towerBean.getSceneLevel()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(tower.getHp()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(tower.getState()));
					if (tower.getState() != 0) {
						re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(tower.getEndTime()));
					}
					List<?> results = DBUtil.namedQuery(id, "from UserInviteFriend uif where uif.masterID=" + id
									+ " and uif.cityID=" + city.getCityNo() + " and uif.towerID=" + tower.getId());
					UserInviteFriend result = null;
					if (results != null && results.size() > 0) {
						result = (UserInviteFriend) results.get(0);
					}
					Cmd cmd = Cmd.getInstance();
					cmd.appendString(result != null ? result.getInviteFriends() : "");
					re = DataFactory.addByteArray(re, cmd.getResponse());
				}
			}

			if (cityID != 0) {
				List<UserSceneGoods> goodsList = city.getGoods();
				re = DataFactory.addByteArray(re, DataFactory.getbyte(goodsList.size()));
				Iterator<UserSceneGoods> goodsIte = goodsList.iterator();
				while (goodsIte.hasNext()) {
					UserSceneGoods goods = goodsIte.next();
					re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getId()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getType()));
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(goods.getTime()));
					List<DropGoodsBean> dropGoodsList = goods.getDropGoods();
					re = DataFactory.addByteArray(re, DataFactory.getbyte(dropGoodsList.size()));
					Iterator<DropGoodsBean> dropGoodsIte = dropGoodsList.iterator();
					while (dropGoodsIte.hasNext()) {
						DropGoodsBean dropGoods = dropGoodsIte.next();
						re = DataFactory.addByteArray(re, DataFactory.getbyte(dropGoods.getGoodsID()));
						re = DataFactory.addByteArray(re, DataFactory.getbyte(dropGoods.getNum()));
					}
				}
			}
		}
		return re;
	}

	public static byte[] getCity(long id, byte[] information) throws Exception {
		byte[] re = new byte[] { (byte) 0x00 };
		long sceneID = id;// DataFactory.doubleBytesToLong(DataFactory.get(information,
							// 10, 8));
		int cityID = DataFactory.getInt(DataFactory.get(information, 18, 4));
		byte[] city = getCity(sceneID, cityID);
		if (city != null) {
			UserCastle castle = UserMemory.getCastle(id);
			if (castle.getCurrentCity() != cityID) {
				castle.setCurrentCity(cityID);
				castle.setChange(true);
			}
			re = DataFactory.addByteArray(re, city);
		} else {
			re = new byte[] { (byte) 0x01 };
		}
		return re;
	}

	public static byte[] flipCard(long id, byte[] information) throws Exception {
		byte[] re = null;
		int level = DataFactory.getInt(DataFactory.get(information, 10, 4));
		UserCity city = UserMemory.getCity(id, level);
		UserCastle castle = UserMemory.getCastle(id);
		if (city != null && !city.isPrize()) {
			DropGoodsBean prizeGoods = Goods.fitGoods(PrizeType.CITYPRIZE, city.getCityID(), null, castle.getRace());
			if (prizeGoods != null) {
				List<ThingBean> get = new LinkedList<ThingBean>();
				get.addAll(StorageOp.storeGoods(id, prizeGoods.getGoodsID(), prizeGoods.getNum()));
				city.setPrize(true);
				city.setChange(true);
				re = DataFactory.getbyte(1);
				re = DataFactory.getbyte(prizeGoods.getGoodsID());
				re = DataFactory.addByteArray(re, DataFactory.getbyte(prizeGoods.getNum()));
				GameLog.createLog(id, 16, null, true, get, null, null);
			} else {
				log.error("Game_Error:the finishing city:" + city.getCityID() + " prize is error");
			}
		}
		return re;
	}

	public static byte[] resetCity(long id, byte[] information) {
		byte[] re = null;
		int cityID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		UserCity city = UserMemory.getCity(id, cityID);
		if (city != null && cityID > 1 && cityID < CopyOp.COPY_START_NUM && EnergyOp.consume(id, 2)) {
			int firstTimes = 1;
			if (cityID > 1) {
				CityBean lastCityBean = (CityBean) Goods.getById(GoodsCate.CITYBEAN, cityID - 1);
				firstTimes = lastCityBean.getLastTimes() + 1;
			}
			city.setCurrTimesNum(firstTimes);
			city.setChange(true);
			re = new byte[] { 0x00 };
			log.info("Game_Info:id:" + id + " reset cityID:" + cityID);
		} else {
			re = new byte[] { 0x01 };
		}
		return re;
	}

	public static boolean cancle(long id, double returnRate, int towerID, int type) {// 0取消升级维修
																						// 1取消建造
		UserCity userCity = UserMemory.getCurrentCity(id);
		UserTower tower = getTower(id, towerID);
		if (tower != null) {
			if (type == 0 && tower.getState() > 1) {
				tower.setState(0);
				userCity.setChange(true);
				SlaverOp.cancelWork(id, towerID);
				return true;
			} else if (type == 1) {
				userCity.getTowers().remove(tower);
				userCity.setChange(true);
				SlaverOp.cancelWork(id, towerID);
				return true;
			}
		}
		return false;
	}

	public static boolean resetCity(long id, int targetRace, Session session) {
		session.createSQLQuery("delete from usercityinfo where masterID = " + id).executeUpdate();
		UserCity city0 = new UserCity();
		city0.setCityID(0);
		city0.setCityNo(0);
		city0.setCurrTimesNum(0);
		city0.setGoods(new LinkedList<UserSceneGoods>());
		city0.setTowers(new LinkedList<UserTower>());
		CityOp.initTower(city0, 0, targetRace);
		UserCityInfo cityInfo0 = new UserCityInfo();
		cityInfo0.setMasterID(id);
		cityInfo0.setCityID(0);
		cityInfo0.setInfo(city0.encode2Ja());

		UserCity city1 = new UserCity();
		city1.setCityID(1);
		city1.setCityNo(1);
		city1.setCurrTimesNum(1);
		city1.setGoods(new LinkedList<UserSceneGoods>());
		city1.setTowers(new LinkedList<UserTower>());
		CityOp.initTower(city1, 1, targetRace);
		UserCityInfo cityInfo1 = new UserCityInfo();
		cityInfo1.setMasterID(id);
		cityInfo1.setCityID(1);
		cityInfo1.setInfo(city1.encode2Ja());

		session.save(cityInfo0);
		session.save(cityInfo1);

		session.createSQLQuery("delete from userinvitefriend where masterID = " + id).executeUpdate();
		return true;
	}

	/**
	 * 好友点塔
	 * @param id
	 * @param information
	 * @return
	 */
	public static byte[] pickFriend(long id, byte[] information) {
		Cmd req = Cmd.getInstance(information);
		Cmd res = Cmd.getInstance();
		try {
			long friendId = req.readLong(10);
			int towerId = req.readInt();
			long now = System.currentTimeMillis();
			
			/* 判断今日拾取数量是否已达上限 */
			if (todayPickFriendCount(id) >= 20) {
				res.appendByte((byte) 0x01);
				return res.getResponse();
			}
			
			/* 判断背包数量是否足够 */
			if (!ChestOp.isStorageSizeEnough(id, 1)) {
				res.appendByte((byte) 0x02);
				return res.getResponse();
			}
			
			List<UserPickfriend> results = DBUtil.namedQuery(id, 
					"from UserPickfriend upf where upf.masterID=" + id + " and upf.friendID=" + friendId);
			UserPickfriend result;
			if (results != null && results.size() > 0) {
				result = results.get(0);
				result = isResetTime(result);
				if (result.getPickedNum() >= 3) {
					res.appendByte((byte) 0x01);
					return res.getResponse();
				}
				result.setLastPickedTime(now);
				result.setPickedNum(result.getPickedNum() + 1);
				DBUtil.update(id, result);
			} else {
				result = new UserPickfriend();
				result.setMasterID(id);
				result.setFriendID(friendId);
				result.setLastPickedTime(now);
				result.setPickedNum(1);
				DBUtil.save(id, result);
			}
			
			TowerBean bean = (TowerBean) Goods.getSingleByGoodID(GoodsCate.TOWERBEAN, towerId);
			int goodId, num;
			if (bean.getLevel() <= 6) {
				goodId = 60000 + bean.getTowerAttType();
				num = 10;
			} else {
				double rate = ToolUtil.getRandom().nextDouble();
				if (rate < 0.95) {
					goodId = 60000 + bean.getTowerAttType();
					num = 15;
				} else {
					goodId = 10001;
					num = 1;
				}
			}
			List<ThingBean> get = StorageOp.storeGoods(id, goodId, num);
			GameLog.createLog(id, 122, null, true, get, null, "pick friend");
			res.appendByte((byte) 0);
			res.appendInt(goodId);
			res.appendInt(num);
			res.appendInt(result.getPickedNum() < 3 ? 0 : 1);
			return res.getResponse();
			
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return new byte[] { 0x02 };
	}
	
	private static UserPickfriend isResetTime(UserPickfriend upf) {
		long now = System.currentTimeMillis();
		long oneDay = 1000 * 60 * 60 * 24;
		if (now / oneDay != upf.getLastPickedTime() / oneDay) {
			upf.setPickedNum(0);
		}
		return upf;
	}
	
	/**
	 * 判断今日点塔数量是否已达上限
	 * @param id
	 * @return
	 */
	private static int todayPickFriendCount(long id) {
		int rtnVal = 0;

		List<UserPickfriend> results = DBUtil.namedQuery(id, 
				"from UserPickfriend upf where upf.masterID=" + id);
		if (results != null) {
			for (Iterator<UserPickfriend> iter = results.iterator(); iter.hasNext(); ) {
				UserPickfriend next = iter.next();
				next = isResetTime(next);
				if (next.getPickedNum() > 0)
					rtnVal ++;
			}
		}
		
		return rtnVal;
	}
	
	/**
	 * 判断能否点塔
	 * @param id
	 * @param friendId
	 * @return
	 */
	public static boolean canPickFriends(long id, long friendId) {
		List<UserPickfriend> results = DBUtil.namedQuery(id, 
				"from UserPickfriend upf where upf.masterID=" + id + " and upf.friendID=" + friendId);
		boolean flag = todayPickFriendCount(id) < 20;
		flag = flag ? results == null || results.size() <= 0 || isResetTime(results.get(0)).getPickedNum() < 3 : flag;
		
		return flag;
	}
}

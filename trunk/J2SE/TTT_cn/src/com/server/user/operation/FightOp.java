package com.server.user.operation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.connect.instance.Instance;
import com.cindy.run.connect.instance.InstanceFactory;
import com.cindy.run.connect.instance.Instance.Attribute;
import com.cindy.run.util.DataFactory;
import com.cindy.run.util.ThreadPoolExecutorTimer;
import com.database.model.bean.AttackMonster;
import com.database.model.bean.BlueprintPieceBean;
import com.database.model.bean.CastleBean;
import com.database.model.bean.CityBean;
import com.database.model.bean.DropGoodsBean;
import com.database.model.bean.ExplosiveGoodsBean;
import com.database.model.bean.FightBean;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.IndexBean;
import com.database.model.bean.MonsterBean;
import com.database.model.bean.PVPListBean;
import com.database.model.bean.PVPRecordBean;
import com.database.model.bean.TimesBean;
import com.database.model.bean.TitleBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserCity;
import com.database.model.bean.UserFightInfo;
import com.database.model.bean.UserReceiveGood;
import com.database.model.bean.UserSceneGoods;
import com.database.model.bean.UserStorage;
import com.server.cache.UserMemory;
import com.server.dispose.TDDispose;
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

public class FightOp {

	public static final int SECURITY_TIME = Configuration.getSecurityTime();
	private static Log log = LogFactory.getLog(FightOp.class);
	private static final ConcurrentHashMap<Long, UserFightInfo> MEMORY = new ConcurrentHashMap<Long, UserFightInfo>();
	private static final ThreadPoolExecutorTimer TIMER = ThreadPoolExecutorTimer.getIntance();
	private static final int TIME = Configuration.getTime();
	private static final int FIGHT_CACHE_TIME = Configuration.getFightCacheTime();
	private static final int TIMES_MONSTER_NUM = Configuration.getTimesMonsterNum();
	private static final int BOSS_DROP_NUM = 3;
	private static Finance financeImpl = FinanceImpl.instance();
	private static final int LIMIT_CASTLE_HP = 0;
	private static final int MAX_SCENE_GOODS_NUM = Configuration.getSceneGoodsNum();
	private static final Object LOCK = new Object();
	private static final int r1 = Configuration.getInt("server.pvp.monster.catch1", 5);
	private static final int r2 = Configuration.getInt("server.pvp.monster.catch2", 2);
	private static final String pvnAward = Configuration.get("server.pvn.award");
	private static FightOp instance = new FightOp();

	private FightOp() {
		TIMER.getPreciseTimer().scheduleAtFixedRate(new Persister(), TIME, TIME, TimeUnit.MILLISECONDS);
	}

	public static FightOp instance() {
		if (instance == null) {
			synchronized (LOCK) {
				if (instance == null) {
					instance = new FightOp();
				}
			}
		}
		return instance;
	}

	private class Persister implements Runnable {

		public void run() {
			try {
				Iterator<Long> ite = MEMORY.keySet().iterator();
				while (ite.hasNext()) {
					long id = ite.next();
					try {
						UserFightInfo bean = MEMORY.get(id);
						if (bean != null && (System.currentTimeMillis() - bean.getTime() > FIGHT_CACHE_TIME)) {
							update(id, bean);
							MEMORY.remove(id);
						}
					} catch (Exception e) {
						log.error(e, e);
					}
				}
			} catch (Exception e) {
				log.error(e, e);
			}
		}
	}

	private static void update(long id, UserFightInfo bean) {
		if (bean != null && bean.isChange()) {
			bean.encode();
			try {
				DBUtil.update(id, bean);
				bean.setChange(false);
			} catch (Exception e) {
				DBUtil.merge(id, bean);
				log.error(e, e);
			}
		}
	}

	private static void save(long id, UserFightInfo bean) {
		try {
			if (bean != null && bean.isChange()) {
				bean.encode();
				DBUtil.saveOrUpdate(id, bean);
				bean.setChange(false);
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	private static UserFightInfo getFromDB(long id) {
		long time = System.currentTimeMillis();
		UserFightInfo info = null;
		try {
			info = (UserFightInfo) DBUtil.get(id, UserFightInfo.class);
			if (info != null) {
				info.decode();
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		long spend = System.currentTimeMillis() - time;
		if (spend > 100) {
			log.info("get fight info from db spend time:" + spend);
		}
		return info;
	}

	private static UserFightInfo getUserFightInfo(long id) {
		UserFightInfo info = MEMORY.get(id);
		if (info == null) {
			info = getFromDB(id);
			MEMORY.put(id, info);
		}
		if (info == null) {
			info = new UserFightInfo();
			info.setId(id);
		}
		info.setTime(System.currentTimeMillis());
		return info;
	}

	public static FightBean getFightBean(long id, int timesID) {
		FightBean bean = null;
		UserFightInfo info = getUserFightInfo(id);
		List<FightBean> userFightBeanList = info.getFightInfoList();
		if (userFightBeanList != null) {
			Iterator<FightBean> ite = userFightBeanList.iterator();
			while (ite.hasNext()) {
				FightBean temp = ite.next();
				if (temp.getTimesID() == timesID) {
					bean = temp;
					return bean;
				}
			}
		}

		if (bean == null) {
			bean = createFightInfo(id, timesID);
			userFightBeanList.add(bean);
			info.setChange(true);

		}
		info.setTime(System.currentTimeMillis());
		return bean;
	}

	public static void initPVEFightBean(long id) {
		FightBean fightBean = createFightInfo(id, 1);
		UserFightInfo info = new UserFightInfo();
		info.setId(id);
		List<FightBean> list = new LinkedList<FightBean>();
		list.add(fightBean);
		info.setFightInfoList(list);
		info.encode();
		info.setChange(true);
		save(id, info);
	}

	private static TimesBean fitGoldMonsterTimes(int cityLevel, TimesBean bean) {
		TimesBean re = bean.clone();
		if (bean != null && bean.getId() < 10000) {
			CityBean cityBean = (CityBean) Goods.getById(GoodsCate.CITYBEAN, cityLevel);
			double rate = cityBean.getGoldMonsterRate();
			double tempRate = ToolUtil.getRandom().nextDouble();
			if (tempRate <= rate && cityBean != null && bean != null && cityBean.getLastTimes() != bean.getId()) {
				List<Integer> goldMonster = Goods.getPveGoldMonster(cityBean.getLevel());
				if (goldMonster != null) {
					int i = ToolUtil.getRandom().nextInt(goldMonster.size());
					TimesBean goldMonsterTimes = (TimesBean) Goods.getById(GoodsCate.TIMESBEAN, goldMonster.get(i));
					if (goldMonsterTimes != null) {
						re.setMonsterGoodID(goldMonsterTimes.getMonsterGoodID());
						re.setMonsterNum(goldMonsterTimes.getMonsterNum());
						re.setBossGoodID(goldMonsterTimes.getBossGoodID());
						re.setBossNum(goldMonsterTimes.getBossNum());
						re.setDropGoods(goldMonsterTimes.getDropGoods());
					}
				}
			}
		}
		return re;
	}

	private static FightBean createFightInfo(long id, int times) {
		long time = System.currentTimeMillis();
		FightBean fightInfo = new FightBean();
		try {
			TimesBean bean = (TimesBean) Goods.getById(GoodsCate.TIMESBEAN, times);
			if (bean != null) {
				UserCity city = UserMemory.getCurrentCity(id);
				UserCastle castle = UserMemory.getCastle(id);
				// 填充黄金怪
				if (city.getCityID() == castle.getMaxCity()) {
					bean = fitGoldMonsterTimes(city.getCityID(), bean);
				}
				int monsterAmount = bean.getMonsterNum() + bean.getBossNum();
				List<UserSceneGoods> goodsList = city.getGoods();
				int count = goodsList.size();
				List<AttackMonster> monsters = new LinkedList<AttackMonster>();
				List<AttackMonster> bosses = new LinkedList<AttackMonster>();
				List<DropGoodsBean> mustDropGoods = TimesBean.decodeDropGoods(bean.getDropGoods());
				for (int i = 0; i < monsterAmount; i++) {
					AttackMonster monster = new AttackMonster();
					monster.setId((TIMES_MONSTER_NUM * (times - 1) + (i + 1)) * 100);
					List<DropGoodsBean> dropGoods = new LinkedList<DropGoodsBean>();
					if (i == monsterAmount - 1) {
						dropGoods.addAll(mustDropGoods);
					}
					if (i < monsterAmount - bean.getBossNum()) {
						if (count <= MAX_SCENE_GOODS_NUM) {
							List<DropGoodsBean> tempDropGoodsList =
									createDropGoods(city.getCityID(), bean, false, castle.getRace());
							if (tempDropGoodsList != null && tempDropGoodsList.size() > 0) {
								dropGoods.addAll(tempDropGoodsList);
							}
							count++;
						}
						monster.setGoodID(bean.getMonsterGoodID());
						monster.setDropGoods(removeUnmustGoods(id, dropGoods));
						monsters.add(monster);
					} else {
						if (count <= MAX_SCENE_GOODS_NUM) {
							List<DropGoodsBean> tempDropGoodsList =
									createDropGoods(city.getCityID(), bean, true, castle.getRace());
							if (tempDropGoodsList != null && tempDropGoodsList.size() > 0) {
								dropGoods.addAll(tempDropGoodsList);
							}
							count++;
						}
						monster.setDropGoods(removeUnmustGoods(id, dropGoods));
						monster.setGoodID(bean.getBossGoodID());
						bosses.add(monster);
					}
				}
				fightInfo.setTimesID(times);
				fightInfo.setMonsters(monsters);
				fightInfo.setBosses(bosses);
			} else {
				log.error("the " + times + " times monster is not exist!");
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		long spend = System.currentTimeMillis() - time;
		if (spend > TDDispose.PERIOD) {
			log.info("create fight info spend time: " + spend);
		}
		return fightInfo;
	}

	/**
	 * 去掉不该掉落的物品
	 * 
	 * @param id
	 * @param bean
	 * @return
	 */
	public static List<DropGoodsBean> removeUnmustGoods(long id, List<DropGoodsBean> list) {

		/* 检查bean类型 */
		for (Iterator<DropGoodsBean> iter = list.iterator(); iter.hasNext();) {
			DropGoodsBean bean = iter.next();

			/* 判断是否图纸碎片 */
			if (Goods.getCate(bean.getGoodsID()) == 2) {
				List<UserStorage> storages = StorageOp.getHidenStorage(id);
				BlueprintPieceBean bpbean = (BlueprintPieceBean) Goods.getSingleByGoodID(GoodsCate.BLUEPRINTPIECEBEAN, bean.getGoodsID());

				/* 遍历包裹是否有这个碎片，如果有，则删除该掉落信息 */
				for (UserStorage storage : storages) {
					if (storage.getGoodID() == bean.getGoodsID() || storage.getGoodID() == bpbean.getMixerID()) {
						iter.remove();
						break;
					}
				}

			}

		}

		return list;
	}

	private static List<DropGoodsBean> createDropGoods(int cityLevel, TimesBean timesBean, boolean isBoss, int race) {
		List<DropGoodsBean> goodsID = new LinkedList<DropGoodsBean>();
		List<ExplosiveGoodsBean> explosiveGoodsList = ExplosiveGoodsBean.decode(timesBean.getExplorsiveGoods());
		try {
			if (isBoss) {
				int i = 0;
				while (i < BOSS_DROP_NUM) {
					DropGoodsBean bean = Goods.fitGoods(PrizeType.MONSTERDROP, cityLevel, explosiveGoodsList, race);
					if (bean != null) {
						goodsID.add(bean);
					}
					i++;
				}
			} else {
				DropGoodsBean bean = Goods.fitGoods(PrizeType.MONSTERDROP, cityLevel, explosiveGoodsList, race);
				if (bean != null) {
					goodsID.add(bean);
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return goodsID;
	}

	private static boolean canFight(long id, int timesID, boolean challenge) {
		UserCastle castle = UserMemory.getCastle(id);
		UserCity city = UserMemory.getCurrentCity(id);
		CityBean cityBean = (CityBean) Goods.getById(GoodsCate.CITYBEAN, city.getCityID());
		if (challenge) {
			if (/*castle.getHp() > LIMIT_CASTLE_HP*/true) {
				return true;
			}
		} else {
			if (/*castle.getHp() > LIMIT_CASTLE_HP*/true && city.getCurrTimesNum() == timesID && cityBean != null
					&& cityBean.getLastTimes() >= timesID) {
				return true;
			}
		}
		log.warn("can not fight!! CurrTimesNum: " + city.getCurrTimesNum() + "-----,cityBean:" + cityBean == null ? "null" : cityBean.getLastTimes());
		return false;
	}

	public static byte[] pve(long id, byte[] information) throws Exception {
		int timesID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		TimesBean timesBean = (TimesBean) Goods.getById(GoodsCate.TIMESBEAN, timesID);
		byte[] re = DataFactory.get(information, 10, 4);
		if (timesBean != null) {
			FightBean fightBean = getFightBean(id, timesID);
			List<AttackMonster> monsters = fightBean.getMonsters();
			List<AttackMonster> bosses = fightBean.getBosses();
			int monsterSize = 0;
			int bossesSize = 0;
			if (monsters != null) {
				monsterSize = monsters.size();
			}
			if (bosses != null) {
				bossesSize = bosses.size();
			}
			re = DataFactory.addByteArray(re, DataFactory.getbyte(monsterSize + bossesSize));

			if (monsters != null) {
				Iterator<AttackMonster> monsterIte = monsters.iterator();
				while (monsterIte.hasNext()) {
					AttackMonster monster = monsterIte.next();
					re = DataFactory.addByteArray(re, DataFactory.getbyte(monster.getId()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(monster.getGoodID()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(timesBean.getMonsterHp()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(timesBean.getMonsterGlory()));
					List<DropGoodsBean> goodsList = monster.getDropGoods();
					re = DataFactory.addByteArray(re, DataFactory.getbyte(goodsList.size()));
					if (goodsList.size() > 0) {
						Iterator<DropGoodsBean> goodsIte = goodsList.iterator();
						while (goodsIte.hasNext()) {
							DropGoodsBean goods = goodsIte.next();
							re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getGoodsID()));
							re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getNum()));
						}
					}
				}
			}

			if (bosses != null) {
				Iterator<AttackMonster> bossIte = bosses.iterator();
				while (bossIte.hasNext()) {
					AttackMonster boss = bossIte.next();
					re = DataFactory.addByteArray(re, DataFactory.getbyte(boss.getId()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(boss.getGoodID()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(timesBean.getBossHp()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(timesBean.getBossGlory()));
					List<DropGoodsBean> goodsList = boss.getDropGoods();
					re = DataFactory.addByteArray(re, DataFactory.getbyte(goodsList.size()));
					if (goodsList.size() > 0) {
						Iterator<DropGoodsBean> goodsIte = goodsList.iterator();
						while (goodsIte.hasNext()) {
							DropGoodsBean goods = goodsIte.next();
							re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getGoodsID()));
							re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getNum()));
						}
					}
				}
			}

			re = DataFactory.addByteArray(new byte[] { 0x00 }, re);
		} else {
			re = new byte[] { 0x01 };
		}
		return re;
	}

	public static byte[] startPve(long id, byte[] information) {
		byte[] re = new byte[] { 0x00 };
		Cmd req = Cmd.getInstance(information);
		int sp = req.readInt(10);

		if (MemcacheOp.getSecurityTime(id, 1))
			re = new byte[] { 0x01 };
		else {
			EnergyOp.revert(id);
			FinanceBean bean = new FinanceBean();
			bean.setId(id);
			bean.setEnergy(-sp);
			if (!financeImpl.charge(bean)) {
				re = new byte[] { 0x02 };
			} else {
				/* 存储玩家PVE保护 */
				MemcacheOp.setSecurityTime(id, System.currentTimeMillis(), 1, FightOp.SECURITY_TIME);
			}
		}

		return re;
	}

	public static boolean isLastTimes(long id, int timesID, UserCity city) {
		CityBean cityBean = (CityBean) Goods.getById(GoodsCate.CITYBEAN, city.getCityID());
		if (cityBean.getLastTimes() <= city.getCurrTimesNum()) {
			return true;
		}
		return false;
	}

	public static void enterNextCity(long id, UserCity city) {
		if (city.getCityID() > 0 && city.getCityID() < CopyOp.COPY_START_NUM) {
			CityOp.initUserCity(id, city.getCityID() + 1);
			UserCastle castle = UserMemory.getCastle(id);
			if (city.getCityID() == castle.getMaxCity()) {
				castle.setMaxCity(castle.getMaxCity() + 1);
				castle.setChange(true);
			}
		}
	}

	public static byte[] pveResult(long id, byte[] information) throws Exception {
		byte[] re = new byte[] { 0x01 };
		boolean challenge = false;

		/* 删除该玩家的PVE保护 */
		MemcacheOp.removeSecurityTime(id);

		if (DataFactory.get(information, 10, 1)[0] == 0) {
			challenge = true;
		}
		int timesID = DataFactory.getInt(DataFactory.get(information, 11, 4));
		int num = DataFactory.getInt(DataFactory.get(information, 15, 4));
		TimesBean timesBean = (TimesBean) Goods.getById(GoodsCate.TIMESBEAN, timesID);
		if (timesBean != null && canFight(id, timesID, challenge)) {
			List<Integer> monsterID = new LinkedList<Integer>();
			for (int i = 0; i < num; i++) {
				monsterID.add(DataFactory.getInt(DataFactory.get(information, 19 + i * 4, 4)));
			}
			FightBean fightBean = getFightBean(id, timesID);
			if (fightBean != null && timesBean != null) {
				UserFightInfo info = getUserFightInfo(id);
				ExpGloryOp.addGlory(id, fightBean, timesBean, monsterID);
				List<AttackMonster> monsters = fightBean.getMonsters();
				List<AttackMonster> bosses = fightBean.getBosses();
				UserCity city = UserMemory.getCurrentCity(id);
				int demage = cleanResult(id, timesBean.getMonsterGoodID(), monsterID, monsters, timesBean);
				demage += cleanResult(id, timesBean.getBossGoodID(), monsterID, bosses, timesBean);
				if (demage > 0) {
					demage = (int) (demage * Math.sqrt(3 * city.getCityID()));
					CastleOp.demage(id, -demage / 2);
				}
				if (!(monsters != null && monsters.size() > 0 || bosses != null && bosses.size() > 0)) {
					if (challenge) {
						TaskOp.doChallengeTask(id, timesID);
					} else {
						if (isLastTimes(id, timesID, city)) {
							enterNextCity(id, city);
							if (city.getCityID() < CopyOp.COPY_START_NUM)
								TaskOp.checkDoTask(id, city.getCityID(), 2, 1);
							else
								TaskOp.checkDoTask(id, city.getCityID(), 3, 2);
						}
						city.setCurrTimesNum(city.getCurrTimesNum() + 1);
						city.setChange(true);
						CopyOp.setCurTimesNum(id, city, city.getCurrTimesNum());

						if (timesBean.getId() == 1)
							TaskOp.doTask(id, 10002, 1);
						else if (timesBean.getId() == 2)
							TaskOp.doTask(id, 10016, 1);
						TaskOp.doTask(id, 40006, 1);
						TaskOp.doTask(id, 10024, 1);
						TaskOp.doTask(id, 10026, 1);
						TaskOp.doTask(id, 50001, 1);
					}
					info.getFightInfoList().remove(fightBean);
				}
				re = new byte[] { 0x00 };
				info.setChange(true);
			} else {
				log.error("result times error:" + timesID + "," + fightBean + "," + timesBean);
			}
		} else {
			UserCastle castle = UserMemory.getCastle(id);
			log.warn("Game_Warning:id:" + id + " can't fight, timesId: " + timesID + ", hp: " + castle.getHp());
		}
		return re;
	}

	private static int cleanResult(long id, int monsterGoods, List<Integer> monsterID, List<AttackMonster> monsters,
			TimesBean timesBean) throws Exception {
		int demage = 0;
		if (monsters != null) {
			Iterator<AttackMonster> monsterIte = monsters.iterator();
			while (monsterIte.hasNext()) {
				boolean isFight = true;
				AttackMonster monster = monsterIte.next();
				for (int j = 0; j < monsterID.size(); j++) {
					if (monster.getId() == monsterID.get(j)) {
						isFight = false;
						break;
					}
				}
				if (isFight) {
					if (monster.getDropGoods().size() > 0) {
						for (int i = 0; i < monster.getDropGoods().size(); i++) {
							DropGoodsBean bean = monster.getDropGoods().get(i);
							UserSceneGoods goods = new UserSceneGoods();
							goods.setId(monster.getId() + i);
							MonsterBean monsterBean =
									(MonsterBean) Goods.getSingleByGoodID(GoodsCate.MONSTERBEAN, monster.getGoodID());
							if (monsterBean.getType() == 3) {
								goods.setType(3);
							} else if (monsterBean.getType() == 1) {
								goods.setType(0);
							} else {
								goods.setType(1);
							}
							goods.setTime(System.currentTimeMillis());
							List<DropGoodsBean> temp = new LinkedList<DropGoodsBean>();
							temp.add(bean);
							goods.setDropGoods(temp);
							CityOp.dropSceneGoods(id, goods);
						}
					}
					monsterIte.remove();
				} else {
					MonsterBean monsterBean =
							(MonsterBean) Goods.getSingleByGoodID(GoodsCate.MONSTERBEAN, monsterGoods);
					demage += monsterBean.getAtk();
				}
			}
		}
		return demage;
	}

	public static boolean isForbid(long id, long[] forbid) {
		for (int i = 0; i < forbid.length; i++) {
			if (id == forbid[i]) {
				return false;
			}
		}
		return true;
	}

	public static byte[] getSinglePVPList(long id) throws Exception {
		byte[] re = null;
		UserMemory.createFriendMem(id);
		UserCastle fighterCastle = UserMemory.getCastle(id);
		if (fighterCastle != null) {
			CastleBean fighterCastleBean =
					(CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, fighterCastle.getCastleID());
			re = DataFactory.doubleToXiaoTouByte(id);
			re = DataFactory.addByteArray(re, DataFactory.getbyte(fighterCastle.getRace()));
			TitleBean titleBean = (TitleBean) Goods.getById(GoodsCate.TITLEBEAN, fighterCastle.getCurrTitle());
			if (titleBean == null) {
				re = DataFactory.addByteArray(re, DataFactory.getbyte(0));
			} else {
				re = DataFactory.addByteArray(re, DataFactory.getbyte(titleBean.getGoodID()));
			}
			re = DataFactory.addByteArray(re, DataFactory.getbyte(fighterCastleBean.getLevel()));

		}
		return re;
	}

	public static byte[] getPVPList(long id) throws Exception {
		byte[] re = DataFactory.getbyte(0);
		UserCastle castle = UserMemory.getCastle(id);
		CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
		int level = castleBean.getLevel();
		List<PVPListBean> pvpList = PVPListOp.getPVPList(id, level);
		if (pvpList != null) {
			int count = 0;
			Iterator<PVPListBean> ite = pvpList.iterator();
			while (ite.hasNext() && count <= 10) {
				PVPListBean bean = ite.next();
				/* 对象不为自己，可攻击，非保护 */
				if (bean != null && bean.getId() != id && PVPState(id, bean.getId())[0] == 0x00) {
					try {
						re = DataFactory.addByteArray(re, getSinglePVPList(bean.getId()));
					} catch (Exception e) {
						continue;
					}
					count++;
					if (count >= 10) {
						break;
					}
				}
			}
			DataFactory.replace(re, 0, DataFactory.getbyte(count));
		}
		return re;
	}

	private static boolean isFlipAway(PVPRecordBean record, int mark) {
		if (record != null) {
			List<Integer> marks = record.getLossGoods();
			if (marks != null) {
				Iterator<Integer> ite = marks.iterator();
				while (ite.hasNext()) {
					if (ite.next() == mark) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static byte[] pvp(long id, byte[] information) throws Exception {
		long defender = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));
		long time = DataFactory.doubleBytesToLong(DataFactory.get(information, 18, 8));
		boolean isRevenge = DataFactory.get(information, 26, 1)[0] == 0x00;
		UserMemory.createFriendMem(defender);
		byte[] re = new byte[] { 0x01 };
		if (id == defender) {
			return re;
		}

		int hp = PVPRecordOp.sysHp(defender);
		if (hp <= 0) {
			log.info("The hp identity leave out, defender(" + defender + ")'s hp is 0");
			return re;
		}
		/* 判断双方是否可进入PVP */
		if (MemcacheOp.getSecurityTime(id, 1) || MemcacheOp.getSecurityTime(defender, 0)) {
			return new byte[] { 0x03 };
		}

		if (EnergyOp.consume(id, 4)) {
			int num = DataFactory.getInt(DataFactory.get(information, 35, 4));
			int[] markIDs = new int[num];
			UserStorage[] tempMonsters = new UserStorage[num];
			for (int i = 0; i < num; i++) {
				int boxID = DataFactory.getInt(DataFactory.get(information, 39 + 8 * i, 4));
				markIDs[i] = DataFactory.getInt(DataFactory.get(information, 43 + 8 * i, 4));
				UserStorage storage = StorageOp.getBoxByIDAndMarkID(id, boxID, markIDs[i]);
				if (storage == null) {
					return new byte[] { 0x02 };
				} else {
					tempMonsters[i] = storage;
				}
			}
			/* 保存攻防方的对战保护 */
			MemcacheOp.setSecurityTime(id, System.currentTimeMillis(), 2, SECURITY_TIME);
			MemcacheOp.setSecurityTime(defender, System.currentTimeMillis(), 2, SECURITY_TIME);

			re = new byte[] { 0x00 };
			re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(defender));
			re = DataFactory.addByteArray(re, LoginOp.getCastle(defender));
			re = DataFactory.addByteArray(re, CityOp.getCity(defender, 0));
			/* 是否无论如何都将攻击方加入被攻击方的黑名单中 */
			if (/* !isRevenge */true) {
				EnemyOp.createEnemyRecord(defender, id);
			}
			/* 如果是好友帮忙复仇，则给被帮忙复仇的好友发送邮件 */
			if (isRevenge) {
				long firendID = DataFactory.doubleBytesToLong(DataFactory.get(information, 27, 8));
				if (firendID != 0l) {
					MessageOp.createMessage(id, firendID, defender, 0, 5, null, null, null, null);
				} else {
					/* 不是好友帮忙复仇，判定为自己复仇，完成任务 */
					TaskOp.doTask(id, 20004, 1);
				}
			} else {
				TaskOp.doTask(id, 20010, 1);
			}
			/* 记录日志 */
			TDDispose.statisticLog.collectPvpLog(id, time, 0, Arrays.asList(tempMonsters));
			for (int j = 0; j < num; j++) {
				StorageOp.removeGoods(tempMonsters[j], markIDs[j]);
			}
		}

		return re;
	}

	/**
	 * 获取PVP状态是否可被攻击
	 * 
	 * @param id
	 * @param information
	 * @return
	 */
	public static byte[] getPVPState(long id, byte[] information) {

		long defender = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));

		return PVPState(id, defender);
	}

	public static byte[] PVPState(long id, long defender) {
		int hp = PVPRecordOp.sysHp(defender);
		if (hp <= 0) {
			return new byte[] { 0x01 };
		}
		int castleLevel = CastleOp.getCastleLevel(defender);
		long protectTime = PVPListOp.getProtectTime(defender, castleLevel);
		long freashProtectTime = PVPListOp.getFreashProtectTime(defender);
		if (protectTime > System.currentTimeMillis() || freashProtectTime > System.currentTimeMillis()) {
			return new byte[] { 0x02 };
		}

		/* 如果玩家在对战保护期 */
		// if (MemcacheOp.getSecurityTime(id, 1)) {
		// return new byte[] { 0x03 };
		// }
		byte[] re = new byte[] { 0x00 };
		re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(defender));

		return re;
	}

	public static UserStorage flipStorage(long id) {
		try {
			List<UserStorage> storage = UserMemory.getStorages(id).getStorage();
			if (storage != null) {
				PVPRecordBean record = PVPRecordOp.getPVPRecord(id);
				Iterator<UserStorage> ite = storage.iterator();
				while (ite.hasNext()) {
					UserStorage bean = ite.next();
					IndexBean indexBean = (IndexBean) Goods.getById(GoodsCate.INDEXBEAN, bean.getGoodID());
					if (bean != null && bean.getMarkIDs() != null && indexBean != null && indexBean.isCanPrize()) {
						JSONArray ja = JSONArray.fromObject(bean.getMarkIDs());
						if (ja != null && ja.size() > 0) {
							int mark = ja.getInt(0);
							if (!isFlipAway(record, mark)) {
								return bean;
							}
						}
					} else {
						if (indexBean == null) {
							log.error("Game_Error:the goods:" + bean.getGoodID());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return null;
	}

	private static int robMaterial(long id, long defender, List<Integer> aliveMonsterGoodID, int type) {
		int loss = 0;
		FinanceBean financeBean = financeImpl.getFinance(defender);
		Iterator<Integer> ite = aliveMonsterGoodID.iterator();
		while (ite.hasNext()) {
			Integer goodID = ite.next();
			MonsterBean monsterBean = (MonsterBean) Goods.getSingleByGoodID(GoodsCate.MONSTERBEAN, goodID);
			if (type == 1) {
				loss += monsterBean.getCreateNeedRock() * monsterBean.getRobTimes();
				loss = loss > (int) (financeBean.getRock() * 0.2) ? (int) (financeBean.getRock() * 0.2) : loss;
			} else if (type == 2) {
				loss += monsterBean.getCreateNeedMetal() * monsterBean.getRobTimes();
				loss = loss > (int) (financeBean.getMetal() * 0.2) ? (int) (financeBean.getMetal() * 0.2) : loss;
			} else if (type == 3) {
				loss += monsterBean.getCreateNeedCrystal() * monsterBean.getRobTimes();
				loss = loss > (int) (financeBean.getCrystal() * 0.2) ? (int) (financeBean.getCrystal() * 0.2) : loss;
			} else if (type == 4) {
				loss += monsterBean.getCreateNeedCoin();
				loss = loss > (int) (financeBean.getCoin() * 0.2) ? (int) (financeBean.getCoin() * 0.2) : loss;
			}
		}
		return loss;
	}

	private static void typeMonster(int goodID, List<DropGoodsBean> lossMonster) {
		boolean isAdd = false;
		if (lossMonster == null) {
			lossMonster = new LinkedList<DropGoodsBean>();
		}
		if (lossMonster != null) {
			Iterator<DropGoodsBean> ite = lossMonster.iterator();
			while (ite.hasNext()) {
				DropGoodsBean monster = ite.next();
				if (monster.getGoodsID() == goodID) {
					monster.setNum(monster.getNum() + 1);
					isAdd = true;
					return;
				}
			}
			if (!isAdd) {
				DropGoodsBean monster = new DropGoodsBean();
				monster.setGoodsID(goodID);
				monster.setNum(1);
				lossMonster.add(monster);
			}
		}
	}

	public static byte[] pvpResult(long id, byte[] information, InstanceFactory ifactory) throws Exception {
		byte[] re = null;
		long defender = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));

		/* 清除攻防方的战斗保护 */
		MemcacheOp.removeSecurityTime(id);
		MemcacheOp.removeSecurityTime(defender);

		UserMemory.createFriendMem(defender);
		int lossHp = 0;// DataFactory.getInt(DataFactory.get(information, 18,
		// 4));
		long time = DataFactory.doubleBytesToLong(DataFactory.get(information, 22, 8));
		List<DropGoodsBean> lossGoodsList = new LinkedList<DropGoodsBean>();
		List<DropGoodsBean> usedPropList = new LinkedList<DropGoodsBean>();
		PVPRecordBean record = PVPRecordOp.getPVPRecord(defender);
		int lossMonsterNum = DataFactory.getInt(DataFactory.get(information, 30, 4));
		information = DataFactory.get(information, 34, information.length - 34);
		List<ThingBean> lossMonster = new LinkedList<ThingBean>();
		List<Integer> aliveMonsterGoodID = new LinkedList<Integer>();
		List<DropGoodsBean> lossMonsters = new LinkedList<DropGoodsBean>();
		List<UserStorage> storages = new LinkedList<UserStorage>();
		Integer[] monsterIDS = new Integer[lossMonsterNum];
		UserCastle castle = UserMemory.getCastle(id);
		CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
		for (int i = 0; i < lossMonsterNum; i++) {
			int boxID = DataFactory.getInt(DataFactory.get(information, 0 + 13 * i, 4));
			int markID = DataFactory.getInt(DataFactory.get(information, 4 + 13 * i, 4));
			int goodID = DataFactory.getInt(DataFactory.get(information, 8 + 13 * i, 4));
			// UserStorage storage = StorageOp.getBoxByID(id, boxID);
			// 将怪物ID加入待捕获列表
			monsterIDS[i] = Integer.valueOf(goodID);
			if (DataFactory.get(information, 12 + 13 * i, 1)[0] == 0x00) {
				lossMonster.add(new ThingBean(1, boxID, goodID, 1, JSONArray.fromObject("[" + markID + "]")));
				typeMonster(goodID, lossMonsters);
			} else {
				MonsterBean monsterBean = (MonsterBean) Goods.getSingleByGoodID(GoodsCate.MONSTERBEAN, goodID);
				if (monsterBean != null) {
					lossHp += monsterBean.getAtk();
				}
				int monsterCount = StorageOp.getMonsterCount(id);
				if(monsterCount + 1 <= castleBean.getMonsterVol()){
					StorageOp.storeGoods(id, goodID, 1);
					UserStorage s = new UserStorage();
					s.setGoodID(goodID);
					s.setNum(1);
					storages.add(s);
				}
				aliveMonsterGoodID.add(goodID);
			}
		}
		/* 保存日志 */
		TDDispose.statisticLog.collectPvpLog(id, time, 1, storages);

		record.setHp(record.getHp() - lossHp);
		information = DataFactory.get(information, lossMonsterNum * 13, information.length - lossMonsterNum * 13);
		byte result = information[0];
		int messageType = 2;
		if (result == 0x00) {
			PVPListOp.protect(defender);
			messageType = 1;
		}
		/* 怪物 捕获 */
		List<DropGoodsBean> rewards = null;
		if (result == 1) {
			rewards = pvpRewardMonster(defender, monsterIDS);
			if (rewards != null && rewards.size() > 0) {
				updateDefenderMonster(defender, rewards);
			}
		}
		int usedPropNum = DataFactory.getInt(DataFactory.get(information, 1, 4));
		information = DataFactory.get(information, 5, information.length - 5);
		for (int i = 0; i < usedPropNum; i++) {
			int goodsID = DataFactory.getInt(DataFactory.get(information, 4 * i, 4));
			// record.getUsedProps().add(goodsID);
			DropGoodsBean usedProp = new DropGoodsBean();
			usedProp.setGoodsID(goodsID);
			usedProp.setNum(1);
			usedPropList.add(usedProp);
		}

		int lossCoin = 0;
		int lossRock = 0;
		int lossMetal = 0;
		int lossCrystal = 0;
		List<DropGoodsBean> messageContent = new LinkedList<DropGoodsBean>();
		if (result != 1) {
			lossRock = robMaterial(id, defender, aliveMonsterGoodID, 1);
			lossMetal = robMaterial(id, defender, aliveMonsterGoodID, 2);
			lossCrystal = robMaterial(id, defender, aliveMonsterGoodID, 3);
			lossCoin = robMaterial(id, defender, aliveMonsterGoodID, 4);
			FinanceBean robFinance = new FinanceBean();
			robFinance.setId(id);
			robFinance.setRock(lossRock);
			robFinance.setMetal(lossMetal);
			robFinance.setCrystal(lossCrystal);
			robFinance.setCoin(lossCoin);
			financeImpl.consume(robFinance);
			record.setRock(record.getRock() - lossRock);
			record.setMetal(record.getMetal() - lossMetal);
			record.setCrystal(record.getCrystal() - lossCrystal);
			record.setCoin(record.getCoin() - lossCoin);
			messageContent.add(new DropGoodsBean(60001, lossRock));
			messageContent.add(new DropGoodsBean(60002, lossMetal));
			messageContent.add(new DropGoodsBean(60003, lossCrystal));
			messageContent.add(new DropGoodsBean(60006, lossCoin));
		}

		if (result == 0x00) {
			castle.setPvpWinCount(castle.getPvpWinCount() + 1);
			castle.setChange(true);

			List<ThingBean> lost = new LinkedList<ThingBean>();
			List<ThingBean> get = new LinkedList<ThingBean>();
			UserStorage storage = flipStorage(defender);
			if (storage != null) {
				JSONArray markJa = JSONArray.fromObject(storage.getMarkIDs());
				if (record.getLossGoods() == null) {
					record.setLossGoods(new LinkedList<Integer>());
				}
				record.getLossGoods().add(markJa.getInt(0));
				List<ThingBean> getThing = StorageOp.storeGoods(id, storage.getGoodID(), 1);
				if (getThing != null) {
					DropGoodsBean lossGoods = new DropGoodsBean();
					lossGoods.setGoodsID(storage.getGoodID());
					lossGoods.setNum(1);
					lossGoodsList.add(lossGoods);
				}
				lost.add(new ThingBean(1, storage.getBoxID(), storage.getGoodID(), 1, JSONArray.fromObject(storage
						.getMarkIDs())));
				get.addAll(getThing);
			}
			GameLog.createLog(id, 15, null, true, get, lossMonster, "pvp get");
			GameLog.createLog(defender, 16, null, true, null, lost, "pvp lost");
			TaskOp.doTask(id, 40012, 1);
			TaskOp.doTask(id, 40013, 1);
		}
		re = DataFactory.getbyte(lossCoin);
		re = DataFactory.addByteArray(re, DataFactory.getbyte(lossRock));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(lossMetal));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(lossCrystal));
		re = DataFactory.addByteArray(re, DataFactory.getbyte(lossGoodsList.size()));
		for (int i = 0; i < lossGoodsList.size(); i++) {
			re = DataFactory.addByteArray(re, DataFactory.getbyte(lossGoodsList.get(i).getGoodsID()));
		}
		messageContent.addAll(lossGoodsList);
		MessageOp.createMessage(id, defender, 0, castle.getRace(), messageType, messageContent, usedPropList,
				lossMonsters, rewards);
		PVPRecordOp.modifyPVPRecord(record);
		pvpResultNotify(ifactory, defender, result, lossHp);
		TaskOp.doTask(id, 50006, 1);
		return re;
	}

	/**
	 * 更新防守方怪物
	 * 
	 * @param denfender
	 *            防守方ID
	 * @param monsterList
	 *            怪物列表
	 * @return
	 */
	public static boolean updateDefenderMonster(long defender, List<DropGoodsBean> monsterList) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currdate = sdf.format(new Date());
		try {
			Iterator<DropGoodsBean> goodsIte = monsterList.iterator();
			UserReceiveGood receive = null;
			while (goodsIte.hasNext()) {
				DropGoodsBean goods = goodsIte.next();
				receive = new UserReceiveGood();
				receive.setMasterID(defender);
				receive.setCreateDate(currdate);
				receive.setGoodID(goods.getGoodsID());
				receive.setNum(goods.getNum());
				receive.setIsReceive(0);
				//0手动补偿 1 怪物捕获 2 送礼
				receive.setType(1);
				DBUtil.save(defender, receive);
			}
			return true;
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}

	/**
	 * 捕获攻方派出的怪物
	 * 
	 * @param defender
	 *            防守方
	 * @param monsters
	 *            攻方怪物ID数组
	 * @return 捕获怪物ID列表
	 */
	public static List<DropGoodsBean> pvpRewardMonster(long defender, Integer[] monsters) {
		try {
			List<DropGoodsBean> get = new LinkedList<DropGoodsBean>();
			List<Integer> upmonster = new ArrayList<Integer>();
			// 防守方灵魂塔等级
			int soulTowerLevel = UserMemory.getSoulTower(defender).getSoulTowerLevel();
			// 将小于玩家灵魂塔等级的怪物放入待捕获列表upmonster
			for (int i = 0; i < monsters.length; i++) {
				MonsterBean mb = (MonsterBean) Goods.getSingleByGoodID(GoodsCate.MONSTERBEAN, monsters[i]);
				if (mb != null && mb.getLevel() < soulTowerLevel) {
					upmonster.add(monsters[i]);
				}
			}
			if (upmonster.size() < 1) {
				log.info("==monster's level greater than defender's==");
				return null;
			}
			Random random = new Random();
			// 捕获到1只就不进行 捕获两只的概率判断
			boolean cached = false;
			// 只有一只怪，捕获一只
			if (monsters.length > 0) {
				if (random.nextInt(100) <= r1) {
					get.add(new DropGoodsBean(monsters[0], 1));
					cached = true;
				}
			}
			// 大于1只，
			if (!cached && monsters.length > 1) {
				if (random.nextInt(101) < r2) {
					Random index = new Random();
					int len = upmonster.size();
					get.add(new DropGoodsBean(upmonster.get(index.nextInt(len)), 1));
					get.add(new DropGoodsBean(upmonster.get(index.nextInt(len)), 1));
				}
			}
			// 判断是否超过玩家灵魂塔上限
			int monsterCount = StorageOp.getMonsterCount(defender);
			UserCastle castle = UserMemory.getCastle(defender);
			CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
			if (monsterCount + get.size() > castleBean.getMonsterVol()) {
				log.info("==catch monster failed,ID:" + defender + " out of limit==");
				return null;
			}
			// 输出日志
			Iterator<DropGoodsBean> iter = get.iterator();
			while (iter.hasNext()) {
				DropGoodsBean goods = iter.next();
				log.info("catch monsters :[" + goods.getGoodsID() + "," + goods.getNum() + "]");
			}
			return get;
		} catch (Exception e) {
			log.error(e, e);
		}
		return null;
	}

	public static void pvpResultNotify(InstanceFactory ifactory, long defender, byte result, int lossHp) {
		try {
			Instance role = ifactory.getInstaceByAttr(Attribute.UID, defender);
			if (role != null) {
				byte[] head = (byte[]) role.getAttribute(TDDispose.SESSION_DESTINATION);
				head[0] = 0x3c;
				byte[] information = new byte[] { 0x01 };
				if (result == 1) {
					information[0] = 0x00;
				}
				information = DataFactory.addByteArray(information, DataFactory.getbyte(lossHp));
				TDDispose.sendInformation(role, DataFactory.addByteArray(head, information));

			}
		} catch (IOException e) {
			log.error(e, e);
		}

	}

	public static void initPVPRecord(long id) {
		try {
			PVPRecordBean record = new PVPRecordBean();
			record.setId(id);
			record.setLossGoodsInfo(new JSONArray().toString());
			DBUtil.save(id, record);
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public static void offline(long id) {
		UserFightInfo bean = MEMORY.get(id);
		if (bean != null) {
			update(id, bean);
		}
		MEMORY.remove(id);
	}

	// pvn(npc)
	// 防守方为系统
	public static byte[] pvn(long id, byte[] information) throws Exception {
		// 怪物 长度
		int monsterlen = DataFactory.getInt(DataFactory.get(information, 10, 4));
		byte[] re = new byte[] { 0x01 };
		int hp = PVPRecordOp.sysHp(id);
		if (hp <= 0) {
			log.info("The hp identity leave out, attacker(" + id + ")'s hp is 0");
			return re;
		}

		if (EnergyOp.consume(id, 5)) {
			int[] markIDs = new int[monsterlen];
			UserStorage[] tempMonsters = new UserStorage[monsterlen];
			Long hps = 0L;
			for (int i = 0; i < monsterlen; i++) {
				int boxID = DataFactory.getInt(DataFactory.get(information, 14 + 8 * i, 4));
				markIDs[i] = DataFactory.getInt(DataFactory.get(information, 18 + 8 * i, 4));
				UserStorage storage = StorageOp.getBoxByIDAndMarkID(id, boxID, markIDs[i]);
				if (storage == null) {
					return re;
				} else {
					tempMonsters[i] = storage;
				}

				MonsterBean monster = (MonsterBean) Goods.getSingleByGoodID(GoodsCate.MONSTERBEAN, storage.getGoodID());
				hps += monster.getHp();
			}
			re = new byte[] { 0x00 };
			List<DropGoodsBean> award = getPvnAward(hps);
			// 检查背包容量
			if (!ChestOp.isStorageSizeEnough(id, award.size())) {
				return new byte[] { 0x02 };
			}

			if (award != null && award.size() > 0) {
				Iterator<DropGoodsBean> iterator = award.iterator();
				re = DataFactory.addByteArray(re, DataFactory.getbyte(award.size()));
				while (iterator.hasNext()) {
					DropGoodsBean goods = iterator.next();
					re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getGoodsID()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getNum()));
					// 更新到背包中
					StorageOp.storeGoods(id, goods.getGoodsID(), goods.getNum());
				}
			} else {
				re = DataFactory.addByteArray(re, DataFactory.getbyte(0));
			}

			for (int j = 0; j < monsterlen; j++) {
				StorageOp.removeGoods(tempMonsters[j], markIDs[j]);
			}
			/* 记录日志 */
			TDDispose.statisticLog.collectPvpLog(id, System.currentTimeMillis(), 2, Arrays.asList(tempMonsters));
		}

		return re;
	}

	private static List<DropGoodsBean> getPvnAward(Long val) {
		JSONArray jarray = JSONArray.fromObject(pvnAward);
		List<DropGoodsBean> list = new ArrayList<DropGoodsBean>();
		DropGoodsBean goods = null;
		for (int i = 0; i < jarray.size(); i++) {
			JSONArray subArray = jarray.getJSONArray(i);
			JSONArray between = subArray.getJSONArray(0);
			JSONArray award = subArray.getJSONArray(1);
			for (int j = 0; j < award.size(); j++) {
				JSONArray subAward = award.getJSONArray(j);
				Integer goodsID = subAward.getInt(0);
				Integer goodsNum = subAward.getInt(1);
				Long min = between.getLong(0);
				Long max = between.getLong(1);
				if (min <= val && max >= val) {
					goods = new DropGoodsBean(goodsID, goodsNum);
					list.add(goods);
				} else if (i == jarray.size() - 1 && max < val) {
					goods = new DropGoodsBean(goodsID, goodsNum);
					list.add(goods);
				}
			}
		}
		return list;
	}

	public static void main(String[] args) {
		System.out.println(getPvnAward(100000000L));
	}
}

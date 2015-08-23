package com.server.goods;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.cindy.run.util.ThreadPoolExecutorTimer;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.BlueprintBean;
import com.database.model.bean.BlueprintPieceBean;
import com.database.model.bean.CastleBean;
import com.database.model.bean.CityBean;
import com.database.model.bean.DropGoodsBean;
import com.database.model.bean.ExplosiveGoodsBean;
import com.database.model.bean.GoodsBean;
import com.database.model.bean.IndexBean;
import com.database.model.bean.MineBean;
import com.database.model.bean.MonsterBean;
import com.database.model.bean.PrizeBean;
import com.database.model.bean.PropBean;
import com.database.model.bean.Race;
import com.database.model.bean.SlaverBean;
import com.database.model.bean.SoulTowerBean;
import com.database.model.bean.StoneBean;
import com.database.model.bean.TaskBean;
import com.database.model.bean.TechnologyTowerBean;
import com.database.model.bean.TechnologyTreeBean;
import com.database.model.bean.TimesBean;
import com.database.model.bean.TitleBean;
import com.database.model.bean.TowerBean;
import com.database.model.bean.TreasureChestBean;
import com.server.util.Configuration;
import com.server.util.ToolUtil;

public class Goods {

	private static Log log = LogFactory.getLog(Goods.class);
	private static final int GOODS_REFRESH_TIME = Configuration.getGoodsRefreshTime();
	private static ConcurrentHashMap<Integer, GoodsBean> GOODSBEAN = new ConcurrentHashMap<Integer, GoodsBean>();
	private static ConcurrentHashMap<Integer, BlueprintBean> BLUEPRINTBEAN =
			new ConcurrentHashMap<Integer, BlueprintBean>();
	private static ConcurrentHashMap<Integer, BlueprintPieceBean> BLUEPRINTPIECEBEAN =
			new ConcurrentHashMap<Integer, BlueprintPieceBean>();
	private static ConcurrentHashMap<Integer, CastleBean> CASTLEBEAN = new ConcurrentHashMap<Integer, CastleBean>();
	private static ConcurrentHashMap<Integer, CityBean> CITYBEAN = new ConcurrentHashMap<Integer, CityBean>();
	private static ConcurrentHashMap<Integer, MonsterBean> MONSTERBEAN = new ConcurrentHashMap<Integer, MonsterBean>();
	private static ConcurrentHashMap<Integer, PropBean> PROPBEAN = new ConcurrentHashMap<Integer, PropBean>();
	private static ConcurrentHashMap<Integer, SlaverBean> SLAVERBEAN = new ConcurrentHashMap<Integer, SlaverBean>();
	private static ConcurrentHashMap<Integer, StoneBean> STONEBEAN = new ConcurrentHashMap<Integer, StoneBean>();
	private static ConcurrentHashMap<Integer, TowerBean> TOWERBEAN = new ConcurrentHashMap<Integer, TowerBean>();
	private static ConcurrentHashMap<Integer, TimesBean> TIMESBEAN = new ConcurrentHashMap<Integer, TimesBean>();
	private static ConcurrentHashMap<Integer, Race> RACE = new ConcurrentHashMap<Integer, Race>();
	private static ConcurrentHashMap<Integer, TitleBean> TITLEBEAN = new ConcurrentHashMap<Integer, TitleBean>();
	private static ConcurrentHashMap<Integer, PrizeBean> PRIZEBEAN = new ConcurrentHashMap<Integer, PrizeBean>();
	private static ConcurrentHashMap<Integer, TaskBean> TASKBEAN = new ConcurrentHashMap<Integer, TaskBean>();
	private static ConcurrentHashMap<Integer, IndexBean> INDEXBEAN = new ConcurrentHashMap<Integer, IndexBean>();
	private static ConcurrentHashMap<Integer, SoulTowerBean> SOULTOWERBEAN =
			new ConcurrentHashMap<Integer, SoulTowerBean>();
	private static ConcurrentHashMap<Integer, MineBean> MINEBEAN = new ConcurrentHashMap<Integer, MineBean>();
	private static ConcurrentHashMap<Integer, TechnologyTowerBean> TECTOWERBEAN =
			new ConcurrentHashMap<Integer, TechnologyTowerBean>();
	private static ConcurrentHashMap<Integer, TechnologyTreeBean> TECTREEBEAN =
			new ConcurrentHashMap<Integer, TechnologyTreeBean>();
	private static ConcurrentHashMap<Integer, TreasureChestBean> TREASURECHESTBEAN =
			new ConcurrentHashMap<Integer, TreasureChestBean>();
	private final ThreadPoolExecutorTimer TIMER = ThreadPoolExecutorTimer.getIntance();
	private static final Object[] PVE_GOLD_MONSTER_TIMES = new Object[20];
	private static final int TIME = Configuration.getTime();
	private static final Object LOCK = new Object();
	private static Goods instance = new Goods();

	public Goods() {
		TIMER.getPreciseTimer().scheduleAtFixedRate(new RefreshGood(), 100, GOODS_REFRESH_TIME, TimeUnit.MILLISECONDS);
	}

	public static Goods instance() {
		if (instance == null) {
			synchronized (LOCK) {
				if (instance == null) {
					instance = new Goods();
				}
			}
		}
		return instance;
	}

	private class RefreshGood implements Runnable {

		public void run() {
			try {
				init();
			} catch (Exception e) {
				log.error("Game_Error:game parameter value is wrong");
				log.error(e, e);
			}
		}
	}

	private static List getFromDB(Class clasz, String tableName) {
		Session session = null;
		try {
			session = HibernateUtil.getDefaultSession();
			SQLQuery query = session.createSQLQuery("select * from " + tableName);
			query.addEntity(clasz);
			List list = query.list();
			return list;
		} catch (Exception e) {
			log.error(e, e);
		} finally {
			HibernateUtil.closeSession(session);
		}
		return null;
	}

	public static void init() {
		try {
			List<GoodsBean> goodsList = getFromDB(GoodsBean.class, "goodsbean");
			if (goodsList != null) {
				ConcurrentHashMap<Integer, GoodsBean> temp = new ConcurrentHashMap<Integer, GoodsBean>();
				Iterator<GoodsBean> goodsIte = goodsList.iterator();
				while (goodsIte.hasNext()) {
					GoodsBean goods = goodsIte.next();
					temp.put(goods.getId(), goods);
				}
				GOODSBEAN = temp;
			}

			List<BlueprintBean> blueprintList = getFromDB(BlueprintBean.class, "blueprintbean");
			if (blueprintList != null) {
				ConcurrentHashMap<Integer, BlueprintBean> temp = new ConcurrentHashMap<Integer, BlueprintBean>();
				Iterator<BlueprintBean> blueprintIte = blueprintList.iterator();
				while (blueprintIte.hasNext()) {
					BlueprintBean bp = blueprintIte.next();
					temp.put(bp.getId(), bp);
				}
				BLUEPRINTBEAN = temp;
			}

			List<BlueprintPieceBean> bppList = getFromDB(BlueprintPieceBean.class, "blueprintpiecebean");
			if (bppList != null) {
				ConcurrentHashMap<Integer, BlueprintPieceBean> temp =
						new ConcurrentHashMap<Integer, BlueprintPieceBean>();
				Iterator<BlueprintPieceBean> bppIte = bppList.iterator();
				while (bppIte.hasNext()) {
					BlueprintPieceBean bpp = bppIte.next();
					temp.put(bpp.getId(), bpp);
				}
				BLUEPRINTPIECEBEAN = temp;
			}

			List<CastleBean> castleList = getFromDB(CastleBean.class, "castlebean");
			if (castleList != null) {
				ConcurrentHashMap<Integer, CastleBean> temp = new ConcurrentHashMap<Integer, CastleBean>();
				Iterator<CastleBean> castleIte = castleList.iterator();
				while (castleIte.hasNext()) {
					CastleBean castle = castleIte.next();
					temp.put(castle.getId(), castle);
				}
				CASTLEBEAN = temp;
			}

			List<CityBean> cityList = getFromDB(CityBean.class, "citybean");
			if (cityList != null) {
				ConcurrentHashMap<Integer, CityBean> temp = new ConcurrentHashMap<Integer, CityBean>();
				Iterator<CityBean> cityIte = cityList.iterator();
				while (cityIte.hasNext()) {
					CityBean city = cityIte.next();
					temp.put(city.getLevel(), city);
				}
				CITYBEAN = temp;
			}

			List<MonsterBean> monsterList = getFromDB(MonsterBean.class, "monsterbean");
			if (monsterList != null) {
				ConcurrentHashMap<Integer, MonsterBean> temp = new ConcurrentHashMap<Integer, MonsterBean>();
				Iterator<MonsterBean> monsterIte = monsterList.iterator();
				while (monsterIte.hasNext()) {
					MonsterBean monster = monsterIte.next();
					temp.put(monster.getId(), monster);
				}
				MONSTERBEAN = temp;
			}

			List<PropBean> propList = getFromDB(PropBean.class, "propbean");
			if (propList != null) {
				ConcurrentHashMap<Integer, PropBean> temp = new ConcurrentHashMap<Integer, PropBean>();
				Iterator<PropBean> propIte = propList.iterator();
				while (propIte.hasNext()) {
					PropBean prop = propIte.next();
					temp.put(prop.getId(), prop);
				}
				PROPBEAN = temp;
			}

			List<SlaverBean> slaverList = getFromDB(SlaverBean.class, "slaverbean");
			if (slaverList != null) {
				ConcurrentHashMap<Integer, SlaverBean> temp = new ConcurrentHashMap<Integer, SlaverBean>();
				Iterator<SlaverBean> slaverIte = slaverList.iterator();
				while (slaverIte.hasNext()) {
					SlaverBean slaver = slaverIte.next();
					temp.put(slaver.getId(), slaver);
				}
				SLAVERBEAN = temp;
			}

			List<StoneBean> stoneList = getFromDB(StoneBean.class, "stonebean");
			if (stoneList != null) {
				ConcurrentHashMap<Integer, StoneBean> temp = new ConcurrentHashMap<Integer, StoneBean>();
				Iterator<StoneBean> stoneIte = stoneList.iterator();
				while (stoneIte.hasNext()) {
					StoneBean stone = stoneIte.next();
					temp.put(stone.getId(), stone);
				}
				STONEBEAN = temp;
			}

			List<TowerBean> towerList = getFromDB(TowerBean.class, "towerbean");
			if (towerList != null) {
				ConcurrentHashMap<Integer, TowerBean> temp = new ConcurrentHashMap<Integer, TowerBean>();
				Iterator<TowerBean> towerIte = towerList.iterator();
				while (towerIte.hasNext()) {
					TowerBean tower = towerIte.next();
					temp.put(tower.getId(), tower);
				}
				TOWERBEAN = temp;
			}

			List<TimesBean> timesList = getFromDB(TimesBean.class, "timesbean");
			if (timesList != null) {
				ConcurrentHashMap<Integer, TimesBean> tempTimesBean = new ConcurrentHashMap<Integer, TimesBean>();
				Iterator<TimesBean> timesIte = timesList.iterator();
				while (timesIte.hasNext()) {
					TimesBean times = timesIte.next();
					if (times.getId() < 0) {
						List<Integer> temp = (List<Integer>) PVE_GOLD_MONSTER_TIMES[times.getCityID() - 1];
						if (temp == null) {
							temp = new ArrayList<Integer>();
							PVE_GOLD_MONSTER_TIMES[times.getCityID() - 1] = temp;
						}
						if (!temp.contains(times.getId())) {
							temp.add(times.getId());
						}
					}
					tempTimesBean.put(times.getId(), times);
				}
				TIMESBEAN = tempTimesBean;
			}

			List<Race> raceList = getFromDB(Race.class, "race");
			if (raceList != null) {
				ConcurrentHashMap<Integer, Race> temp = new ConcurrentHashMap<Integer, Race>();
				Iterator<Race> raceIte = raceList.iterator();
				while (raceIte.hasNext()) {
					Race race = raceIte.next();
					temp.put(race.getId(), race);
				}
				RACE = temp;
			}

			List<TitleBean> titleList = getFromDB(TitleBean.class, "titlebean");
			if (titleList != null) {
				ConcurrentHashMap<Integer, TitleBean> temp = new ConcurrentHashMap<Integer, TitleBean>();
				Iterator<TitleBean> titleIte = titleList.iterator();
				while (titleIte.hasNext()) {
					TitleBean title = titleIte.next();
					temp.put(title.getId(), title);
				}
				TITLEBEAN = temp;
			}

			List<PrizeBean> prizeList = getFromDB(PrizeBean.class, "prizebean");
			if (prizeList != null) {
				ConcurrentHashMap<Integer, PrizeBean> temp = new ConcurrentHashMap<Integer, PrizeBean>();
				Iterator<PrizeBean> prizeIte = prizeList.iterator();
				while (prizeIte.hasNext()) {
					PrizeBean prize = prizeIte.next();
					temp.put(prize.getId(), prize);
				}
				PRIZEBEAN = temp;
			}

			List<TaskBean> taskList = getFromDB(TaskBean.class, "taskbean");
			if (taskList != null) {
				ConcurrentHashMap<Integer, TaskBean> temp = new ConcurrentHashMap<Integer, TaskBean>();
				Iterator<TaskBean> taskIte = taskList.iterator();
				while (taskIte.hasNext()) {
					TaskBean task = taskIte.next();
					temp.put(task.getId(), task);
				}
				TASKBEAN = temp;
			}

			List<IndexBean> indexList = getFromDB(IndexBean.class, "indexbean");
			if (indexList != null) {
				ConcurrentHashMap<Integer, IndexBean> temp = new ConcurrentHashMap<Integer, IndexBean>();
				Iterator<IndexBean> indexIte = indexList.iterator();
				while (indexIte.hasNext()) {
					IndexBean index = indexIte.next();
					temp.put(index.getIndexID(), index);
				}
				INDEXBEAN = temp;
			}

			List<SoulTowerBean> soulTowerList = getFromDB(SoulTowerBean.class, "soultowerbean");
			if (soulTowerList != null) {
				ConcurrentHashMap<Integer, SoulTowerBean> temp = new ConcurrentHashMap<Integer, SoulTowerBean>();
				Iterator<SoulTowerBean> soulTowerIte = soulTowerList.iterator();
				while (soulTowerIte.hasNext()) {
					SoulTowerBean soulTower = soulTowerIte.next();
					temp.put(soulTower.getLevel(), soulTower);
				}
				SOULTOWERBEAN = temp;
			}

			List<MineBean> mineList = getFromDB(MineBean.class, "minebean");
			if (mineList != null) {
				ConcurrentHashMap<Integer, MineBean> temp = new ConcurrentHashMap<Integer, MineBean>();
				Iterator<MineBean> mineIte = mineList.iterator();
				while (mineIte.hasNext()) {
					MineBean mine = mineIte.next();
					temp.put(mine.getId(), mine);
				}
				MINEBEAN = temp;
			}

			List<TechnologyTowerBean> tecTowerList = getFromDB(TechnologyTowerBean.class, "technologytowerbean");
			if (tecTowerList != null) {
				ConcurrentHashMap<Integer, TechnologyTowerBean> temp =
						new ConcurrentHashMap<Integer, TechnologyTowerBean>();
				Iterator<TechnologyTowerBean> tecTowerIte = tecTowerList.iterator();
				while (tecTowerIte.hasNext()) {
					TechnologyTowerBean tecTower = tecTowerIte.next();
					temp.put(tecTower.getLevel(), tecTower);
				}
				TECTOWERBEAN = temp;
			}

			List<TechnologyTreeBean> tecTreeList = getFromDB(TechnologyTreeBean.class, "technologytreebean");
			if (tecTreeList != null) {
				ConcurrentHashMap<Integer, TechnologyTreeBean> temp =
						new ConcurrentHashMap<Integer, TechnologyTreeBean>();
				Iterator<TechnologyTreeBean> tecTreeIte = tecTreeList.iterator();
				while (tecTreeIte.hasNext()) {
					TechnologyTreeBean tecTree = tecTreeIte.next();
					temp.put(tecTree.getId(), tecTree);
				}
				TECTREEBEAN = temp;
			}

			List<TreasureChestBean> treasureChestBoxList = getFromDB(TreasureChestBean.class, "treasurechestbean");
			if (treasureChestBoxList != null) {
				ConcurrentHashMap<Integer, TreasureChestBean> temp =
						new ConcurrentHashMap<Integer, TreasureChestBean>();
				Iterator<TreasureChestBean> treasureChestIte = treasureChestBoxList.iterator();
				while (treasureChestIte.hasNext()) {
					TreasureChestBean treasureChest = treasureChestIte.next();
					temp.put(treasureChest.getId(), treasureChest);
				}
				TREASURECHESTBEAN = temp;
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	// 0商品 1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 10道具
	public static Object getById(GoodsCate cate, int id) {
		if (cate == GoodsCate.GOODSBEAN) {
			return GOODSBEAN.get(id);
		} else if (cate == GoodsCate.STONEBEAN) {
			return STONEBEAN.get(id);
		} else if (cate == GoodsCate.BLUEPRINTPIECEBEAN) {
			return BLUEPRINTPIECEBEAN.get(id);
		} else if (cate == GoodsCate.BLUEPRINTBEAN) {
			return BLUEPRINTBEAN.get(id);
		} else if (cate == GoodsCate.TOWERBEAN) {
			return TOWERBEAN.get(id);
		} else if (cate == GoodsCate.MONSTERBEAN) {
			return MONSTERBEAN.get(id);
		}/*
		 * else if(cate == 6){ return STONEBEAN.get(id); }
		 */else if (cate == GoodsCate.SLAVERBEAN) {
			return SLAVERBEAN.get(id);
		} else if (cate == GoodsCate.CASTLEBEAN) {
			return CASTLEBEAN.get(id);
		} else if (cate == GoodsCate.CITYBEAN) {
			return CITYBEAN.get(id);
		} else if (cate == GoodsCate.PROPBEAN) {
			return PROPBEAN.get(id);
		} else if (cate == GoodsCate.TIMESBEAN) {
			return TIMESBEAN.get(id);
		} else if (cate == GoodsCate.RACE) {
			return RACE.get(id);
		} else if (cate == GoodsCate.TITLEBEAN) {
			return TITLEBEAN.get(id);
		} else if (cate == GoodsCate.PRIZEBEAN) {
			return PRIZEBEAN.get(id);
		} else if (cate == GoodsCate.TASKBEAN) {
			return TASKBEAN.get(id);
		} else if (cate == GoodsCate.INDEXBEAN) {
			return INDEXBEAN.get(id);
		} else if (cate == GoodsCate.SOULTOWERBEAN) {
			return SOULTOWERBEAN.get(id);
		} else if (cate == GoodsCate.MINEBEAN) {
			return MINEBEAN.get(id);
		} else if (cate == GoodsCate.TECHNOLOGYTOWERBEAN) {
			return TECTOWERBEAN.get(id);
		} else if (cate == GoodsCate.TECHNOLOGYTREEBEAN) {
			return TECTREEBEAN.get(id);
		} else if (cate == GoodsCate.TREASURECHESTBEAN) {
			return TREASURECHESTBEAN.get(id);
		} else {
			return null;
		}
	}

	public static List getGoodsByCate(GoodsCate cate) {
		List<Object> list = new LinkedList<Object>();
		if (cate == GoodsCate.GOODSBEAN) {
			Iterator<Integer> ite = GOODSBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				list.add(GOODSBEAN.get(i));
			}
		} else if (cate == GoodsCate.STONEBEAN) {
			Iterator<Integer> ite = STONEBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				list.add(STONEBEAN.get(i));
			}
		} else if (cate == GoodsCate.BLUEPRINTPIECEBEAN) {
			Iterator<Integer> ite = BLUEPRINTPIECEBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				list.add(BLUEPRINTPIECEBEAN.get(i));
			}
		} else if (cate == GoodsCate.BLUEPRINTBEAN) {
			Iterator<Integer> ite = BLUEPRINTBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				list.add(BLUEPRINTBEAN.get(i));
			}
		} else if (cate == GoodsCate.TOWERBEAN) {
			Iterator<Integer> ite = TOWERBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				list.add(TOWERBEAN.get(i));
			}
		} else if (cate == GoodsCate.MONSTERBEAN) {
			Iterator<Integer> ite = MONSTERBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				list.add(MONSTERBEAN.get(i));
			}
		} else if (cate == GoodsCate.SLAVERBEAN) {
			Iterator<Integer> ite = SLAVERBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				list.add(SLAVERBEAN.get(i));
			}
		} else if (cate == GoodsCate.CASTLEBEAN) {
			Iterator<Integer> ite = CASTLEBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				list.add(CASTLEBEAN.get(i));
			}
		} else if (cate == GoodsCate.CITYBEAN) {
			Iterator<Integer> ite = CITYBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				list.add(CITYBEAN.get(i));
			}
		} else if (cate == GoodsCate.PROPBEAN) {
			Iterator<Integer> ite = PROPBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				list.add(PROPBEAN.get(i));
			}
		} else if (cate == GoodsCate.PRIZEBEAN) {
			Iterator<Integer> ite = PRIZEBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				list.add(PRIZEBEAN.get(i));
			}
		} else if (cate == GoodsCate.TASKBEAN) {
			Iterator<Integer> ite = TASKBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				list.add(TASKBEAN.get(i));
			}
		}
		return list;
	}

	// 0商品 1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 1GoodsCate.GOODSBEAN道具
	public static Object getSingleByGoodID(GoodsCate cate, int goodID) {
		if (cate == GoodsCate.GOODSBEAN) {
			Iterator<Integer> ite = GOODSBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (GOODSBEAN.get(i).getId() == goodID) {
					return GOODSBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.STONEBEAN) {
			Iterator<Integer> ite = STONEBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (STONEBEAN.get(i).getGoodID() == goodID) {
					return STONEBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.BLUEPRINTPIECEBEAN) {
			Iterator<Integer> ite = BLUEPRINTPIECEBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (BLUEPRINTPIECEBEAN.get(i).getGoodID() == goodID) {
					return BLUEPRINTPIECEBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.BLUEPRINTBEAN) {
			Iterator<Integer> ite = BLUEPRINTBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (BLUEPRINTBEAN.get(i).getGoodID() == goodID) {
					return BLUEPRINTBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.TOWERBEAN) {
			Iterator<Integer> ite = TOWERBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (TOWERBEAN.get(i).getGoodID() == goodID) {
					return TOWERBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.MONSTERBEAN) {
			Iterator<Integer> ite = MONSTERBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (MONSTERBEAN.get(i).getGoodID() == goodID) {
					return MONSTERBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.SLAVERBEAN) {
			Iterator<Integer> ite = SLAVERBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (SLAVERBEAN.get(i).getGoodID() == goodID) {
					return SLAVERBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.CASTLEBEAN) {
			Iterator<Integer> ite = CASTLEBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (CASTLEBEAN.get(i).getGoodID() == goodID) {
					return CASTLEBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.CITYBEAN) {
			Iterator<Integer> ite = CITYBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (CITYBEAN.get(i).getGoodID() == goodID) {
					return CITYBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.PROPBEAN) {
			Iterator<Integer> ite = PROPBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (PROPBEAN.get(i).getGoodID() == goodID) {
					return PROPBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.TITLEBEAN) {
			Iterator<Integer> ite = TITLEBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (TITLEBEAN.get(i).getGoodID() == goodID) {
					return TITLEBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.TREASURECHESTBEAN) { // 获取宝箱
			Iterator<Integer> ite = TREASURECHESTBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (TREASURECHESTBEAN.get(i).getGoodID() == goodID) {
					return TREASURECHESTBEAN.get(i);
				}
			}
		}
		return null;
	}

	// 0商品 1宝石 2碎片 3图纸 4炮塔 5怪物 6材料 7奴隶 8城堡 9关 10道具
	public static Object getByGoodIDAndLevel(GoodsCate cate, int goodID, int level) {
		if (cate == GoodsCate.GOODSBEAN) {
			Iterator<Integer> ite = GOODSBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (GOODSBEAN.get(i).getId() == goodID && GOODSBEAN.get(i).getLevel() == level) {
					return GOODSBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.BLUEPRINTBEAN) {
			Iterator<Integer> ite = BLUEPRINTBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (BLUEPRINTBEAN.get(i).getGoodID() == goodID && BLUEPRINTBEAN.get(i).getLevel() == level) {
					return BLUEPRINTBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.TOWERBEAN) {
			Iterator<Integer> ite = TOWERBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (TOWERBEAN.get(i).getGoodID() == goodID && TOWERBEAN.get(i).getSceneLevel() == level) {
					return TOWERBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.MONSTERBEAN) {
			Iterator<Integer> ite = MONSTERBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (MONSTERBEAN.get(i).getGoodID() == goodID && MONSTERBEAN.get(i).getLevel() == level) {
					return MONSTERBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.SLAVERBEAN) {
			Iterator<Integer> ite = SLAVERBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (SLAVERBEAN.get(i).getGoodID() == goodID && SLAVERBEAN.get(i).getLevel() == level) {
					return SLAVERBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.CASTLEBEAN) {
			Iterator<Integer> ite = CASTLEBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (CASTLEBEAN.get(i).getGoodID() == goodID && CASTLEBEAN.get(i).getLevel() == level) {
					return CASTLEBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.CITYBEAN) {
			Iterator<Integer> ite = CITYBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (CITYBEAN.get(i).getGoodID() == goodID && CITYBEAN.get(i).getLevel() == level) {
					return CITYBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.SOULTOWERBEAN) {
			Iterator<Integer> ite = SOULTOWERBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (SOULTOWERBEAN.get(i).getGoodID() == goodID && SOULTOWERBEAN.get(i).getLevel() == level) {
					return SOULTOWERBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.MINEBEAN) {
			Iterator<Integer> ite = MINEBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (MINEBEAN.get(i).getGoodID() == goodID && MINEBEAN.get(i).getLevel() == level) {
					return MINEBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.TECHNOLOGYTOWERBEAN) {
			Iterator<Integer> ite = TECTOWERBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (TECTOWERBEAN.get(i).getGoodID() == goodID && TECTOWERBEAN.get(i).getLevel() == level) {
					return TECTOWERBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.TECHNOLOGYTREEBEAN) {
			Iterator<Integer> ite = TECTREEBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (TECTREEBEAN.get(i).getGoodID() == goodID && TECTREEBEAN.get(i).getLevel() == level) {
					return TECTREEBEAN.get(i);
				}
			}
		} else if (cate == GoodsCate.TREASURECHESTBEAN) {
			Iterator<Integer> ite = TREASURECHESTBEAN.keySet().iterator();
			while (ite.hasNext()) {
				int i = ite.next();
				if (TREASURECHESTBEAN.get(i).getGoodID() == goodID && TREASURECHESTBEAN.get(i).getLevel() == level) {
					return TREASURECHESTBEAN.get(i);
				}
			}
		}
		return null;
	}

	public static int getCate(int goodID) {
		int cate = goodID / 10000;
		return cate;
	}

	public static double getExplorsiveRate(List<ExplosiveGoodsBean> explorsiveGoods, int goodsID) {
		double rate = 0;
		if (explorsiveGoods != null) {
			Iterator<ExplosiveGoodsBean> ite = explorsiveGoods.iterator();
			while (ite.hasNext()) {
				ExplosiveGoodsBean bean = ite.next();
				if (bean.getGoods() == goodsID) {
					rate = bean.getRate();
				}
			}
		}
		return rate;
	}

	public static boolean canDrop(int goodID, int race) {
		try {
			int cate = getCate(goodID);
			if (cate == 2) {
				BlueprintPieceBean bpb = (BlueprintPieceBean) getSingleByGoodID(GoodsCate.BLUEPRINTPIECEBEAN, goodID);
				if (bpb != null && bpb.getRace() != race) {
					return false;
				}
			} else if (cate == 3) {
				BlueprintBean bb = (BlueprintBean) getSingleByGoodID(GoodsCate.BLUEPRINTBEAN, goodID);
				if (bb != null && bb.getRace() != race) {
					return false;
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return true;
	}

	public static DropGoodsBean fitGoods(PrizeType prizeType, int goodsLevel, List<ExplosiveGoodsBean> explorsiveGoods,
			int race) {
		double rate = ToolUtil.getRandom().nextDouble();
		DropGoodsBean goods = new DropGoodsBean();
		Iterator<Integer> ite = PRIZEBEAN.keySet().iterator();
		double countRate = 0d;
		while (ite.hasNext()) {
			int id = ite.next();
			PrizeBean bean = PRIZEBEAN.get(id);
			int goodsID = bean.getGoodID();
			if (canDrop(goodsID, race)) {
				double explorsiveRate = getExplorsiveRate(explorsiveGoods, goodsID);

				if (bean.getCityLevel() == goodsLevel) {
					if (prizeType == PrizeType.MONSTERDROP) {
						countRate += bean.getDropRate();
					} else if (prizeType == PrizeType.CITYPRIZE) {
						countRate += bean.getCityPrizeRate();
					} else if (prizeType == PrizeType.TASKPRIZE) {
						countRate += bean.getTaskPrizeRate();
					} else if (prizeType == PrizeType.SYSTEMCREATE) {
						countRate += bean.getSystemCreateRate();
					}
				}

				if (rate < countRate + explorsiveRate) {
					goods.setGoodsID(goodsID);
					goods.setNum(bean.getPrizeNum());
					return goods;
				}

				if (countRate > 1) {
					log.error("Game_Error:the amount of the flip card rate is > 1");
					goods.setGoodsID(goodsID);
					goods.setNum(bean.getPrizeNum());
					return goods;
				}
			}
		}
		return null;
	}

	public static boolean isCoin(int goodsID) {
		if (goodsID == 60006) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isRock(int goodsID) {
		if (goodsID == 60001) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isMetal(int goodsID) {
		if (goodsID == 60002) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isCrystal(int goodsID) {
		if (goodsID == 60003) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSoul(int goodsID) {
		if (goodsID == 60004) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isCash(int goodsID) {
		if (goodsID == 60007) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSystemCash(int goodsID) {
		if (goodsID == 60008) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isBadge(int goodsID) {
		if (goodsID == 60009) {
			return true;
		} else {
			return false;
		}
	}

	public static List<Integer> getPveGoldMonster(int cityID) {
		List<Integer> re = null;
		re = (List<Integer>) PVE_GOLD_MONSTER_TIMES[cityID - 1];
		return re;
	}
}

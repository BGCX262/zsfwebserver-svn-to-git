package com.server.user.operation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;

import com.cindy.run.util.ThreadPoolExecutorTimer;
import com.database.model.bean.CastleBean;
import com.database.model.bean.LoginRecord;
import com.database.model.bean.MemcachePVPListBean;
import com.database.model.bean.PVPListBean;
import com.database.model.bean.UserCastle;
import com.server.cache.UserMemory;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class PVPListOp {

	private static Log log = LogFactory.getLog(PVPListOp.class);
	private static final ThreadPoolExecutorTimer TIMER = ThreadPoolExecutorTimer.getIntance();
	private static HashMap<Integer, ConcurrentHashMap<Long, PVPListBean>> pvpListMap =
			new HashMap<Integer, ConcurrentHashMap<Long, PVPListBean>>();
	private static final Random RANDOM = new Random();
	// private static final int TIME = Configuration.getTime();
	private static PVPListOp instance = new PVPListOp();
	private static final Object LOCK = new Object();
	public static final int PVP_LEVEL = Configuration.getPvpLevel();
	public static final int PVP_PROTECT_TIME = Configuration.getUserProtectTime();
	public static final int FRESH_USER_PVP_PROTECT_TIME = Configuration.getFreshUserProtectTime();
	private static final boolean IS_PVP_PROTECT = Configuration.isPvpProtect();
	private static final int UP_OFFSET = 3;
	private static final int LOW_OFFSET = 3;

	public PVPListOp() {
		TIMER.getPreciseTimer().scheduleAtFixedRate(new RefreshPVPList(), 100, 10 * 60000, TimeUnit.MILLISECONDS);
	}

	public static PVPListOp instance() {
		if (instance == null) {
			synchronized (LOCK) {
				if (instance == null) {
					instance = new PVPListOp();
				}
			}
		}
		return instance;
	}

	private class RefreshPVPList implements Runnable {

		@Override
		public void run() {
			initPVPList();
			// printPvpList();
		}
	}

	public static void printPvpList() {
		Iterator<Integer> ite = pvpListMap.keySet().iterator();
		while (ite.hasNext()) {
			int level = ite.next();
			ConcurrentHashMap map = pvpListMap.get(level);
			String str = "level:" + level + ":  ";
			Iterator<Long> iter = map.keySet().iterator();
			while (iter.hasNext()) {
				str += iter.next() + ", ";
			}
			log.info(str);
		}
	}

	private static void initPVPList() {
		try {
			HashMap<Integer, ConcurrentHashMap<Long, PVPListBean>> temp =
					new HashMap<Integer, ConcurrentHashMap<Long, PVPListBean>>();
			Set<String> keySet = MemcacheOp.getMemcacheKeySet(false);
			if (keySet != null) {
				Iterator<String> ite = keySet.iterator();
				while (ite.hasNext()) {
					String key = ite.next();
					sortPVPList(temp, key);
				}
				pvpListMap.clear();
				pvpListMap = temp;
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public static void sortPVPList(HashMap<Integer, ConcurrentHashMap<Long, PVPListBean>> temp, String key) {
		try {
			if (key.matches("level\\d+\\:\\d+")) {
				Object obj = MemcacheOp.get(key);
				if (obj != null) {
					PVPListBean bean = (PVPListBean) obj;
					int level = Integer.valueOf(key.substring(5, key.indexOf(":")));
					ConcurrentHashMap<Long, PVPListBean> pvpList = temp.get(level);
					if (pvpList == null) {
						pvpList = new ConcurrentHashMap<Long, PVPListBean>();
						temp.put(level, pvpList);
					}
					if (pvpList.size() <= 15000 || (pvpList.size() > 15000 && RANDOM.nextInt(100) > 80)) {
						if (bean.getProtectTime() < System.currentTimeMillis()) {
							pvpList.put(bean.getId(), bean);
						}
					}
				}
			}

		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public static List<PVPListBean> getPVPList(long id, int level) {
		List<PVPListBean> list = new LinkedList<PVPListBean>();
		try {
			ConcurrentHashMap<Long, PVPListBean> pvpList = new ConcurrentHashMap<Long, PVPListBean>();
			try {
				for (int i = Math.max(0, level - LOW_OFFSET); i <= level + UP_OFFSET; i++) {
					ConcurrentHashMap<Long, PVPListBean> map = pvpListMap.get(i);
					if (map != null)
						pvpList.putAll(map);
				}
			} catch (Exception e) {
				log.error(e, e);
			}
			if (pvpList.size() <= 0) {
				initPVPList();
				for (int i = Math.max(0, level - LOW_OFFSET); i <= level + UP_OFFSET; i++) {
					ConcurrentHashMap<Long, PVPListBean> map = pvpListMap.get(i);
					if (map != null)
						pvpList.putAll(map);
				}
			}
			if (pvpList.size() > 0) {
				Object[] ids = pvpList.keySet().toArray();
				int size = ids.length;
				int length = size / 20;
				for (int i = 0; i < 20 && i < size; i++) {
					if (size < 20) {
						PVPListBean bean = pvpList.get((Long) ids[i]);
						if (bean.getProtectTime() < System.currentTimeMillis()) {
							list.add(bean);
						}
					} else {
						int begin = i * length;
						int r = RANDOM.nextInt(length);
						if (pvpList.get((Long) ids[begin + r]).getProtectTime() < System.currentTimeMillis()) {
							list.add(pvpList.get((Long) ids[begin + r]));
						}
					}
				}
			} else {
				list = getPVPListFromDB(id, level);
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return list;
	}

	public static List<PVPListBean> getPVPListFromDB(long id, int level) {
		List<PVPListBean> list = new LinkedList<PVPListBean>();
		List<MemcachePVPListBean> memcachePVPList = null;
		try {
			memcachePVPList =
					DBUtil.namedQuery(id, "from MemcachePVPListBean mpl where mpl.level = " + level + " limit 100");
		} catch (Exception e) {
			log.error(e, e);
		}
		if (memcachePVPList != null) {
			int size = memcachePVPList.size();
			for (int i = 0; i < 10; i++) {
				int r = RANDOM.nextInt(size);
				PVPListBean bean = new PVPListBean();
				MemcachePVPListBean memPVPListBean = memcachePVPList.get(r);
				bean.setId(memPVPListBean.getId());
				bean.setProtectTime(memPVPListBean.getProtectTime());
				list.add(bean);
			}
		}
		return list;
	}

	public static void mergePVPListBeanOfDB(long id, MemcachePVPListBean memPVPListBean) {
		try {
			if (memPVPListBean != null) {
				DBUtil.update(id, memPVPListBean);
			}
		} catch (Exception e) {
			DBUtil.merge(id, memPVPListBean);
			log.error(e, e);
		}
	}

	public static void initPVPListBean(long id, long freashProtectTime) {
		try {
			UserCastle castle = UserMemory.getCastle(id);
			if (castle != null) {
				CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
				if (castleBean.getLevel() >= PVP_LEVEL) {
					MemcachePVPListBean memPVPListBean = new MemcachePVPListBean();
					memPVPListBean.setId(id);
					memPVPListBean.setLevel(castleBean.getLevel());
					memPVPListBean.setProtectTime(freashProtectTime);
					DBUtil.merge(id, memPVPListBean);
					if (!MemcacheOp.containsPVPListBean(castleBean.getLevel(), id)) {
						PVPListBean pvpListBean = new PVPListBean();
						pvpListBean.setId(id);
						pvpListBean.setProtectTime(memPVPListBean.getProtectTime());
						MemcacheOp.setPVPListBean(castleBean.getLevel(), pvpListBean);
					}
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public static MemcachePVPListBean
			resetPVPListBean(long id, int oldCastleLevel, int newCastleLevel, Session session) {
		MemcachePVPListBean memPVPListBean = null;
		try {
			long freashProtectTime = System.currentTimeMillis() + FRESH_USER_PVP_PROTECT_TIME;
			session.createSQLQuery(
					"update loginrecord set freashProtectTime = " + freashProtectTime + " where id = " + id)
					.executeUpdate();
			memPVPListBean = new MemcachePVPListBean();
			memPVPListBean.setId(id);
			memPVPListBean.setLevel(newCastleLevel);
			memPVPListBean.setProtectTime(freashProtectTime);
			session.update(memPVPListBean);
		} catch (Exception e) {
			log.error(e, e);
		}
		return memPVPListBean;
	}

	public static MemcachePVPListBean getMemPVPListBeanFromDB(long id) {
		MemcachePVPListBean bean = null;
		try {
			bean = (MemcachePVPListBean) DBUtil.get(id, MemcachePVPListBean.class);
		} catch (Exception e) {
			log.error(e, e);
		}
		return bean;
	}

	public static void addPVPListBeanToMemcache(long id) {
		try {
			UserCastle castle = UserMemory.getCastle(id);
			CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
			if (castleBean.getLevel() >= PVP_LEVEL) {
				if (!MemcacheOp.containsPVPListBean(castleBean.getLevel(), id)) {
					PVPListBean pvpListBean = new PVPListBean();
					pvpListBean.setId(id);
					MemcachePVPListBean memPVPListBean = getMemPVPListBeanFromDB(id);
					if (memPVPListBean != null) {
						pvpListBean.setProtectTime(memPVPListBean.getProtectTime());
					}
					MemcacheOp.setPVPListBean(castleBean.getLevel(), pvpListBean);
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public static void upgradeModify(long id) {
		UserCastle castle = UserMemory.getCastle(id);
		CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
		deletePVPListBean(castleBean.getLevel() - 1, castle.getId());
		if (castleBean.getLevel() >= PVP_LEVEL) {
			if (!MemcacheOp.containsPVPListBean(castleBean.getLevel(), id)) {
				PVPListBean pvpListBean = new PVPListBean();
				pvpListBean.setId(id);
				MemcachePVPListBean memPVPListBean = getMemPVPListBeanFromDB(id);
				if (memPVPListBean != null) {
					pvpListBean.setProtectTime(memPVPListBean.getProtectTime());
				}
				MemcacheOp.setPVPListBean(castleBean.getLevel(), pvpListBean);
				if (memPVPListBean == null) {
					memPVPListBean = new MemcachePVPListBean();
					memPVPListBean.setId(id);
				}
				memPVPListBean.setLevel(castleBean.getLevel());
				mergePVPListBeanOfDB(id, memPVPListBean);
			}
		}
	}

	public static void protect(long id) {
		if (IS_PVP_PROTECT) {
			try {
				CastleBean castleBean =
						(CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, UserMemory.getCastle(id).getCastleID());
				PVPListBean pvpListBean = PVPListOp.getPVPListBean(castleBean.getLevel(), id);
				pvpListBean.setProtectTime(System.currentTimeMillis() + PVP_PROTECT_TIME);
				MemcacheOp.setPVPListBean(castleBean.getLevel(), pvpListBean);
				DBUtil.executeUpdate(id, "update memcachepvplistbean set protectTime = "
						+ (System.currentTimeMillis() + PVP_PROTECT_TIME) + " where id = " + id);
			} catch (Exception e) {
				log.error(e, e);
			}
		}
	}

	public static void deletePVPListBean(int level, long id) {
		try {
			MemcacheOp.deletePVPListBean(level, id);
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public static PVPListBean getPVPListBean(int level, long id) {
		PVPListBean pvpListBean = MemcacheOp.getPVPListBean(level, id);
		if (pvpListBean == null) {
			pvpListBean = new PVPListBean();
			pvpListBean.setId(id);
		}
		return pvpListBean;
	}

	public static long getProtectTime(long id, int level) {
		try {
			PVPListBean pvpListBean = MemcacheOp.getPVPListBean(level, id);
			if (pvpListBean != null) {
				return pvpListBean.getProtectTime();
			} else {
				MemcachePVPListBean memPVPListBean = getMemPVPListBeanFromDB(id);
				if (memPVPListBean != null) {
					return memPVPListBean.getProtectTime();
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return 0;
	}

	public static long getFreashProtectTime(long id) {
		try {
			LoginRecord lr = (LoginRecord) DBUtil.get(id, LoginRecord.class);
			if (lr != null) {
				return lr.getFreashProtectTime();
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return 0;
	}
}

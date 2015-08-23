package com.server.user.operation;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.CastleBean;
import com.database.model.bean.Enemy;
import com.database.model.bean.UserCastle;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class EnemyOp {

	private static Log log = LogFactory.getLog(EnemyOp.class);

	private static final int ENEMY_NUM = Configuration.getSaveEnemyNum();

	@SuppressWarnings("unchecked")
	private static List<Enemy> getEnemy(long id) {
		List<Enemy> enemyList = null;
		try {
			enemyList = DBUtil.namedQuery(id, "from Enemy e where e.masterID = " + id);
		} catch (Exception e) {
			log.error(e, e);
		}
		if (enemyList != null) {
			clean(enemyList);
		}
		return enemyList;
	}

	@SuppressWarnings("unchecked")
	public static void clean(List<Enemy> enemyList) {
		if (enemyList != null) {
			Comparator c = new ComparatorImpl();
			Collections.sort(enemyList, c);
			Iterator<Enemy> ite = enemyList.iterator();
			int i = 0;
			int size = enemyList.size();
			while (ite.hasNext()) {
				Enemy enemy = ite.next();
				if (i < size - ENEMY_NUM || (System.currentTimeMillis() - enemy.getTime() > (long) 2 * 24 * 3600 * 1000)) {
					ite.remove();
					i++;
				}
			}
		}
	}

	public static void createEnemyRecord(long masterID, long enemyID) {
		try {
			Enemy enemy = new Enemy();
			enemy.setMasterID(masterID);
			enemy.setEnemyID(enemyID);
			enemy.setTime(System.currentTimeMillis());
			List list = DBUtil.namedQuery(masterID, "from Enemy e where e.masterID = " + masterID + " and e.enemyID =" + enemyID);
			if (list == null || list.size() <= 0) {
				DBUtil.save(masterID, enemy);
			}
		} catch (Exception e) {
			log.error(e, e);
		}

	}

	public static byte[] getEnemy(long sceneID, byte[] information) {
		byte[] re = DataFactory.getbyte(0);
		List<Enemy> enemyList = getEnemy(sceneID);
		if (enemyList != null) {
			int size = 0;
			DataFactory.replace(re, 0, DataFactory.getbyte(enemyList.size()));
			Iterator<Enemy> ite = enemyList.iterator();
			while (ite.hasNext()) {
				Enemy enemy = ite.next();
				// HibernateUtil.closeSession(HibernateUtil.checkExistCurrentSession(enemy.getEnemyID()));
				UserCastle castle = (UserCastle) DBUtil.get(enemy.getEnemyID(), UserCastle.class);;
				if (castle != null) {
					re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(enemy.getEnemyID()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(castle.getRace()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(castle.getCurrTitle()));
					CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
					re = DataFactory.addByteArray(re, DataFactory.getbyte(castleBean.getLevel()));
					size++;
				}
			}
			DataFactory.replace(re, 0, DataFactory.getbyte(size));
		}
		return re;
	}
}

class ComparatorImpl implements Comparator<Enemy> {

	public int compare(Enemy o1, Enemy o2) {
		if (o1.getId() > o2.getId()) {
			return 1;
		} else if (o1.getId() < o2.getId()) {
			return -1;
		} else {
			return 0;
		}
	}
}
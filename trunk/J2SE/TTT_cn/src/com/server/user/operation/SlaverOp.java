package com.server.user.operation;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;

import com.cindy.run.connect.instance.Instance;
import com.cindy.run.util.DataFactory;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.CastleBean;
import com.database.model.bean.DropGoodsBean;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.PVPRecordBean;
import com.database.model.bean.Race;
import com.database.model.bean.SlaverBean;
import com.database.model.bean.SoulTowerBean;
import com.database.model.bean.TechnologyTowerBean;
import com.database.model.bean.TowerBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserSlaver;
import com.database.model.bean.UserSlavers;
import com.database.model.bean.UserSoulTower;
import com.database.model.bean.UserTechnology;
import com.database.model.bean.UserTower;
import com.server.cache.UserMemory;
import com.server.dispose.TDDispose;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.Cmd;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class SlaverOp {
	private static Log log = LogFactory.getLog(SlaverOp.class);
	private static Finance financeImpl = FinanceImpl.instance();
	private static List<DropGoodsBean> slaverWorkPrize = new LinkedList<DropGoodsBean>();
	
	static {
		JSONArray jso = JSONArray.fromObject(Configuration.getSlaverWorkPrize());
		for (int i = 0; i < jso.size(); i++) {
			JSONArray jsonArray = jso.getJSONArray(i);
			DropGoodsBean bean = new DropGoodsBean();
			bean.setGoodsID(jsonArray.getInt(0));
			bean.setNum(jsonArray.getInt(1));
			slaverWorkPrize.add(bean);
		}
	}
	
	public static byte[] getSlavers(long id){
		byte[] re = null;
		try{
			UserSlavers slavers = UserMemory.getSlavers(id);
			if (slavers.getSlavers().size() >= 3) {
				TaskOp.doTask(id, 10020, 1);
			}
			re = DataFactory.getbyte(0);
			if(slavers != null){
				List<UserSlaver> slaverList = slavers.getSlavers();
				if(slaverList != null){
					int count = 0;
					Iterator<UserSlaver> slaverIte = slaverList.iterator();
					while(slaverIte.hasNext()){
						UserSlaver slaver = slaverIte.next();
						if(slaver.getState() != 2 && slaver.getChange() != 3){
							SlaverBean bean = (SlaverBean)Goods.getById(GoodsCate.SLAVERBEAN, slaver.getSlaverID());
							re = DataFactory.addByteArray(re, DataFactory.getbyte(slaver.getId()));
							int speedUp = bean.getSpeedup();
							if(slaver.getFriendID() != 0){
								re = DataFactory.addByteArray(re, DataFactory.getbyte(0));
							}else{
								re = DataFactory.addByteArray(re, DataFactory.getbyte(bean.getGoodID())); 
							}
							re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(slaver.getFriendID()));
							re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(slaver.getEscapeTime()));
							re = DataFactory.addByteArray(re, DataFactory.getbyte(speedUp));
							re = DataFactory.addByteArray(re, DataFactory.getbyte(slaver.getState()));
							if(slaver.getState() == 1 || slaver.getState() == 3){
								re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(slaver.getEndTime()));
								re = DataFactory.addByteArray(re, DataFactory.getbyte(slaver.getWorkID()));
							}
							count++;
						}
					}
					DataFactory.replace(re, 0, DataFactory.getbyte(count));
				}
				else{
					re = DataFactory.getbyte(0);
				}
			}else{
				re = DataFactory.getbyte(0);
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return re;
	}
	
	public static UserSlaver getFriendSlaver(long masterID, long friendID){
		List<UserSlaver> slavers = UserMemory.getSlavers(masterID).getSlavers();
		for (Iterator<UserSlaver> iter = slavers.iterator(); iter.hasNext(); ) {
			UserSlaver next = iter.next();
			if (next != null && next.getFriendID() == friendID && next.getChange() != 3)
				return next;
		}
		return null;
	}
	
	public static UserSlaver find(long id, long slaID){
		UserSlavers slavers = UserMemory.getSlavers(id);
		if(slavers != null){
			List<UserSlaver> slaverList = slavers.getSlavers();
			if(slaverList != null){
				Iterator<UserSlaver> ite = slaverList.iterator();
				while(ite.hasNext()){
					UserSlaver sla = ite.next();
					if(sla.getId() == slaID && sla.getChange() != 3){
						return sla;
					}
				}
			}
		}
		return null;
	}
	
	public static boolean isFree(long id, long[] slaID) {
		for(int i = 0; i < slaID.length; i++){
			UserSlaver sla = find(id, slaID[i]);
			if(sla == null || sla.getState() != 0){
				return false;
			}
		}
		return true;
	}
	
	public static int workForTower(long id, long[] slaID, UserTower tower, int type) { // 0建造 1升级 2维修
		int workTime = 0;
		try{
			if(tower != null){
				TowerBean towerBean = (TowerBean)Goods.getById(GoodsCate.TOWERBEAN, tower.getTowerID());
				if(towerBean != null){
					int workCountTime = 0;
					if(type == 0){
						workCountTime = towerBean.getTime();
					}else if(type == 1){
						workCountTime = towerBean.getUpTime();
					}else if(type == 2){
						//Race race = (Race) Goods.getById(GoodsCate.RACE, towerBean.getRace());
						workCountTime = ((towerBean.getHp() - tower.getHp()) * towerBean.getSceneLevel() / 4 + 25) * 1000;
					}
					int speedUp = getSpeedUp(id, slaID);

					if (speedUp != 0)
						workTime = (int) (workCountTime / ((speedUp + 10) / 100f));
					for(int i = 0; i < slaID.length; i++){
						UserSlaver slaver = find(id, slaID[i]);
						slaver.setState(1);
						slaver.setEndTime(System.currentTimeMillis() + workTime);
						slaver.setWorkID(tower.getId());
						slaver.setChange(2);
					}
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return workTime;
	}
	
	public static byte[] catchSlaver(long id, byte[] information) throws Exception{
		byte[] re = null;
		long friendID = DataFactory.doubleBytesToLong(DataFactory.get(information, 10, 8));
		UserSlaver fs = getFriendSlaver(id, friendID);
		FinanceBean bean = new FinanceBean();
		bean.setId(id);
		bean.setCoin(-200);
		if(id != friendID && fs == null && financeImpl.charge(bean)) {
			UserSlavers slavers = UserMemory.getSlavers(id);
			List<UserSlaver> slaverList = slavers.getSlavers();
			UserSlaver slaver = new UserSlaver();
			UserMemory.createFriendMem(friendID);
			SlaverBean catchSlaver = catchSlaver(id, friendID);
			slaver.setSlaverID(catchSlaver.getId());
			slaver.setMasterID(id);
			slaver.setFriendID(friendID);
			slaver.setEscapeTime(System.currentTimeMillis() + catchSlaver.getTime());
			slaver.setState(0);
			slaver.setId((Integer) DBUtil.saveAndReturn(id, slaver));
			slaverList.add(slaver);
			re = new byte[]{0x00}; 
			
			TaskOp.doTask(id, 10004, 1);
		}else{
			re = new byte[]{0x01}; 
		}
		return re;
	}
	
	private static SlaverBean catchSlaver(long id, long friendID){
		UserCastle castle = UserMemory.getCastle(id);
		UserCastle friendCastle = UserMemory.getCastle(friendID);
		CastleBean bean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
		CastleBean friendBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, friendCastle.getCastleID());
		SlaverBean slaverBean = (SlaverBean) Goods.getById(GoodsCate.SLAVERBEAN, 3000 / (Math.abs(bean.getLevel() - friendBean.getLevel()) + 30));
		return slaverBean;
	}
	
	public static void cancelWork(long id, int workID) {
		speedup(id, workID, 0);
	}
	
	public static long repairCastle(long id, long[] slaID, int hp) {
		long endTime = 0;
		try{
			UserCastle castle = UserMemory.getCastle(id);
			if(castle != null){
				CastleBean castleBean = (CastleBean)Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
				if(castleBean != null){
					Race race = (Race) Goods.getById(GoodsCate.RACE, castle.getRace());
					int	workCountTime = (hp * (60000 / race.getWorkPerMin())) / castleBean.getLevel();
					int speedUp = 0;
					for(int i = 0; i < slaID.length; i++){
						UserSlaver slaver = find(id, slaID[i]);
						if(slaver != null){
							SlaverBean slaverBean = (SlaverBean)Goods.getById(GoodsCate.SLAVERBEAN, slaver.getSlaverID());
							speedUp += slaverBean.getSpeedup();
						}
					}
					endTime = System.currentTimeMillis() + workCountTime;
					for(int i = 0; i < slaID.length; i++){
						UserSlaver slaver = find(id, slaID[i]);
						slaver.setState(1);
						slaver.setEndTime(endTime);
						slaver.setWorkID(0);
						slaver.setChange(2);
					}
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return endTime;
	}
	
	public static long upgradeCastle(long id, long[] slaID ) {
		long endTime = 0;
		try{
			UserCastle castle = UserMemory.getCastle(id);
			if(castle != null){
				CastleBean castleBean = (CastleBean)Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
				if(castleBean != null){
					int workCountTime = castleBean.getUpTime();
					endTime = System.currentTimeMillis() + workCountTime;
					for(int i = 0; i < slaID.length; i++){
						UserSlaver slaver = find(id, slaID[i]);
						slaver.setState(1);
						slaver.setEndTime(endTime);
						slaver.setWorkID(0);
						slaver.setChange(2);
					}
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return endTime;
	}
	
	public static UserSlavers initSlaver(long id){
		UserSlavers slavers = new UserSlavers();
		slavers.setId(id);
		List<UserSlaver> slaverList = new LinkedList<UserSlaver>();

		UserSlaver slaver = new UserSlaver();
		slaver.setSlaverID(1);
		slaver.setEscapeTime(Long.MAX_VALUE);
		slaver.setEndTime(0);
		slaver.setFriendID(0);
		slaver.setMasterID(id);
		slaver.setState(0);
		slaver.setWorkID(0);
		//slaver.setId((Integer) DBUtil.saveAndReturn(id, slaver));
		//slaverList.add(slaver);
		
		UserSlaver slaver2 = new UserSlaver();
		slaver2.setSlaverID(1);
		slaver2.setEscapeTime(System.currentTimeMillis() + 48 * 3600000);
		slaver2.setEndTime(0);
		slaver2.setFriendID(0);
		slaver2.setMasterID(id);
		slaver2.setState(0);
		slaver2.setWorkID(0);
		slaver2.setId((Integer) DBUtil.saveAndReturn(id, slaver2));
		slaverList.add(slaver2);
		
		slavers.setSlavers(slaverList);
		if(slavers != null){
			slavers.setTime(System.currentTimeMillis());
		}
		return slavers;
	}
	
	public static boolean resetSlaver(long id, Session session){
		session.createSQLQuery("update userslaver set state = 0 where masterID = " + id).executeUpdate();
		return true;
	}
	
	public static long upgradeSoulTower(long id, long[] slaID ) {
		long endTime = 0;
		try{
			UserSoulTower userSoulTower = UserMemory.getSoulTower(id);
			if(userSoulTower != null){
				SoulTowerBean soulTowerBean = (SoulTowerBean)Goods.getById(GoodsCate.SOULTOWERBEAN, userSoulTower.getSoulTowerLevel());
				if(soulTowerBean != null){
					int workCountTime = soulTowerBean.getUpgradeTime();
					endTime = System.currentTimeMillis() + workCountTime;
					for(int i = 0; i < slaID.length; i++){
						UserSlaver slaver = find(id, slaID[i]);
						slaver.setState(1);
						slaver.setEndTime(endTime);
						slaver.setWorkID(-2);
						slaver.setChange(2);
					}
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return endTime;
	}
	
	public static long upgradeTecTower(long id, long[] slaID){
		long endTime = 0;
		try{
			UserTechnology userTec = UserMemory.getUserTec(id);
			if(userTec != null){
				TechnologyTowerBean tecTowerBean = (TechnologyTowerBean)Goods.getById(GoodsCate.TECHNOLOGYTOWERBEAN, userTec.getTecTowerLevel());
				if(tecTowerBean != null){
					int workCountTime = tecTowerBean.getUpgradeTime();
					endTime = System.currentTimeMillis() + workCountTime;
					for(int i = 0; i < slaID.length; i++){
						UserSlaver slaver = find(id, slaID[i]);
						slaver.setState(1);
						slaver.setEndTime(System.currentTimeMillis() + workCountTime);
						slaver.setWorkID(-3);
						slaver.setChange(2);
					}
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return endTime;
	}
	public static List<ThingBean> addSlaver(long id, int goodsID, int num){
		List<ThingBean> get = null;
		SlaverBean slaverBean = (SlaverBean) Goods.getSingleByGoodID(GoodsCate.SLAVERBEAN, goodsID);
		UserCastle castle = UserMemory.getCastle(id);
		UserSlavers slavers = UserMemory.getSlavers(id);
		List<UserSlaver> slaverList = slavers.getSlavers();
		if(slaverBean != null && castle != null && slaverList != null){
			for(int i = 0; i < num; i++){
				UserSlaver slaver = new UserSlaver();
				slaver.setMasterID(id);
				slaver.setSlaverID(slaverBean.getId());
				slaver.setEscapeTime(slaverBean.getTime() + System.currentTimeMillis());
				slaver.setId((Integer) DBUtil.saveAndReturn(id, slaver));
				slaverList.add(slaver);
				ThingBean to = new ThingBean(8, slaver.getId(), goodsID, 1, null);
				if(get == null){
					get = new LinkedList<ThingBean>();
				}
				get.add(to);
			}
			return get;
		}
		return get;
	}
	public static List<ThingBean> addSlaver(long id, int goodsID, int num, long time){
		List<ThingBean> get = null;
		SlaverBean slaverBean = (SlaverBean) Goods.getSingleByGoodID(GoodsCate.SLAVERBEAN, goodsID);
		UserCastle castle = UserMemory.getCastle(id);
		UserSlavers slavers = UserMemory.getSlavers(id);
		List<UserSlaver> slaverList = slavers.getSlavers();
		if(slaverBean != null && castle != null && slaverList != null){
			for(int i = 0; i < num; i++){
				UserSlaver slaver = new UserSlaver();
				slaver.setMasterID(id);
				slaver.setSlaverID(slaverBean.getId());
				slaver.setEscapeTime(time + System.currentTimeMillis());
				slaver.setId((Integer) DBUtil.saveAndReturn(id, slaver));
				slaverList.add(slaver);
				ThingBean to = new ThingBean(8, slaver.getId(), goodsID, 1, null);
				if(get == null){
					get = new LinkedList<ThingBean>();
				}
				get.add(to);
			}
			return get;
		}
		return get;
	}
	
	public static void sendSlaverList(long id, Instance role, byte[] information){
		byte[] head = DataFactory.get(information, 0, 10);
		head[0] = 0x65;
		try {
			TDDispose.sendInformation(role, DataFactory.addByteArray(head, getSlavers(id)));
		} catch (IOException e) {
			log.error(e, e);
		}
	}
	
	public static void speedup(long id, int workID, long endTime){
		UserSlavers slavers = UserMemory.getSlavers(id);
		if(slavers != null){
			List<UserSlaver> slaverList = slavers.getSlavers();
			if(slaverList != null){
				Iterator<UserSlaver> slaverIte = slaverList.iterator();
				while(slaverIte.hasNext()){
					UserSlaver slaver = slaverIte.next();
					if(slaver.getState() == 1 && slaver.getWorkID() == workID){
						slaver.setEndTime(endTime);
						slaver.setChange(2);
					}
				}
			}
		}
	
	}
	
	public static int getSlaverSize(long id) {
		int size = 0;
		List<UserSlaver> slaverList = UserMemory.getSlavers(id).getSlavers();
		if(slaverList != null){
			Iterator<UserSlaver> ite = slaverList.iterator();
			while(ite.hasNext()){
				UserSlaver slaver = ite.next();
				if(slaver != null && slaver.getChange() != 3){
					size++;
				}
			}
		}
		return size;
	}
	
	/**
	 * 解除好友工人
	 * @param id
	 * @param information
	 * @return
	 */
	public static byte[] removeSlaver(long id, byte[] information) {
		try {
			Cmd req = Cmd.getInstance(information);
			Cmd res = Cmd.getInstance();
			
			long friendId = req.readLong(10);
			List<UserSlaver> slavers = UserMemory.getSlavers(id).getSlavers();
			for (Iterator<UserSlaver> iter = slavers.iterator(); iter.hasNext(); ) {
				UserSlaver slaver = iter.next();
				if (slaver.getFriendID() == friendId) {
					slaver.setChange(3);
				}
			}
			res.appendByte((byte) 0x00);
			
			return res.getResponse();
			
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return new byte[] { 0x01 };
	}
	
	/**
	 * 计算升级加速时间
	 * @param id
	 * @param slaID
	 * @return
	 */
	public static int getSpeedUp(long id, long[] slaID) {
		int speedUp = 0;
		for(int i = 0; i < slaID.length; i++){
			UserSlaver slaver = find(id, slaID[i]);
			if(slaver != null){
				SlaverBean slaverBean = (SlaverBean)Goods.getById(GoodsCate.SLAVERBEAN, slaver.getSlaverID());
				speedUp += slaverBean.getSpeedup() - (i + 1) * 10;
			}
		}
		return speedUp;
	}

	/**
	 * 工人打工
	 * @param id
	 * @param cmd
	 * @return
	 */
	public static Cmd slaverWork(long id, Cmd cmd) {
		Cmd res = Cmd.getInstance();
		res.appendByte((byte) 2);
		try {
			int slaverId = cmd.readInt(10);

			UserSlaver slaver = find(id, slaverId);
			if (slaver == null || slaver.getState() != 0) {
				res.clear();
				res.appendByte((byte) 1);
				return res;
			}
			Calendar now = Calendar.getInstance();
			now.setTimeInMillis(System.currentTimeMillis());
			Calendar begin = Calendar.getInstance();
			begin.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE), 20, 0, 0);
			Calendar end = Calendar.getInstance();
			end.set(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DATE), 2, 0, 0);
			end.add(Calendar.DATE, 1);
			if (now.get(Calendar.HOUR_OF_DAY) < 3) {
				begin.add(Calendar.DATE, -1);
				end.add(Calendar.DATE, -1);
			}
			if (now.after(begin) && now.before(end)) {
				slaver.setState(3);
				long hour = 1000 * 60 * 60;
				slaver.setEndTime(now.getTimeInMillis() + hour * 8);
				slaver.setWorkID(1000);
				slaver.setChange(2);
				res.clear();
				res.appendByte((byte) 0);
				res.appendLong(slaver.getEndTime());
			} else {
				return res;
			}
			
		} catch (Exception e) {
			log.error(e, e);
		}
		return res;
	}

	/**
	 * 领取工人打工奖励
	 * @param id
	 * @param cmd
	 * @return
	 */
	public static Cmd getSlaverWorkPriz(long id, Cmd cmd) {
		Cmd res = Cmd.getInstance();
		res.appendInt(0);
		try {

			int slaverId = cmd.readInt(10);
			UserSlaver slaver = find(id, slaverId);
			if (slaver != null && slaver.getState() == 4) {
				slaver.setState(0);
				slaver.setChange(2);
				SlaverBean bean = (SlaverBean) Goods.getById(GoodsCate.SLAVERBEAN, slaver.getSlaverID());
				
				res.clear();
				res.appendInt(1);
				List<ThingBean> get = new LinkedList<ThingBean>();
				res.appendInt(60006);
				int num = slaver.getSlaverID() == 2 ? 300 : 2 * bean.getSpeedup();
				num = slaver.getSlaverID() == 3 ? 500 : num;
				res.appendInt(num);
				get.addAll(StorageOp.storeGoods(id, 60006, num));
				GameLog.createLog(id, 133, null, true, get, null, "getSlaverWorkPriz");
			}
			
		} catch (Exception e) {
			log.error(e, e);
		}
		return res;
	}

	/**
	 * 取消工人打工
	 * @param id
	 * @param cmd
	 */
	public static void cancleSlaverWork(long id, Cmd cmd) {
		try {
			int slaverId = cmd.readInt(10);
			UserSlaver slaver = find(id, slaverId);
			if (slaver != null && slaver.getState() == 3) {
				slaver.setState(0);
				slaver.setChange(2);
			}
			
		} catch (Exception e) {
			log.error(e, e);
		}
	}
}

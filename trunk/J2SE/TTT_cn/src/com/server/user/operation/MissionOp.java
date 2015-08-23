package com.server.user.operation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cindy.run.util.DataFactory;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.Mission;
import com.server.log.GameLog;
import com.server.log.ThingBean;
import com.server.util.DBUtil;

public class MissionOp {
	private static Log log = LogFactory.getLog(MissionOp.class);
	
	public static int getMaxID(List<Mission> missionList){
		int max = 0;
		if(missionList != null){
			Iterator<Mission> ite = missionList.iterator();
			while(ite.hasNext()){
				Mission m = ite.next();
				if(max < m.getId()){
					max = m.getId();
				}
			}
		}
		return max;
	}
	
	public static void save(Mission mission){
		try{
			DBUtil.save(mission.getMasterID(), mission);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static void delete(Mission mission){
		DBUtil.delete(mission.getMasterID(), mission);
		//delete(mission.getMasterID(), mission.getId());
	}
	
	private static void delete(long id, int queueID){
		try{
			DBUtil.executeUpdate(id, "delete from mission where id = " + queueID + " and masterID = " + id);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static List<Mission> getMissionQueue(long id){
		List<Mission> list = null;
		try{
			list = DBUtil.namedQuery(id, "from Mission m where m.masterID = " + id);
		}catch(Exception e){
			log.error(e, e);
		}
		return list;
	}
	
	public static Mission getMission(long id, int queueID){
		Mission m = null;
		List<Mission> list = getMissionQueue(id);
		Iterator<Mission> ite = list.iterator();
		while(ite.hasNext()){
			Mission mission = ite.next();
			if(mission.getId() == queueID){
				m = mission;
				break;
			}
		}
		return m;
	}
	
	public static byte[] getMissionQueue(long id, byte[] information)throws Exception{
		byte[] re = null;
		List<Mission> missionList = getMissionQueue(id);
		if(missionList != null){
			re = DataFactory.getbyte(missionList.size());
			Iterator<Mission> ite = missionList.iterator();
			while(ite.hasNext()){
				Mission mission = ite.next();
				re = DataFactory.addByteArray(re, DataFactory.getbyte(mission.getId()));
				re = DataFactory.addByteArray(re, DataFactory.getbyte(mission.getProduct()));
				re = DataFactory.addByteArray(re, DataFactory.getbyte(mission.getNum()));
				re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(mission.getEndTime()));
			}
		}else{
			re = DataFactory.getbyte(0);
		}
		return re;
	}
	
	public static void store(long id, byte[] information) throws Exception{
		int queueID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		Mission mission = getMission(id, queueID);
		if(mission != null && mission.getEndTime() < System.currentTimeMillis()){
			int num = mission.getNum();
			List<ThingBean> get = new LinkedList<ThingBean>();
			get.addAll(StorageOp.storeGoods(id, mission.getProduct(), num));
			GameLog.createLog(id, 9, null, true, get, null, null);
			MissionOp.delete(mission);
		}
	}
}

package com.server.user.operation;

import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.alisoft.xplatform.asf.cache.IMemcachedCache;
import com.database.hibernate.util.MemcacheUtil;
import com.database.model.bean.PVPListBean;
import com.database.model.bean.PVPRecordBean;
import com.database.model.bean.PickedFriends;
import com.database.model.bean.UserLottery;

public class MemcacheOp {
	private static Log log = LogFactory.getLog(MemcacheOp.class);
	private static IMemcachedCache memCachedClient = MemcacheUtil.getOnlineMemcached();
	
	public static PVPRecordBean getPVPRecord(long id){
		PVPRecordBean record = null;
		try{
			record = (PVPRecordBean)memCachedClient.get("pvpRecord" + id);
			if(record != null){
				record.decode();
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return record;
	}

	public static void setPvpRecord(PVPRecordBean record){
		if(record != null){
			try{
				record.encode();
				memCachedClient.put("pvpRecord" + record.getId(), record, 3600 * 24 * 2);
			}catch(Exception e){
				log.error(e, e);
			}
		}
	}
	
	public static PVPListBean getPVPListBean(int level, long id){
		PVPListBean bean = null;
		try{
			String key = "level" + level + ":" + id;
			Object obj = memCachedClient.get(key);
			if(obj != null){
				bean = (PVPListBean)obj;
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return bean;
	}
	
	public static void removePVPListBean(int level, long id){
		try{
			String key = "level" + level + ":" + id;
			memCachedClient.remove(key);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static void setPVPListBean(int level, PVPListBean pvpListBean){
		if(pvpListBean != null){
			try{
				memCachedClient.put("level" + level + ":" + pvpListBean.getId(), pvpListBean);
			}catch(Exception e){
				log.error(e, e);
			}
		}
	}
	
	public static void deletePVPListBean(int level, long id){
		try{
			memCachedClient.remove("level" + level + ":" + id);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	
	public static boolean containsPVPListBean(int level, long id){
		try{
			return memCachedClient.containsKey("level" + level + ":" + id);
		}catch(Exception e){
			log.error(e, e);
			return false;
		}
	}
	
	public static Set<String> getMemcacheKeySet(boolean fast){
		try{
			return memCachedClient.keySet(fast);
		}catch(Exception e){
			log.error(e, e);
			return null;
		}
	}
	
	public static Object get(String key){
		try{
			return memCachedClient.get(key);
		}catch(Exception e){
			log.error(e, e);
			return null;
		}
	}
	
	public static PickedFriends getPickedFriends(long id){
		try{
			Object obj = memCachedClient.get("pickedFriends:" + id );
			if(obj != null){
				return (PickedFriends)obj;
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return null;
	}
	
	public static void setPickedFriends(long id, PickedFriends pickedFriends, int expiry){
		try{
			memCachedClient.put("pickedFriends:" + id, pickedFriends, expiry);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static Long getIssueTime(long id){
		try{
			Object obj = memCachedClient.get("issueTime:" + id);
			if(obj != null){
				return (Long)obj;
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return null;
	}
	
	public static void setIssueTime(long id, Long issueTime, int expiry){
		try{
			memCachedClient.put("issueTime:" + id, issueTime, expiry);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static UserLottery getLottery(long id){
		try{
			Object obj = memCachedClient.get("lottery:" + id);
			if(obj != null){
				return (UserLottery)obj;
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return null;
	}
	
	public static void setLottery(long id, UserLottery userLottery, int expiry){
		try{
			memCachedClient.put("lottery:" + id, userLottery, expiry);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	/**
	 * 设置安全时间
	 * @param id
	 * @param time
	 * @param expiry	有效时间
	 */
	public static void setSecurityTime(long id, long time, int state, int expiry) {
		try{
			memCachedClient.put("security:" + id, time + "," + state, expiry);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static boolean getSecurityTime(long id, int state) {
		try{
			Object obj = memCachedClient.get("security:" + id);
			if (obj != null) {
				String str = (String) obj;
				String[] split = str.split(",");
				if (state == 1)
					return state != Integer.valueOf(split[1]).intValue();
				return (System.currentTimeMillis() - Long.valueOf(split[0]).longValue()) < FightOp.SECURITY_TIME * 1000;
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return false;
	}
	
	public static void removeSecurityTime(long id) {
		try{
			memCachedClient.remove("security:" + id);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static void setOnlineUser(String str, int type) {
		try{
			memCachedClient.put("online" + type, str);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static String getOnlineUser() {
		try{
			return (String) memCachedClient.get("online");
		}catch(Exception e){
			log.error(e, e);
		}
		return null;
	}
	
	public static String getUsers() {
		try{
			return (String) memCachedClient.get("users");
		}catch(Exception e){
			log.error(e, e);
		}
		return null;
	}
	
	public static void setUsers(String users) {
		try{
			memCachedClient.put("users", users);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public static void main(String[] args) throws Exception {
		setSecurityTime(1, System.currentTimeMillis(), 0, FightOp.SECURITY_TIME);
		Thread.sleep(2000);
		System.out.println(getSecurityTime(1, 0));
	}
}

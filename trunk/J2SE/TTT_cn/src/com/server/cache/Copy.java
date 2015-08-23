package com.server.cache;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.CopyBean;
import com.database.model.bean.MemoryBean;
import com.database.model.bean.UserCopy;
import com.server.user.operation.CopyOp;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class Copy  implements Cache {
	private static Log log = LogFactory.getLog(Copy.class);
	private static final int COPY_CACHE_TIME = Configuration.getCopyCacheTime();
	private static final int MASTER_COPY_CACHE_TIME = Configuration.getMasterCopyCacheTime();
	private static final int FRINED_COPY_CACHE_TIME = Configuration.getFriendCopyCacheTime();
	private static final int DAY = Configuration.getDay();
	@Override
	public Object getFromDB(long id){
		UserCopy copy = null;
		Session session = null;
		try{
			session = HibernateUtil.currentSession(id);
			copy = (UserCopy) session.get(UserCopy.class, id);
			if(copy != null){
				copy.setTime(System.currentTimeMillis());
				copy.decode();
			}
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return copy;
	}
	
	@Override
	public Object gerFromDB(long id, Object para){
		return getFromDB(id);
	}

	public static boolean isOverOneWeek(long beforeTime, long afterTime){
		if(afterTime - beforeTime >= 7 * DAY){
			return true;
		}else{
			Date beforeDate = new Date(beforeTime);
			Date afterDate = new Date(afterTime);
			int bd = beforeDate.getDay();
			int ad = afterDate.getDay();
			if(beforeDate.getDay() == 0){
				bd = 7;
			}
			if(afterDate.getDay() == 0){
				ad = 7;
			}
			if(bd == ad && afterTime - beforeTime > DAY){
				return true;
			}else if(bd > ad){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void clean(MemoryBean bean){
		if(bean != null){
			UserCopy copy = bean.getCopy();
			if(copy != null && isOverOneWeek(copy.getResetTime(), System.currentTimeMillis())){
				List<CopyBean> copyList = copy.getCopyList();
				if(copyList != null && copyList.size() > 0){
					copyList.clear();
					DBUtil.executeUpdate(copy.getId(), "delete from usercityinfo where masterID = " + copy.getId() + " and cityID >= " + CopyOp.COPY_START_NUM + " and cityID < 200");
					DBUtil.executeUpdate(copy.getId(), "delete from userinvitefriend where masterID = " + copy.getId() + " and cityID >= " + CopyOp.COPY_START_NUM + " and cityID < 200");
					copy.setChange(true);
				}
			}
		}
	}
	
	
	@Override
	public void update(MemoryBean bean) {
		if (bean != null && bean.canWrite()) {
			UserCopy copy = bean.getCopy();
			if(copy != null && copy.isChange()){
				copy.encode();
				DBUtil.saveOrUpdate(copy.getId(), copy);
				copy.setChange(false);
			}
		}
	}
	
	public void persist(MemoryBean bean){
		try{
			if(bean != null){
				UserCopy copy = bean.getCopy();
				if(copy != null){
					if(System.currentTimeMillis() - copy.getTime() > COPY_CACHE_TIME){
						update(bean);
					}
					if(System.currentTimeMillis() - copy.getTime() > MASTER_COPY_CACHE_TIME
							&& bean.isMaster()){
						update(bean);
						bean.setCopy(null);
					}
					if(System.currentTimeMillis() - copy.getTime() > FRINED_COPY_CACHE_TIME
							&& !bean.isMaster()){
						bean.setCopy(null);
					}
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
	}
}

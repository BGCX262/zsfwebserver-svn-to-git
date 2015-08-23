package com.server.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.connect.instance.Instance;
import com.cindy.run.util.ThreadPoolExecutorTimer;
import com.database.model.bean.CopyBean;
import com.database.model.bean.MemoryBean;
import com.database.model.bean.UserCastle;
import com.database.model.bean.UserCity;
import com.database.model.bean.UserCopy;
import com.database.model.bean.UserMine;
import com.database.model.bean.UserProps;
import com.database.model.bean.UserSlaver;
import com.database.model.bean.UserSlavers;
import com.database.model.bean.UserSoulTower;
import com.database.model.bean.UserStorage;
import com.database.model.bean.UserStorages;
import com.database.model.bean.UserTask;
import com.database.model.bean.UserTasks;
import com.database.model.bean.UserTechnology;
import com.server.identity.Identityer;
import com.server.user.operation.CastleOp;
import com.server.user.operation.CityOp;
import com.server.user.operation.MineOp;
import com.server.user.operation.SlaverOp;
import com.server.user.operation.SoulTowerOp;
import com.server.user.operation.StorageOp;
import com.server.user.operation.TaskOp;
import com.server.user.operation.TechnologyOp;
import com.server.util.Configuration;

public class UserMemory {
	private static Log log = LogFactory.getLog(UserMemory.class);
	private static final ConcurrentHashMap<Long, MemoryBean> MEMORY = new ConcurrentHashMap<Long, MemoryBean>();
	private static final ThreadPoolExecutorTimer TIMER = ThreadPoolExecutorTimer.getIntance();
	private static final int TIME = Configuration.getTime();
	private static final int MASTER_VALID_TIME = Configuration.getMasterValidTime();
	private static final int FRIEND_VALID_TIME = Configuration.getFriendValidTime();
	private static Cache castleImpl = new Castle();
	private static Cache cityImpl = new City(); 
	private static Cache slaversImpl = new Slaver();
	private static Cache storagesImpl = new Storage();
	private static Cache taskImpl = new Task();
	private static Cache soulTowerImpl = new SoulTower();
	private static Cache mineImpl = new Mine();
	private static Cache propImpl = new Prop();
	private static Cache technologyImpl = new Technology();
	private static Cache copyImpl = new Copy();
	private static final Object LOCK = new Object();
	private static UserMemory instance = new UserMemory();
	//private static Finance financeImpl = FinanceImpl.instance();
	
	public UserMemory(){
		TIMER.getPreciseTimer().scheduleAtFixedRate(new Persister(), TIME, TIME, TimeUnit.MILLISECONDS);
	}
	
	public static UserMemory instance(){
		if(instance == null){
			synchronized (LOCK){
				if(instance == null){
					instance = new UserMemory();
				}
			}
		}
		return instance;
	}
	
	private class Persister implements Runnable{
		public void run() {
			try{
				Iterator<Long> ite = MEMORY.keySet().iterator();
				while(ite.hasNext()){
					try{
						Long id = ite.next();
						MemoryBean bean = MEMORY.get(id);	
						castleImpl.persist(bean);
						cityImpl.persist(bean);
						slaversImpl.persist(bean);
						storagesImpl.persist(bean);
						taskImpl.persist(bean);
						soulTowerImpl.persist(bean);
						mineImpl.persist(bean);
						propImpl.persist(bean);
						technologyImpl.persist(bean);
						copyImpl.persist(bean);
						if(bean != null){
							if(bean.isMaster()){
								if(System.currentTimeMillis() - bean.getTime() > MASTER_VALID_TIME){
									remove(id);
								}
							}else{
								if(System.currentTimeMillis() - bean.getTime() > FRIEND_VALID_TIME){
									remove(id);
								}
							}
						}else{
							remove(id);
						}
					}catch(Exception e){
						log.error(e, e);
					}
				}
			}catch(Exception e){
				log.error(e, e);
			}
		}
	}
	
	public static boolean canDo(long id){
		MemoryBean bean = get(id);
		if(bean == null){
			bean = createMasterMem(id);
		}
		if(bean != null){
			if(bean.canDo(id)){
				return true;
			}
		}
		return false;
	}
	
	public static MemoryBean get(long id){
		MemoryBean bean = MEMORY.get(id);
		if(bean != null){
			bean.setTime(System.currentTimeMillis());
		}
		return bean;
	}
	
	public static MemoryBean remove(long id){
		return MEMORY.remove(id);
	}
	
	public static UserCastle getCastle(long id){
		UserCastle castle = null;
		MemoryBean bean = get(id);
		if(bean != null){
			castle = bean.getCastle();
			if(castle == null){
				castle = (UserCastle)castleImpl.getFromDB(id);
				/*if(castle == null){
					castle = new UserCastle();
					castle.setId(id);
				}*/
				bean.setCastle(castle);
			}
			castleImpl.clean(bean);
			if (castle != null)
				castle.setTime(System.currentTimeMillis());
		}
		return castle;
	}
	
	public static UserCity getCity(long id , int cityID){
		UserCity city = null;
		MemoryBean bean = get(id);;
		if(bean != null){
			city = bean.getCity();
			if(city == null || (city != null && city.getCityID() != cityID)){
				if(city != null){
					cityImpl.update(bean);
				}
				city = (UserCity)cityImpl.gerFromDB(id, cityID);
				/*if(city == null){
					city = new UserCity();
					city.setId(id);
					city.setCityID(cityID);
				}*/
				if(city != null){
					bean.setCity(city);
				}
			}
			if(city != null){
				city.setTime(System.currentTimeMillis());
				cityImpl.clean(bean);
			}
			/*if(bean.isMaster()){
				SyncSceneGoodsOp.syncSceneGoods(id, city);
			}*/
		}
		return city;
	}
	
	
	public static UserSlavers getSlavers(long id){
		UserSlavers slavers = null;
		MemoryBean bean = get(id);
		if(bean != null){
			slavers = bean.getSlavers();
			if(slavers == null){
				slavers = (UserSlavers)slaversImpl.getFromDB(id);
				if(slavers == null){
					slavers = new UserSlavers();
					slavers.setId(id);
					slavers.setSlavers(new LinkedList<UserSlaver>());
				}
				bean.setSlavers(slavers);
			}
			if(slavers != null){
				slaversImpl.clean(bean);
				slavers.setTime(System.currentTimeMillis());
			}
		}
		return slavers;
	}
	
	public static UserStorages getStorages(long id){
		UserStorages storages = null;
		MemoryBean bean = get(id);
		if(bean != null){
			storages = bean.getStorages();
			if(storages == null){
				storages = (UserStorages)storagesImpl.getFromDB(id);
				if(storages == null){
					storages = new UserStorages();
					storages.setId(id);
					storages.setStorage(new ArrayList<UserStorage>());
				}
				bean.setStorages(storages);
			}
			if(storages != null){
				storagesImpl.clean(bean);
				storages.setTime(System.currentTimeMillis());
			}
		}
		return storages;
	}
	
	public static UserCity getCurrentCity(long id){
		UserCity city = getCity(id, getCastle(id).getCurrentCity());
		return city;
	}
	
	public static UserTasks getTasks(long id){
		UserTasks tasks = null;
		MemoryBean bean = get(id);
		if(bean != null){
			tasks = bean.getTasks();
			if(tasks == null){
				tasks = (UserTasks)taskImpl.getFromDB(id);
				if(tasks == null){
					tasks = new UserTasks();
					tasks.setId(id);
					tasks.setTasks(new LinkedList<UserTask>());
				}
				bean.setTasks(tasks);
			}
			if(tasks != null){
				taskImpl.clean(bean);
				tasks.setTime(System.currentTimeMillis());
			}
		}
		return tasks;
	}
	
	public static UserSoulTower getSoulTower(long id){
		UserSoulTower soulTower = null;
		MemoryBean bean = get(id);
		if(bean != null){
			soulTower = bean.getSoulTower();
			if(soulTower == null){
				soulTower = (UserSoulTower)soulTowerImpl.getFromDB(id);
				/*if(soulTower == null){
					soulTower = new UserSoulTower();
					soulTower.setId(id);
					soulTower.setSoulTowerLevel(1);
				}*/
				bean.setSoulTower(soulTower);
			}
			//if(soulTower != null){
				soulTowerImpl.clean(bean);
				soulTower.setTime(System.currentTimeMillis());
			//}
		}
		return soulTower;
	}
	
	public static UserMine getUserMine(long id){
		UserMine userMine = null;
		MemoryBean bean = get(id);
		if(bean != null){
			userMine = bean.getUserMine();
			if(userMine == null){
				userMine = (UserMine)mineImpl.getFromDB(id);
				/*if(userMine == null){
					userMine = new UserMine();
					userMine.setId(id);
					userMine.setRockMineLevel(1);
					userMine.setMetalMineLevel(1);
					userMine.setCrystalMineLevel(1);
				}*/
				bean.setUserMine(userMine);
			}
			if(userMine != null){
				mineImpl.clean(bean);
				userMine.setTime(System.currentTimeMillis());
			}
		}
		return userMine;
	}
	
	public static UserProps getUserProps(long id){
		UserProps userProps = null;
		MemoryBean bean = get(id);
		if(bean != null){
			userProps = bean.getUserProps();
			if(userProps == null){
				userProps = (UserProps)propImpl.getFromDB(id);
				/*if(userProps == null){
					userProps = new UserProps();
					userProps.setId(id);
					userProps.setUsedProps(new LinkedList<UserProp>());
				}*/
				bean.setUserProps(userProps);
			}
			if(userProps != null){
				propImpl.clean(bean);
				userProps.setTime(System.currentTimeMillis());
			}
		}
		return userProps;
	}
	
	public static UserTechnology getUserTec(long id){
		UserTechnology userTec = null;
		MemoryBean bean = get(id);
		if(bean != null){
			userTec = bean.getUserTec();
			if(userTec == null){
				userTec = (UserTechnology)technologyImpl.getFromDB(id);
				if(userTec == null){
					userTec = TechnologyOp.initTechnology(id);
				}
				bean.setUserTec(userTec);
			}
			if(userTec != null){
				technologyImpl.clean(bean);
				userTec.setTime(System.currentTimeMillis());
			}
		}
		return userTec;
	}
	
	public static UserCopy getCopy(long id){
		UserCopy copy = null;
		MemoryBean bean = get(id);
		if(bean != null){
			copy = bean.getCopy();
			if(copy == null){
				copy = (UserCopy) copyImpl.getFromDB(id);
				if(copy == null){
					copy = new UserCopy();
					copy.setId(id);
					copy.setCopyList(new LinkedList<CopyBean>());
				}
				bean.setCopy(copy);
				if(copy != null){
					copyImpl.clean(bean);
					copy.setTime(System.currentTimeMillis());
				}
			}
		}
		return copy;
	}
	
	public static void initUser(long id, int timezone, String name, Instance role){
		MemoryBean bean = get(id);
		if(bean == null){
			bean = createMasterMem(id);
		}
		if(bean != null){
			bean.setCastle(CastleOp.initUserCastle(id, timezone, name, role));
			bean.setCity(CityOp.initUserCity(id, 1));//初始化关卡1场景
			CityOp.initUserCity(id, 0);//初始化pvp场景
			bean.setTasks(TaskOp.intiTask(id));
			bean.setStorages(StorageOp.initStorage(id));
			bean.setSlavers(SlaverOp.initSlaver(id));
			bean.setSoulTower(SoulTowerOp.initSoulTower(id));
			bean.setUserMine(MineOp.initMine(id));
			bean.setUserTec(TechnologyOp.initTechnology(id));
		}
	}
	
	public static void offline(long id){
		MemoryBean bean = get(id);
		if(bean != null){
			castleImpl.update(bean);
			cityImpl.update(bean);
			slaversImpl.update(bean);
			storagesImpl.update(bean);
			taskImpl.update(bean);
			soulTowerImpl.update(bean);
			mineImpl.update(bean);
			propImpl.update(bean);
			technologyImpl.update(bean);
			copyImpl.update(bean);
		}
		remove(id);
		log.info("id:" + id + " logout");
	}
	
	public static void put(long id, MemoryBean bean){
		MEMORY.put(id, bean);
		/*Iterator<Long> ite = MEMORY.keySet().iterator();
		while(ite.hasNext()){
			Long temp = ite.next();
			System.out.println(MEMORY.get(temp));
		}*/
	}
	
	public static void putIfAbsent(long id, MemoryBean bean){
		MEMORY.putIfAbsent(id, bean);
	}
	
	public static MemoryBean createMasterMem(long id){
		MemoryBean bean = new MemoryBean();
		bean.setIdentityCode(Identityer.createIdentity(id));
		bean.setMasterPriorty();
		bean.setTime(System.currentTimeMillis());
		put(id, bean);
		/*FinanceBean financeBean = financeImpl.getFinance(id);
		if(financeBean != null){
			financeBean.setMaster(true);
		}*/
		return bean;
	}
	
	public static MemoryBean createFriendMem(long id){
		MemoryBean bean = get(id);
		if(bean != null){
			bean.setTime(System.currentTimeMillis());
		}
		if(bean == null){
			bean = new MemoryBean();
			bean.setFriendPriorty();
			bean.setTime(System.currentTimeMillis());
			UserMemory.putIfAbsent(id, bean);
		}
		return bean;
	}
}

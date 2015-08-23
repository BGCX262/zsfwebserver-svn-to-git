package com.server.finance;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cindy.run.util.ThreadPoolExecutorTimer;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.CastleBean;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.UserCastle;
import com.server.cache.UserMemory;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class FinanceImpl implements Finance {
	private static Log log = LogFactory.getLog(FinanceImpl.class);
	private static final long TIME = Configuration.getTime();
	private static final ConcurrentHashMap<Long, FinanceBean> MEMORY = new ConcurrentHashMap<Long, FinanceBean>();
	private static final ConcurrentHashMap<Long, FinanceBean> VARMEMORY = new ConcurrentHashMap<Long, FinanceBean>();
	private static final ThreadPoolExecutorTimer TIMER = ThreadPoolExecutorTimer.getIntance();
	private static final int DROPTIME = Configuration.getDropTime();
	private static final int PERSISTTIME = Configuration.getPersistTime();
	private static final Object LOCK = new Object();
	private static FinanceImpl instance = new FinanceImpl();
	private static final int INITCOIN = Configuration.getInitCoin();
	private static final int INITROCK = Configuration.getInitRock();
	private static final int INITMETAL = Configuration.getInitMetal();
	private static final int INITCRYSTAL = Configuration.getInitCrystal();
	private static final int INITSOUL = Configuration.getInitSoul();
	private static final int INITENERGY = Configuration.getInitEnergy();
	private static final int INITGLORY = Configuration.getInitGlory();
	private static final int INITCASH = Configuration.getInitCash();
	private static final int INITSYSTEMCASH = Configuration.getInitSystemCash();
	private static final int INITBADGE = Configuration.getInitBadge();
	private FinanceImpl(){
		TIMER.getPreciseTimer().scheduleAtFixedRate(new Persister(), TIME, TIME, TimeUnit.MILLISECONDS);
	}
	
	public static FinanceImpl instance(){
		if(instance == null){
			synchronized (LOCK){
				if(instance == null){
					instance = new FinanceImpl();
				}
			}
		}
		return instance;
	}
	
	private class Persister implements Runnable{
		public void run() {
			try{
				Iterator<Long> ite1 = VARMEMORY.keySet().iterator();
				while(ite1.hasNext()){
					try{
						long id = ite1.next();
						FinanceBean bean = VARMEMORY.get(id);
						if(bean.isChange() && System.currentTimeMillis() - bean.getTime() > PERSISTTIME){
							update(bean);
							bean.setTime(System.currentTimeMillis());
							//bean.setPerviousTime(System.currentTimeMillis());
						}else if(System.currentTimeMillis() - bean.getTime() > DROPTIME){
							VARMEMORY.remove(id);
						}
					}catch(Exception e){
						log.error(e, e);
					}
				}
				
				Iterator<Long> ite2 = MEMORY.keySet().iterator();
				while(ite2.hasNext()){
					try{
						long id = ite2.next();
						FinanceBean bean = MEMORY.get(id);
						if(System.currentTimeMillis() - bean.getTime() > DROPTIME){
							MEMORY.remove(id);
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
	
	private boolean update(FinanceBean bean) {
		if(bean != null /*&& bean.isMaster() */&& bean.isChange()){
			Session session = null;
			Transaction tr = null;
			try {
				session = HibernateUtil.currentSession(bean.getId());
				tr = session.beginTransaction();
				String sql = "update financebean set cash = cash + " + bean.getCash() + 
							  ", coin = coin + " + bean.getCoin() + 
							  ", crystal = crystal + " + bean.getCrystal() +
							  ", energy = energy + " + bean.getEnergy() + 
							  ", glory = glory + " + bean.getGlory() + 
							  ", maxMmarkID = maxMmarkID + "  + bean.getMaxMmarkID() + 
							  ", metal = metal + " + bean.getMetal() + 
							  ", rock = rock + " + bean.getRock() + 
							  ", soul = soul + " + bean.getSoul() + 
							  ", systemCash = systemCash + " + bean.getSystemCash() + 
							  ", badge = badge + " + bean.getBadge() + 
							  " where id = " + bean.getId();
				session.createSQLQuery(sql).executeUpdate();
				tr.commit();
				bean.setCash(0);
				bean.setCoin(0);
				bean.setCrystal(0);
				bean.setEnergy(0);
				bean.setGlory(0);
				bean.setMaxMmarkID(0);
				bean.setMetal(0);
				bean.setRock(0);
				bean.setSoul(0);
				bean.setSystemCash(0);
				bean.setBadge(0);
				bean.setChange(false);
			} catch (Exception e) {
				tr.rollback();
				log.error(e, e);
			} finally {
				HibernateUtil.closeSession(session);
			}
		
		}
		return true;
	}
	
	private FinanceBean getFinanceFromDB(Long id){
		Session session = null;
		FinanceBean bean = null;
		try{
			session = HibernateUtil.currentSession(id);
			bean = (FinanceBean) session.get(FinanceBean.class, id);
		}catch(Exception e){
			log.error(e, e);
		}finally{
			HibernateUtil.closeSession(session);
		}
		return bean ;
	}
	
	public FinanceBean getBaseFinance(long id, int type) {//0 MEMORY 1 VARMEMEORY 
		ConcurrentHashMap<Long, FinanceBean> temp = MEMORY;
		if(type == 1){
			temp = VARMEMORY;
		} 
		FinanceBean bean = temp.get(id);
		if(bean == null){
			try {
				if(type ==0 ){
					bean = getFinanceFromDB(id);
				}
				if(bean == null){
					bean = new FinanceBean();
					bean.setId(id);
				}
				temp.putIfAbsent(id, bean);
			} catch (Exception e) {
				log.error(e, e);
			}
		}
		if(bean != null){
			bean.setTime(System.currentTimeMillis());
		}
		return bean;
	}
	
	public FinanceBean getVarFinance(long id) {
		return getBaseFinance(id, 1);
	}
	
	public FinanceBean getMemFinance(long id) {
		return getBaseFinance(id, 0);
	}
	
	@Override
	public FinanceBean getFinance(long id) {
		FinanceBean bean = getMemFinance(id);
		return bean;
	}
	
	@Override
	public boolean afford(FinanceBean augend, FinanceBean addend){
		if(addend.getSystemCash() != 0 && augend.getSystemCash() + addend.getSystemCash() < 0 ){
			return false;
		}
		if(addend.getCash() != 0 && augend.getCash() + addend.getCash() < 0){
			return false;
		}
		if(addend.getCoin() != 0 && augend.getCoin() + addend.getCoin() < 0){
			return false;
		}
		if(addend.getCrystal() != 0 && augend.getCrystal() + addend.getCrystal() < 0){
			return false;
		}
		if(addend.getEnergy() != 0 && augend.getEnergy() + addend.getEnergy() < 0){
			return false;
		}
		if(addend.getGlory() != 0 && augend.getGlory() + addend.getGlory() < 0){
			return false;
		}
		if(addend.getMaxMmarkID() != 0 && augend.getMaxMmarkID() + addend.getMaxMmarkID() < 0){
			return false;
		}
		if(addend.getMetal() != 0 && augend.getMetal() + addend.getMetal() < 0){
			return false;
		}
		if(addend.getRock() != 0 && augend.getRock() + addend.getRock() < 0){
			return false;
		}
		if(addend.getSoul() != 0 && augend.getSoul() + addend.getSoul() < 0){
			return false;
		}
		if(addend.getBadge() != 0 && augend.getBadge() + addend.getBadge() < 0){
			return false;
		}
		return true;
	}
	
	public void addChange(FinanceBean var, double cash, int coin, int crystal, int energy, int glory, int maxMmarkID, int metal, int rock,
							int soul, double systemCash, int badge){
		var.setCash(var.getCash() + cash);
		var.setCoin(var.getCoin() + coin);
		var.setCrystal(var.getCrystal() + crystal);
		var.setEnergy(var.getEnergy() + energy);
		var.setGlory(var.getGlory() + glory);
		var.setMaxMmarkID(var.getMaxMmarkID() + maxMmarkID);
		var.setMetal(var.getMetal() + metal);
		var.setRock(var.getRock() + rock);
		var.setSoul(var.getSoul() + soul);
		var.setSystemCash(var.getSystemCash() + systemCash);
		var.setBadge(var.getBadge() + badge);
		var.setChange(true);
	
	}
	
	private void increase(FinanceBean augend, FinanceBean addend) {
		double cash = addend.getCash();
		int coin = addend.getCoin();
		int crystal = addend.getCrystal();
		int energy = addend.getEnergy();
		int glory = addend.getGlory();
		int maxMmarkID = addend.getMaxMmarkID();
		int metal = addend.getMetal();
		int rock = addend.getRock();
		int soul = addend.getSoul();
		double systemCash = addend.getSystemCash();
		int badge = addend.getBadge();
		if(augend.getCash() + addend.getCash() < 0){
			cash = -augend.getCash();
		}
		if(augend.getCoin() + addend.getCoin() < 0){
			coin = -augend.getCoin();
		}
		if(augend.getCrystal() + addend.getCrystal() < 0){
			crystal = -augend.getCrystal();
		}
		if(addend.getEnergy() != 0){
			try{
				//CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, UserMemory.getCastle(augend.getId()).getCastleID());
				UserCastle us = UserMemory.getCastle(augend.getId());
				if(augend.getEnergy() + addend.getEnergy() > us.getEnergyLimit()){
					energy = us.getEnergyLimit() - augend.getEnergy();
				}
			}catch(Exception e){
				log.error(e, e);
				if(augend.getEnergy() + addend.getEnergy() < 0){
					energy = -augend.getEnergy();
				}
			}
		}
		if(augend.getGlory() + addend.getGlory() < 0){
			glory = -augend.getGlory();
		}
		if(augend.getMaxMmarkID() + addend.getMaxMmarkID() < 0){
			maxMmarkID = -augend.getMaxMmarkID();
		}
		if(augend.getMetal() + addend.getMetal() < 0){
			metal = -augend.getMetal();
		}
		if(augend.getRock() + addend.getRock() < 0){
			rock = -augend.getRock();
		}
		if(addend.getSoul() != 0){
			try{
				CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, UserMemory.getCastle(augend.getId()).getCastleID());
				if(augend.getSoul() + addend.getSoul() > castleBean.getSoulVol()){
					soul = castleBean.getSoulVol() - augend.getSoul();
				}
			}catch(Exception e){
				log.error(e, e);
				if(augend.getSoul() + addend.getSoul() < 0){
					soul = -augend.getSoul();
				}
			}
		}
		if(augend.getSystemCash() + addend.getSystemCash() < 0){
			systemCash = -augend.getSystemCash();
		}
		if(augend.getBadge() + addend.getBadge() < 0){
			badge = -augend.getBadge();
		}
		addChange(augend, cash, coin, crystal, energy, glory, maxMmarkID, metal, rock, soul, systemCash, badge);
		FinanceBean var = getVarFinance(augend.getId());
		addChange(var, cash, coin, crystal, energy, glory, maxMmarkID, metal, rock, soul, systemCash, badge);
	}
	
	@Override
	public boolean charge(FinanceBean addend) {
		try {
			FinanceBean bean = getMemFinance(addend.getId());
			FinanceBean augend = bean;
			if(augend == addend){
				log.warn("bad addend!");
			}else{
				if(afford(augend, addend)){
					increase(augend, addend);
					bean.setChange(true);
					return true;
				}else{
					log.error("the finance can't afford the value");
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return false;
	}
	
	@Override
	public boolean consume(FinanceBean addend) {
		boolean b = false;
		try {
			FinanceBean bean = getMemFinance(addend.getId());
			FinanceBean augend = bean;
			if(augend == addend){
				log.warn("bad addend!");
			}else{
				increase(augend, addend);
				bean.setChange(true);
				return true;
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		return b;
	}
	
	@Override
	public void offline(long id) {
		FinanceBean bean = VARMEMORY.get(id);
		update(bean);
		VARMEMORY.remove(id);
		MEMORY.remove(id);
	}
	
	public static FinanceBean initFinance(long id){
		FinanceBean finance = instance.getFinance(id);
		try{
			finance.setId(id);
			finance.setMaxMmarkID(1);//0标志城堡
			finance.setCash(INITCASH);
			finance.setCoin(INITCOIN);
			finance.setCrystal(INITCRYSTAL);
			finance.setEnergy(INITENERGY);
			finance.setGlory(INITGLORY);
			finance.setMetal(INITMETAL);
			finance.setRock(INITROCK);
			finance.setSoul(INITSOUL);
			finance.setSystemCash(INITSYSTEMCASH);
			finance.setBadge(INITBADGE);
			DBUtil.save(id, finance);
			finance.setTime(System.currentTimeMillis());
			MEMORY.put(id, finance);
		}catch(Exception e){
			log.error(e, e);
		}
		return finance;
	}

	public static boolean resetFinance(long id, Session session){
		session.createSQLQuery("update financebean set energy = " + INITENERGY + " , soul = " + INITSOUL + " where id = " + id).executeUpdate();
		return true;
	}
}

package com.server.user.operation;

import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.AttackMonster;
import com.database.model.bean.FightBean;
import com.database.model.bean.FinanceBean;
import com.database.model.bean.TimesBean;
import com.database.model.bean.TitleBean;
import com.database.model.bean.UserCastle;
import com.server.cache.UserMemory;
import com.server.finance.Finance;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;

public class ExpGloryOp {
	private static Log log = LogFactory.getLog(ExpGloryOp.class);
	private static Finance financeImpl = FinanceImpl.instance();
	
	public static void addGlory(long id, FightBean fightBean, TimesBean timesBean, List<Integer> monsterID) {
		List<AttackMonster> monsters = fightBean.getMonsters();
		List<AttackMonster> bosses = fightBean.getBosses();
		int monsterNum = getNum(monsters, monsterID);
		int bossNum = getNum(bosses, monsterID);
		FinanceBean bean = new FinanceBean();
		bean.setId(id);
		bean.setGlory(monsterNum * timesBean.getMonsterGlory() + bossNum * timesBean.getBossGlory());
		bean.setSoul(monsterNum + bossNum - monsterID.size());
		financeImpl.charge(bean);
	}
	
	private static int getNum(List<AttackMonster> monsters, List<Integer> monsterID){
		int num = monsters.size();
		Iterator<Integer> ite = monsterID.iterator();
		while(ite.hasNext()){
			int id = ite.next();
			Iterator<AttackMonster> monsterIte = monsters.iterator();
			while(monsterIte.hasNext()){
				if(id ==  monsterIte.next().getId()){
					num--;
				}
			}
		}
		return num;
	}
	
	public static void getTitle(long id, byte[] information) throws Exception{
		int goodID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		TitleBean titleBean = (TitleBean)Goods.getSingleByGoodID(GoodsCate.TITLEBEAN, goodID);
		if(titleBean != null){
			UserCastle castle = UserMemory.getCastle(id);
			JSONArray ja = null;
			if(castle.getTitles() != null){
				ja = JSONArray.fromObject(castle.getTitles());
			}else{
				ja = new JSONArray();
			}
			ja.add(titleBean.getId());
			castle.setTitles(ja.toString());
			FinanceBean bean = financeImpl.getFinance(id);
			boolean suc = false;
			if(bean.getGlory() > titleBean.getGlory()){
				suc = true;
			}
			if(suc){
				castle.setChange(true);
			}else{
				throw new Exception("Game_Warnin:the glory is not enough");
			}
		}
	}

	public static void useTitle(long id, byte[] information) throws Exception{
		int goodID = DataFactory.getInt(DataFactory.get(information, 10, 4));
		UserCastle castle = UserMemory.getCastle(id);
		JSONArray ja = JSONArray.fromObject(castle.getTitles());
		TitleBean titleBean = (TitleBean)Goods.getSingleByGoodID(GoodsCate.TITLEBEAN, goodID);
		if(ja != null && titleBean!= null){
			for(int i = 0; i < ja.size(); i++){
				if(ja.getInt(i) == titleBean.getId()){
					castle.setCurrTitle(titleBean.getId());
					castle.setChange(true);
				}
			}
		}else{
			throw new Exception("Game_Warning:id:" + id + " hasn't title:" + goodID);
		}
	}
}

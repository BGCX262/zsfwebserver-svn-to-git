package com.server.gameDate.statistics;

import java.io.Serializable;
import java.util.Calendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.util.Increase;
import com.cindy.run.util.PersistDatabaseData;
import com.cindy.run.util.Update;

public class PersistGoodsCost {
	private static Log log = LogFactory.getLog(PersistGoodsCost.class);
	private static PersistDatabaseData<GoodsCost> persist = null;
	private static PersistGoodsCost instance = new PersistGoodsCost();
	public static PersistGoodsCost getInstance(){
		return instance;
	}
	
	public PersistGoodsCost(){
		start();
	}
	
	private class GoodsCostID implements Serializable{
		private int day;
		private int goods;
		private int costType;
		private Calendar cadar = Calendar.getInstance();
		public GoodsCostID(){
			
		}
		
		public GoodsCostID(int goods,int costType){
			this.day = splitInt(cadar.get(Calendar.YEAR), cadar.get(Calendar.MONTH) + 1,cadar.get(Calendar.DATE));
			this.goods = goods;
			this.costType = costType;
		}
		
		public int getDay() {
			return day;
		}

		public void setDay(int day) {
			this.day = day;
		}

		public int getGoods() {
			return goods;
		}

		public void setGoods(int goods) {
			this.goods = goods;
		}

		public int getCostType() {
			return costType;
		}

		public void setCostType(int costType) {
			this.costType = costType;
		}

		public boolean equals(Object obj) {
			if(this == obj) {
				return true;
			}
			if(!(obj instanceof GoodsCostID)) {
				return false;
			}
			GoodsCostID CostID = (GoodsCostID) obj;
			return CostID.getDay() == day && CostID.getCostType() == costType && CostID.getGoods() == goods;
		}
		
		public int hashCode() {
			int result = 17;
			return result + day + costType + goods;
		}
		
		public int splitInt(int year,int month ,int day){
			String date = "";
			date = date + year + month + day;
			return Integer.parseInt(date.toString());
		}
	}
	
	private class GoodsCost {
		private GoodsCostID ID;
		private int num;
		
		public GoodsCost(int goods,int costType,int num){
			this.ID = new GoodsCostID(goods,costType);
			this.num = num;
		}
		public GoodsCostID getID() {
			return ID;
		}

		public void setID(GoodsCostID id) {
			ID = id;
		}
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
	}
	
	private PersistDatabaseData<GoodsCost> init() {
		PersistDatabaseData<GoodsCost> persist = new PersistDatabaseData<GoodsCost>(GoodsCost.class);
		persist.setSynchron(true);
		return persist;
	}
	
	public void start() {
		persist = init();
		Increase<GoodsCost> increaseContent = new Increase<GoodsCost>() {
			public GoodsCost increase(GoodsCost augend, GoodsCost addend) {
				augend.setNum(augend.getNum() + addend.getNum());
				return augend;
			}
		};
		Update<GoodsCost> updateContent = new Update<GoodsCost>() {
			public boolean update(GoodsCost data) {
				try {
					String executeQuery = "insert into t_goodscost_collect values(current_date," + data.getID().getCostType() + "," + data.getID().getGoods() + "," + data.getNum() +") on duplicate key update num = num + " + data.getNum();
					DBOperateUtil.execute(executeQuery);
				} catch (Exception e) {
					log.error(e, e);
					return false;
				}
				return true;
			}
		};
		persist.start(increaseContent, updateContent);
	}
	
	public void increase(int goods,int costType,int num){
		try {
			GoodsCost data = new GoodsCost(goods,costType,num);
			persist.increase(data.getID(), data);
		} catch (InterruptedException e) {
			log.error(e, e);
		}
		return;
	}
}
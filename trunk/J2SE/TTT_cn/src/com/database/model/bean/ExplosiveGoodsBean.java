package com.database.model.bean;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

public class ExplosiveGoodsBean {
	private int goods;
	private double rate;
	public int getGoods() {
		return goods;
	}
	public void setGoods(int goods) {
		this.goods = goods;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	
	public static String encode(List<ExplosiveGoodsBean> explosiveGoodsList){
		JSONArray ja = new JSONArray();
		if(explosiveGoodsList != null){
			Iterator<ExplosiveGoodsBean> ite = explosiveGoodsList.iterator();
			while(ite.hasNext()){
				ExplosiveGoodsBean bean =  ite.next();
				JSONArray jar = new JSONArray();
				jar.add(bean.getGoods());
				jar.add(bean.getRate());
				ja.add(jar);
			}
		}
		return ja.toString();
	}
	
	public static List<ExplosiveGoodsBean> decode(String jaStr){
		List<ExplosiveGoodsBean> explosiveGoodsList = new LinkedList<ExplosiveGoodsBean>();
		if(jaStr != null && !jaStr.equals("")){
			JSONArray ja = JSONArray.fromObject(jaStr);
			for(int i = 0; i < ja.size(); i++){
				JSONArray jar = ja.getJSONArray(i);
				if(jar != null){
					ExplosiveGoodsBean bean = new ExplosiveGoodsBean();
					bean.setGoods(jar.getInt(0));
					bean.setRate(jar.getDouble(1));
					explosiveGoodsList.add(bean);
				}
			}
		}
		return explosiveGoodsList;
	}
}

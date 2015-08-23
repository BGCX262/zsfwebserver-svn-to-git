package com.database.model.bean;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.json.JSONArray;

public class DropGoodsBean implements Serializable{
	private static Log log = LogFactory.getLog(DropGoodsBean.class);
	private static final long serialVersionUID = 1L;
	private int goodsID;
	private int num;
	
	public DropGoodsBean(){
	}
	
	public DropGoodsBean(int goodsID, int num){
		this.goodsID = goodsID;
		this.num = num;
	}
	
	public int getGoodsID() {
		return goodsID;
	}
	public void setGoodsID(int goodsID) {
		this.goodsID = goodsID;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String encode2Ja(){
		JSONArray ja = new JSONArray();
		ja.add(this.goodsID);
		ja.add(this.num);
		return ja.toString();
	}
	public static DropGoodsBean decode2Bean(String jaStr){
		DropGoodsBean bean = null;
		if(jaStr != null){
			JSONArray ja = JSONArray.fromObject(jaStr);
			bean = new DropGoodsBean();
			bean.setGoodsID(ja.getInt(0));
			bean.setNum(ja.getInt(1));
		}
		return bean;
	}
	
	public static String encode2Ja(List<DropGoodsBean> goodsList){
		JSONArray ja = new JSONArray();
		if(goodsList != null){
			Iterator<DropGoodsBean> ite = goodsList.iterator();
			while(ite.hasNext()){
				DropGoodsBean goods = ite.next();
				ja.add(goods.encode2Ja());
			}
		}
		return ja.toString();
	}
	
	public static List<DropGoodsBean> decode2List(String jaStr){
		List<DropGoodsBean> list = new LinkedList<DropGoodsBean>();
		if(jaStr != null){
			JSONArray ja = JSONArray.fromObject(jaStr);
			if(ja != null){
				for(int i = 0; i < ja.size(); i++){
					DropGoodsBean bean = decode2Bean(ja.getString(i));
					if(bean != null){
						list.add(bean);
					}
				}
			}
		}
		return list;
	}
	
	public String toString() {
		return getGoodsID() + "=" + getNum();
	}
}

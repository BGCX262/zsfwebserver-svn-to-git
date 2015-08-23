package com.database.model.bean;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

public class AttackMonster {
	private int id;
	private int goodID; //怪物GoodID
	private List<DropGoodsBean> dropGoods;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<DropGoodsBean> getDropGoods() {
		return dropGoods;
	}
	public void setDropGoods(List<DropGoodsBean> dropGoods) {
		this.dropGoods = dropGoods;
	}
	public int getGoodID() {
		return goodID;
	}
	public void setGoodID(int goodID) {
		this.goodID = goodID;
	}
	public String encode2Ja(){
		JSONArray ja = new JSONArray();
		ja.add(id);
		ja.add(goodID);
		JSONArray jar = new JSONArray();
		if(this.dropGoods != null){
			Iterator<DropGoodsBean> goodsIte = this.dropGoods.iterator();
			while(goodsIte.hasNext()){
				DropGoodsBean goods = goodsIte.next();
				jar.add(goods.encode2Ja());
			}
		}
		ja.add(jar.toString());
		return ja.toString();
	}
	
	public static AttackMonster decode2Bean(String strJa){
		AttackMonster monster = null;
		if(strJa != null){
			monster = new AttackMonster();
			JSONArray ja = JSONArray.fromObject(strJa);
			monster.setId(ja.getInt(0));
			monster.setGoodID(ja.getInt(1));
			List<DropGoodsBean> goodsList = new LinkedList<DropGoodsBean>();
			JSONArray jar = ja.getJSONArray(2);
			for(int i = 0; i < jar.size(); i++){
				String str = jar.getString(i);
				if(str != null){
					goodsList.add(DropGoodsBean.decode2Bean(str));
				}
			}
			monster.setDropGoods(goodsList);
		}
		return monster;
	}
}

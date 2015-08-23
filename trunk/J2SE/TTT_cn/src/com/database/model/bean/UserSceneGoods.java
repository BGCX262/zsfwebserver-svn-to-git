package com.database.model.bean;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.sf.json.JSONArray;

public class UserSceneGoods implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private int type;//0boss 1普通  2系统刷 3黄金怪
	private List<DropGoodsBean> dropGoods;
	private long creater;//生成包人的ID
	private long time;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<DropGoodsBean> getDropGoods() {
		return dropGoods;
	}
	public void setDropGoods(List<DropGoodsBean> dropGoods) {
		this.dropGoods = dropGoods;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long getCreater() {
		return creater;
	}
	public void setCreater(long creater) {
		this.creater = creater;
	}
	public static List<UserSceneGoods> decodeFromJa(JSONArray ja){
		List<UserSceneGoods> list = new LinkedList<UserSceneGoods>();
		if(ja != null){
			for(int i = 0; i < ja.size(); i++){
				JSONArray jaa = ja.getJSONArray(i);
				if(jaa != null){
					UserSceneGoods goods = new UserSceneGoods();
					goods.setId(jaa.getInt(0));
					goods.setType(jaa.getInt(1));
					goods.setTime(jaa.getLong(2));
					goods.setCreater(jaa.getLong(3));
					List<DropGoodsBean> goodsList = new LinkedList<DropGoodsBean>();
					JSONArray jar = jaa.getJSONArray(4);
					for(int j = 0; j < jar.size(); j++){
						JSONArray jarry = jar.getJSONArray(j);
						DropGoodsBean goodsBean = new DropGoodsBean();
						goodsBean.setGoodsID(jarry.getInt(0));
						goodsBean.setNum(jarry.getInt(1));
						goodsList.add(goodsBean);
					}
					goods.setDropGoods(goodsList);
					list.add(goods);
				}
			}
		}
		return list;
	}
	
	public static String encode2Ja(List<UserSceneGoods> list){
		JSONArray ja = new JSONArray();
		if(list != null){
			Iterator<UserSceneGoods> ite = list.iterator();
			while(ite.hasNext()){
				JSONArray jaa = new JSONArray();
				UserSceneGoods goods = ite.next();
				jaa.add(goods.getId());
				jaa.add(goods.getType());
				jaa.add(goods.getTime());
				jaa.add(goods.getCreater());
				JSONArray jarray = new JSONArray();
				List<DropGoodsBean> goodsList = goods.getDropGoods();
				for(int i = 0; i < goodsList.size(); i++){
					DropGoodsBean bean = goodsList.get(i);
					JSONArray jar = new JSONArray();
					jar.add(bean.getGoodsID());
					jar.add(bean.getNum());
					jarray.add(jar);
				}
				jaa.add(jarray);
				ja.add(jaa);
			}
		}
		return ja.toString();
	}
	
/*	public static void main(String[] args) {
		List<UserSceneGoods> list = new LinkedList<UserSceneGoods>();
		UserSceneGoods goods = new UserSceneGoods();
		goods.setId(1);
		goods.setType(1);
		goods.setTime(System.currentTimeMillis());
		List<DropGoodsBean> li = new LinkedList<DropGoodsBean>();
		DropGoodsBean d = new DropGoodsBean();
		d.setGoodID(101);
		d.setNum(10);
		li.add(d);
		goods.setGoodID(li);
		list.add(goods);
		
		UserSceneGoods goods2 = new UserSceneGoods();
		goods2.setId(2);
		goods2.setType(2);
		goods2.setTime(System.currentTimeMillis());
		goods2.setGoodID(li);
		list.add(goods2);
		
		List<UserSceneGoods> lis = decodeFromJa(JSONArray.fromObject(goods.encode2Ja(list)));
		System.out.println(lis.get(0).getGoodID().get(0).getGoodID());
	}*/
}

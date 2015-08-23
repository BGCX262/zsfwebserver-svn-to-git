package com.database.model.bean;

import net.sf.json.JSONArray;

public class TurretBean {
	private int id;
	private Integer stoneID;//镶嵌宝石的goodID
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Integer getStoneID() {
		return stoneID;
	}
	public void setStoneID(Integer stoneID) {
		this.stoneID = stoneID;
	}
	public static TurretBean decode2Bean(JSONArray ja) {
		if(ja != null){
			TurretBean bean = new TurretBean();
			bean.setId(ja.getInt(0));
			Object obj = ja.opt(1);
			if(!obj.equals(null)){
				bean.setStoneID((Integer)obj);
			}
			return bean;
		}
		return null;
	}
	public String encode2Ja() {
		JSONArray ja = new JSONArray();
		ja.add(id);
		ja.add(stoneID);
		return ja.toString();
	}
}

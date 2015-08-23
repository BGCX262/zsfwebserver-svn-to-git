package com.database.model.bean;

import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

public class TimesBean implements Cloneable{
	private int id;
	private int cityID;//关ID
	private int monsterHp;//怪物HP
	private int monsterGoodID;//怪物goodsID
	private int monsterGlory;//打怪物获得glory
	private int monsterNum;//普通怪物个数
	private int bossNum;//boss个数
	private int bossHp;//boss HP
	private int bossGoodID;//boss goodsID
	private int BossGlory;//打怪物获得glory
	private String dropGoods;//必掉物品JSONArray格式[[20001, 1],[20003, 1]]
	private String explorsiveGoods;//JSONArray格式[[20001, 0.20],[20003,0.10]]
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	public int getMonsterHp() {
		return monsterHp;
	}
	public void setMonsterHp(int monsterHp) {
		this.monsterHp = monsterHp;
	}
	public int getBossHp() {
		return bossHp;
	}
	public void setBossHp(int bossHp) {
		this.bossHp = bossHp;
	}
	public int getMonsterGoodID() {
		return monsterGoodID;
	}
	public void setMonsterGoodID(int monsterGoodID) {
		this.monsterGoodID = monsterGoodID;
	}
	public int getBossGoodID() {
		return bossGoodID;
	}
	public void setBossGoodID(int bossGoodID) {
		this.bossGoodID = bossGoodID;
	}
	public int getBossNum() {
		return bossNum;
	}
	public void setBossNum(int bossNum) {
		this.bossNum = bossNum;
	}
	public int getMonsterGlory() {
		return monsterGlory;
	}
	public void setMonsterGlory(int monsterGlory) {
		this.monsterGlory = monsterGlory;
	}
	public int getBossGlory() {
		return BossGlory;
	}
	public void setBossGlory(int bossGlory) {
		BossGlory = bossGlory;
	}
	public String getExplorsiveGoods() {
		return explorsiveGoods;
	}
	public void setExplorsiveGoods(String explorsiveGoods) {
		this.explorsiveGoods = explorsiveGoods;
	}
	public String getDropGoods() {
		return dropGoods;
	}
	public void setDropGoods(String dropGoods) {
		this.dropGoods = dropGoods;
	}
	public int getMonsterNum() {
		return monsterNum;
	}
	public void setMonsterNum(int monsterNum) {
		this.monsterNum = monsterNum;
	}
	public static List<DropGoodsBean> decodeDropGoods(String jaStr){
		List<DropGoodsBean> list = new LinkedList<DropGoodsBean>();
		if(jaStr != null && !jaStr.equals("")){
			JSONArray ja = JSONArray.fromObject(jaStr);
			for(int i = 0; i < ja.size(); i++){
				list.add(DropGoodsBean.decode2Bean(ja.getString(i)));
			}
		}
		return list;
	}
	
	public TimesBean clone(){
		TimesBean o = null;
		try{
			o = (TimesBean)super.clone();
		}catch(CloneNotSupportedException e){
			e.printStackTrace();
		}
		return o;
	}
}

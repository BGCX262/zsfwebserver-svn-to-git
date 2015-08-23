package com.database.model.bean;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

public class PVPRecordBean implements Serializable {
	private static final long serialVersionUID = 8199327717859339858L;
	private long id;
	private int hp;//玩家血量
	private int coin;//玩家coin变量
	private int rock;//玩家rock变量                
	private int metal;//玩家metal变量              
	private int crystal;//玩家crystal变量  
	private List<Integer> usedProps;
	private List<Integer> lossGoods;
	private String usedPropsInfo;
	private String lossGoodsInfo;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public int getCoin() {
		return coin;
	}
	public void setCoin(int coin) {
		this.coin = coin;
	}
	public List<Integer> getLossGoods() {
		return lossGoods;
	}
	public void setLossGoods(List<Integer> lossGoods) {
		this.lossGoods = lossGoods;
	}
	public String getLossGoodsInfo() {
		return lossGoodsInfo;
	}
	public void setLossGoodsInfo(String lossGoodsInfo) {
		this.lossGoodsInfo = lossGoodsInfo;
	}
	public int getRock() {
		return rock;
	}
	public void setRock(int rock) {
		this.rock = rock;
	}
	public int getMetal() {
		return metal;
	}
	public void setMetal(int metal) {
		this.metal = metal;
	}
	public int getCrystal() {
		return crystal;
	}
	public void setCrystal(int crystal) {
		this.crystal = crystal;
	}
	public List<Integer> getUsedProps() {
		return usedProps;
	}
	public void setUsedProps(List<Integer> usedProps) {
		this.usedProps = usedProps;
	}
	public String getUsedPropsInfo() {
		return usedPropsInfo;
	}
	public void setUsedPropsInfo(String usedPropsInfo) {
		this.usedPropsInfo = usedPropsInfo;
	}
	public void encode(){
		JSONArray ja = new JSONArray();
		if(lossGoods != null){
			Iterator<Integer> ite = lossGoods.iterator();
			while(ite.hasNext()){
				ja.add(ite.next());
			}
		}
		lossGoodsInfo = ja.toString();
		
		JSONArray jar = new JSONArray();
		if(usedProps != null){
			Iterator<Integer> ite = usedProps.iterator();
			while(ite.hasNext()){
				jar.add(ite.next());
			}
		}
		usedPropsInfo = jar.toString();
	}
	
	public void decode(){
		if(lossGoodsInfo != null){
			lossGoods = JSONArray.toList(JSONArray.fromObject(lossGoodsInfo));
		}
		if(lossGoods == null){
			lossGoods = new LinkedList<Integer>();
		}	
		if(usedPropsInfo != null){
			usedProps = JSONArray.toList(JSONArray.fromObject(usedPropsInfo));
		}
		if(usedProps == null){
			usedProps = new LinkedList<Integer>();
		}
	}
}

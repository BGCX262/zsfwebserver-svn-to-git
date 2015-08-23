package com.server.log;

import net.sf.json.JSONArray;

public class ThingBean {
	private int pos; // 1仓库  2场景背包  3存物箱  4场景炮塔 5任务队列 6对战获得  7经济体系 8奴隶
	private int posID;
	private int goodID;
	private int num;
	JSONArray marks;
	
	public ThingBean(){}
	public ThingBean(int pos, int posID, int goodID, int num, JSONArray marks){
		this.pos = pos;
		this.posID = posID;
		this.goodID = goodID;
		this.num = num;
		this.marks = marks;
	}
	public int getPos() {
		return pos;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public int getPosID() {
		return posID;
	}
	public void setPosID(int posID) {
		this.posID = posID;
	}
	public int getGoodID() {
		return goodID;
	}
	public void setGoodID(int goodID) {
		this.goodID = goodID;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public JSONArray getMarks() {
		return marks;
	}
	public void setMarks(JSONArray marks) {
		this.marks = marks;
	}
	/*public static String encode2Ja(ThingBean bean){
		JSONArray ja = new JSONArray();
		ja.add(bean);
		return ja.toString();
	}*/
}

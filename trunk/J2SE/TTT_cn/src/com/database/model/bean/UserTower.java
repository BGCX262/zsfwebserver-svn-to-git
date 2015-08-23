package com.database.model.bean;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.sf.json.JSONArray;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserTower {
	private static Log log = LogFactory.getLog(UserTower.class);
	private int id;
	private List<TurretBean> turrets;//镶嵌宝石的goodID
	private int towerID;
	private int state;//0正常  1建造  2升级 3维修
	private long time;//上次减hp时间
	private int hp;
	private long endTime;
	private int posID;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTowerID() {
		return towerID;
	}
	public void setTowerID(int towerID) {
		this.towerID = towerID;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public long getEndTime() {
		return endTime;
	}
	public int getPosID() {
		return posID;
	}
	public void setPosID(int posID) {
		this.posID = posID;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public List<TurretBean> getTurrets() {
		return turrets;
	}
	public void setTurrets(List<TurretBean> turrets) {
		this.turrets = turrets;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public static List<UserTower> decodeFromJa(JSONArray ja){
		List<UserTower> list = new LinkedList<UserTower>();
		try{
			if(ja != null){
				for(int i = 0; i < ja.size(); i++){
					UserTower towers = new UserTower();
					JSONArray array = ja.getJSONArray(i);
					towers.setId(array.getInt(0));
					towers.setPosID(array.getInt(1));
					towers.setTowerID(array.getInt(2));
					towers.setState(array.getInt(3));
					towers.setEndTime(array.getLong(4));
					towers.setHp(array.getInt(5));
					towers.setTime(array.getLong(6));
					List<TurretBean> turrets = new LinkedList<TurretBean>();
					JSONArray jar = array.getJSONArray(7);
					for(int j = 0; j < jar.size(); j++){
						turrets.add(TurretBean.decode2Bean(JSONArray.fromObject(jar.getJSONArray(j))));
					}
					towers.setTurrets(turrets);
					list.add(towers);
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return list;
	}
	public static JSONArray encode2Ja(List<UserTower> list){
		JSONArray array = new JSONArray();
		try{
			if(list != null && list.size() > 0){
				for(int i = 0; i < list.size(); i++){
					JSONArray ja = new JSONArray();
					UserTower towers = list.get(i);
					ja.add(towers.getId());
					ja.add(towers.getPosID());
					ja.add(towers.getTowerID());
					ja.add(towers.getState());
					ja.add(towers.getEndTime());
					ja.add(towers.getHp());
					ja.add(towers.getTime());
					JSONArray jaa = new JSONArray();
					List<TurretBean> turrets = towers.getTurrets();
					if(turrets != null){
						Iterator<TurretBean> ite = turrets.iterator();
						while(ite.hasNext()){
							TurretBean turret = ite.next();
							jaa.add(turret.encode2Ja());
						}
					}
					ja.add(jaa.toString());
					array.add(ja);
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return array;
	}
}

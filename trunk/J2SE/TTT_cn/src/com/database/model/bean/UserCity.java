package com.database.model.bean;

import java.util.List;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserCity{
	private static Log log = LogFactory.getLog(UserCity.class);
	private long id;
	private long time;
	private volatile boolean change;
	private int cityID;
	private int cityNo;//关卡地图编号
	private volatile int currTimesNum;
	private List<UserTower> towers;
	private List<UserSceneGoods> goods;
	private boolean prize;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<UserTower> getTowers() {
		return towers;
	}
	public void setTowers(List<UserTower> towers) {
		this.towers = towers;
	}
	public int getCurrTimesNum() {
		return currTimesNum;
	}
	public void setCurrTimesNum(int currTimesNum) {
		this.currTimesNum = currTimesNum;
	}
	public int getCityID() {
		return cityID;
	}
	public void setCityID(int cityID) {
		this.cityID = cityID;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public boolean isChange() {
		return change;
	}
	public void setChange(boolean change) {
		this.change = change;
	}
	public List<UserSceneGoods> getGoods() {
		return goods;
	}
	public void setGoods(List<UserSceneGoods> goods) {
		this.goods = goods;
	}
	public boolean isPrize() {
		return prize;
	}
	public void setPrize(boolean prize) {
		this.prize = prize;
	}
	public int getCityNo() {
		return cityNo;
	}
	public void setCityNo(int cityNo) {
		this.cityNo = cityNo;
	}
	public static UserCity decodeFromJA(UserCityInfo cityInfo){
		UserCity city = new UserCity();
		try{
			if(cityInfo != null){
				JSONObject jb = JSONObject.fromObject(cityInfo.getInfo());
				city.setId(cityInfo.getMasterID());
				city.setCityID(jb.getInt("cityID"));
				city.setCurrTimesNum(jb.getInt("currTimesNum"));
				city.setPrize(jb.getBoolean("prize"));
				city.setCityNo(jb.getInt("cityNo"));
				city.setTowers(UserTower.decodeFromJa(jb.getJSONArray("towers")));
				if(jb.containsKey("sceneGoods")){
					city.setGoods(UserSceneGoods.decodeFromJa(jb.getJSONArray("sceneGoods")));
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		return city;
	}
	public String encode2Ja(){
		JSONObject jb = new JSONObject();
		try{
			jb.put("cityID", this.cityID);
			jb.put("currTimesNum", this.currTimesNum);
			jb.put("prize", this.prize);
			jb.put("cityNo", this.cityNo);
			jb.put("towers", UserTower.encode2Ja(this.towers));
			jb.put("sceneGoods", UserSceneGoods.encode2Ja(this.goods));
		}catch(Exception e){
			log.error(e, e);
		}
		return jb.toString();
	}
}

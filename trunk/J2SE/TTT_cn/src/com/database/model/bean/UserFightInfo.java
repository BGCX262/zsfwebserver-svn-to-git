package com.database.model.bean;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.sf.json.JSONArray;

public class UserFightInfo {
	private long id;
	private String info;
	private List<FightBean> fightInfoList;
	private long time;
	private boolean change;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public List<FightBean> getFightInfoList() {
		return fightInfoList;
	}
	public void setFightInfoList(List<FightBean> fightInfoList) {
		this.fightInfoList = fightInfoList;
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
	public void decode(){
		fightInfoList = new LinkedList<FightBean>();
		if(info != null){
			if(info.charAt(0) == '['){
				if(info.charAt(1) == '['){
					JSONArray ja = JSONArray.fromObject(info);
					for(int i = 0; i < ja.size(); i++){
						FightBean temp = FightBean.decode2Bean(ja.getString(i));
						if(temp != null){
							fightInfoList.add(temp);
						}
					}
				}else{
					FightBean temp = FightBean.decode2Bean(info);
					if(temp != null){
						fightInfoList.add(temp);
					}
				}
			}
		}
	}
	
	public void encode(){
		JSONArray ja = new JSONArray();
		if(fightInfoList != null){
			Iterator<FightBean> ite = fightInfoList.iterator();
			while(ite.hasNext()){
				FightBean bean = ite.next();
				if(bean != null){
					ja.add(bean.encode2Ja());
				}
			}
		}
		info = ja.toString();
	}
}

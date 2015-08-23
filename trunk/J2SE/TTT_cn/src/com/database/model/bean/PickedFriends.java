package com.database.model.bean;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;

public class PickedFriends implements Serializable{
	private static final long serialVersionUID = 900945701680835953L;
	private long id;
	private long time;
	private List<Long> picked;
	private transient String info;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<Long> getPicked() {
		return picked;
	}
	public void setPicked(List<Long> picked) {
		this.picked = picked;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public void wrap(){
		if(picked != null){
			JSONArray ja = new JSONArray();
			Iterator<Long> ite = picked.iterator();
			while(ite.hasNext()){
				ja.add(ite.next());
			}
			info = ja.toString();
		}
	}
	
	public void unwrap(){
		if(info != null){
			JSONArray ja = JSONArray.fromObject(info);
			if(ja != null){
				picked = new LinkedList<Long>();
				for(int i = 0; i < ja.size(); i++){
					picked.add(ja.getLong(i));
				}
			}
		}
	}
} 

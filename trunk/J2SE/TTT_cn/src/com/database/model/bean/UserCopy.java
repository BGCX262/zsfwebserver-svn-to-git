package com.database.model.bean;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.sf.json.JSONArray;

public class UserCopy implements Serializable{
	private static final long serialVersionUID = 2687711010901257610L;
	private long id;
	private List<CopyBean> copyList;
	private String copy;
	private long resetTime;//重置时间
	private volatile boolean change;
	private long time;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public List<CopyBean> getCopyList() {
		return copyList;
	}
	public void setCopyList(List<CopyBean> copyList) {
		this.copyList = copyList;
	}
	public String getCopy() {
		return copy;
	}
	public void setCopy(String copy) {
		this.copy = copy;
	}
	public boolean isChange() {
		return change;
	}
	public void setChange(boolean change) {
		this.change = change;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long getResetTime() {
		return resetTime;
	}
	public void setResetTime(long resetTime) {
		this.resetTime = resetTime;
	}
	public void encode(){
		if(copyList != null && copyList.size() > 0){
			JSONArray ja = new JSONArray();
			Iterator<CopyBean>  ite = copyList.iterator();
			while(ite.hasNext()){
				CopyBean bean = ite.next();
				if(bean != null){
					JSONArray jaa = new JSONArray();
					jaa.add(bean.getCityID());
					jaa.add(bean.getCurTimesNum());
					jaa.add(bean.getOpenTime());
					ja.add(jaa);
				}
			}
			copy = ja.toString();
		}
	}
	
	public void decode(){
		copyList = new LinkedList<CopyBean>();
		if(copy != null && copy != ""){
			JSONArray ja = JSONArray.fromObject(copy);
			for(int i = 0; i < ja.size(); i++){
				JSONArray jaa = ja.getJSONArray(i);
				if(jaa != null){
					CopyBean copy = new CopyBean();
					copy.setCityID(jaa.getInt(0));
					copy.setCurTimesNum(jaa.getInt(1));
					copy.setOpenTime(jaa.getLong(2));
					copyList.add(copy);
				}
			}
		}
	}
}

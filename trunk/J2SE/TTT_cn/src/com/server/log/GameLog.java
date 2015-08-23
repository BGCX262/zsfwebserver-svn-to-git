package com.server.log;

import java.util.Iterator;
import java.util.List;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.server.util.Configuration;

public class GameLog {
	private static Log log = LogFactory.getLog(GameLog.class);
	private static boolean isWriteGameLog = Configuration.getIsWriteGameLog();
	public static void createLog(long id, int what, Integer handleID, boolean success, List<ThingBean> get, List<ThingBean> lost, String comment){
		if(isWriteGameLog){
			HandleLogBean bean = new HandleLogBean(id, what, System.currentTimeMillis(), handleID, success, get, lost, comment);
			String logInfo = create(bean);
			log.info(logInfo);
		}
	}
	
	private static String create(HandleLogBean bean){
		JSONObject json = new JSONObject();
		if(bean != null){
			json.put("who", bean.getWho());
			json.put("when", bean.getWhen());
			json.put("what", bean.getWhat());
			json.put("handle", bean.getHandle());
			json.put("success", bean.isSuccess());
			
			JSONArray getJa = new JSONArray();
			List<ThingBean> get = bean.getGet();
			if(get != null){
				Iterator<ThingBean> ite = get.iterator();
				while(ite.hasNext()){
					ThingBean thing = ite.next();
					getJa.add(thing);
				}
			}
			json.put("get", getJa);
			
			JSONArray lostJa = new JSONArray();
			List<ThingBean> lost = bean.getLost();
			if(lost != null){
				Iterator<ThingBean> ite = lost.iterator();
				while(ite.hasNext()){
					ThingBean thing = ite.next();
					lostJa.add(thing);
				}
			}
			json.put("lost", lostJa);
			json.put("comment", bean.getComment());
		}
		return json.toString();
	}
	
/*	public static HandleLogBean parseLog(String jsonStr){
		HandleLogBean bean = null;
		if(jsonStr != null){
			JSONObject json = JSONObject.fromObject(jsonStr);
			Map<String, Class<SourceBean>> map = new HashMap<String, Class<SourceBean>>();
			map.put("from", SourceBean.class);
			map.put("to", SourceBean.class);
			List<ThingBean> get = JSONArray.toList(json.getJSONArray("get"), ThingBean.class, map);
			List<ThingBean> lost = JSONArray.toList(json.getJSONArray("lost"), ThingBean.class, map);
			bean = new HandleLogBean(json.getLong("who"), json.getInt("what"),
					json.getLong("when"), json.getInt("handle"), json.getBoolean("success"),
					get,lost, json.getString("comment"));
			
		}
		return bean;
	}*/
}	

package com.ttt.instance;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import net.sf.cindy.util.ThreadPoolExecutorTimer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InstanceFactory {
	private static final Log log = LogFactory.getLog(InstanceFactory.class);
	private static final ConcurrentHashMap<Object, Instance> instanceMap = new ConcurrentHashMap<Object, Instance>();
	private static final ThreadPoolExecutorTimer timer = ThreadPoolExecutorTimer.getIntance();
	private static final InstanceFactory factory = new InstanceFactory();
	
	static{
		timer.getNewPreciseTimer().scheduleAtFixedRate(new Runnable(){
			@Override
			public void run() {
				try{
					Iterator<Object> ite = instanceMap.keySet().iterator();
					while(ite.hasNext()){
						try{
							Object key = ite.next();
							Instance instance = instanceMap.get(key);
							if(instance != null){
								if(instance.getSession() == null || !instance.getSession().isStarted()){
									instanceMap.remove(key);
								}
							}else{
								instanceMap.remove(key);
							}
						
						}catch(Exception e){
							log.error(e, e);
						}
					}
				}catch(Exception e){
					log.error(e, e);
				}
			}
		}, 100, 60000, TimeUnit.MILLISECONDS);
	}
	
	public static InstanceFactory getFactory(){
		return factory;
	}
	
	public Instance addInstance(Instance instance){
		if(instance != null){
			if(instanceMap.containsKey(instance.getId())){
				log.warn("Instance: " + instance + " is alread exist int instance factory!");
			}else{
				return instanceMap.putIfAbsent(instance.getId(), instance);
			}
		}
		return instance;
	}
	
	public Instance removeInstance(Instance instance){
		if(instance != null){
			if(instanceMap.containsKey(instance.getId())){
				return instanceMap.remove(instance.getId());
			}else{
				log.warn("Instance factory hasn't instance: " + instance);
			}
		}
		return instance;
	}
	
	public Instance getInstance(Object id){
		return instanceMap.get(id);
	}
	
	public ConcurrentHashMap<Object, Instance> getInstances(){
		return instanceMap;
	}
	
	public Instance getInstaceByAttr(Instance.Attribute attr, Object uid){
		Instance instance = null;
		Iterator<Object> ite = instanceMap.keySet().iterator();
		while(ite.hasNext()){
			Instance temp = instanceMap.get(ite.next());
			if(temp != null && temp.getAttribute(attr) != null){
				if(temp.getAttribute(attr).equals(uid)){
					return temp;
				}
			}
		}
		return instance;
	}
	
/*	public static void main(String[] args) {
		Long uid = 12313232l;
		LocalInstanceFactory f = new LocalInstanceFactory();
		LocalDefaultInstance d1 = new LocalDefaultInstance();
		d1.setAttribute(Instance.Attribute.UID, 1231322l);
		LocalDefaultInstance d2 = (LocalDefaultInstance) f.getInstaceByAttr(Instance.Attribute.UID, uid);
		System.out.println(d2);
	}
*/
}

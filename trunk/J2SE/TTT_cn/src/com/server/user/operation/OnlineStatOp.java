package com.server.user.operation;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.connect.instance.Instance;
import com.cindy.run.util.DataFactory;
import com.cindy.run.util.ThreadPoolExecutorTimer;
import com.server.dispose.TDDispose;
import com.server.util.Configuration;
import com.server.util.HttpUtil;
import com.server.util.MD5Util;

public class OnlineStatOp {
	private static Log log = LogFactory.getLog(OnlineStatOp.class);
	private static String ip = "127.0.0.1";
	private static String DEST_ADDRESS = Configuration.getStatDestAddr();
	private static final ThreadPoolExecutorTimer TIMER = ThreadPoolExecutorTimer.getIntance();
	private static final boolean SEND_ONLINE_NUM = Configuration.isSendOnlineNum();
	private static final int ONLINE_TYPE = Configuration.getOnlineType();
	private static OnlineStatOp instance = new OnlineStatOp();
	public static void getIP(){
		try {
			Enumeration netInterfaces;
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while(netInterfaces.hasMoreElements()){   
			    NetworkInterface ni=(NetworkInterface)netInterfaces.nextElement();   
			    List<InterfaceAddress> list = ni.getInterfaceAddresses();
			    if(list != null){
			    	Iterator<InterfaceAddress> ite = list.iterator();
			    	while(ite.hasNext()){
			    		InterfaceAddress temp = ite.next();
			    		if(temp != null && temp.getBroadcast() != null){
			    			ip = temp.getAddress().getHostAddress();
			    			return;
			    		}
			    	}
			    }
			}  
		} catch (SocketException e) {
			log.error(e, e);
		}
	}
	
	public OnlineStatOp(){
		getIP();
		if(SEND_ONLINE_NUM){

			TIMER.getPreciseTimer().scheduleAtFixedRate(new Runnable(){
				@Override
				public void run() {
					try {
						int onlineNum = TDDispose.getOnlieNum();
						//HttpUtil.request(DEST_ADDRESS + "?" + "ip=" + ip + "&online_num=" + onlineNum + "&sig=" + MD5Util.md5("ebmQCLWxSdT5bUXu", "ip=" + ip + "&online_num=" + onlineNum));
						Collection<Instance> users = TDDispose.getOnlieUser();
						StringBuffer sb = new StringBuffer();
						for (Instance role : users) {
							Object uid = role.getAttribute("uid");
							if (uid != null)
								sb.append(DataFactory.doubleBytesToLong((byte[]) uid) + ",");
						}
						String str = sb.toString();
						str = str.length() > 0 ? str.substring(0, str.length() - 1) : str;
						MemcacheOp.setOnlineUser(str, ONLINE_TYPE);
					} catch (Exception e) {
						log.error(e, e);
					} 
				}
				
			}, 100, 60000, TimeUnit.MILLISECONDS);
		}
	}
	
	public static void instance(){
		if(instance == null){
			instance = new OnlineStatOp();
		}
	}
}

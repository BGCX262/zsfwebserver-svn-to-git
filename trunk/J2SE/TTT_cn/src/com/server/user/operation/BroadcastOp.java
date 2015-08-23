/*package com.server.user.operation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.management.relation.Role;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.util.DataFactory;
import com.cindy.run.util.ThreadPoolExecutorTimer;
import com.server.dispose.TDDispose;
import com.server.util.Configuration;

public class BroadcastOp {
	private static Log log = LogFactory.getLog(BroadcastOp.class);
	private static final ThreadPoolExecutorTimer TIMER = ThreadPoolExecutorTimer.getIntance();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
	private static final RoleFactory ROLE_FACTORY = RoleFactory.getInstance();
	private static final Role ROLE = ROLE_FACTORY.getNewIdleRole();
	private static byte[] information = new byte[]{0x76,0x00,0x00,0x08,0x01,0x00,0x07,0x00,0x0f,0x0f};
	private static final Object LOCK = new Object();
	private static BroadcastOp instance = new BroadcastOp();
	private BroadcastOp(){
		TIMER.getPreciseTimer().scheduleAtFixedRate(new Handle(), 100, 60000, TimeUnit.MILLISECONDS);
	}
	
	public static void instance(){
		if(instance == null){
			synchronized(LOCK){
				if(instance == null){
					instance = new BroadcastOp();
				}
			}
		}
	}
	
	class Handle implements Runnable{

		@Override
		public void run() {
			shutdownNotify();
		}
	}
	
	public static void shutdownNotify(){
		try{
			String dateStr = Configuration.getShutdownTime();
			if(dateStr != null){
				Date date = sdf.parse(dateStr);
				long shutdownTime = date.getTime();
				if(ROLE != null&& shutdownTime > System.currentTimeMillis() 
						&& (shutdownTime - System.currentTimeMillis()) < 180000){
					information = DataFactory.addByteArray(information, DataFactory.doubleToXiaoTouByte(shutdownTime));
					ROLE.sendInformation(information, TDDispose.CENTER_SERVER);
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
	}
}
*/
package com.server.user.operation;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.util.DataFactory;
import com.server.util.Configuration;

public class ActivityOp {
	private static final Log LOG = LogFactory.getLog(ActivityOp.class);
	public static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
	private static long ACTIVITY_END_TIME = 0;
	static{
		try {
			String dateStr = Configuration.getActivityEndTime();
			if(dateStr != null){
				ACTIVITY_END_TIME = DATEFORMAT.parse(dateStr).getTime();
			}
		} catch (ParseException e) {
			LOG.error(e, e);
		}
		
	}
	
	public static byte[] getActivityEndTime(){
		byte[] re = null;
		re = DataFactory.doubleToXiaoTouByte(ACTIVITY_END_TIME);
		return re;
	}
}

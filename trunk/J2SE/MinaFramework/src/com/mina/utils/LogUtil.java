package com.mina.utils;

import org.apache.log4j.Logger;


/**
 * 日志工具类
 * @author zsf
 * 2011-4-12 上午10:45:29
 */
public class LogUtil {
	
	private static final Logger log = Logger.getLogger("");
	
	public static void debug(Object message, Throwable t) {
		log.debug(message, t);
	}
	
	public static void debug(Object message) {
		log.debug(message);
	}
	
	public static void info(Object message, Throwable t) {
		log.info(message, t);
	}
	
	public static void info(Object message) {
		log.info(message);
	}
	
	public static void warn(Object message, Throwable t) {
		log.warn(message, t);
	}
	
	public static void warn(Object message) {
		log.warn(message);
	}
	
	public static void error(Object message, Throwable t) {
		log.error(message, t);
	}
	
	public static void error(Object message) {
		log.error(message);
	}

}

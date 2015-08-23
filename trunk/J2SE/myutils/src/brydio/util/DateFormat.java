package brydio.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期的格式化工具
 * @author D-io
 *
 */
public class DateFormat {
	
	public static SimpleDateFormat format = new SimpleDateFormat();
	
	static {
		format = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
		format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
	}
	
	/**
	 * 获得格式化之后的结果
	 * @param date
	 * @return
	 */
	public static String getResult(Date date) {
		return format.format(date);
	}
	
	/**
	 * 根据给定格式获得格式化之后的结果
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getResult(Date date, String pattern) {
		format.applyPattern(pattern);
		return format.format(date);
	}

}

package brydio.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * ���ڵĸ�ʽ������
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
	 * ��ø�ʽ��֮��Ľ��
	 * @param date
	 * @return
	 */
	public static String getResult(Date date) {
		return format.format(date);
	}
	
	/**
	 * ���ݸ�����ʽ��ø�ʽ��֮��Ľ��
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getResult(Date date, String pattern) {
		format.applyPattern(pattern);
		return format.format(date);
	}

}

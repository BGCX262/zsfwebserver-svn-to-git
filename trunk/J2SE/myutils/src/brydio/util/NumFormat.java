package brydio.util;

import java.text.NumberFormat;

/**
 * 用户格式化数字，取整等等操作
 * @author D-io
 *
 */
public class NumFormat {
	
	public static NumberFormat format = NumberFormat.getInstance();

	static {
		format.setMinimumFractionDigits(0);
		format.setMaximumFractionDigits(2);
		format.setGroupingUsed(false);
	}
	
	/**
	 * 获得格式化后的结果
	 * @param d
	 * @return
	 */
	public static double getResult(double d) {
		return Double.valueOf(format.format(d)).doubleValue();
	}

	/**
	 * 获得格式化后的结果
	 * @param d
	 * @return
	 */
	public static String getString(double d) {
		return format.format(d);
	}
	
	/**
	 * 获得格式化后的结果
	 * @param d
	 * @return
	 */
	public static double getResult(double d, int min, int max) {
		format.setMinimumFractionDigits(min);
		format.setMaximumFractionDigits(max);
		return Double.valueOf(format.format(d)).doubleValue();
	}

	/**
	 * 获得格式化后的结果
	 * @param d
	 * @return
	 */
	public static String getString(double d, int min, int max) {
		format.setMinimumFractionDigits(min);
		format.setMaximumFractionDigits(max);
		return format.format(d);
	}
	
	/**
	 * 获得格式化后的结果
	 * @param d
	 * @return
	 */
	public static float getResult(float d) {
		return Float.valueOf(format.format(d)).floatValue();
	}

	/**
	 * 获得格式化后的结果
	 * @param d
	 * @return
	 */
	public static String getString(float d) {
		return format.format(d);
	}
	
	/**
	 * 获得格式化后的结果
	 * @param d
	 * @return
	 */
	public static float getResult(float d, int min, int max) {
		format.setMinimumFractionDigits(min);
		format.setMaximumFractionDigits(max);
		return Float.valueOf(format.format(d)).floatValue();
	}

	/**
	 * 获得格式化后的结果
	 * @param d
	 * @return
	 */
	public static String getString(float d, int min, int max) {
		format.setMinimumFractionDigits(min);
		format.setMaximumFractionDigits(max);
		return format.format(d);
	}

}

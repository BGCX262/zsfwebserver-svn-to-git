package brydio.util;

import java.math.BigDecimal;

/**
 * 用于浮点数的加减乘除运算
 * @author D-io
 *
 */
public class Math {
	
	/**
	 * 浮点相加
	 * @param a
	 * @param b
	 * @return
	 */
	public static double add(double a, double b) {
		return new BigDecimal(a).add(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * 浮点相减
	 * @param a
	 * @param b
	 * @return
	 */
	public static double minus(double a, double b) {
		return new BigDecimal(a).subtract(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * 浮点相乘
	 * @param a
	 * @param b
	 * @return
	 */
	public static double multiply(double a, double b) {
		return new BigDecimal(a).multiply(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * 浮点相除
	 * @param a
	 * @param b
	 * @return
	 */
	public static double divide(double a, double b) {
		return new BigDecimal(a).divide(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * 浮点相加(单精度)
	 * @param a
	 * @param b
	 * @return
	 */
	public static float add(float a, float b) {
		return new BigDecimal(a).add(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * 浮点相加(单精度)
	 * @param a
	 * @param b
	 * @return
	 */
	public static float minus(float a, float b) {
		return new BigDecimal(a).subtract(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * 浮点相加(单精度)
	 * @param a
	 * @param b
	 * @return
	 */
	public static float multiply(float a, float b) {
		return new BigDecimal(a).multiply(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * 浮点相加(单精度)
	 * @param a
	 * @param b
	 * @return
	 */
	public static double divide(float a, float b) {
		return new BigDecimal(a).divide(new BigDecimal(b)).floatValue();
	}

}

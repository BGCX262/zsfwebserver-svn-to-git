package brydio.util;

import java.math.BigDecimal;

/**
 * ���ڸ������ļӼ��˳�����
 * @author D-io
 *
 */
public class Math {
	
	/**
	 * �������
	 * @param a
	 * @param b
	 * @return
	 */
	public static double add(double a, double b) {
		return new BigDecimal(a).add(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * �������
	 * @param a
	 * @param b
	 * @return
	 */
	public static double minus(double a, double b) {
		return new BigDecimal(a).subtract(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * �������
	 * @param a
	 * @param b
	 * @return
	 */
	public static double multiply(double a, double b) {
		return new BigDecimal(a).multiply(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * �������
	 * @param a
	 * @param b
	 * @return
	 */
	public static double divide(double a, double b) {
		return new BigDecimal(a).divide(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * �������(������)
	 * @param a
	 * @param b
	 * @return
	 */
	public static float add(float a, float b) {
		return new BigDecimal(a).add(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * �������(������)
	 * @param a
	 * @param b
	 * @return
	 */
	public static float minus(float a, float b) {
		return new BigDecimal(a).subtract(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * �������(������)
	 * @param a
	 * @param b
	 * @return
	 */
	public static float multiply(float a, float b) {
		return new BigDecimal(a).multiply(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * �������(������)
	 * @param a
	 * @param b
	 * @return
	 */
	public static double divide(float a, float b) {
		return new BigDecimal(a).divide(new BigDecimal(b)).floatValue();
	}

}

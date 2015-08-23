

import java.math.BigDecimal;

public class DoubleMath {
	
	/**
	 * 两个double相加
	 * @param a
	 * @param b
	 * @return
	 */
	public static double add(double a, double b) {
		return new BigDecimal(a).add(new BigDecimal(b)).doubleValue();
	}

}

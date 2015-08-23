

import java.text.NumberFormat;

public class DoubleFormat {
	
	public static NumberFormat format = null;
	
	static {
		format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(0);
		format.setGroupingUsed(false);
	}
	
	public static double getResult(double d) {
		return Double.valueOf(format.format(d)).doubleValue();
	}

}

import java.math.BigDecimal;
import java.text.DecimalFormat;


public class TestDouble {

	public static void main(String[] args) {
		double num1 = 4.025d;
		double num2 = 1000;
		
		System.out.println(new DecimalFormat("0.00").format(new BigDecimal(Double.toString(num1)).doubleValue()));
		
	}

}

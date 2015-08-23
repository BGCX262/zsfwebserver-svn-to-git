import java.text.NumberFormat;


public class TestForamt {
	
	public static void main(String[] args) {
		NumberFormat format = NumberFormat.getInstance();
		
		//format.setMinimumIntegerDigits(9);
		format.setMaximumFractionDigits(2);
		format.setGroupingUsed(false);
		
		System.out.println(format.format(5.705000000000001));
	}

}

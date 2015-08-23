import java.text.NumberFormat;


public class TestArray {
	
	public static void main(String[] args) {
		int[] intArr = new int[] {
				1, 2
		};
		
		intArr[1] = 0;
		
		System.out.println(intArr.length);
		
		NumberFormat 
		format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(0);
		format.setGroupingUsed(false);
		
		System.out.println(format.format(10000.05));
	}

}

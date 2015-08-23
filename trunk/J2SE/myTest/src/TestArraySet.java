import java.util.Arrays;



public class TestArraySet {
	
	public int theNum = 1;
	
	public static void main(String[] args) {
		
		TestArraySet[] arr = new TestArraySet[] {
				new TestArraySet(),
				new TestArraySet(),
				new TestArraySet(),
				new TestArraySet(),
				new TestArraySet()
		};
		
		TestArraySet tas = arr[2];
		
		tas.theNum = 20;
		
		System.out.println(Arrays.asList(arr));
		
	}
	
	public String toString() {
		return theNum + "";
	}

}

import java.util.ArrayList;
import java.util.List;


public class TestContast {
	
	public String str = "asd";
	
	public static void main(String[] args) {
		List<TestContast> list = new ArrayList<TestContast>();
		
		TestContast tc = new TestContast("asd");
		
		list.add(tc);
		
		System.out.println(list.contains(tc));
	}
	
	public TestContast(String str) {
		this.str = str;
	}

}

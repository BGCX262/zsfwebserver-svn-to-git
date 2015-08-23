
public class TestForeach {
	
	public static void main(String[] args) {
		TestObj[] objs = new TestObj[] {
				new TestObj("123"),
				new TestObj("213"),
				new TestObj("321"),
				new TestObj("231"),
				new TestObj("132")
		};
		
		
		for (TestObj str : objs) {
			if (str.str.equals("213")) {
				str.str = new String("333");
			}
		}
		
		System.out.println(objs[1].str);
	}

}

class TestObj {
	public String str = "123";
	
	public TestObj(String str) {
		this.str = str;
	}
}

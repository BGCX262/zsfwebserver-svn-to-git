

public class TestField {
	
	public MyTest myTest;
	
	public static void main(String[] args) {
		TestField field = new TestField();
		field.myTest = new MyTest();
		//field.myTest.obj = new Object();
		field.new TestOut().print();
	}

	class TestOut {
		
		public void print() {
			TestField.this.myTest.staff();
		}
		
	}

}

class MyTest {
	
	public Object obj;
	
	public void staff() {
		System.out.println(obj);
	}
	
}
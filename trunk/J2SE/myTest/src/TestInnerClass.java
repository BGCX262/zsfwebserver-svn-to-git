

public class TestInnerClass {
	
	/** 需要访问的属性 **/
	public String name;
	
	public static void main(String[] args) {
		TestInnerClass innerClass = new TestInnerClass();
		
		innerClass.name = "123";
		
		TheInnerClass theInnerClass = innerClass.new TheInnerClass();
		theInnerClass.print();
	}

	/**
	 * 内部类
	 * @author zsf
	 */
	class TheInnerClass {
		
		/**
		 * 访问属性的方法
		 */
		public void print() {
			System.out.println(TestInnerClass.this.name);
		}
		
	}

}

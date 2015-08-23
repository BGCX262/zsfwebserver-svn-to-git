package testObjcet;

import java.util.ArrayList;


public class TestMain {
	
	public static void main(String[] args) {
		Object1 obj = new Object1();
		
		obj.name = "name";
		obj.age = 1;
		obj.list = new ArrayList<Integer>();
		
		Test1 test1 = new Test1();
		test1.setObj(obj);
		
		System.out.println(obj);
		
	}

}

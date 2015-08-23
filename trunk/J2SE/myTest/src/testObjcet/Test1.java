package testObjcet;

import java.util.ArrayList;


public class Test1 {
	
	public void setObj(Object1 o) {
		o.name = "setted";
		o.age = 99;
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(123);
		o.list = list;
		
	}

}

import java.util.ArrayList;


public class TestNoClone {
	
	public static void main(String[] args) {
		Object o = new Object();
		
		ArrayList<Object> list = new ArrayList<Object>();
		
		list.add(o);
		
		o = null;
		
		System.out.println(list.get(0));
		
	}

}

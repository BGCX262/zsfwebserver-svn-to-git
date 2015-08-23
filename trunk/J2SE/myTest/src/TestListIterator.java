import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;


public class TestListIterator {
	
	public static void main(String[] args) {
		Date begin = new Date();
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		
		ListIterator<String> iter = list.listIterator();
		for ( ; iter.hasNext(); ) {
			iter.next();
		}
		
		for ( ; iter.hasPrevious(); ) {
			System.out.println(iter.previous() + ", " + iter.nextIndex());
		}
		Date end = new Date();
		System.out.println("用时" + (end.getTime() - begin.getTime()));
		
		for (String str : list) {
			System.out.println(str);
		}
	}

}

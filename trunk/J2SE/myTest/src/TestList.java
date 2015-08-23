import java.util.*;


public class TestList {
	
	public static final int COUNT = 10;
	
	public static void main(String[] args) throws Exception {
		
		/*List<String> list = new ArrayList<String>();
		
		for (int i = 0; i < COUNT; i ++) {
			list.add("abc" + i);
		}
		
		list = list.subList(0, list.size());
		System.out.println(list);

		long begin = System.currentTimeMillis();
		
		for (int i = 0; i < COUNT; i++) {
			//list.remove(0);
			list.remove(list.size() - 1);
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println(end - begin);
		
		synchronized (list) {
			list.wait();
		}
		
		System.out.println(TestList.class.getResource("config.properties"));*/
		
		List<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < 10000; i++) {
			list.add(i);
		}
		
		long begin = System.currentTimeMillis();
		
		for (Iterator<Integer> iter = list.iterator(); iter.hasNext(); ) {
			Integer next = iter.next();
			if (next != 10)
				iter.remove();
		}
		
		long end = System.currentTimeMillis();
		System.out.println(end - begin);
		
		List<Integer> temp = new LinkedList<Integer>();
		
		for (Iterator<Integer> iter = list.iterator(); iter.hasNext(); ) {
			Integer next = iter.next();
			if (next == 10)
				temp.add(next);
		}
		
		begin = System.currentTimeMillis();
		System.out.println(begin - end);
		
	}

}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TestMap {
	
	public String name = "123";
	
	public static void main(String[] args) {
		/*HashMap<Integer, List<TestMap>> map = new HashMap<Integer, List<TestMap>>();
		
		List<TestMap> list = new ArrayList<TestMap>();
		list.add(new TestMap("1"));
		list.add(new TestMap("2"));
		list.add(new TestMap("3"));
		list.add(new TestMap("4"));
		list.add(new TestMap("5"));
		list.add(new TestMap("6"));
		map.put(1, list);
		map.put(2, list);
		
		System.out.println(map.toString().replaceAll("\\{", "").replaceAll("\\[", "").replaceAll("], ", ";").replaceAll(", ", "_").replaceAll("]}", ""));*/
		
		HashMap<Long, Long> map = new HashMap<Long, Long>();
		map.put(11l, 10l);
		map.put(13l, 12l);
		map.put(14l, 15l);
		map.put(17l, 16l);
		
		System.out.println(map.containsKey(11l));
		System.out.println(map.containsKey(11l));
		System.out.println(map.containsKey(12l));
		System.out.println(map.containsKey(12l));
		System.out.println(map.containsValue(11l));
		System.out.println(map.containsValue(11l));
		System.out.println(map.containsValue(12l));
		System.out.println(map.containsValue(12l));
		
	}
	
	public String toString() {
		return name;
	}
	
	public TestMap(String name) {
		this.name = name;
	}

}

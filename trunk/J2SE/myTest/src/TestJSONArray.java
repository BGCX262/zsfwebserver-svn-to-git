import net.sf.json.JSONArray;



public class TestJSONArray {

	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		
		String str = "[1,\"s\",2]";
		
		JSONArray arr = JSONArray.fromObject(str);
		
		System.out.println(arr.get(1));
		
		System.out.println(System.currentTimeMillis() - begin);
		
	}
	
}

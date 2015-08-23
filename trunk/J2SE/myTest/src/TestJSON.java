import net.sf.json.JSONObject;



public class TestJSON {
	
	public static void main(String[] args) {
		JSONObject jso = new JSONObject();
		jso.put("hp", 15);
		jso.put("mp", 10);
		jso.put("strpatk", 1.5);
		jso.put("agipatk", 0.5);
		jso.put("matk", 1.2);
		jso.put("dodge", 0.1);
		jso.put("hit", 0.2);
		jso.put("crit", 0.2);
		jso.put("pasd", 0.12);
		jso.put("pdf", 3);
		jso.put("mdf", 2);
		System.out.println(jso.toString().replace("\"", "\\\""));
		
	}

}

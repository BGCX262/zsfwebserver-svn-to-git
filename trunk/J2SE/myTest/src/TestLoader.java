import java.io.BufferedReader;
import java.io.FileReader;

import net.sf.json.JSONArray;



public class TestLoader {
	
	public static void main(String[] args) throws Exception {
		
		String path = "C:\\Users\\D-io\\Desktop\\人人网.txt";
		
		BufferedReader br = new BufferedReader(new FileReader(path));
		
		String str = "";
		while ((str = br.readLine()) != null) {
			System.out.println(str);
			JSONArray.fromObject(str);
			
		}
		
	}

}

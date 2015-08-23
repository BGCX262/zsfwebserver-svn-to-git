import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class TestURL {
	
	private static final String URL = "http://www.baidu.com";
	
	public static void main(String[] args) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) new URL(URL).openConnection();
		conn.setDoInput(true);
		BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
		
		StringBuffer sb = new StringBuffer();
		byte[] bytes = new byte[1024];
		while (bis.read(bytes) != -1) {
			sb.append(new String(bytes, "gbk"));
		}
		bis.close();
		conn.disconnect();
		
		System.out.println(sb.toString());
	}

}

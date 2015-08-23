import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;


/**
 * 测试客户端连接
 * 
 * @author D-io
 * 
 */
public class TestStrusConnect extends Thread {

	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("192.168.27.47", 1705);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		while (true) {
			bw.write("01%1111111111111111111111111");
			bw.newLine();
			bw.flush();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String str = br.readLine();
			System.out.println(str);
			bw.close();
			br.close();
			
			break;
			
			//Thread.sleep(500);
		}
	}
}

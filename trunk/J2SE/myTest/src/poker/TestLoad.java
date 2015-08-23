package poker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class TestLoad {
	
	public static void main(String[] args) throws Exception {
		String path = "logs/dollaroPoker.log";
		String path1 = "XXX.log";
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(TestLoad.class.getResourceAsStream(path)));
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path1)));
		
		String temp = "";
		int num = 0;
		while ((temp = reader.readLine()) != null && num++ <= 5001) {
			if (temp.lastIndexOf("] [") == -1) {
				temp = temp.substring(temp.lastIndexOf("] ") + 2);
				writer.write(temp);
				writer.newLine();
			}
		}
		writer.flush();
		writer.close();
		
		reader.close();
	}

}

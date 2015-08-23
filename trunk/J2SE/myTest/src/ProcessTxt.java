import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;



public class ProcessTxt {
	
	public static void main(String[] args) throws Exception {
		File file = new File("C:\\Users\\D-io\\Desktop\\users.sql");
		File file2 = new File("C:\\Users\\D-io\\Desktop\\users1.sql");
		BufferedReader br = new BufferedReader(new FileReader(file));
		BufferedWriter bw = new BufferedWriter(new FileWriter(file2));
		int[] tasks = new int[] {10085,10086,10087};
		
		String temp = "";
		int i = 0;
		while ((temp = br.readLine()) != null) {
			i++;
			temp = temp.replace("asddsa", tasks[i % tasks.length] + "");
			bw.write(temp);
			bw.newLine();
		}
		bw.close();
		br.close();
	}

}

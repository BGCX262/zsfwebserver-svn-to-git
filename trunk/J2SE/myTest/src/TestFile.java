import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;



public class TestFile {
	
	public static void main(String[] args) throws Exception {
		String strPath = "C:\\Users\\D-io\\Desktop\\多彩飙片换QB大赛@kt11@松岛枫40部合集";
		File path = new File(strPath);
		
		File[] files = path.listFiles();
		
		for (File file : files) {
			if (file.isDirectory()) {
				File[] listFiles = file.listFiles();
				
				for (File f : listFiles) {
					FileInputStream bis = new FileInputStream(f);
					FileOutputStream bos = new FileOutputStream(strPath + "\\" + f.getName());
					
					byte[] buff = new byte[1024];
					while (bis.read(buff) != -1) {
						bos.write(buff);
					}
					
					bos.close();
					bis.close();
					
				}
				
			}
			
		}
		
	}
	
	class Test {
		
		
		
	}

}

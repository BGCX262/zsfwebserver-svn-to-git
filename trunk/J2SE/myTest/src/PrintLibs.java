import java.io.File;
import java.io.FileFilter;



public class PrintLibs {

	public static void main(String[] args) {
		String speator = ":";
		String str = ".";
		String spe = "libs/";
		
		File path = new File("e:\\td_lib");
		File[] files = path.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return pathname != null && pathname.getName().endsWith("jar");
			}
		});
		
		for (File file : files) {
			str += speator + spe + file.getName();
		}
		
		System.out.println(str);
	}
	
}

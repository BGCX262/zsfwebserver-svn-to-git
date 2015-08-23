import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

public class TheAppendUtil {

	private static File path = null;

	public static String[] spliters = null;

	private static StringBuffer sb = new StringBuffer();

	public static void main(String[] args) {
		
		try {
			System.out.println(UIManager.getInstalledLookAndFeels()[1].getClassName());
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[1].getClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int flag = jfc.showOpenDialog(null);
		
		while (flag != JFileChooser.APPROVE_OPTION)
			flag = jfc.showOpenDialog(null);
			
		String str = JOptionPane.showInputDialog("请输入后缀名（如：.java;.as）如有多个，请已半角分号分隔：");
		
		args = new String[] { jfc.getSelectedFile().getPath(), str };

		if (!checkArgs(args))
			return;

		System.out.println("开始读取文件...");
		processFiles(path);

		FileSystemView fsv = FileSystemView.getFileSystemView();
		String desktopPath = fsv.getHomeDirectory().getAbsolutePath();
		System.out.println("保存文件，保存目录为：" + desktopPath + "\\Result.txt");
		
		JOptionPane.showMessageDialog(null, "读取成功，保存目录为：" + desktopPath + "\\Result.txt");

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(desktopPath + "\\Result.txt"));

			bw.write(sb.toString());
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static synchronized void processFiles(File file) {
		FilenameFilter filter = new MyFilenameFilter();
		
		File[] files = file.listFiles(filter);

		for (File f : files) {
			if (f.isDirectory()) {
				processFiles(f);
				continue;
			}

			boolean flag = false;
			for (String str : spliters) {
				if (f.getName().endsWith(str)) {
					flag = true;
					break;
				}
			}

			if (!flag)
				continue;

			try {

				BufferedReader reader = new BufferedReader(new FileReader(f));
				
				String temp = "";
				while ((temp = reader.readLine()) != null) {
					sb.append(temp + "\r\n");
					
				}
				reader.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private static boolean checkArgs(String[] args) {
		System.out.println("开始参数判断...");

		File f = new File(args[0]);

		if (!f.exists() || !f.isDirectory()) {
			System.out.println("请指定第一个参数，为目标文件夹");
			return false;
		}

		if (args[1].indexOf(";") != -1)
			spliters = args[1].split(";");
		else
			spliters = new String[] { args[1] };

		for (String str : spliters) {
			if (!str.startsWith(".")) {
				System.out.println("请指定第一个参数，为目标文件后缀名，如有多个，请以半角分号分隔");
				return false;
			}

		}

		path = f;

		return true;
	}

}

class MyFilenameFilter implements FilenameFilter {

	public boolean accept(File dir, String name) {

		if (new File(dir + "\\" + name).isDirectory())
			return true;
		
		for (String str : TheAppendUtil.spliters) {
			if (name.endsWith(str))
				return true;
		}
		
		return false;
	}
	
}

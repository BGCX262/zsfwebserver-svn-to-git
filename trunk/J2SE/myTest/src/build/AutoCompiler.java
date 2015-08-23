package build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Scanner;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class AutoCompiler extends Thread {

	private static Files files;

	public void run() {
		try {
			files = loadFiles();
			Collection<SimpleFile> values = files.values();

			for (Iterator<SimpleFile> iter = values.iterator(); iter.hasNext();) {
				SimpleFile next = iter.next();
				if (next.change) {
					build(next.file);
					next.change = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 编译
	 * @param file
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void build(File file) {
		try {
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			if (compiler == null)
				throw new IllegalArgumentException("No JDK");
			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
			String fileName = file.getName().substring(0, file.getName().indexOf(".")) + ".java";
			File tofile = new File(AutoCompiler.class.getResource("bin").getPath() + "\\" + fileName);
			PrintWriter pw;
			pw = new PrintWriter(tofile);
			pw.println(getFileContent(file));
			pw.close();
			Iterable compilationUnits =
					fileManager.getJavaFileObjectsFromStrings(Arrays.asList(tofile.getAbsolutePath()));
			JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits);
			task.call();
			fileManager.close();
			tofile.delete();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件内容
	 * @param file
	 * @return
	 */
	public String getFileContent(File file) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("package build.bin;\r\n");
			sb.append("import build.AbstractParent;\r\n");
			sb.append("public class " + file.getName().substring(0, file.getName().indexOf(".")) + " extends AbstractParent {\r\n");
			sb.append("public void exec() {");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String temp = null;
			while ((temp = br.readLine()) != null)
				sb.append(temp + "\r\n");
			sb.append("}");
			sb.append("}");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	/**
	 * 加载目录下的所有文件
	 */
	public Files loadFiles() {
		File dir = new File(AutoCompiler.class.getResource("files").getPath());
		File[] files = dir.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith(".txt");
			}
		});
		Files files2 = new Files();
		files2.init(files);
		return files2;
	}
	
	public static void exec(String className) {
		try {
			URLClassLoader loader = new URLClassLoader(new URL[] {
					AutoCompiler.class.getResource("bin")
			});
			System.out.println(AutoCompiler.class.getResource("bin"));
			Class<?> clazz = loader.loadClass("build.bin." + className);
			
			AbstractParent instance = (AbstractParent) clazz.newInstance();
			instance.exec();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		AutoCompiler compiler = new AutoCompiler();
		compiler.start();

		Scanner scanner = new Scanner(System.in);
		String in = null;
		while (!(in = scanner.nextLine()).equals("exit")) {
			exec(in);
		}
	}

}

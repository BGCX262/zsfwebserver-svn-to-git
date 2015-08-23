import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestReplace {

	public static void main(String[] args) throws Exception {
		String str = "[[[[[[[[[[[[[[[[[[[[[[[[[[[]]]]]]]]]]]]]]]]]]]]]]]]]]]";

		System.out.println(str.replaceFirst("\\[", "123"));

		String s = "dsadsa/* 寮洪敓鏂ゆ嫹杞敓鏂ゆ嫹閿熺\n * \n * 即鎲嬫嫹閿熸枻鎷峰疄閿熻鐨勬帴鍖℃嫹 */dsadsa";

		Pattern pattern1 = Pattern.compile("//(.*)"); // 特征是所有以双斜线开头的
		Matcher matcher1 = pattern1.matcher(s);
		s = matcher1.replaceAll(""); // 替换第一种注释

		Pattern pattern2 = Pattern.compile("/\\*(.*?)\\*/", Pattern.DOTALL); // 特征是以/*开始，以*/结尾，Pattern.DOTALL的意思是糊涂模式，这种模式下.（点号）匹配所有字符
		Matcher matcher2 = pattern2.matcher(s);
		s = matcher2.replaceAll(""); // 替换第二种注释

		Pattern pattern3 = Pattern.compile("/\\*\\*(.*?)\\*/", Pattern.DOTALL); // 特征是以/**开始，以*/结尾
		Matcher matcher3 = pattern3.matcher(s);
		s = matcher3.replaceAll(""); // 替换第三种注释

		System.out.println(s); // 打印结果

		// BufferedReader br = new BufferedReader(new
		// FileReader("C:\\Users\\D-io\\Desktop\\新建文件夹\\1.txt"));
		//
		// String s;
		// StringBuffer sb = new StringBuffer();
		// while ((s = br.readLine()) != null) {
		// sb.append(new String(s.getBytes("iso8859-1"), "gbk") + "\r\n");
		// }
		// br.close();
		//
		// s = sb.toString();
		// s = s.replaceAll("/\\*\\*[.|\n]*\\*\\*/", "");
		// s = s.replaceAll("/\\*\\*[.|\n]*\\*/", "");
		// s = s.replaceAll("/\\*[.|\n]*\\*/", "");
		// s = s.replaceAll("//.*", "");
		//
		// BufferedWriter bw = new BufferedWriter(new
		// FileWriter("C:\\Users\\D-io\\Desktop\\新建文件夹\\2.txt"));
		// bw.write(s);
		// bw.close();

	}

}

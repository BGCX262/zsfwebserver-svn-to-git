import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;


public class ProcessFileTest {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\D-io\\Desktop\\C语言经典算法100例.txt"));
		BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\D-io\\Desktop\\C语言经典算法100例1.html"));
		
		boolean title = false;
		boolean fenxi = false;
		boolean code = false;
		
		StringBuffer sb = new StringBuffer();
		sb.append("<style type=\"text/css\">*{font-size:12px;}b{text-decoration:none;color:gray;}b:hover{text-decoration:underline;}table{width:100%;border-color:blue;}table td{border-color:#DBDBDB;}</style>");
		sb.append("<table border=\"1\" cellspacing=\"0\" cellpadding =\"5\" style=\"border-color:blue;\">\r\n");
		
		int row = 0;
		String str;
		while ((str = br.readLine()) != null) {
			if (str.indexOf("题目") != -1) {
				title = true;
				fenxi = false;
				code = false;
				row++;
				if (row > 1) {
					sb.append("\t\t\t</pre>\r\n\t\t</td>\r\n\t</tr>\r\n");
				}
				sb.append("\t<tr>\r\n\t\t<td>\r\n");
				sb.append("\t\t\t" + str + "\r\n");
				
			} else if (str.indexOf("程序分析") != -1) {
				title = false;
				fenxi = true;
				code = false;
				sb.append("\t<tr id=\"tishi"+row+"\" style=\"display:none;\">\r\n\t\t<td>\r\n");
				sb.append("\t\t\t" + str + "\r\n");
				
			} else if (str.indexOf("程序源代码") != -1) {
				title = false;
				fenxi = false;
				code = true;
				sb.append("\t<tr id=\"code"+row+"\" style=\"display:none;\">\r\n\t\t<td>\r\n");
				sb.append("\t\t\t" + str + "\r\n\t\t\t<pre>\r\n");
				
			} else if (title) {
				if (str.indexOf("_______________________________") == -1) {
					sb.append("\t\t\t" + str + "\r\n");
				} else {
					sb.append("\t\t\t<br>\r\n\t\t\t<b onclick=\"try{if(tishi"+row+".style.display=='none')tishi"+row+".style.display='block';else tishi"+row+".style.display='none'}catch(e){}\">显示提示</b>\r\n");
					sb.append("\t\t\t<b onclick=\"try{if(code"+row+".style.display=='none')code"+row+".style.display='block';else code"+row+".style.display='none'}catch(e){}\">显示代码</b>\r\n");
					sb.append("\t\t</td>\r\n\t</tr>\r\n");
				}
			} else if (fenxi) {
				if (str.indexOf("_______________________________") == -1) {
					sb.append("\t\t\t" + str + "\r\n");
				} else {
					sb.append("\t\t</td>\r\n\t</tr>\r\n");
				}
			} else if (code) {
				sb.append("\t\t\t\t" + str + "\r\n");
			}
		}
		
		br.close();
		bw.write(sb.toString());
		bw.close();
	}

}

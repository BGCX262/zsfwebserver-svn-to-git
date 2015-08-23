import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;


public class MakeConfigFile {

	public static void main(String[] args) throws Exception {
		double[] points = new double[] {
				0.05, 0.05, 0.1, 0.1, 0.5, 0.5, 1, 1, 5, 5, 10, 10, 25, 50, 100
		};
		int[] players = new int[] { 4 };
		String[] names = new String[] {
				"Berlin", 
				"Oslo",
				"Paris",
				"Monaco",
				"Rome",
				"Moscow",
				"Tokyo",
				"Hamburg",
				"Athens",
				"Sofia",
				"Madrid",
				"Lisbon",
				"Dublin",
				"London",
				"Bern",
				"Vienna",
				"Prague",
				"Vegas",
				"Venice",
				"Milan",
				
		};
		String[] idxs = new String[] { "I", "II", "III", "IV", "V" };
		
		StringBuffer sb = new StringBuffer();
		
		String str1 = "<Room name=\"(name)\" maxUsers=\"(maxUsers)\" isGame=\"true\" isPrivate=\"false\" isTemp=\"false\" autoJoin=\"false\">";
		String str2 = "	<Vars>";
		//String str3 = "		<Var name=\"sb\" type=\"n\" private=\"false\">(sb)</Var>";
		String str3 = "		<Var name=\"point\" type=\"n\" private=\"false\">(sb)</Var>";
		//String str4 = "		<Var name=\"limit\" type=\"s\" private=\"true\">(fl)</Var>";
		//String str5 = "		<Var name=\"size\" type=\"n\" private=\"true\">(size)</Var>";
		String str6 = "		<Var name=\"ip\" type=\"n\" private=\"true\">(ip)</Var>";
		String str7 = "	</Vars>";
		String str8 = "	<Extensions>";
		String str9 = "		<extension name=\"(name)Ext\" className=\"javaExtensions.com.yule27.BlackJack.server.BJRoomExt\" type=\"java\"/>";
		String str10 = "	</Extensions>";
		String str11 = "</Room>";
		
		//int count = 0;
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < idxs.length; j++) {
				sb.append(str1.replaceFirst("\\(maxUsers\\)", players[0] * 2 + "").replaceFirst("\\(name\\)", names[i] + idxs[j]) + "\r\n");
				sb.append(str2 + "\r\n");
				sb.append(str3.replaceFirst("\\(sb\\)", points[i] + "") + "\r\n");
				//sb.append(str4.replaceFirst("\\(fl\\)", (players[j] == 6) ? "F/L" : "N/L") + "\r\n");
				//sb.append(str5.replaceFirst("\\(size\\)", players[j] + "") + "\r\n");
				sb.append(str6.replaceFirst("\\(ip\\)", points[i] < 5 ? "0" : "1") + "\r\n");
				sb.append(str7 + "\r\n");
				sb.append(str8 + "\r\n");
				sb.append(str9.replaceFirst("\\(name\\)", names[i] + idxs[j]) + "\r\n");
				sb.append(str10 + "\r\n");
				sb.append(str11 + "\r\n");
			}
			sb.append("\r\n");
		}
		/*for (int i = 0; i < names.length; i++) {
			String[] arr = names[i].split("	");
			String name = arr[0];
			double pt = Double.valueOf(arr[1]).doubleValue();
			int size = Integer.valueOf(arr[2]).intValue();
			sb.append(str1.replaceFirst("\\(maxUsers\\)", size * 2 + "").replaceFirst("\\(name\\)", name) + "\r\n");
			sb.append(str2 + "\r\n");
			sb.append(str3.replaceFirst("\\(sb\\)", pt + "") + "\r\n");
			sb.append(str4.replaceFirst("\\(fl\\)", (size == 6) ? "nolimit" : "limit") + "\r\n");
			sb.append(str5.replaceFirst("\\(size\\)", size + "") + "\r\n");
			sb.append(str6.replaceFirst("\\(ip\\)", (pt <= 5.0d ? 0 : 1) + "") + "\r\n");
			sb.append(str7 + "\r\n");
			sb.append(str8 + "\r\n");
			sb.append(str9.replaceFirst("\\(name\\)", name) + "\r\n");
			sb.append(str10 + "\r\n");
			sb.append(str11 + "\r\n");
		}
		System.out.println(sb.toString()); */
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("d:\\config.xml")));
		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}

}

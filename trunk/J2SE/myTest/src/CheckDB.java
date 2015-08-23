import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;


public class CheckDB {
	
	public static void main(String[] args) throws Exception {
		String sql = "select gh_deskname, count(gh_deskname) from tb_gamehistory group by gh_deskname order by gh_deskname";

		Connection conn = MyTools.getConnByMySql("localhost", "3306", "db_ttlgame_test", "root", "root");
		ResultSet rs = MyTools.getRs(sql, conn);
		
		rs.last();
		int size = rs.getRow();
		rs.beforeFirst();
		
		/* 数据 */
		String[] deskNames = new String[size];
		String[] deskPoints = new String[size];
		String[] deskTypes = new String[size];
		int[] counts = new int[size];
		int i = 0;
		
		/* 结果 */
		int flCount = 0;
		int nlCount = 0;
		HashMap<String, Integer> rooms = new HashMap<String, Integer>();
		int mRoom = 0;
		int sRoom = 0;
		
		while (rs.next()) {
			/* 保存数据 */
			String deskName = rs.getString(1);
			int count = rs.getInt(2);
			
			String[] str = deskName.split(" ");
			deskNames[i] = str[0];
			deskPoints[i] = str[1];
			deskTypes[i] = str[2];
			counts[i] = count;
			
			System.out.println(str[0] + "\t" + str[1] + "\t" + str[2] + "\t" + count);
			
			i++;
			
			/* 计算结果 */
			if (str[2].equals("F/L"))
				flCount += count;
			else 
				nlCount += count;
			
			Integer itg = rooms.get(str[1]);
			if (itg == null)
				rooms.put(str[1], count);
			else
				rooms.put(str[1], itg + count);
			
			if (str[0].endsWith("I") || str[0].endsWith("II") || str[0].endsWith("III") || str[0].endsWith("IV")) {
				sRoom += count;
			} else {
				mRoom += count;
			}
		}
		
		System.out.println("\n\n");
		System.out.println("\n玩家偏好\n" + rooms.toString().replaceAll("\\{", "").replaceAll("\\}", "").replaceAll(", ", "\n").replaceAll("=", "\t"));
		
		System.out.println("\n房间偏好\n系统房\t自建房\tF/L房游戏局数\tN/L房游戏局数\n" + sRoom + "\t" + mRoom + "\t" + flCount + "\t" + nlCount);
	}

}

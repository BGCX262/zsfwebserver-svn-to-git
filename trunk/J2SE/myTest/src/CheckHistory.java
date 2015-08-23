import java.sql.Connection;
import java.sql.ResultSet;


public class CheckHistory {

	public static void main(String[] args) throws Exception {
		String sql = "select gh_playerids from tb_gamehistory";
		
		Connection conn = MyTools.getConnByMySql("localhost", "3306", "db_ttlgame_test", "root", "root");
		ResultSet rs = MyTools.getRs(sql, conn);
		
		int[] pCount = new int[11];
		while (rs.next()) {
			String res = rs.getString(1);
			int size = 0;
			
			if (res.indexOf("><") != -1)
				size = res.split("><").length;
			
			pCount[size]++;
		}
		
		String wjrs = "玩家数量\t游戏次数\n";
		for (int j = 0; j < pCount.length; j++) {
			wjrs += j + "人\t" + pCount[j] + "\n";
		}
		System.out.println(wjrs);
	}

}

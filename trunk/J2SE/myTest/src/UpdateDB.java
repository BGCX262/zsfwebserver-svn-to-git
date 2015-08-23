import java.sql.Connection;
import java.sql.Statement;



public class UpdateDB {
	
	public static void main(String[] args) throws Exception {
		Connection conn = MyTools.getConnByMySql("localhost", "3306", "td", "root", "root");
		Statement st = conn.createStatement();
		conn.setAutoCommit(false);
		int rows = st.executeUpdate("update towerbean set upNeedRock=upNeedRock*1.15, upNeedMetal=upNeedMetal*1.15, upNeedCrystal=upNeedCrystal*1.15 where level>10");
		
	}

}

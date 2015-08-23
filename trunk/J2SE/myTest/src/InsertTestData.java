import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Date;


public class InsertTestData {

	public static void main(String[] args) throws Exception {
		Timestamp stamp = new Timestamp(new Date().getTime());
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ttlgame", 
				"root", "root");
		PreparedStatement stat = null;
		String sql, bank;
		for (int i = 1; i <= 1000; i++) {
			sql = "insert into tb_userinfo values(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			bank = "insert into tb_bank values(null, ?, ?)";
			stat = conn.prepareStatement(sql);
			stat.setString(1, i + "");
			stat.setString(2, "1");
			stat.setString(3, i + "");
			stat.setString(4, "test@test.com");
			stat.setString(5, "zn");
			stat.setString(6, "Head" + i % 10);
			//stat.setString(7, stamp);
			stat.setTimestamp(8, stamp);
			stat.setInt(9, 0);
			stat.setInt(10, 0);
			stat.setInt(11, 0);
			
			stat.executeUpdate();
			
			stat = conn.prepareStatement(bank);
			stat.setString(1, i + "");
			stat.setDouble(2, 1000);
			
			stat.executeUpdate();
			
		}
		
		stat.close();
		
		conn.close();
		
	}

}

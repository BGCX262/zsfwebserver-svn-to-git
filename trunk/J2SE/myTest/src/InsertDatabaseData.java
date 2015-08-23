import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;



public class InsertDatabaseData {
	
	static int blueint = 40301;
	static int id = 700;

	public static void main(String[] args) throws Exception {
		String sql = "select * from blueprintbean where level = 10;";
		List<String> list = new ArrayList<String>();

		Connection conn = MyTools.getConnByMySql("localhost", "3306", "td", "root", "root");
		//ResultSet rs = MyTools.getRs(sql, conn);
		PreparedStatement ps = conn.prepareStatement("insert into indexbean values(?,4,0,0,1,1,'炮塔')");
		
		for (int i = 0 ; i < 120; i++) {
			ps.setInt(1, blueint++);
			ps.executeUpdate();
		}
		//MyTools.modifyDatabase(list.toArray(new String[] {}), conn, false);
		
		/*int count = 0;
		while (rs.next()) {
			System.out.print(rs.getString("name") + "\t");
			System.out.print(rs.getInt("product") + "\t");
			System.out.println(rs.getInt("baseLevel"));
			
			String s9 = "update blueprintbean set synthesizeSuccessID = " + (++blueint) + ",synthesizeFailedID=" + rs.getInt("goodID") + 
						" where goodID="+rs.getInt("goodID") + " and id="+rs.getInt("id");
			
			String s10 = "insert into blueprintbean values("+(++id)+","+(blueint)+",3,'"+rs.getString("name")+"',11,"+rs.getInt("baseLevel")+"," +
					"'"+rs.getString("resourceName")+"',"+rs.getInt("race")+","+(blueint + 10000)+","+rs.getInt("upID")+"," +
							rs.getInt("primaryStoneDeID") + ","+rs.getInt("seniorStoneDeID")+","+rs.getDouble("primaryStoneUpRate")+"," +
									rs.getDouble("seniorStoneUpRate") + ",'',"+rs.getInt("goodID")+","+(blueint+1)+")";
			
			String i10 = "insert into indexbean values("+(blueint)+",3,0,0,1,1,'"+rs.getString("name")+"');";
			String i11 = "insert into indexbean values("+(blueint+1)+",3,0,0,1,1,'"+rs.getString("name")+"');";
			String i12 = "insert into indexbean values("+(blueint+2)+",3,0,0,1,1,'"+rs.getString("name")+"');";
			String i13 = "insert into indexbean values("+(blueint+3)+",3,0,0,1,1,'"+rs.getString("name")+"');";
			
			String s11 = "insert into blueprintbean values("+(++id)+","+(++blueint)+",3,'"+rs.getString("name")+"',12,"+rs.getInt("baseLevel")+"," +
			"'"+rs.getString("resourceName")+"',"+rs.getInt("race")+","+(blueint + 10000)+","+rs.getInt("upID")+"," +
					rs.getInt("primaryStoneDeID") + ","+rs.getInt("seniorStoneDeID")+","+rs.getDouble("primaryStoneUpRate")+"," +
							rs.getDouble("seniorStoneUpRate") + ",'',"+rs.getInt("goodID")+","+(blueint+1)+")";
			
			String s12 = "insert into blueprintbean values("+(++id)+","+(++blueint)+",3,'"+rs.getString("name")+"',13,"+rs.getInt("baseLevel")+"," +
			"'"+rs.getString("resourceName")+"',"+rs.getInt("race")+","+(blueint + 10000)+","+rs.getInt("upID")+"," +
					rs.getInt("primaryStoneDeID") + ","+rs.getInt("seniorStoneDeID")+","+rs.getDouble("primaryStoneUpRate")+"," +
							rs.getDouble("seniorStoneUpRate") + ",'',"+rs.getInt("goodID")+","+(blueint+1)+")";
			
			String s13 = "insert into blueprintbean values("+(++id)+","+(++blueint)+",3,'"+rs.getString("name")+"',14,"+rs.getInt("baseLevel")+"," +
			"'"+rs.getString("resourceName")+"',"+rs.getInt("race")+","+(blueint + 10000)+","+rs.getInt("upID")+"," +
					rs.getInt("primaryStoneDeID") + ","+rs.getInt("seniorStoneDeID")+","+rs.getDouble("primaryStoneUpRate")+"," +
							rs.getDouble("seniorStoneUpRate") + ",'',"+rs.getInt("goodID")+","+(blueint)+")";
			
			MyTools.modifyDatabase(new String[] {s9,s10,s11,s12,s13,i10,i11,i12,i13}, conn, false);
			count++;
		}*/
		//System.out.println(count);
	}

}

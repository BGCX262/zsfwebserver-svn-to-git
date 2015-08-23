import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;


public class CheckDatabase {
	
	public static void main(String[] args) {
		String sql = "select sum(p_money), gh_id from tb_poundage where p_game='zhajinhua' group by gh_id order by gh_id";
		String sql1 = "select gh_result, gh_id, gh_date from tb_gamehistory_zjh order by gh_id";
		String sql2 = "select sum(bd_value), bd_date from tb_bankdetails where bd_type='zhajinhua' group by bd_date order by bd_date";
		String sql3 = "select sum(p_money), p_date from tb_poundage where p_game='zhajinhua' group by p_date order by p_date";
		
		Connection conn = MyTools.getConnByMySql("localhost", "3306", "db_ttlgame", "root", "root");
		ResultSet rs = MyTools.getRs(sql, conn);
		ResultSet rs1 = MyTools.getRs(sql1, conn);
		ResultSet rs2 = MyTools.getRs(sql2, conn);
		ResultSet rs3 = MyTools.getRs(sql3, conn);
		
		//double r = 0;
		try {
			
			while (rs.next()) {
				rs1.next();
				
				if (rs.getInt(2) < rs1.getInt(2)) {
					rs.next();
				} else if (rs1.getInt(2) < rs.getInt(2)) {
					rs1.next();
				}
				
				String str = rs1.getString(1);
				str = str.replaceAll("\\[(\\d|[a-z|A-Z])+\\]:", "");
				String[] strArr = str.split(";");
				double rsd = 0.0;
				
				for (String s : strArr) {
					rsd = MyTools.add(rsd, Double.valueOf(s).doubleValue());
				}
				
				double res = MyTools.add(rs.getDouble(1), rsd);
				if (MyTools.getResult(res) != 0) {
					System.out.println(rs1.getString(1));
					System.out.println(str);
					System.out.println(rs.getInt(2) + "行----------" + rs1.getInt(2) + "行");
					System.out.println(rs.getDouble(1) + "----------------" + rsd + "=" + rs1.getString(1));
				}
				
			}
			System.out.println("游戏记录查询完毕");
			double r1 = 0;
			double r2 = 0;
			while (rs2.next()) {
				rs3.next();
				
				r1 = MyTools.add(rs2.getDouble(1), r1);
				r2 = MyTools.add(rs3.getDouble(1), r2);

				Date d1 = MyTools.dateFormat.parse(rs2.getString(2).substring(0, rs2.getString(2).lastIndexOf(".")));
				Date d2 = MyTools.dateFormat.parse(rs3.getString(2).substring(0, rs3.getString(2).lastIndexOf(".")));
				
				if (d1.after(d2)) {
					rs2.next();
				} else if (d2.after(d1)) {
					rs3.next();
				}
				
				if (MyTools.add(rs2.getDouble(1), rs3.getDouble(1)) != 0) {
					System.out.println(rs2.getDouble(1) + " ------------ " + rs3.getDouble(1));
					System.out.println(rs2.getString(2) + "------------" + rs3.getString(2));
				}
			}
			r1 = MyTools.getResult(r1);
			r2 = MyTools.getResult(r2);
			System.out.println(r1 + " -0-0-0-0-0-0-0-0-0-0-0-0-0- " + r2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

package dbTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.TimeZone;


public class GetFightInfo {
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		
		Calendar cb = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
		cb.set(cb.get(Calendar.YEAR), cb.get(Calendar.MONTH), 9, 0, 0, 0);
		Calendar c = (Calendar) cb.clone();
		c.add(Calendar.DATE, 1);
		
		System.out.println("开始时间： " + cb.getTime().toLocaleString());
		System.out.println("结束时间： " + c.getTime().toLocaleString());
		
		Connection conn = MyTools.getConnByMySql("66.175.97.20", "3306", "td", "centospub", "09492389");
		
		String 总战斗次数 = "select count(*) as '总战斗次数' from message where time between ? and ? and (messageType=1 or messageType=2)";
		String 每天攻击次数 = "select count(*) as '每天攻击次数' from message where time between ? and ? and (messageType=1 or messageType=2)";
		String 每天被攻击次数 = 每天攻击次数;
		String 攻击成功次数 = "select count(*) as '攻击成功次数' from message where time between ? and ? and messageType=1";
		String 防守成功次数 = "select count(*) as '防守成功次数' from message where time between ? and ? and messageType=2";
		String 每天被打成HP为0次数 = "select count(*) as '每天被打成HP为0次数' from message where time between ? and ? and messageType=1";
		
		/* 总战斗次数 */
		PreparedStatement pstat = conn.prepareStatement(总战斗次数);
		
		pstat.setLong(1, cb.getTime().getTime());
		pstat.setLong(2, c.getTime().getTime());
		
		ResultSet rs = pstat.executeQuery();
		rs.next();
		总战斗次数 = rs.getDouble(1) + "";
		//System.out.println(pstat.toString().substring(pstat.toString().indexOf(": ") + 2, pstat.toString().length()));
		System.out.println("总战斗次数 \t\t-------------- success~");
		
		/* 每天攻击次数 */
		pstat = conn.prepareStatement(每天攻击次数);
		
		pstat.setLong(1, cb.getTime().getTime());
		pstat.setLong(2, c.getTime().getTime());
		
		rs = pstat.executeQuery();
		rs.next();
		每天攻击次数 = rs.getDouble(1) + "";
		//System.out.println(pstat.toString().substring(pstat.toString().indexOf(": ") + 2, pstat.toString().length()));
		System.out.println("每天攻击次数 \t\t-------------- success~");
		
		/* 每天被攻击次数 */
		每天被攻击次数 = 每天攻击次数;
		System.out.println("每天被攻击次数 \t\t-------------- success~");
		
		/* 攻击成功次数 */
		pstat = conn.prepareStatement(攻击成功次数);
		
		pstat.setLong(1, cb.getTime().getTime());
		pstat.setLong(2, c.getTime().getTime());
		
		rs = pstat.executeQuery();
		rs.next();
		//攻击成功次数 = rs.getDouble(1) + "";
		//System.out.println(pstat.toString().substring(pstat.toString().indexOf(": ") + 2, pstat.toString().length()));
		System.out.println("攻击成功次数 \t\t-------------- success~");
		
		/* 防守成功次数 */
		pstat = conn.prepareStatement(防守成功次数);
		
		pstat.setLong(1, cb.getTime().getTime());
		pstat.setLong(2, c.getTime().getTime());
		
		rs = pstat.executeQuery();
		rs.next();
		//防守成功次数 = rs.getDouble(1) + "";
		//System.out.println(pstat.toString().substring(pstat.toString().indexOf(": ") + 2, pstat.toString().length()));
		System.out.println("防守成功次数 \t\t-------------- success~");
		
		/* 每天被打成HP为0次数 */
		pstat = conn.prepareStatement(每天被打成HP为0次数);
		
		pstat.setLong(1, cb.getTime().getTime());
		pstat.setLong(2, c.getTime().getTime());
		
		rs = pstat.executeQuery();
		rs.next();
		每天被打成HP为0次数 = rs.getDouble(1) + "";
		//System.out.println(pstat.toString().substring(pstat.toString().indexOf(": ") + 2, pstat.toString().length()));
		System.out.println("每天被打成HP为0次数 \t-------------- success~");
		
		System.out.println("\r\n======================================================\r\n");
		
		System.out.println("总战斗次数: " + 总战斗次数);
		System.out.println("每天攻击次数: " + 每天攻击次数);
		System.out.println("每天被攻击次数: " + 每天被攻击次数);
		//System.out.println("攻击成功次数: " + 攻击成功次数);
		//System.out.println("防守成功次数: " + 防守成功次数);
		System.out.println("每天被打成HP为0次数: " + 每天被打成HP为0次数);
		
	}

}
/*
开始时间： 2011-1-7 0:00:00
结束时间： 2011-1-8 0:00:00
总战斗次数: 9466
每天被打成HP为0次数: 4410
 */
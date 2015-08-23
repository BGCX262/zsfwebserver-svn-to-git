package brydio.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 用于数据库一系列操作的工具
 * @author D-io
 *
 */
public class DatabaseUtil {

	/**
	 * 获得MySQL连接
	 * @param ip		IP地址
	 * @param port		端口
	 * @param dbName	数据库名
	 * @param user		用户名
	 * @param password	密码
	 * @return
	 */
	public static Connection getConnByMySql(String ip, String port, String dbName, String user, String password) {
		Connection conn = null;
		
		String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			System.err.println("驱动未找到，错误信息：" + e.getMessage());
		} catch (SQLException e) {
			System.err.println("连接错误，错误信息：" + e.getMessage());
		}
		
		return conn;
	}
	
	/**
	 * 执行查询语句
	 * @param sql	SQL语句
	 * @param conn	连接对象
	 * @return
	 */
	public static ResultSet getRs(String sql, Connection conn) {
		Statement st;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			System.err.println("执行查询语句时出错，错误信息：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 修改数据库
	 * @param sql			增删改命令数组
	 * @param conn			连接对象
	 * @param isTransform	是否开启事务锁
	 * @return
	 */
	public static boolean modifyDatabase(String[] sql, Connection conn, boolean isTransform) {
		boolean flag = false;
		
		PreparedStatement stat = null;

		try {
			if (isTransform) {
				conn.setAutoCommit(false);
			}
			for (String s : sql) {
				stat = conn.prepareStatement(s);
				
				if (!stat.execute()) {
					flag = false;
					break;
				}
			}
			
			if (isTransform) {
				if (flag) {
					conn.commit();
				}
				
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			System.err.println("执行修改语句时出错，错误信息：" + e.getMessage());
			if (isTransform) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.err.println("数据库回滚失败，错误信息：" + e.getMessage());
				}
			}
		}
		
		return flag;
	}
}

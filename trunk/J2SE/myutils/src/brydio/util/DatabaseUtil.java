package brydio.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * �������ݿ�һϵ�в����Ĺ���
 * @author D-io
 *
 */
public class DatabaseUtil {

	/**
	 * ���MySQL����
	 * @param ip		IP��ַ
	 * @param port		�˿�
	 * @param dbName	���ݿ���
	 * @param user		�û���
	 * @param password	����
	 * @return
	 */
	public static Connection getConnByMySql(String ip, String port, String dbName, String user, String password) {
		Connection conn = null;
		
		String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			System.err.println("����δ�ҵ���������Ϣ��" + e.getMessage());
		} catch (SQLException e) {
			System.err.println("���Ӵ��󣬴�����Ϣ��" + e.getMessage());
		}
		
		return conn;
	}
	
	/**
	 * ִ�в�ѯ���
	 * @param sql	SQL���
	 * @param conn	���Ӷ���
	 * @return
	 */
	public static ResultSet getRs(String sql, Connection conn) {
		Statement st;
		try {
			st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			return rs;
		} catch (SQLException e) {
			System.err.println("ִ�в�ѯ���ʱ����������Ϣ��" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * �޸����ݿ�
	 * @param sql			��ɾ����������
	 * @param conn			���Ӷ���
	 * @param isTransform	�Ƿ���������
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
			System.err.println("ִ���޸����ʱ����������Ϣ��" + e.getMessage());
			if (isTransform) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.err.println("���ݿ�ع�ʧ�ܣ�������Ϣ��" + e.getMessage());
				}
			}
		}
		
		return flag;
	}
}

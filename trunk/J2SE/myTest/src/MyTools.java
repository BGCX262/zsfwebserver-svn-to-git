import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;


public class MyTools {
	
	/**
	 * ��ʽ������
	 */
	public static NumberFormat format = null;
	
	/**
	 * ����ת����
	 */
	public static SimpleDateFormat dateFormat = null;
	
	/**
	 * �����ļ�·��
	 */
	private static String path = "config.properties";
	
	/**
	 * �����ļ�
	 */
	private static Properties properties = null;
	
	/**
	 * ��ʼ��
	 */
	static {
		format = NumberFormat.getInstance();
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(0);
		format.setGroupingUsed(false);
		
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		
		properties = new Properties();
		try {
			properties.load(MyTools.class.getResourceAsStream(path));
		} catch (FileNotFoundException e) {
			System.err.println("�ļ�δ�ҵ���������Ϣ��" + e.getMessage());
		} catch (IOException e) {
			System.err.println("�ļ���ȡ���󣬴�����Ϣ��" + e.getMessage());
		}
	}
	
	/**
	 * �ַ���ת��������
	 * @param str
	 * @return
	 */
	public static int string2Int(String str) {
		return Integer.valueOf(str).intValue();
	}
	
	/**
	 * �ַ���ת���ɸ�����
	 * @param str
	 * @return
	 */
	public static double string2Double(String str) {
		return Double.valueOf(str).doubleValue();
	}
	
	/**
	 * �ַ���ת���ɸ����ͣ������ȣ�
	 * @param str
	 * @return
	 */
	public static float string2Float(String str) {
		return Float.valueOf(str).floatValue();
	}
	
	/**
	 * ��������ת���ɼ���
	 * @param a
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Integer> number2List(final int[] a){   
        return new AbstractList(){   

            @Override   
            public Object get(int index)   
            {   
                return new Integer(a[index]);   
            }   

            @Override   
            public int size()   
            {   
                return a.length;   
            }   
            
            public Object set(int index, Object o){   
                int oldVal = a[index];   
                a[index] = ((Integer)o).intValue();   
                return new Integer(oldVal);   
            }
            
        };
    }
	
	/**
	 * ��������ת���ɼ���
	 * @param a
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Double> number2List(final double[] a){   
        return new AbstractList(){   

            @Override   
            public Object get(int index)   
            {   
                return new Double(a[index]);   
            }   

            @Override   
            public int size()   
            {   
                return a.length;   
            }   
            
            public Object set(int index, Object o){   
            	double oldVal = a[index];   
                a[index] = ((Double)o).doubleValue();   
                return new Double(oldVal);   
            }
            
        };
    }
	
	/**
	 * ��������ת���ɼ���(������)
	 * @param a
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Float> number2List(final float[] a){   
        return new AbstractList(){   

            @Override   
            public Object get(int index)   
            {   
                return new Float(a[index]);   
            }   

            @Override   
            public int size()   
            {   
                return a.length;   
            }   
            
            public Object set(int index, Object o){   
            	float oldVal = a[index];   
                a[index] = ((Float)o).floatValue();   
                return new Float(oldVal);   
            }
            
        };
    }
	
	/**
	 * ���MySQL����
	 * @param ip		IP��ַ
	 * @param port		�˿�
	 * @param db		���ݿ���
	 * @param user		�û���
	 * @param password	����
	 * @return
	 */
	public static Connection getConnByMySql(String ip, String port, String db, String user, String password) {
		Connection conn = null;
		
		String url = "jdbc:mysql://" + ip + ":" + port + "/" + db;
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
			st = conn.createStatement(ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE);
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
					//break;
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
	
	/**
	 * ѡ������
	 * @param intArr	��������
	 * @param desc		������trueΪ����falseΪ����
	 */
	public static void sortBySelect(int[] intArr, boolean desc) {
		int max = 0;
		int m = 0;
		
		for (int i = 0; i < intArr.length; i++) {
			max = intArr[i];
			m = i;
			for (int j = i + 1; j < intArr.length; j++) {
				if (desc) {
					if (max > intArr[j]) {
						max = intArr[j];
						m = j;
					}
				} else {
					if (max < intArr[j]) {
						max = intArr[j];
						m = j;
					}
				}
			}
			if (m != i) {
				intArr[m] = intArr[i];
				intArr[i] = max;
			}
		}
	}
	
	/**
	 * ѡ������
	 * @param doubleArr	��������
	 * @param desc		������trueΪ����falseΪ����
	 */
	public static void sortBySelect(double[] doubleArr, boolean desc) {
		double max = 0;
		int m = 0;
		
		for (int i = 0; i < doubleArr.length; i++) {
			max = doubleArr[i];
			m = i;
			for (int j = i + 1; j < doubleArr.length; j++) {
				if (desc) {
					if (max > doubleArr[j]) {
						max = doubleArr[j];
						m = j;
					}
				} else {
					if (max < doubleArr[j]) {
						max = doubleArr[j];
						m = j;
					}
				}
			}
			if (m != i) {
				doubleArr[m] = doubleArr[i];
				doubleArr[i] = max;
			}
		}
	}
	
	/**
	 * ѡ������
	 * @param floatArr	��������(������)
	 * @param desc		������trueΪ����falseΪ����
	 */
	public static void sortBySelect(float[] floatArr, boolean desc) {
		float max = 0;
		int m = 0;
		
		for (int i = 0; i < floatArr.length; i++) {
			max = floatArr[i];
			m = i;
			for (int j = i + 1; j < floatArr.length; j++) {
				if (desc) {
					if (max > floatArr[j]) {
						max = floatArr[j];
						m = j;
					}
				} else {
					if (max < floatArr[j]) {
						max = floatArr[j];
						m = j;
					}
				}
			}
			if (m != i) {
				floatArr[m] = floatArr[i];
				floatArr[i] = max;
			}
		}
	}
	
	/**
	 * ��ʽ��������
	 * @param d
	 * @return
	 */
	public static double getResult(double d) {
		return Double.valueOf(format.format(d)).doubleValue();
	}
	
	/**
	 * ��ʽ��������(������)
	 * @param d
	 * @return
	 */
	public static double getResult(float f) {
		return Float.valueOf(format.format(f)).floatValue();
	}
	
	/**
	 * �������
	 * @param a
	 * @param b
	 * @return
	 */
	public static double add(double a, double b) {
		return new BigDecimal(a).add(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * �������
	 * @param a
	 * @param b
	 * @return
	 */
	public static double minus(double a, double b) {
		return new BigDecimal(a).subtract(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * �������
	 * @param a
	 * @param b
	 * @return
	 */
	public static double multiply(double a, double b) {
		return new BigDecimal(a).multiply(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * �������
	 * @param a
	 * @param b
	 * @return
	 */
	public static double divide(double a, double b) {
		return new BigDecimal(a).divide(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * �������(������)
	 * @param a
	 * @param b
	 * @return
	 */
	public static float add(float a, float b) {
		return new BigDecimal(a).add(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * �������(������)
	 * @param a
	 * @param b
	 * @return
	 */
	public static float minus(float a, float b) {
		return new BigDecimal(a).subtract(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * �������(������)
	 * @param a
	 * @param b
	 * @return
	 */
	public static float multiply(float a, float b) {
		return new BigDecimal(a).multiply(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * �������(������)
	 * @param a
	 * @param b
	 * @return
	 */
	public static double divide(float a, float b) {
		return new BigDecimal(a).divide(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * ���ʱ��
	 * @param date
	 * @param type
	 * @return
	 */
	public static String dateFormart(Date date, String type) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
		return dateFormat.format(dateFormat);
	}

	/**
	 * ���ʱ��
	 * @param date
	 * @return
	 */
	public static String dateFormart(Date date) {
		return dateFormat.format(dateFormat);
	}
	
	/**
	 * ���ݼ����ֵ
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * ���ݼ���ö���
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		return properties.getProperty(key);
	}
	
	public static void main(String[] args) {
		System.out.println(System.getenv());
	}
}

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
	 * 格式化工具
	 */
	public static NumberFormat format = null;
	
	/**
	 * 日期转换器
	 */
	public static SimpleDateFormat dateFormat = null;
	
	/**
	 * 属性文件路径
	 */
	private static String path = "config.properties";
	
	/**
	 * 属性文件
	 */
	private static Properties properties = null;
	
	/**
	 * 初始化
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
			System.err.println("文件未找到，错误信息：" + e.getMessage());
		} catch (IOException e) {
			System.err.println("文件读取错误，错误信息：" + e.getMessage());
		}
	}
	
	/**
	 * 字符串转换成整形
	 * @param str
	 * @return
	 */
	public static int string2Int(String str) {
		return Integer.valueOf(str).intValue();
	}
	
	/**
	 * 字符串转换成浮点型
	 * @param str
	 * @return
	 */
	public static double string2Double(String str) {
		return Double.valueOf(str).doubleValue();
	}
	
	/**
	 * 字符串转换成浮点型（单精度）
	 * @param str
	 * @return
	 */
	public static float string2Float(String str) {
		return Float.valueOf(str).floatValue();
	}
	
	/**
	 * 整形数组转换成集合
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
	 * 浮点数组转换成集合
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
	 * 浮点数组转换成集合(单精度)
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
	 * 获得MySQL连接
	 * @param ip		IP地址
	 * @param port		端口
	 * @param db		数据库名
	 * @param user		用户名
	 * @param password	密码
	 * @return
	 */
	public static Connection getConnByMySql(String ip, String port, String db, String user, String password) {
		Connection conn = null;
		
		String url = "jdbc:mysql://" + ip + ":" + port + "/" + db;
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
			st = conn.createStatement(ResultSet.CONCUR_READ_ONLY, ResultSet.TYPE_SCROLL_INSENSITIVE);
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
	
	/**
	 * 选择排序
	 * @param intArr	整形数组
	 * @param desc		升序降序true为降序false为升序
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
	 * 选择排序
	 * @param doubleArr	浮点数组
	 * @param desc		升序降序true为降序false为升序
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
	 * 选择排序
	 * @param floatArr	浮点数组(单精度)
	 * @param desc		升序降序true为降序false为升序
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
	 * 格式化浮点数
	 * @param d
	 * @return
	 */
	public static double getResult(double d) {
		return Double.valueOf(format.format(d)).doubleValue();
	}
	
	/**
	 * 格式化浮点数(单精度)
	 * @param d
	 * @return
	 */
	public static double getResult(float f) {
		return Float.valueOf(format.format(f)).floatValue();
	}
	
	/**
	 * 浮点相加
	 * @param a
	 * @param b
	 * @return
	 */
	public static double add(double a, double b) {
		return new BigDecimal(a).add(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * 浮点相减
	 * @param a
	 * @param b
	 * @return
	 */
	public static double minus(double a, double b) {
		return new BigDecimal(a).subtract(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * 浮点相乘
	 * @param a
	 * @param b
	 * @return
	 */
	public static double multiply(double a, double b) {
		return new BigDecimal(a).multiply(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * 浮点相除
	 * @param a
	 * @param b
	 * @return
	 */
	public static double divide(double a, double b) {
		return new BigDecimal(a).divide(new BigDecimal(b)).doubleValue();
	}
	
	/**
	 * 浮点相加(单精度)
	 * @param a
	 * @param b
	 * @return
	 */
	public static float add(float a, float b) {
		return new BigDecimal(a).add(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * 浮点相加(单精度)
	 * @param a
	 * @param b
	 * @return
	 */
	public static float minus(float a, float b) {
		return new BigDecimal(a).subtract(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * 浮点相加(单精度)
	 * @param a
	 * @param b
	 * @return
	 */
	public static float multiply(float a, float b) {
		return new BigDecimal(a).multiply(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * 浮点相加(单精度)
	 * @param a
	 * @param b
	 * @return
	 */
	public static double divide(float a, float b) {
		return new BigDecimal(a).divide(new BigDecimal(b)).floatValue();
	}
	
	/**
	 * 获得时间
	 * @param date
	 * @param type
	 * @return
	 */
	public static String dateFormart(Date date, String type) {
		dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
		return dateFormat.format(dateFormat);
	}

	/**
	 * 获得时间
	 * @param date
	 * @return
	 */
	public static String dateFormart(Date date) {
		return dateFormat.format(dateFormat);
	}
	
	/**
	 * 根据键获得值
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * 根据键获得对象
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

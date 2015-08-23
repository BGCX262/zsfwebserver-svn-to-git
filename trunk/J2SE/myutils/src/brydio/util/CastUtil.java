package brydio.util;

import java.util.AbstractList;
import java.util.List;

/**
 * 用于转换的工具
 * @author D-io
 *
 */
public class CastUtil {
	
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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

}

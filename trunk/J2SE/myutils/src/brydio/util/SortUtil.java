package brydio.util;

/**
 * 排序的工具
 * @author D-io
 *
 */
public class SortUtil {

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
	 * 选择排序
	 * @param floatArr	数组
	 * @param desc		升序降序true为降序false为升序
	 */
	public static void sortBySelect(byte[] byteArr, boolean desc) {
		byte max = 0;
		int m = 0;
		
		for (int i = 0; i < byteArr.length; i++) {
			max = byteArr[i];
			m = i;
			for (int j = i + 1; j < byteArr.length; j++) {
				if (desc) {
					if (max > byteArr[j]) {
						max = byteArr[j];
						m = j;
					}
				} else {
					if (max < byteArr[j]) {
						max = byteArr[j];
						m = j;
					}
				}
			}
			if (m != i) {
				byteArr[m] = byteArr[i];
				byteArr[i] = max;
			}
		}
	}
	
	/**
	 * 选择排序
	 * @param floatArr	数组
	 * @param desc		升序降序true为降序false为升序
	 */
	public static void sortBySelect(long[] longArr, boolean desc) {
		long max = 0;
		int m = 0;
		
		for (int i = 0; i < longArr.length; i++) {
			max = longArr[i];
			m = i;
			for (int j = i + 1; j < longArr.length; j++) {
				if (desc) {
					if (max > longArr[j]) {
						max = longArr[j];
						m = j;
					}
				} else {
					if (max < longArr[j]) {
						max = longArr[j];
						m = j;
					}
				}
			}
			if (m != i) {
				longArr[m] = longArr[i];
				longArr[i] = max;
			}
		}
	}
	
}

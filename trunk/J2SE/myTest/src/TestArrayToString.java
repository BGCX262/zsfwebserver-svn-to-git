import java.util.AbstractList;
import java.util.List;

public class TestArrayToString {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Integer> intArrayAsList(final int[] a, final int order) {
		if (a == null)
			throw new NullPointerException();

		return new AbstractList() {

			@Override
			public Object get(int index) {
				return new Integer(order == 1 ? a[index] : a[size() - index - 1]);
			}

			@Override
			public int size() {
				return a.length;
			}

			// 排序所用到的方法
			public Object set(int index, Object o) {
				int oldVal = order == 1 ? a[index] : a[size() - index - 1];
				a[index] = ((Integer) o).intValue();
				return new Integer(oldVal);
			}

			@Override
			public String toString() {
				return super.toString().substring(1, size() * 3 - 1);
			}

		};
	}

	public static void main(String[] args) {
		long begin = System.currentTimeMillis();
		int[] intArr = new int[] { 1, 2, 3, 4, 5, 6 };
		System.out.println(intArrayAsList(intArr, -1));
		long end = System.currentTimeMillis();
		System.out.println(end - begin);
	}

}

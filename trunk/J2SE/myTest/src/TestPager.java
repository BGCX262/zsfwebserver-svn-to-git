

public class TestPager {
	
	static int currentPage = 19;
	static int pageCount = 19;
	static int pageSize = 30;		// 显示的页面索引数量

	public static void main(String[] args) {
		
		/* 首先，实现效果肯定是一个循环，循环作用于中间10个页面下标 */
		/* 其次，判断页首页尾是否显示 */
		/* 最后，判断翻页是否显示 */
		
		/* 判断页首 */
		if (currentPage > pageSize / 2) {
			System.out.print("1... ");
		}
		
		int n = Math.max(currentPage - pageSize / 2 + 1, 1);
		n = Math.min(n, pageCount - pageSize + 1);
		int i;
		for (i = n; i < n + pageSize; i++) {
			System.out.print(i + " ");
		}
		
		if (i < pageCount)
			System.out.print("..." + pageCount);
		
	}

}

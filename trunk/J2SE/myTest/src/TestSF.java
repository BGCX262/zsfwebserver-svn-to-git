

public class TestSF {

	/** 需要替换的颜色 **/
	public static final int COLOR = 0xffffffff;

	/**
	 * 将指定颜色不透明度设为50%
	 * @param pxs
	 * @return
	 */
	public int[] replaceColor(int[] pxs) {
		
		for (int i = 0; i < pxs.length; i++) {
			if (pxs[i] == COLOR) {
				pxs[i] = pxs[i] ^ 0xff | (0xff >> 1);
			}
			
		}
		
		return pxs;
	}
	
	public static void main(String[] args) {
		int test = 0xffffffff;
		test = test ^ 0xff | (0xff >> 1);
		
		System.out.println(Integer.toHexString(test));
		System.out.println((0xff << 1) & 0xff);
	}
	
}

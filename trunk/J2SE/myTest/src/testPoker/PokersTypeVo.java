package testPoker;

/**
 * 一组牌牌型的描述
 * 
 * @author xy
 */
public class PokersTypeVo {
	/**
	 * 非炸弹牌型全部小于10
	 */
	public static final int TYPE_DAN = 1; // 单张
	public static final int TYPE_DUI = 2; // 对子
	public static final int TYPE_SHUN = 3; // 顺子
	public static final int TYPE_LIANDUI = 4; // 连对
	public static final int TYPE_SAN = 5; // 三张
	public static final int TYPE_LIANSAN = 6; // 连三张

	/**
	 * 炸弹牌型全部大于10，并按大小排列
	 */
	public static final int BOMB_4 = 14; // 4星炸弹
	public static final int BOMB_5 = 15; // 5星炸弹
	public static final int BOMB_6 = 16; // 6星炸弹
	public static final int BOMB_7 = 17; // 7星炸弹
	public static final int BOMB_8 = 18; // 8星炸弹
	public static final int BOMB_9 = 19; // 9星炸弹
	public static final int BOMB_10 = 20; // 10星炸弹
	public static final int BOMB_11 = 21; // 11星炸弹

	/* 牌型 */
	public int type = 0;

	/* 最大的牌点 */
	public int maxPoint = 0;

	/* 牌的张数 */
	public int len = 0;

	public String toString() {
		String[] arr = new String[] { "单张", "对子", "顺子", "连对", "三张", "连三张" };

		String str = "";
		if (type < 10) {
			str = arr[type - 1];
		} else {
			str = (type - 10) + "星炸弹";
		}

		return str + "; 最大的牌点=" + maxPoint + "; 张数=" + len;
	}
}
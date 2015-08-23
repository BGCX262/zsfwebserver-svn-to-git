package testPoker;

public class Poker implements Comparable<Poker> {

	/**
	 * 扑克的点数 3-10，J，Q，K，A，2，小王，大王
	 */
	public static final int BIG_JOKER = 17;
	public static final int SML_JOKER = 16;
	public static final int POKER_2 = 15;
	public static final int POKER_A = 14;
	public static final int POKER_K = 13;
	public static final int POKER_Q = 12;
	public static final int POKER_J = 11;
	public static final int POKER_10 = 10;
	public static final int POKER_9 = 9;
	public static final int POKER_8 = 8;
	public static final int POKER_7 = 7;
	public static final int POKER_6 = 6;
	public static final int POKER_5 = 5;
	public static final int POKER_4 = 4;
	public static final int POKER_3 = 3;

	/**
	 * 扑克的牌型
	 */
	public static final int HEI = 3;
	public static final int HONG = 2;
	public static final int MEI = 1;
	public static final int FANG = 0;

	/**
	 * 牌的点数，花色，中文名，E文名
	 */
	public int id = 0;
	public int points = 0;
	public int type = 0;
	public String name = null;
	public String uiName = null;

	public Poker(int pkId) {
		this.id = pkId;
		this.points = pkId / 4 + 3;
		this.type = pkId % 4;

		init();
	}
	
	public Poker(int points, int type) {
		this.id = (points - 3) * 4 + type;
		this.points = points;
		this.type = type;

		init();
	}
	
	private void init() {
		if (id == 53) {
			points = BIG_JOKER;
		}

		switch (type) {
		case HEI:
			name = "黑";
			uiName = "Spades";
			break;
		case HONG:
			name = "红";
			uiName = "Hearts";
			break;
		case MEI:
			name = "梅";
			uiName = "Clabs";
			break;
		case FANG:
			name = "方";
			uiName = "Diamonds";
			break;
		default:
			name = "??";
			uiName = "??";
		}

		switch (points) {
		case BIG_JOKER:
			name = "大王";
			uiName = "RedJoker";
			break;
		case SML_JOKER:
			name = "小王";
			uiName = "BlackJoker";
			break;
		case POKER_2:
			name += "2";
			uiName += "2";
			break;
		case POKER_A:
			name += "A";
			uiName += "A";
			break;
		case POKER_K:
			name += "K";
			uiName += "K";
			break;
		case POKER_Q:
			name += "Q";
			uiName += "Q";
			break;
		case POKER_J:
			name += "J";
			uiName += "J";
			break;
		default:
			name += points;
			uiName += points;
		}
	}

	public String toString() {
		return name;
	}

	public int compareTo(Poker o) {
		return id < o.id ? 1 : -1;
	}

}

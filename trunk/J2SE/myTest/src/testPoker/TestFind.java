package testPoker;

import java.util.ArrayList;
import java.util.Iterator;

public class TestFind {

	private static ArrayList<Poker> sourcePks = null;
	private static ArrayList<Poker> targetPks = null;

	static int count = 0;
	
	/**
	 * 从 sourcePks中找到比targetPks大的牌
	 * 
	 * @param sourcePks
	 *            Array[PokerVo] 待找的很多牌
	 * @param targetPks
	 *            Array[PokerVo] 需要进行比对的几张牌
	 * @return Array[PokerVo] 最后比targetPks大的牌型
	 */
	public static ArrayList<ArrayList<Poker>> getHelpPks(
			ArrayList<Poker> sourcePks, ArrayList<Poker> targetPks) {
		TestFind.sourcePks = sourcePks;
		TestFind.targetPks = targetPks;
		ArrayList<Poker> list = new ArrayList<Poker>();
		ArrayList<ArrayList<Poker>> realTeams = new ArrayList<ArrayList<Poker>>();

		/* 获取王牌的张数 */
		//count = getCount(53, sourcePks);
		/*
		 * 利用穷举法，列出所有可匹配的牌型放进队列里 因循环次数不可知，使用递归
		 */
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker poker = sourcePks.get(i);
			if (poker.points > targetPks.get(0).points) {
				dgFind(i, list, realTeams);
			}
		}

		return realTeams;
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private static void dgFind1(int idx, ArrayList<Poker> rtnList, ArrayList<ArrayList<Poker>> realTeams){
		if (idx < sourcePks.size()) {
			for (int i = idx; i < sourcePks.size(); i++) {
				Poker pk = sourcePks.get(i);
				if (rtnList.size() == targetPks.size()) {
					PokersTypeVo type1 = Rule.getType(rtnList.toArray(new Poker[0]));
					PokersTypeVo type2 = Rule.getType(targetPks.toArray(new Poker[0]));

					/* 判断牌型是否比给的牌大 */
					if (type1 != null && type1.type == type2.type
							&& Rule.compare(type1, type2) > 0) {
						/* 此牌型是可用牌型 */
						mySort(rtnList);
						if (!realTeams.contains(rtnList))
							realTeams.add((ArrayList<Poker>) rtnList.clone());
						rtnList.clear();
					} else {
						/* 计算需要删除的个数 */
						int num = 1;
						for (int j = rtnList.size() - 1; j >= 0; j--) {
							/* 检查队列里面有手牌的最后N张 */
							if (rtnList.size() - j == sourcePks.size() - sourcePks.lastIndexOf(rtnList.get(j))) {
								num = rtnList.size() - j + 1;
							} else {
								break;
							}
						}
						
						/* 如果找出的牌型不匹配，则删除最后添加的N个，然后继续递归 */
						while (num -- > 0 && rtnList.size() > 0) {
							if (rtnList.get(rtnList.size() - 1).id == 53)
								System.out.println();
							rtnList.remove(rtnList.size() - 1);
						}
					}
					
				} else if (rtnList.size() < targetPks.size()) {
					/* 将牌放进集合中并再次递归 */
					if (!rtnList.contains(pk))
						rtnList.add(pk);
					dgFind(idx + 1, rtnList, realTeams);
					break;
				}
			}
		}
	}

	/**
	 * 递归递推法
	 * 
	 * @param idx
	 * @param targetPks
	 * @param sourcePks
	 */
	@SuppressWarnings("unchecked")
	private static int dgFind(int idx, ArrayList<Poker> rtnList, ArrayList<ArrayList<Poker>> realTeams) {
		/* 根据位置开始查找 */
		if (idx < sourcePks.size()) {
			for (int i = idx; i < sourcePks.size(); i++) {
				/* 每个都往里面放，然后判断是否满足条件 */
				if (!rtnList.contains(sourcePks.get(i))) {
					rtnList.add(sourcePks.get(i));
					/* 当牌的长度达到满足条件时 */
					if (rtnList.size() == targetPks.size()) {
						PokersTypeVo type1 = Rule.getType(rtnList.toArray(new Poker[0]));
						PokersTypeVo type2 = Rule.getType(targetPks.toArray(new Poker[0]));

						/* 判断牌型是否比给的牌大 */
						if (type1 != null && type1.type == type2.type
								&& Rule.compare(type1, type2) > 0) {
							/* 此牌型是可用牌型 */
							mySort(rtnList);
							if (!realTeams.contains(rtnList))
								realTeams.add((ArrayList<Poker>) rtnList.clone());
							rtnList.clear();
						} else {
							/* 计算需要删除的个数 */
							int num = 1;
							for (int j = rtnList.size() - 1; j >= 0; j--) {
								/* 检查队列里面有手牌的最后N张 */
								if (rtnList.size() - j == sourcePks.size() - sourcePks.lastIndexOf(rtnList.get(j))) {
									num = rtnList.size() - j + 1;
								} else {
									break;
								}
							}
							
							/* 如果找出的牌型不匹配，则删除最后添加的N个，然后继续递归 */
							int count = 0;
							while (num -- > 0 && rtnList.size() > 0) {
								if (rtnList.get(rtnList.size() - 1).id == 53)
									System.out.println();
								rtnList.remove(rtnList.size() - 1);
								count ++;
							}
							if (count > 1)
								return count;
						}
					} else {
						/* 如果长度没有达到要求，则继续递归 */
						int count = dgFind(idx + 1, rtnList, realTeams);
						if (count > 0)
							System.out.println();
					}
				}
			}
		}
		return 0;
	}

	/**
	 * 选择排序（高性能版）
	 * @param list
	 */
	private static void mySort(ArrayList<Poker> list) {
		int maxxer = -1;
		Poker max = null;
		for (int i = 0; i < list.size(); i++) {
			Poker poker = list.get(i);
			max = poker;
			maxxer = i;
			for (int j = i + 1; j < list.size(); j++) {
				Poker pk = list.get(j);
				if (pk.id < max.id) {
					max = pk;
					maxxer = j;
				}
			}

			if (max.id != poker.id) {
				list.set(maxxer, poker);
				list.set(i, max);
			}
		}
	}
	
	/**
	 * 根据ID在指定集合里找出出现次数
	 * @param id
	 * @param pks
	 * @return
	 */
	@SuppressWarnings("unused")
	private static int getCount(int id, ArrayList<Poker> pks) {
		int rtnVal = 0;
		
		for (Iterator<Poker> iter = pks.iterator(); iter.hasNext(); ) {
			if (iter.next().id == id) {
				rtnVal ++;
			}
		}
		
		return rtnVal;
	}

	public static void main(String[] args) {
		ArrayList<Poker> sourcePks = new ArrayList<Poker>();
		ArrayList<Poker> targetPks = new ArrayList<Poker>();

		sourcePks.add(new Poker(Poker.POKER_3, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_3, Poker.MEI));
		sourcePks.add(new Poker(Poker.POKER_4, Poker.HONG));
		sourcePks.add(new Poker(Poker.POKER_5, Poker.HEI));
		sourcePks.add(new Poker(Poker.POKER_5, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_5, Poker.MEI));
		sourcePks.add(new Poker(Poker.POKER_5, Poker.HONG));
		sourcePks.add(new Poker(Poker.POKER_5, Poker.HEI));
		sourcePks.add(new Poker(Poker.POKER_6, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_7, Poker.MEI));
		sourcePks.add(new Poker(Poker.POKER_7, Poker.HONG));
		sourcePks.add(new Poker(Poker.POKER_8, Poker.HEI));
		sourcePks.add(new Poker(Poker.POKER_8, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_10, Poker.MEI));
		sourcePks.add(new Poker(Poker.POKER_Q, Poker.HONG));
		sourcePks.add(new Poker(Poker.POKER_Q, Poker.HEI));
		sourcePks.add(new Poker(Poker.POKER_Q, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_K, Poker.MEI));
		sourcePks.add(new Poker(Poker.POKER_K, Poker.HONG));
		sourcePks.add(new Poker(Poker.POKER_K, Poker.HEI));
		sourcePks.add(new Poker(Poker.POKER_A, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_A, Poker.MEI));
		sourcePks.add(new Poker(Poker.POKER_A, Poker.HONG));
		sourcePks.add(new Poker(Poker.POKER_2, Poker.HEI));
		sourcePks.add(new Poker(Poker.POKER_2, Poker.FANG));
		sourcePks.add(new Poker(52));
		sourcePks.add(new Poker(53));

		targetPks.add(new Poker(Poker.POKER_3, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_3, Poker.FANG));
		//targetPks.add(new Poker(Poker.POKER_3, Poker.FANG));
		/*targetPks.add(new Poker(Poker.POKER_4, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_4, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_4, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_5, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_5, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_5, Poker.FANG));*/

		System.out.println(getHelpPks(sourcePks, targetPks));
	}

}

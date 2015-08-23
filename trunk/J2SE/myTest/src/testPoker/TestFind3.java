package testPoker;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;

public class TestFind3 {

	private static ArrayList<Poker> sourcePks = null;
	private static ArrayList<Poker> targetPks = null;
	private static PokersTypeVo type = null;
	private static int bigCount = 0;
	private static int smlCount = 0;
	private static int[] bigIdx = new int[2];
	private static int[] smlIdx = new int[2];

	/**
	 * �� sourcePks���ҵ���targetPks�����
	 * 
	 * @param sourcePks
	 *            Array[PokerVo] ���ҵĺܶ���
	 * @param targetPks
	 *            Array[PokerVo] ��Ҫ���бȶԵļ�����
	 * @return Array[PokerVo] ����targetPks�������
	 */
	public static ArrayList<ArrayList<Poker>> getHelpPks(ArrayList<Poker> sourcePks, ArrayList<Poker> targetPks) {
		Date begin = new Date();
		TestFind3.sourcePks = sourcePks;
		TestFind3.targetPks = targetPks;
		ArrayList<Poker> list = new ArrayList<Poker>();
		ArrayList<ArrayList<Poker>> realTeams = new ArrayList<ArrayList<Poker>>();

		type = Rule.getType(targetPks.toArray(new Poker[0]));

		if (type == null)
			return null;
		
		/* ��ȡ���� */
		for (int i = sourcePks.size() - 1; i >= 0; i--) {
			Poker pk = sourcePks.get(i);
			if (pk.points == Poker.BIG_JOKER) {
				bigIdx[bigCount] = i;
				bigCount++;
			} else if (pk.points == Poker.SML_JOKER) {
				smlIdx[smlCount] = i;
				smlCount++;
			} else if (pk.points <= Poker.POKER_2)
				break;
		}
		
		/* Ѱ����ͨ�� */
		switch (type.type) {
		case PokersTypeVo.TYPE_DAN:
			findDan(list, realTeams);
			break;
		case PokersTypeVo.TYPE_DUI:
			findDui(list, realTeams);
			break;
		case PokersTypeVo.TYPE_SHUN:
			findShun(list, realTeams);
			break;
		case PokersTypeVo.TYPE_SAN:
			findSan(list, realTeams);
			break;
		case PokersTypeVo.TYPE_LIANDUI:
			findLianDui(list, realTeams);
			break;
		case PokersTypeVo.TYPE_LIANSAN:
			findLianSan(list, realTeams);
			break;

		default:
			break;
		}
		
		/* Ѱ��ը�� */
		findJokerBomb(list, realTeams); // ��ը
		ArrayList<ArrayList<Poker>> lst = findShunBomb(list, realTeams); // ˳ը

		sortResult(lst);
		findLianShunBomb(list, realTeams, lst); // ��˳ը

		/* ������������ */
		sortResult(realTeams);
		
		/* �����ը�����޳����Լ�С���� */
		if (type.type >= PokersTypeVo.BOMB_4) {
			for (Iterator<ArrayList<Poker>> iter = realTeams.iterator(); iter.hasNext(); ) {
				PokersTypeVo tp = Rule.getType(iter.next().toArray(new Poker[0]));
				if (Rule.compare(type, tp) >= 0) {
					iter.remove();
				}
			}
		}
		
		/* �����ͬ�������� */
		clearSame(realTeams);
		
		/* �����Ч�� */
		//clearUnsaze(realTeams);

		System.out.println(realTeams.size());
		Date end = new Date();
		System.out.println(end.getTime() - begin.getTime() + "ms");
		return realTeams;
	}
	
	/**
	 * �ҵ���
	 * @param list
	 * @param realTeams
	 */
	private static void findDan(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (Iterator<Poker> iter = sourcePks.iterator(); iter.hasNext(); ) {
			Poker pk = iter.next();
			if (pk.points > targetPks.get(0).points) {
				add2List(list, pk);
				add2Team(realTeams, list);
				list.clear();
			}
		}
	}
	
	/**
	 * �Ҷ���
	 * @param list
	 * @param realTeams
	 */
	private static void findDui(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pk = sourcePks.get(i);
			list.clear();
			add2List(list, pk);
			if (pk.points > targetPks.get(0).points) {
				if (i + 1 < sourcePks.size()) {
					Poker pkm = sourcePks.get(i+1);
					if (pkm.points == pk.points) {
						add2List(list, pkm);
						add2Team(realTeams, list);
						list.clear();
					} else {
						addJoker(list, realTeams, 0);
						if (list.size() == 2) {
							add2Team(realTeams, list);
							list.clear();
						}
					}
				}
			}
		}
	}
	
	/**
	 * ��˳��
	 * @param list
	 * @param realTeams
	 */
	private static void findShun(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		int tBigCount = bigCount;
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			tBigCount = bigCount;
			list.clear();
			add2List(list, pki);
			if (pki.points > targetPks.get(0).points) {
				for (int j = i + 1; j < sourcePks.size(); j++) {
					Poker pkj = sourcePks.get(j);
					if (pkj.points - list.size() == pki.points && list.size() < targetPks.size() && 
							pkj.points != Poker.SML_JOKER && pkj.points != Poker.POKER_2) {
						add2List(list, pkj);
					} else if (pkj.points - list.size() > pki.points && list.size() == targetPks.size()) {
						add2Team(realTeams, list);
						list.clear();
						break;
					} else if (list.size() < targetPks.size() && tBigCount > 0) {
						if (pkj.points - list.size() > pki.points) {
							addJoker(list, realTeams, tBigCount - 1);
							tBigCount --;
							
							if (list.size() < targetPks.size() && 
									pkj.points - 2 == list.get(list.size() - 2).points) {
								add2List(list, pkj);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * ������
	 * @param list
	 * @param realTeams
	 */
	private static void findSan(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pk = sourcePks.get(i);
			list.clear();
			add2List(list, pk);
			if (pk.points > targetPks.get(0).points) {
				if (i + 1 < sourcePks.size()) {
					Poker pkm = sourcePks.get(i+1);
					if (pkm.points == pk.points) {
						add2List(list, pkm);
					} else { 
						addJoker(list, realTeams, 1);
					}
					if (i + 2 < sourcePks.size() && 
							sourcePks.get(i+2).points == pk.points) {
						Poker pkn = sourcePks.get(i+2);
						add2List(list, pkn);
						add2Team(realTeams, list);
						list.clear();
					} else {
						addJoker(list, realTeams, 0);
						if (list.size() == 3) {
							add2Team(realTeams, list);
							list.clear();
						}
					}
				}
			}
		}
	}
	
	/**
	 * ������
	 * @param list
	 * @param realTeams
	 */
	private static void findLianDui(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			list.clear();
			if (pki.points > targetPks.get(0).points) {
				add2List(list, pki);
				for (int j = i + 1; j < sourcePks.size(); j++) {
					Poker pkj = sourcePks.get(j);
					if ((list.size() < 1 || pkj.points - 1 == list.get(list.size() - 1).points || 
							pkj.points == list.get(list.size() - 1).points) && 
							(list.size() < 2 || pkj.points != list.get(list.size() - 2).points) && 
							list.size() < targetPks.size() && pkj.points != Poker.SML_JOKER && pkj.points != Poker.POKER_2) {
						add2List(list, pkj);
					} else {
						if (list.size() == targetPks.size()) {
							PokersTypeVo tp = Rule.getType(list.toArray(new Poker[0]));
							if (tp != null && tp.type == type.type)
								add2Team(realTeams, list);
							list.clear();
						} else if (list.size() < targetPks.size()) {
							if (list.size() + 1 == targetPks.size()) {
								addJoker(list, realTeams, 0);
							} else if (list.size() + 2 == targetPks.size()) {
								addJoker(list, realTeams, 0);
								addJoker(list, realTeams, 1);
							}
							if (list.size() == targetPks.size()) {
								PokersTypeVo tp = Rule.getType(list.toArray(new Poker[0]));
								if (tp != null && tp.type == type.type)
									add2Team(realTeams, list);
								list.clear();
							}
						}
					}
					if (list.size() < 1 || pkj.points - 2 >= list.get(list.size() - 1).points) {
						break;
					}
				}
			}
		}
	}
	
	/**
	 * ��������
	 * @param list
	 * @param realTeams
	 */
	private static void findLianSan(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			list.clear();
			if (pki.points > targetPks.get(0).points) {
				add2List(list, pki);
				for (int j = i + 1; j < sourcePks.size(); j++) {
					Poker pkj = sourcePks.get(j);
					if ((list.size() < 1 || pkj.points - 1 == list.get(list.size() - 1).points || 
							pkj.points == list.get(list.size() - 1).points) && 
							(list.size() < 3 || pkj.points != list.get(list.size() - 3).points) && 
							list.size() < targetPks.size() && pkj.points != Poker.SML_JOKER && pkj.points != Poker.POKER_2) {
						add2List(list, pkj);
					} else {
						if (list.size() == targetPks.size()) {
							PokersTypeVo tp = Rule.getType(list.toArray(new Poker[0]));
							if (tp != null && tp.type == type.type)
								add2Team(realTeams, list);
							list.clear();
						} else if (list.size() < targetPks.size()) {
							if (list.size() + 1 == targetPks.size()) {
								addJoker(list, realTeams, 0);
							} else if (list.size() + 2 == targetPks.size()) {
								addJoker(list, realTeams, 0);
								addJoker(list, realTeams, 1);
							}
							if (list.size() == targetPks.size()) {
								PokersTypeVo tp = Rule.getType(list.toArray(new Poker[0]));
								if (tp != null && tp.type == type.type)
									add2Team(realTeams, list);
								list.clear();
							}
						}
					}
					if (list.size() < 1 || pkj.points - 2 >= list.get(list.size() - 1).points) {
						break;
					}
				}
			}
		}
	}
	
	/**
	 * ���������ͬ�ļ���
	 * @param realTeams
	 */
	private static void clearSame(ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < realTeams.size(); i++) {
			ArrayList<Poker> listi = realTeams.get(i);
			for (int j = i + 1; j < realTeams.size(); j++) {
				ArrayList<Poker> listj = realTeams.get(j);
				
				if (listi.size() == listj.size()) {
					boolean flag = true;
					PokersTypeVo tp1 = Rule.getType(listi.toArray(new Poker[0]));
					PokersTypeVo tp2 = Rule.getType(listj.toArray(new Poker[0]));
					
					flag = (Rule.compare(tp1, tp2) == 0);
					
					/* ���������ͬ�����Ƴ� */
					if (flag) {
						realTeams.remove(j);
						j --;
					}
				}
			}
		}
	}
	
	/*private static void clearUnsaze(ArrayList<ArrayList<Poker>> realTeams) {
		for (Iterator<E>)
	}*/

	/**
	 * ��ը
	 * 
	 * @param list
	 * @param realTeams
	 */
	@SuppressWarnings("unchecked")
	private static void findJokerBomb(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		list.clear();
		if (bigCount + smlCount >= 3) {
			if (bigCount >= 2 && smlCount >= 2) {
				list.add(sourcePks.get(smlIdx[0]));
				list.add(sourcePks.get(smlIdx[1]));
				list.add(sourcePks.get(bigIdx[0]));
				list.add(sourcePks.get(bigIdx[1]));
				mySort(list);
				if (!realTeams.contains(list))
					realTeams.add((ArrayList<Poker>) list.clone());
				list.clear();
				list.add(sourcePks.get(smlIdx[0]));
				list.add(sourcePks.get(smlIdx[1]));
				list.add(sourcePks.get(bigIdx[1]));
				mySort(list);
				if (!realTeams.contains(list))
					realTeams.add((ArrayList<Poker>) list.clone());
				list.clear();
				list.add(sourcePks.get(smlIdx[0]));
				list.add(sourcePks.get(bigIdx[0]));
				list.add(sourcePks.get(bigIdx[1]));
				mySort(list);
				if (!realTeams.contains(list))
					realTeams.add((ArrayList<Poker>) list.clone());
				list.clear();
			} else {
				for (int i = 0; i < bigIdx.length; i++) {
					if (bigIdx[i] != 0)
						list.add(sourcePks.get(bigIdx[i]));
				}
				for (int i = 0; i < smlIdx.length; i++) {
					if (smlIdx[i] != 0)
						list.add(sourcePks.get(smlIdx[i]));
				}
				mySort(list);
				if (!realTeams.contains(list))
					realTeams.add((ArrayList<Poker>) list.clone());
				list.clear();
			}
		}
	}

	/**
	 * ˳ը
	 * 
	 * @param list
	 * @param realTeams
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<ArrayList<Poker>> findShunBomb(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		ArrayList<ArrayList<Poker>> rtnList = new ArrayList<ArrayList<Poker>>();
		int tBigCount = bigCount;
		list.clear();
		int step = 0;
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			if (pki.points == Poker.BIG_JOKER || pki.points == Poker.SML_JOKER)
				continue;
			step = 1;
			for (int j = i + 1; j <= sourcePks.size(); j++) {
				Poker pkj = null;
				if (j < sourcePks.size())
					pkj = sourcePks.get(j);
				if (pkj != null && pki.points == pkj.points) {
					step++;
				} else {
					/* ����������������� */
					if (step >= 4) {
						for (int l = i; l < i + step; l++) {
							add2List(list, sourcePks.get(l));
						}
						mySort(list);
						PokersTypeVo tp = Rule.getType(list.toArray(new Poker[0]));
						if (tp != null && tp.type >= PokersTypeVo.BOMB_4 && !realTeams.contains(list)) {
							realTeams.add((ArrayList<Poker>) list.clone());
							rtnList.add((ArrayList<Poker>) list.clone());
						}
						list.clear();
					}
					/* һ������ */
					if (tBigCount >= 1 && step >= 3) {
						for (int l = i; l < i + step; l++) {
							add2List(list, sourcePks.get(l));
						}
						add2List(list, sourcePks.get(bigIdx[0]));
						mySort(list);
						PokersTypeVo tp = Rule.getType(list.toArray(new Poker[0]));
						if (tp != null && tp.type >= PokersTypeVo.BOMB_4 && !realTeams.contains(list)) {
							realTeams.add((ArrayList<Poker>) list.clone());
							rtnList.add((ArrayList<Poker>) list.clone());
						}
						list.clear();
					}
					/* �������� */
					if (tBigCount >= 2 && step >= 2) {
						for (int l = i; l < i + step; l++) {
							add2List(list, sourcePks.get(l));
						}
						add2List(list, sourcePks.get(bigIdx[0]));
						add2List(list, sourcePks.get(bigIdx[1]));
						mySort(list);
						PokersTypeVo tp = Rule.getType(list.toArray(new Poker[0]));
						if (tp != null && tp.type >= PokersTypeVo.BOMB_4 && !realTeams.contains(list)) {
							realTeams.add((ArrayList<Poker>) list.clone());
							rtnList.add((ArrayList<Poker>) list.clone());
						}
						list.clear();
					}
				}
			}
		}
		return rtnList;
	}

	/**
	 * ��˳ը
	 * 
	 * @param list
	 * @param realTeams
	 */
	@SuppressWarnings("unchecked")
	private static void findLianShunBomb(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams, ArrayList<ArrayList<Poker>> bombs) {
		list.clear();
		for (ListIterator<ArrayList<Poker>> iter = bombs.listIterator(); iter.hasNext();) {
			ArrayList<Poker> pksi = iter.next();
			if (pksi.get(0).points != Poker.POKER_2 && pksi.get(0).points != Poker.SML_JOKER) {
				int size = pksi.size();
				int jokerCount = getJokerCount(pksi);
				list.clear();
				list.addAll(pksi);

				for (int j = iter.nextIndex(); j < bombs.size(); j++) {
					ArrayList<Poker> pksj = bombs.get(j);
					if (pksj.get(0).points != Poker.POKER_2 && 
							pksj.get(0).points != Poker.SML_JOKER) {
						if (pksj.size() == size && pksj.get(0).points == list.get(list.size() - size).points + 1 && 
								jokerCount + getJokerCount(pksj) <= bigCount) {
							list.addAll(pksj);
							
							PokersTypeVo tp = Rule.getType(list.toArray(new Poker[0]));
							if (tp != null && tp.type >= PokersTypeVo.BOMB_4)
								if (!realTeams.contains(list)) {
									realTeams.add((ArrayList<Poker>) list.clone());
									int count = pksj.size();
									while (count -- > 0) {
										list.remove(list.size() - 1);
									}
								}
						}
					}
				}
			}
		}
	}

	/**
	 * ����ڼ������ж��ٸ�����
	 * 
	 * @param list
	 * @return
	 */
	private static int getJokerCount(ArrayList<Poker> list) {
		int rtnVal = 0;
		for (Iterator<Poker> iter = list.iterator(); iter.hasNext();) {
			if (iter.next().points == Poker.BIG_JOKER)
				rtnVal++;
		}
		return rtnVal;
	}

	/**
	 * ��鲢���
	 * 
	 * @param list
	 * @param pk
	 */
	private static void add2List(ArrayList<Poker> list, Poker pk) {
		if (!list.contains(pk))
			list.add(pk);
	}

	/**
	 * ��鲢���
	 * 
	 * @param list
	 * @param pk
	 */
	@SuppressWarnings("unchecked")
	private static void add2Team(ArrayList<ArrayList<Poker>> realTeams, ArrayList<Poker> list) {
		if (!realTeams.contains(list))
			realTeams.add((ArrayList<Poker>) list.clone());
	}

	/**
	 * ѡ�����򣨸����ܰ棩
	 * 
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
	 * �Խ������
	 * 
	 * @param realTeams
	 */
	private static void sortResult(ArrayList<ArrayList<Poker>> realTeams) {
		int minner = -1;
		ArrayList<Poker> min = null;
		for (int i = 0; i < realTeams.size(); i++) {
			ArrayList<Poker> list = realTeams.get(i);
			min = list;
			minner = i;
			for (int j = i + 1; j < realTeams.size(); j++) {
				ArrayList<Poker> lt = realTeams.get(j);
				PokersTypeVo type1 = Rule.getType(min.toArray(new Poker[0]));
				PokersTypeVo type2 = Rule.getType(lt.toArray(new Poker[0]));
				if (Rule.compare(type1, type2) > 0) {
					min = lt;
					minner = j;
				}
			}

			if (minner != i) {
				realTeams.set(minner, list);
				realTeams.set(i, min);
			}
		}
	}
	
	/**
	 * ��ӵ�һ������
	 * @param list
	 * @param realTeams
	 */
	private static void addJoker(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams, int jokidx) {
		if (bigCount > jokidx) {
			add2List(list, sourcePks.get(bigIdx[jokidx]));
		}
	}

	public static void main(String[] args) {
		ArrayList<Poker> sourcePks = new ArrayList<Poker>();
		ArrayList<Poker> targetPks = new ArrayList<Poker>();

		//÷3,��4,��4,��5,��6,��6,÷7,��8,��9,��9,��10,��10,��10,÷J,��Q,��Q,��Q,��K,��K,��K,÷A,��A,÷2,��2,С��,����,����
		targetPks.add(new Poker(Poker.POKER_3, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_3, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_3, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_3, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_4, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_4, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_4, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_4, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_5, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_5, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_5, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_5, Poker.FANG));

		sourcePks.add(new Poker(Poker.POKER_6, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_6, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_6, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_6, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_7, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_7, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_7, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_7, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_8, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_8, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_8, Poker.FANG));
		sourcePks.add(new Poker(Poker.POKER_8, Poker.FANG));

		System.out.println(sourcePks);
		System.out.println(targetPks);

		System.out.println(getHelpPks(sourcePks, targetPks).toString().replaceAll("\\], \\[", "\n"));
	}

}

package testPoker;

import java.util.ArrayList;
import java.util.Iterator;

public class TestFind2 {

	private static ArrayList<Poker> sourcePks = null;
	private static ArrayList<Poker> targetPks = null;
	private static PokersTypeVo type = null;

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
		TestFind2.sourcePks = sourcePks;
		TestFind2.targetPks = targetPks;
		ArrayList<Poker> list = new ArrayList<Poker>();
		ArrayList<ArrayList<Poker>> realTeams = new ArrayList<ArrayList<Poker>>();

		type = Rule.getType(targetPks.toArray(new Poker[0]));
		
		/* 根据牌的张数分别取 */
		if (type != null) {
			switch (type.len) {
			case 1:
				findBy1(list, realTeams);
				break;
			case 2:
				findBy2(list, realTeams);
				break;
			case 3:
				findBy3(list, realTeams);
				break;
			case 4:
				findBy4(list, realTeams);
				break;
			case 5:
				findBy5(list, realTeams);
				break;
			case 6:
				findBy6(list, realTeams);
				break;
			case 7:
				findBy7(list, realTeams);
				break;
			case 8:
				findBy8(list, realTeams);
				break;
			case 9:
				findBy9(list, realTeams);
				break;
			case 10:
				findBy10(list, realTeams);
				break;
			case 11:
				findBy11(list, realTeams);
				break;
			case 12:
				findBy12(list, realTeams);
				break;
			case 14:
				findBy14(list, realTeams);
				break;
			case 15:
				findBy15(list, realTeams);
				break;
			case 16:
				findBy16(list, realTeams);
				break;
			case 18:
				findBy18(list, realTeams);
				break;
			case 20:
				findBy20(list, realTeams);
				break;
			case 21:
				findBy21(list, realTeams);
				break;
			case 22:
				findBy22(list, realTeams);
				break;
			case 24:
				findBy24(list, realTeams);
				break;
			case 25:
				findBy25(list, realTeams);
				break;
			case 27:
				findBy27(list, realTeams);
				break;

			default:
				break;
			}
		}
		System.out.println(realTeams.size());
		return realTeams;
	}

	/**
	 * 找一张
	 * @param list
	 * @param realTeams
	 */
	@SuppressWarnings("unchecked")
	private static void findBy1(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (Iterator<Poker> iter = sourcePks.iterator(); iter.hasNext(); ) {
			Poker pk = iter.next();
			if (pk.points > targetPks.get(0).points) {
				list.add(pk);
				if (!realTeams.contains(list))
					realTeams.add((ArrayList<Poker>) list.clone());
				list.clear();
			}
		}
	}

	/**
	 * 找对子
	 * @param list
	 * @param realTeams
	 */
	private static void findBy2(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			if (pki.points > targetPks.get(0).points) {
				list.add(pki);
				for (int j = i + 1; j < sourcePks.size(); j++) {
					list.add(sourcePks.get(j));
					check(list, realTeams);
				}
				list.clear();
			}
		}
	}

	/**
	 * 找三张
	 * @param list
	 * @param realTeams
	 */
	private static void findBy3(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			if (pki.points > targetPks.get(0).points) {
				add2List(list, pki);
				for (int j = i + 1; j < sourcePks.size(); j++) {
					add2List(list, sourcePks.get(j));
					for (int l = j + 1; l < sourcePks.size(); l++) {
						add2List(list, sourcePks.get(l));
						check(list, realTeams);
					}
					while (list.size() > 1)
						list.remove(list.size() - 1);
				}
				list.clear();
			}
		}
	}

	/**
	 * 找四张
	 * @param list
	 * @param realTeams
	 */
	private static void findBy4(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			if (pki.points > targetPks.get(0).points) {
				add2List(list, pki);
				for (int j = i + 1; j < sourcePks.size(); j++) {
					add2List(list, sourcePks.get(j));
					for (int l = j + 1; l < sourcePks.size(); l++) {
						add2List(list, sourcePks.get(l));
						for (int m = l + 1; m < sourcePks.size(); m++) {
							add2List(list, sourcePks.get(m));
							check(list, realTeams);
						}
						while (list.size() > 2)
							list.remove(list.size() - 1);
					}
					while (list.size() > 1)
						list.remove(list.size() - 1);
				}
				list.clear();
			}
		}
	}

	/**
	 * 找五张
	 * @param list
	 * @param realTeams
	 */
	private static void findBy5(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			if (pki.points > targetPks.get(0).points) {
				add2List(list, pki);
				for (int j = i + 1; j < sourcePks.size(); j++) {
					add2List(list, sourcePks.get(j));
					for (int l = j + 1; l < sourcePks.size(); l++) {
						add2List(list, sourcePks.get(l));
						for (int m = l + 1; m < sourcePks.size(); m++) {
							add2List(list, sourcePks.get(m));
							for (int k = m + 1; k < sourcePks.size(); k++) {
								add2List(list, sourcePks.get(k));
								check(list, realTeams);
							}
							while (list.size() > 3)
								list.remove(list.size() - 1);
						}
						while (list.size() > 2)
							list.remove(list.size() - 1);
					}
					while (list.size() > 1)
						list.remove(list.size() - 1);
				}
				list.clear();
			}
		}
	}

	/**
	 * 找六张
	 * @param list
	 * @param realTeams
	 */
	private static void findBy6(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			if (pki.points > targetPks.get(0).points) {
				add2List(list, pki);
				for (int j = i + 1; j < sourcePks.size(); j++) {
					add2List(list, sourcePks.get(j));
					for (int l = j + 1; l < sourcePks.size(); l++) {
						add2List(list, sourcePks.get(l));
						for (int m = l + 1; m < sourcePks.size(); m++) {
							add2List(list, sourcePks.get(m));
							for (int k = m + 1; k < sourcePks.size(); k++) {
								add2List(list, sourcePks.get(k));
								for (int a = k + 1; a < sourcePks.size(); a++) {
									add2List(list, sourcePks.get(a));
									check(list, realTeams);
								}
								while (list.size() > 4)
									list.remove(list.size() - 1);
							}
							while (list.size() > 3)
								list.remove(list.size() - 1);
						}
						while (list.size() > 2)
							list.remove(list.size() - 1);
					}
					while (list.size() > 1)
						list.remove(list.size() - 1);
				}
				list.clear();
			}
		}
	}

	/**
	 * 找7张
	 * @param list
	 * @param realTeams
	 */
	private static void findBy7(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			if (pki.points > targetPks.get(0).points) {
				add2List(list, pki);
				for (int j = i + 1; j < sourcePks.size(); j++) {
					add2List(list, sourcePks.get(j));
					for (int l = j + 1; l < sourcePks.size(); l++) {
						add2List(list, sourcePks.get(l));
						for (int m = l + 1; m < sourcePks.size(); m++) {
							add2List(list, sourcePks.get(m));
							for (int k = m + 1; k < sourcePks.size(); k++) {
								add2List(list, sourcePks.get(k));
								for (int a = k + 1; a < sourcePks.size(); a++) {
									add2List(list, sourcePks.get(a));
									for (int b = a + 1; b < sourcePks.size(); b++) {
										add2List(list, sourcePks.get(b));
										check(list, realTeams);
									}
									while (list.size() > 5)
										list.remove(list.size() - 1);
								}
								while (list.size() > 4)
									list.remove(list.size() - 1);
							}
							while (list.size() > 3)
								list.remove(list.size() - 1);
						}
						while (list.size() > 2)
							list.remove(list.size() - 1);
					}
					while (list.size() > 1)
						list.remove(list.size() - 1);
				}
				list.clear();
			}
		}
	}

	/**
	 * 找8张
	 * @param list
	 * @param realTeams
	 */
	private static void findBy8(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			if (pki.points > targetPks.get(0).points) {
				add2List(list, pki);
				for (int j = i + 1; j < sourcePks.size(); j++) {
					add2List(list, sourcePks.get(j));
					for (int l = j + 1; l < sourcePks.size(); l++) {
						add2List(list, sourcePks.get(l));
						for (int m = l + 1; m < sourcePks.size(); m++) {
							add2List(list, sourcePks.get(m));
							for (int k = m + 1; k < sourcePks.size(); k++) {
								add2List(list, sourcePks.get(k));
								for (int a = k + 1; a < sourcePks.size(); a++) {
									add2List(list, sourcePks.get(a));
									for (int b = a + 1; b < sourcePks.size(); b++) {
										add2List(list, sourcePks.get(b));
										for (int c = b + 1; c < sourcePks.size(); c++) {
											add2List(list, sourcePks.get(c));
											check(list, realTeams);
										}
										while (list.size() > 6)
											list.remove(list.size() - 1);
									}
									while (list.size() > 5)
										list.remove(list.size() - 1);
								}
								while (list.size() > 4)
									list.remove(list.size() - 1);
							}
							while (list.size() > 3)
								list.remove(list.size() - 1);
						}
						while (list.size() > 2)
							list.remove(list.size() - 1);
					}
					while (list.size() > 1)
						list.remove(list.size() - 1);
				}
				list.clear();
			}
		}
	}

	/**
	 * 找九张
	 * @param list
	 * @param realTeams
	 */
	private static void findBy9(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			if (pki.points > targetPks.get(0).points) {
				add2List(list, pki);
				for (int j = i + 1; j < sourcePks.size(); j++) {
					add2List(list, sourcePks.get(j));
					for (int l = j + 1; l < sourcePks.size(); l++) {
						add2List(list, sourcePks.get(l));
						for (int m = l + 1; m < sourcePks.size(); m++) {
							add2List(list, sourcePks.get(m));
							for (int k = m + 1; k < sourcePks.size(); k++) {
								add2List(list, sourcePks.get(k));
								for (int a = k + 1; a < sourcePks.size(); a++) {
									add2List(list, sourcePks.get(a));
									for (int b = a + 1; b < sourcePks.size(); b++) {
										add2List(list, sourcePks.get(b));
										for (int c = b + 1; c < sourcePks.size(); c++) {
											add2List(list, sourcePks.get(c));
											for (int d = c + 1; d < sourcePks.size(); d++) {
												add2List(list, sourcePks.get(d));
												check(list, realTeams);
											}
											while (list.size() > 7)
												list.remove(list.size() - 1);
										}
										while (list.size() > 6)
											list.remove(list.size() - 1);
									}
									while (list.size() > 5)
										list.remove(list.size() - 1);
								}
								while (list.size() > 4)
									list.remove(list.size() - 1);
							}
							while (list.size() > 3)
								list.remove(list.size() - 1);
						}
						while (list.size() > 2)
							list.remove(list.size() - 1);
					}
					while (list.size() > 1)
						list.remove(list.size() - 1);
				}
				list.clear();
			}
		}
	}

	private static void findBy10(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			if (pki.points > targetPks.get(0).points) {
				add2List(list, pki);
				for (int j = i + 1; j < sourcePks.size(); j++) {
					add2List(list, sourcePks.get(j));
					for (int l = j + 1; l < sourcePks.size(); l++) {
						add2List(list, sourcePks.get(l));
						for (int m = l + 1; m < sourcePks.size(); m++) {
							add2List(list, sourcePks.get(m));
							for (int k = m + 1; k < sourcePks.size(); k++) {
								add2List(list, sourcePks.get(k));
								for (int a = k + 1; a < sourcePks.size(); a++) {
									add2List(list, sourcePks.get(a));
									for (int b = a + 1; b < sourcePks.size(); b++) {
										add2List(list, sourcePks.get(b));
										for (int c = b + 1; c < sourcePks.size(); c++) {
											add2List(list, sourcePks.get(c));
											for (int d = c + 1; d < sourcePks.size(); d++) {
												add2List(list, sourcePks.get(d));
												for (int e = d + 1; e < sourcePks.size(); e++) {
													add2List(list, sourcePks.get(e));
													check(list, realTeams);
												}
												while (list.size() > 8)
													list.remove(list.size() - 1);
											}
											while (list.size() > 7)
												list.remove(list.size() - 1);
										}
										while (list.size() > 6)
											list.remove(list.size() - 1);
									}
									while (list.size() > 5)
										list.remove(list.size() - 1);
								}
								while (list.size() > 4)
									list.remove(list.size() - 1);
							}
							while (list.size() > 3)
								list.remove(list.size() - 1);
						}
						while (list.size() > 2)
							list.remove(list.size() - 1);
					}
					while (list.size() > 1)
						list.remove(list.size() - 1);
				}
				list.clear();
			}
		}
	}

	/**
	 * 找11张
	 * @param list
	 * @param realTeams
	 */
	private static void findBy11(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			if (pki.points > targetPks.get(0).points) {
				add2List(list, pki);
				for (int j = i + 1; j < sourcePks.size(); j++) {
					add2List(list, sourcePks.get(j));
					for (int l = j + 1; l < sourcePks.size(); l++) {
						add2List(list, sourcePks.get(l));
						for (int m = l + 1; m < sourcePks.size(); m++) {
							add2List(list, sourcePks.get(m));
							for (int k = m + 1; k < sourcePks.size(); k++) {
								add2List(list, sourcePks.get(k));
								for (int a = k + 1; a < sourcePks.size(); a++) {
									add2List(list, sourcePks.get(a));
									for (int b = a + 1; b < sourcePks.size(); b++) {
										add2List(list, sourcePks.get(b));
										for (int c = b + 1; c < sourcePks.size(); c++) {
											add2List(list, sourcePks.get(c));
											for (int d = c + 1; d < sourcePks.size(); d++) {
												add2List(list, sourcePks.get(d));
												for (int e = d + 1; e < sourcePks.size(); e++) {
													add2List(list, sourcePks.get(e));
													for (int f = e + 1; f < sourcePks.size(); f++) {
														add2List(list, sourcePks.get(f));
														check(list, realTeams);
													}
													while (list.size() > 9)
														list.remove(list.size() - 1);
												}
												while (list.size() > 8)
													list.remove(list.size() - 1);
											}
											while (list.size() > 7)
												list.remove(list.size() - 1);
										}
										while (list.size() > 6)
											list.remove(list.size() - 1);
									}
									while (list.size() > 5)
										list.remove(list.size() - 1);
								}
								while (list.size() > 4)
									list.remove(list.size() - 1);
							}
							while (list.size() > 3)
								list.remove(list.size() - 1);
						}
						while (list.size() > 2)
							list.remove(list.size() - 1);
					}
					while (list.size() > 1)
						list.remove(list.size() - 1);
				}
				list.clear();
			}
		}
	}

	private static void findBy12(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		for (int i = 0; i < sourcePks.size(); i++) {
			Poker pki = sourcePks.get(i);
			if (pki.points > targetPks.get(0).points) {
				add2List(list, pki);
				for (int j = i + 1; j < sourcePks.size(); j++) {
					add2List(list, sourcePks.get(j));
					for (int l = j + 1; l < sourcePks.size(); l++) {
						add2List(list, sourcePks.get(l));
						for (int m = l + 1; m < sourcePks.size(); m++) {
							add2List(list, sourcePks.get(m));
							for (int k = m + 1; k < sourcePks.size(); k++) {
								add2List(list, sourcePks.get(k));
								for (int a = k + 1; a < sourcePks.size(); a++) {
									add2List(list, sourcePks.get(a));
									for (int b = a + 1; b < sourcePks.size(); b++) {
										add2List(list, sourcePks.get(b));
										for (int c = b + 1; c < sourcePks.size(); c++) {
											add2List(list, sourcePks.get(c));
											for (int d = c + 1; d < sourcePks.size(); d++) {
												add2List(list, sourcePks.get(d));
												for (int e = d + 1; e < sourcePks.size(); e++) {
													add2List(list, sourcePks.get(e));
													for (int f = e + 1; f < sourcePks.size(); f++) {
														add2List(list, sourcePks.get(f));
														for (int g = f + 1; g < sourcePks.size(); g++) {
															add2List(list, sourcePks.get(g));
															check(list, realTeams);
														}
														while (list.size() > 10)
															list.remove(list.size() - 1);
													}
													while (list.size() > 9)
														list.remove(list.size() - 1);
												}
												while (list.size() > 8)
													list.remove(list.size() - 1);
											}
											while (list.size() > 7)
												list.remove(list.size() - 1);
										}
										while (list.size() > 6)
											list.remove(list.size() - 1);
									}
									while (list.size() > 5)
										list.remove(list.size() - 1);
								}
								while (list.size() > 4)
									list.remove(list.size() - 1);
							}
							while (list.size() > 3)
								list.remove(list.size() - 1);
						}
						while (list.size() > 2)
							list.remove(list.size() - 1);
					}
					while (list.size() > 1)
						list.remove(list.size() - 1);
				}
				list.clear();
			}
		}
	}

	private static void findBy14(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		
	}

	private static void findBy15(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		
	}

	private static void findBy16(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		
	}

	private static void findBy18(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		
	}

	private static void findBy20(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		
	}

	private static void findBy21(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		
	}

	private static void findBy22(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		
	}

	private static void findBy24(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		
	}

	private static void findBy25(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		
	}

	private static void findBy27(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		
	}
	
	/**
	 * 检查并添加
	 * @param list
	 * @param pk
	 */
	private static void add2List(ArrayList<Poker> list, Poker pk) {
		if (!list.contains(pk))
			list.add(pk);
	}
	
	/**
	 * 检查添加
	 * @param list
	 * @param realTeams
	 */
	@SuppressWarnings("unchecked")
	private static void check(ArrayList<Poker> list, ArrayList<ArrayList<Poker>> realTeams) {
		PokersTypeVo tp = Rule.getType(list.toArray(new Poker[0]));
		if (Rule.compare(tp, type) > 0) {
			mySort(list);
			if (!realTeams.contains(list))
				realTeams.add((ArrayList<Poker>) list.clone());
			list.clear();
		} else {
			list.remove(list.size() - 1);
		}
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
		targetPks.add(new Poker(Poker.POKER_4, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_5, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_6, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_7, Poker.FANG));
		/*targetPks.add(new Poker(Poker.POKER_5, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_6, Poker.FANG));
		targetPks.add(new Poker(Poker.POKER_6, Poker.FANG));*/
		/*targetPks.add(new Poker(Poker.POKER_5, Poker.FANG));*/

		System.out.println(getHelpPks(sourcePks, targetPks));
	}
}

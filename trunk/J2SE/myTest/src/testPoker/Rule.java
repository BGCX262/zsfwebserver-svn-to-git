package testPoker;

import java.util.Arrays;

/**
 * 扑克牌的规则 所有直接获得牌型的方法(private)均不验证张数，所以不要单独调用 需要获得牌型，请使用getType()
 * 
 * @author zsf
 */
public class Rule {

	/**
	 * 获得指定牌的牌型 vo
	 * 
	 * @param pks
	 * @return
	 */
	public static PokersTypeVo getType(Poker[] pks) {
		if (pks == null || pks.length == 0) {
			return null;
		}

		/* 牌的长度 */
		int len = pks.length;

		/* 最终的牌型 */
		PokersTypeVo vo = null;

		/* 先大到小排序 */
		sortA(pks);

		/* 根据长度来验证 */
		switch (len) {
		case 13:
			break;/* 13张牌够不成牌型 */
		case 17:
			break;/* 17张牌够不成牌型 */
		case 19:
			break;/* 19张牌够不成牌型 */
		case 23:
			break;/* 23张牌够不成牌型 */
		case 26:
			break;/* 26张牌够不成牌型 */

		case 1:
			vo = getDan(pks);
			break;

		case 2:
			vo = getDuizi(pks);
			break;

		case 3:
			/* 3张牌可能是炸弹或者三张 */
			vo = getJokerBomb(pks);
			if (vo == null) {
				vo = getSan(pks);
			}
			break;

		case 4:
			/* 4张牌 可能是王炸，4相炸 */
			vo = getJokerBomb(pks);
			if (vo == null) {
				vo = getXiangBomb(pks);
			}
			break;

		case 5:
			/* 5张牌 可能是5相炸,顺子 */
			vo = getXiangBomb(pks);
			if (vo == null) {
				vo = getShun(pks);
			}
			break;

		case 6:
			/* 6张牌 可能是6相炸,顺子,连对 */
			vo = getXiangBomb(pks);
			if (vo == null) {
				vo = getShun(pks);
			}
			if (vo == null) {
				vo = getLianDui(pks);
			}
			break;

		case 7:
			/* 7张牌 可能是7相炸,顺子 */
			vo = getXiangBomb(pks);
			if (vo == null) {
				vo = getShun(pks);
			}
			break;

		case 8:
			/* 8张牌 可能是8相炸,顺子,连对 */
			vo = getXiangBomb(pks);
			if (vo == null) {
				vo = getShun(pks);
			}
			if (vo == null) {
				vo = getLianDui(pks);
			}
			break;

		case 9:
			/* 9张牌 可能是顺子,连三张 */
			vo = getShun(pks);
			if (vo == null) {
				vo = getLianSan(pks);
			}
			break;

		case 10:
			/* 10张牌 可能是顺子,连对 */
			vo = getShun(pks);
			if (vo == null) {
				vo = getLianDui(pks);
			}
			break;

		case 11:
			/* 11张牌 可能是顺子 */
			vo = getShun(pks);
			break;

		case 12:
			/* 12张牌 可能是4相3连环炸弹，顺子，连对，连三张 */
			vo = getNLianNXiangBomb(pks, 4, 3);
			if (vo == null) {
				vo = getShun(pks);
			}
			if (vo == null) {
				vo = getLianDui(pks);
			}
			if (vo == null) {
				vo = getLianSan(pks);
			}
			break;

		case 14:
			/* 14张牌 可能是连对 */
			vo = getLianDui(pks);
			break;

		case 15:
			/* 15张牌 可能是5相3连环炸弹，连三张 */
			vo = getNLianNXiangBomb(pks, 5, 3);
			if (vo == null) {
				vo = getLianSan(pks);
			}
			break;

		case 16:
			/* 16张牌 可能是4相4连环炸弹，连对 */
			vo = getNLianNXiangBomb(pks, 4, 4);
			if (vo == null) {
				vo = getLianDui(pks);
			}
			break;

		case 18:
			/* 18张牌 可能是6相3连环炸弹，连对,连三张 */
			vo = getNLianNXiangBomb(pks, 6, 3);
			if (vo == null) {
				vo = getLianDui(pks);
			}
			if (vo == null) {
				vo = getLianSan(pks);
			}
			break;

		case 20:
			/* 20张牌 可能是5相4连环炸弹，4相5连环炸弹，连对 */
			vo = getNLianNXiangBomb(pks, 5, 4);
			if (vo == null) {
				vo = getNLianNXiangBomb(pks, 4, 5);
			}
			if (vo == null) {
				vo = getLianDui(pks);
			}
			break;

		case 21:
			/* 21张牌 可能是7相3连环炸弹,连三张 */
			vo = getNLianNXiangBomb(pks, 7, 3);
			if (vo == null) {
				vo = getLianSan(pks);
			}
			break;

		case 22:
			/* 22张牌 可能是连对 */
			vo = getLianDui(pks);
			break;

		case 24:
			/* 24张牌 可能是6相4连环炸弹，4相6连环炸弹，3相8连环炸弹，8相3连环炸弹，连对，连三张 */
			vo = getNLianNXiangBomb(pks, 6, 4);
			if (vo == null) {
				vo = getNLianNXiangBomb(pks, 4, 6);
			}
			if (vo == null) {
				vo = getNLianNXiangBomb(pks, 3, 8);
			}
			if (vo == null) {
				vo = getNLianNXiangBomb(pks, 8, 3);
			}
			if (vo == null) {
				vo = getLianDui(pks);
			}
			if (vo == null) {
				vo = getLianSan(pks);
			}
			break;

		case 25:
			/* 25张牌 可能是5相5连环炸弹 */
			vo = getNLianNXiangBomb(pks, 5, 5);
			break;

		case 27:
			/* 27张牌 可能是连3张 */
			vo = getLianSan(pks);
			break;
		}

		return vo;
	}

	/**
	 * 判断2副牌能否进行比较
	 * 
	 * @param aType
	 * @param bType
	 * @return
	 */
	public static boolean canCompare(PokersTypeVo aType, PokersTypeVo bType) {
		if (aType == null || bType == null) {
			return false;
		}

		/* 有炸弹一定能比较 */
		if (aType.type > 10 || bType.type > 10) {
			return true;
		}

		/* 牌型，张数均一样，可以比较 */
		if (aType.type == bType.type && aType.len == bType.len) {
			return true;
		}

		return false;
	}

	/**
	 * 比较2副牌的大小，
	 * 
	 * @param pksA
	 * @param pksB
	 * @return A > B 1 A = B 0 A < B -1 不能比较 -2
	 */

	public static int compare(PokersTypeVo aType, PokersTypeVo bType) {
		/* 牌型，张数不等，也没有炸弹，则不能比较 */
		if (!canCompare(aType, bType)) {
			return -2;
		}

		/* A是炸弹 */
		if (aType.type > 10) {
			if (bType.type > 10) {
				/* 最特殊的2个6星炸 ,3王是6星中最小的 */
				if (aType.type == PokersTypeVo.BOMB_6 && bType.type == PokersTypeVo.BOMB_6) {
					if (aType.len == 3) {
						return -1;
					}

					if (bType.len == 3) {
						return 1;
					}
				}

				/* B也是炸弹,先比较星级，然后比较张数，最后比较牌点 */
				if (aType.type > bType.type) {
					return 1;
				} else if (aType.type < bType.type) {
					return -1;
				} else {
					/* 2个炸弹比较张数，张数一样比较牌点 */
					if (aType.len < bType.len) {
						return 1;
					} else if (aType.len > bType.len) {
						return -1;
					} else {
						if (aType.maxPoint > bType.maxPoint) {
							return 1;
						} else if (aType.maxPoint < bType.maxPoint) {
							return -1;
						} else {
							/* 炸弹这里可能会有BUG */
							return 0;
						}
					}
				}
			} else {
				/* B不是炸弹 */
				return 1;
			}
		} else {
			/* A不是炸弹 ,B是炸弹 */
			if (bType.type > 10) {
				return -1;
			} else {
				/* 均不是炸弹 */
				if (aType.maxPoint > bType.maxPoint) {
					return 1;
				} else if (aType.maxPoint < bType.maxPoint) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * 从大到小排序
	 */
	public static void sortA(Poker[] pks) {
		Arrays.sort(pks);
	}

	/**
	 * 获得单张
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getDan(Poker[] pks) {
		PokersTypeVo vo = new PokersTypeVo();
		vo.len = 1;
		vo.maxPoint = pks[0].points;
		vo.type = PokersTypeVo.TYPE_DAN;

		return vo;
	}

	/**
	 * 是否是对子,考虑了赖子
	 * 
	 * @param pks
	 */
	private static PokersTypeVo getDuizi(Poker[] pks) {
		if (isGod(pks[0]) || (pks[0].points == pks[1].points)) {
			PokersTypeVo vo = new PokersTypeVo();
			vo.len = 2;
			vo.maxPoint = pks[1].points;
			vo.type = PokersTypeVo.TYPE_DUI;

			return vo;
		}

		return null;
	}

	/**
	 * 是否是3张 ,考虑了赖子
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getSan(Poker[] pks) {
		PokersTypeVo vo = new PokersTypeVo();
		vo.type = PokersTypeVo.TYPE_SAN;
		vo.len = pks.length;

		/* 第一张是赖子 */
		if (isGod(pks[0])) {
			/* 第2张是否也是赖子 */
			if (isGod(pks[1])) {
				/* 是赖子 */
				vo.maxPoint = pks[2].points;
				return vo;
			} else {
				/* 不是赖子，比较后2张 */
				if (pks[1].points == pks[2].points) {
					vo.maxPoint = pks[2].points;
					return vo;
				} else {
					return null;
				}
			}
		} else {
			/* 第一张不是赖子，比较3张 */
			if ((pks[0].points == pks[1].points) && (pks[0].points == pks[2].points)) {
				vo.maxPoint = pks[2].points;
				return vo;
			} else {
				return null;
			}
		}

	}

	/**
	 * 是否是顺子
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getShun(Poker[] pks) {
		/* 不能有小王或者2 */
		if (has2orJoker(pks)) {
			return null;
		}

		/* 第一张是不是赖子 */
		if (isGod(pks[0])) {
			/* 第一张是赖子 ,继续判断第二张是 不是赖子 */
			if (isGod(pks[1])) {
				/* 第二张也是赖子 */
				return getShunWith2God(pks);
			} else {
				/* 第二张不是赖子 */
				return getShunWith1God(pks);
			}
		} else {
			/* 第一张不是赖子 */
			/* 间隔为1 比较 */
			return getShunWith0God(pks);
		}
	}

	/**
	 * 判断是否是连对，考虑了赖子
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getLianDui(Poker[] pks) {
		/* 小王和2不能构成连对 */
		if (has2orJoker(pks)) {
			return null;
		}

		/* 是否有1张赖子 */
		if (isGod(pks[0])) {
			/* 是否有2张赖子 */
			if (isGod(pks[1])) {
				return getLianDuiWith2God(pks);
			} else {
				return getLianDuiWith1God(pks);
			}
		} else {
			return getLianDuiWithNoGod(pks);
		}
	}

	/**
	 * 判断是不是连三张，考虑了赖子
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getLianSan(Poker[] pks) {
		/* 小王和2不能构成三张 */
		if (has2orJoker(pks)) {
			return null;
		}

		/* 是否有1张赖子 */
		if (isGod(pks[0])) {
			/* 是否有2张赖子 */
			if (isGod(pks[1])) {
				return getLianSanWithGod(pks, 2);
			} else {
				return getLianSanWithGod(pks, 1);
			}
		} else {
			return getLianSanWithNoGod(pks);
		}
	}

	/**
	 * 判断是否是由王组成的炸弹
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getJokerBomb(Poker[] pks) {
		int jokerCount = 0;
		for (Poker pk : pks) {
			if (pk.points < Poker.SML_JOKER) {
				return null;
			}

			jokerCount++;
		}

		/* 3张以下不是王炸弹 */
		if (jokerCount < 3) {
			return null;
		}

		PokersTypeVo vo = new PokersTypeVo();
		/* 对于排序后的牌，无论是3王还是4王，最大的牌永远是第2张 */
		vo.maxPoint = pks[1].points;
		vo.len = pks.length;
		/* 4王是7星炸， 3王是6星炸 */
		vo.type = (jokerCount == 4 ? PokersTypeVo.BOMB_7 : PokersTypeVo.BOMB_6);

		return vo;
	}

	/**
	 * 判断是不是N相炸弹
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getXiangBomb(Poker[] pks) {
		PokersTypeVo vo = new PokersTypeVo();
		vo.len = pks.length;
		vo.maxPoint = pks[pks.length - 1].points;
		vo.type = 10 + vo.len;

		/* 是否有一个赖子 */
		if (isGod(pks[0])) {
			/* 是否有2个赖子 */
			if (isGod(pks[1])) {
				if (!isAllSame(slice(pks, 2))) {
					return null;
				}
			} else {
				if (!isAllSame(slice(pks, 1))) {
					return null;
				}
			}
		} else {
			if (!isAllSame(pks)) {
				return null;
			}
		}

		return vo;
	}

	/**
	 * 判断是不是N相N连环的炸弹
	 * 
	 * @param pks
	 * @param xiang
	 * @param lian
	 * @return
	 */
	private static PokersTypeVo getNLianNXiangBomb(Poker[] pks, int xiang, int lian) {
		/* 连炸弹的必要条件 */
		if (xiang < 4 || lian < 3 || has2orJoker(pks)) {
			return null;
		}

		PokersTypeVo vo = new PokersTypeVo();
		/* 连炸最大的牌一定是第三张 */
		vo.maxPoint = pks[2].points;
		vo.len = pks.length;
		vo.type = 10 + xiang + lian;

		/* 第一张是否是赖子 */
		if (isGod(pks[0])) {
			/* 第二张是否是赖子 */
			if (isGod(pks[1])) {
				if (isNLianN(xiang, 2, slice(pks, 2), lian)) {
					return vo;
				}
			} else {
				if (isNLianN(xiang, 1, slice(pks, 1), lian)) {
					return vo;
				}
			}
		} else {
			if (isNLianN(xiang, 0, pks, lian)) {
				return vo;
			}
		}

		return null;
	}

	/**
	 * 不带赖子的牌，是否是顺子
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getShunWith0God(Poker[] pks) {
		int fisrtPk = pks[0].points;
		for (int i = 1; i < pks.length; i++) {
			int pt = pks[i].points;
			if (fisrtPk != pt + i) {
				return null;
			}
		}

		PokersTypeVo vo = new PokersTypeVo();
		vo.type = PokersTypeVo.TYPE_SHUN;
		vo.len = pks.length;
		vo.maxPoint = pks[0].points;
		return vo;
	}

	/**
	 * 带一个赖子的牌，是否是顺子
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getShunWith1God(Poker[] pks) {
		Poker[] pkss = slice(pks, 1);
		PokersTypeVo vo = new PokersTypeVo();
		vo.type = PokersTypeVo.TYPE_SHUN;
		vo.len = pks.length;

		/* 除了赖子以外的是否是顺子 */
		if (getShunWith0God(pkss) != null) {
			if (pkss[0].points == Poker.POKER_A) {
				vo.maxPoint = Poker.POKER_A;
			} else {
				vo.maxPoint = pkss[0].points + 1;
			}
			return vo;
		} else {
			/* 最前一张和最后一张是否相隔顺子需要的点数 */
			if (pkss[0].points == (pkss[pkss.length - 1].points + pkss.length)) {
				/* 前后控制好，中间没有相同的牌，那就是顺子 */
				if (hasSame(pkss)) {
					return null;
				} else {
					vo.maxPoint = pkss[0].points;
					return vo;
				}
			} else {
				return null;
			}
		}
	}

	/**
	 * 带二个赖子的牌，是否是顺子
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getShunWith2God(Poker[] pks) {
		Poker[] pkss = slice(pks, 2);
		PokersTypeVo vo = new PokersTypeVo();
		vo.type = PokersTypeVo.TYPE_SHUN;
		vo.len = pks.length;

		/* 除了赖子以外的是否是顺子 */
		if (getShunWith0God(pkss) != null) {
			if (pkss[0].points == Poker.POKER_A || pkss[0].points == Poker.POKER_K) {
				vo.maxPoint = Poker.POKER_A;
			} else {
				vo.maxPoint = pkss[0].points + 2;
			}
			return vo;
		} else {
			/* 最前一张和最后一张是否相隔顺子需要的点数 */
			if (pkss[0].points == (pkss[pkss.length - 1].points + pkss.length + 1)) {
				/* 前后控制好，中间没有相同的牌，那就是顺子 */
				if (hasSame(pkss)) {
					return null;
				} else {
					vo.maxPoint = pkss[0].points;
					return vo;
				}
			} else if (pkss[0].points == (pkss[pkss.length - 1].points + pkss.length)) {

				/* 前后控制好，中间没有相同的牌，那就是顺子 */
				if (hasSame(pkss)) {
					return null;
				} else {
					if (pkss[0].points == Poker.POKER_A || pkss[0].points == Poker.POKER_K) {
						vo.maxPoint = Poker.POKER_A;
					} else {
						vo.maxPoint = pkss[0].points + 1;
					}
					return vo;
				}

			} else {
				return null;
			}
		}
	}

	/**
	 * 为getShunWithNGod服务，判断排好序(大到小)的牌里面有没有相同的
	 * 
	 * @param pks
	 * @return
	 */
	private static boolean hasSame(Poker[] pks) {
		int prevPk = pks[0].points;
		for (int i = 1; i < pks.length; i++) {
			/* 对于排好序，且没有相同牌的一组牌，后面的一定要比前一个小 */
			if (pks[i].points >= prevPk) {
				return true;
			}
			prevPk = pks[i].points;
		}

		return false;
	}

	/**
	 * 有一个赖子的连对
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getLianDuiWith1God(Poker[] pks) {
		Poker[] pkss = slice(pks, 1);

		PokersTypeVo vo = new PokersTypeVo();
		vo.len = pks.length;
		vo.type = PokersTypeVo.TYPE_LIANDUI;
		vo.maxPoint = pkss[0].points;

		if (isNLianN(2, 1, pkss, 3)) {
			return vo;
		}

		return null;
	}

	/**
	 * 有二个赖子的连对
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getLianDuiWith2God(Poker[] pks) {
		Poker[] pkss = slice(pks, 2);

		PokersTypeVo vo = new PokersTypeVo();
		vo.len = pks.length;
		vo.type = PokersTypeVo.TYPE_LIANDUI;

		if (isNLianN(2, 2, pkss, 3)) {
			/* 如果不带赖子也是连对，则最大的牌需要区分一下 */
			if (isNLianN(2, 0, pkss, 2)) {
				if (pkss[0].points == Poker.POKER_A) {
					vo.maxPoint = Poker.POKER_A;
				} else {
					vo.maxPoint = pkss[0].points + 1;
				}
			} else {
				vo.maxPoint = pkss[0].points;
			}
			return vo;
		}

		return null;
	}

	/**
	 * 没有赖子的牌是不是连三张
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getLianSanWithNoGod(Poker[] pks) {
		int prevPoint = pks[0].points;
		for (int i = 0; i < pks.length; i += 3) {
			// 3张相同
			if (!isAllSame(new Poker[] { pks[i], pks[i + 1], pks[i + 2] })) {
				return null;
			}

			// 间隔为1
			if (i != 0) {
				if (prevPoint - 1 != pks[i].points)
					return null;

				prevPoint = pks[i].points;
			}
		}

		PokersTypeVo vo = new PokersTypeVo();
		vo.len = pks.length;
		vo.maxPoint = pks[0].points;
		vo.type = PokersTypeVo.TYPE_LIANSAN;
		return vo;
	}

	/**
	 * 有一个/二个赖子的牌是不是连三张
	 * 
	 * @param pks
	 * @return
	 */
	private static PokersTypeVo getLianSanWithGod(Poker[] pks, int godCount) {
		Poker[] pkss = slice(pks, godCount);

		PokersTypeVo vo = new PokersTypeVo();
		vo.len = pks.length;
		vo.type = PokersTypeVo.TYPE_LIANSAN;
		vo.maxPoint = pkss[0].points;

		if (isNLianN(3, godCount, pkss, 3)) {
			return vo;
		}

		return null;
	}

	/**
	 * 判断是不是N连 
	 * @param sameCount	用于判断是连对还是连三张
	 * @param godCount	赖子的张数
	 * @param pks		需要判断的牌
	 * @param lianCount	用于判断是2连还是3连
	 * @return 
	 */	
	private static boolean isNLianN(int sameCount, int godCount, Poker[] pks, int lianCount) {
		int xLian = 0;
		int prevPoint = -1;
		
		for(int i = 0; i < pks.length;i++){
			boolean isUseGod = false;
			/* 优先对点数进行判断，点数符合递减的要求，才继续，否则return */
			if(prevPoint == -1){
				prevPoint = pks[i].points;
			}else if(prevPoint != pks[i].points + 1){
				
				/* 赖子的数量够一对或者三张，则全配，否则return */
				if(godCount >= sameCount){
					godCount -= sameCount;
					prevPoint --; 
					xLian ++;
					isUseGod = true;
				}else{
					return false;
				}
				
			}else{
				prevPoint = pks[i].points;
			}
			
			/* 然后对牌进行梯次过滤 */
			Poker[] tempArr = slice(pks, i, i + sameCount);
			
			/* 消耗王后，需要再次判断 */
			if(isUseGod){
				if(prevPoint != tempArr[0].points + 1){
					return false;
				}else{
					prevPoint -- ;
				}
			}
			
			if((tempArr.length == sameCount) && isAllSame(tempArr)){
				i += sameCount -1;
			}else{
				/* 消耗赖子的张数 */
				int needNum = 1;
				tempArr =  slice(pks, i, i + sameCount - needNum);
				while(!isAllSame(tempArr)){
					needNum ++;
					tempArr = slice(pks, i, i + sameCount - needNum);
				}
				
				if(godCount >= needNum){
					godCount -= needNum;
					i += sameCount - needNum - 1;
				}else{
					return false;
				}
			}
			
			/* 成功过滤一次，加1连 */
			xLian++;
		}
		
		/* 就算是连，连的数量也必须满足要求 */
		return (xLian >= lianCount) || (xLian >= lianCount-1 && godCount == 2);
	}

	/**
	 * 是否多有的牌都一样
	 * 
	 * @param pks
	 * @return
	 */
	private static boolean isAllSame(Poker[] pks) {
		Poker fisrt = pks[0];

		for (Poker pk : pks) {
			if (fisrt.points != pk.points) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 没有赖子的连对 6<=L<=24, L%2 = 0, 奇数位置的牌=后面一张牌，且奇数位置的牌点数递减1，不允许出现[2，JOKER]
	 */
	private static PokersTypeVo getLianDuiWithNoGod(Poker[] pks) {
		PokersTypeVo vo = new PokersTypeVo();
		vo.len = pks.length;
		vo.type = PokersTypeVo.TYPE_LIANDUI;

		int prevPoint = pks[0].points;
		for (int i = 0; i < pks.length; i += 2) {
			// 奇数==偶数
			if (pks[i].points != pks[i + 1].points) {
				return null;
			}

			// 偶数间隔为1
			if (i != 0) {
				if (prevPoint - 1 != pks[i].points)
					return null;

				prevPoint = pks[i].points;
			}
		}

		vo.maxPoint = pks[0].points;
		return vo;
	}

	/**
	 * 小王、二 是特殊的
	 */
	private static boolean has2orJoker(Poker[] pks) {
		for (Poker pk : pks) {
			if (pk.points == Poker.POKER_2 || pk.points == Poker.SML_JOKER)
				return true;
		}
		
		return false;
	}

	/**
	 * 是否是赖子
	 * 
	 * @param pk
	 * @return
	 */
	private static boolean isGod(Poker pk) {
		return (pk.points == Poker.BIG_JOKER);
	}

	/**
	 * 返回由原始数组中某一范围的元素构成的新数组，而不修改原始数组。
	 * @param pks
	 * @param startIndex
	 * @return
	 */
	private static Poker[] slice(Poker[] pks, int startIndex) {
		if (pks.length <= startIndex)
			return pks;
		
		Poker[] rtnPks = new Poker[pks.length - startIndex];
		
		int mark = 0;
		for (int i = startIndex; i < pks.length; i++) {
			rtnPks[mark++] = pks[i];
		}
		
		return rtnPks;
	}

	/**
	 * 返回由原始数组中某一范围的元素构成的新数组，而不修改原始数组。
	 * @param pks
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	private static Poker[] slice(Poker[] pks, int startIndex, int endIndex) {
		startIndex = Math.max(startIndex, 0);
		endIndex = Math.min(pks.length, endIndex);
		
		if (startIndex >= endIndex)
			return new Poker[] {};
		
		Poker[] rtnPks = new Poker[endIndex - startIndex];
		
		int mark = 0;
		for (int i = startIndex; i < endIndex; i++) {
			rtnPks[mark++] = pks[i];
		}
		
		return rtnPks;
	}
	
	public static void main(String[] args) {
		/* 初始化扑克 */
		Poker[] pksA = new Poker[] {
				new Poker(Poker.POKER_7, 2),
				new Poker(Poker.POKER_7, 2),
				new Poker(Poker.POKER_K, 2),
				new Poker(Poker.POKER_K, 2),
				new Poker(53),
				new Poker(53)
		};
		
		Poker[] pksB = new Poker[] {
				new Poker(Poker.POKER_10, 2),
				new Poker(Poker.POKER_10, 2),
				new Poker(Poker.POKER_K, 2),
				new Poker(Poker.POKER_K, 2),
				new Poker(53),
				new Poker(53)
		};
		
		PokersTypeVo vo1 = Rule.getType(pksA);
		System.out.println(vo1);
		System.out.println(Arrays.asList(pksA));

		PokersTypeVo vo2 = Rule.getType(pksB);
		System.out.println(vo2);
		System.out.println(Arrays.asList(pksB));
		
		System.out.println(Rule.compare(vo1, vo2));
	}
}

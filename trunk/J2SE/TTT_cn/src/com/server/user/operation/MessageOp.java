package com.server.user.operation;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.cindy.run.util.DataFactory;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.DropGoodsBean;
import com.database.model.bean.Message;
import com.server.util.DBUtil;

public class MessageOp {

	private static Log log = LogFactory.getLog(MessageOp.class);

	@SuppressWarnings("unchecked")
	public static List<Message> getMessages(long id) {
		List<Message> messageList = null;
		try {
			messageList = (List<Message>) DBUtil.namedQuery(id, "from Message m where m.accepter = "
							+ id);
		} catch (Exception e) {
			log.error(e, e);
		}
		if (messageList != null) {
			clean(messageList);
		}
		return messageList;
	}

	public static void createMessage(long sender, long accepter, long attacher,
			int senderRace, int messageType, List<DropGoodsBean> goodsList,
			List<DropGoodsBean> userPropList, List<DropGoodsBean> lossGoods,
			List<DropGoodsBean> reward) {
		Message msg = new Message();
		msg.setAccepter(accepter);
		msg.setSender(sender);
		msg.setAttacher(attacher);
		msg.setMessageType(messageType);
		msg.setSenderRace(senderRace);
		msg.setTime(System.currentTimeMillis());
		msg.setContent(DropGoodsBean.encode2Ja(goodsList));
		msg.setUsedProp(DropGoodsBean.encode2Ja(userPropList));
		msg.setLossGoods(DropGoodsBean.encode2Ja(lossGoods));
		msg.setLossGoods(DropGoodsBean.encode2Ja(lossGoods));
		msg.setReward(DropGoodsBean.encode2Ja(reward));
		try {
			DBUtil.save(accepter, msg);
		} catch (Exception e) {
			DBUtil.merge(accepter, msg);
		}
	}

	public static void clean(List<Message> messageList) {
		if (messageList != null) {
			Iterator<Message> ite = messageList.iterator();
			while (ite.hasNext()) {
				Message msg = ite.next();
				if ((msg.getMessageType() == 0 && (System.currentTimeMillis() - msg
						.getTime()) > (long) (24 * 3600 * 1000))
						|| (System.currentTimeMillis() - msg.getTime() > (long) (7 * 24 * 3600 * 1000))
						|| (messageList.size() > 50 && msg.getMessageType() == 0)) {
					ite.remove();
				}
			}
		}
	}

	public static byte[] getMessages(long id, byte[] information)
			throws Exception {
		byte[] re = null;
		List<Message> messageList = getMessages(id);
		if (messageList != null) {
			re = DataFactory.getbyte(messageList.size());
			Iterator<Message> ite = messageList.iterator();
			while (ite.hasNext()) {
				Message msg = ite.next();
				re = DataFactory.addByteArray(re, DataFactory
						.doubleToXiaoTouByte(msg.getId()));
				if (msg.isOld()) {
					re = DataFactory.addByteArray(re, new byte[] { 0x01 });
				} else {
					re = DataFactory.addByteArray(re, new byte[] { 0x00 });
				}
				re = DataFactory.addByteArray(re, DataFactory
						.doubleToXiaoTouByte(msg.getSender()));
				re = DataFactory.addByteArray(re, DataFactory
						.doubleToXiaoTouByte(msg.getAttacher()));
				re = DataFactory.addByteArray(re, DataFactory
						.doubleToXiaoTouByte(msg.getTime()));
				re = DataFactory.addByteArray(re, DataFactory.getbyte(msg
						.getSenderRace()));
				re = DataFactory.addByteArray(re, DataFactory.getbyte(msg
						.getMessageType()));

				List<DropGoodsBean> goodsList = DropGoodsBean.decode2List(msg
						.getContent());
				re = DataFactory.addByteArray(re, DataFactory.getbyte(goodsList
						.size()));
				Iterator<DropGoodsBean> goodsIte = goodsList.iterator();
				while (goodsIte.hasNext()) {
					DropGoodsBean goods = goodsIte.next();
					re = DataFactory.addByteArray(re, DataFactory.getbyte(goods
							.getGoodsID()));
					re = DataFactory.addByteArray(re, DataFactory.getbyte(goods
							.getNum()));
				}

				List<DropGoodsBean> usedPropsList = DropGoodsBean
						.decode2List(msg.getUsedProp());
				re = DataFactory.addByteArray(re, DataFactory
						.getbyte(usedPropsList.size()));
				Iterator<DropGoodsBean> usedPropIte = usedPropsList.iterator();
				while (usedPropIte.hasNext()) {
					DropGoodsBean prop = usedPropIte.next();
					re = DataFactory.addByteArray(re, DataFactory.getbyte(prop
							.getGoodsID()));
				}

				List<DropGoodsBean> lossGoodsList = DropGoodsBean
						.decode2List(msg.getLossGoods());
				int lossGoodsCount = 0;
				byte[] lossGoodsTemp = new byte[] {};
				if (lossGoodsList != null) {
					lossGoodsCount = lossGoodsList.size();
					Iterator<DropGoodsBean> lossGoodsIte = lossGoodsList
							.iterator();
					while (lossGoodsIte.hasNext()) {
						DropGoodsBean lossMonster = lossGoodsIte.next();
						lossGoodsTemp = DataFactory.addByteArray(lossGoodsTemp,
								DataFactory.getbyte(lossMonster.getGoodsID()));
						lossGoodsTemp = DataFactory.addByteArray(lossGoodsTemp,
								DataFactory.getbyte(lossMonster.getNum()));
					}
				}
				re = DataFactory.addByteArray(re, DataFactory
						.getbyte(lossGoodsCount));
				if (lossGoodsCount > 0) {
					re = DataFactory.addByteArray(re, lossGoodsTemp);
				}

				// 防守方获得怪物 返回。
				List<DropGoodsBean> rewards = DropGoodsBean.decode2List(msg
						.getReward());
				if (rewards != null) {
					re = DataFactory.addByteArray(re, DataFactory.getbyte(rewards.size()));
					Iterator<DropGoodsBean> rewardsIte = rewards.iterator();
					while (rewardsIte.hasNext()) {
						DropGoodsBean reward = rewardsIte.next();
						re = DataFactory.addByteArray(re, DataFactory.getbyte(reward.getGoodsID()));
					}
				}
			}
		} else {
			re = DataFactory.getbyte(0);
		}
		return re;
	}

	public static void readMessage(long id, byte[] information)
			throws Exception {
		long messageID = DataFactory.doubleBytesToLong(DataFactory.get(
				information, 10, 8));
		String sql = "update message set old = true where id = " + messageID;
		DBUtil.executeUpdate(id, sql);
	}

	public static void deleteMessage(long id, byte[] information)
			throws Exception {
		int num = DataFactory.getInt(DataFactory.get(information, 10, 4));
		for (int i = 0; i < num; i++) {
			long messageID = DataFactory.doubleBytesToLong(DataFactory.get(
					information, 14 + i * 8, 8));
			String sql = "delete from message where id = " + messageID;
			DBUtil.executeUpdate(id, sql);
		}
	}
}

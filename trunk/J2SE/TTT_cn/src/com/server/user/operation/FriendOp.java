package com.server.user.operation;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.util.DataFactory;
import com.database.model.bean.CastleBean;
import com.database.model.bean.UserCastle;
import com.server.goods.Goods;
import com.server.goods.GoodsCate;
import com.server.util.Configuration;
import com.server.util.DBUtil;

public class FriendOp {
	private static Log log = LogFactory.getLog(FriendOp.class);
	private static final int FRIEND_NUM_LIMIT = Configuration.getFriendNumLimit();

	public static byte[] getAppFriend(long id, byte[] information) throws Exception{
		byte[] re = null;
		int num = DataFactory.getInt(DataFactory.get(information, 10, 4));
		JSONArray ja = new JSONArray();
		for (int i = 0; i < num; i++) {
			Long l = DataFactory.doubleBytesToLong(DataFactory.get(information, 14 + 8 * i, 8));
			ja.add(l.longValue());
		}
		int count = 0;
		re = DataFactory.getbyte(count);
		for (int i = 0; i < ja.size() && count <= FRIEND_NUM_LIMIT; i++) {
			long friendID = ja.getLong(i);
			//HibernateUtil.closeSession(HibernateUtil.checkExistCurrentSession(friendID));
			UserCastle castle = (UserCastle) DBUtil.doGet(id, friendID, UserCastle.class);
			if(castle != null){
				re = DataFactory.addByteArray(re, DataFactory.doubleToXiaoTouByte(friendID));
				re = DataFactory.addByteArray(re, DataFactory.getbyte(castle.getRace()));
				re = DataFactory.addByteArray(re, DataFactory.getbyte(castle.getCurrTitle()));
				CastleBean castleBean = (CastleBean) Goods.getById(GoodsCate.CASTLEBEAN, castle.getCastleID());
				re = DataFactory.addByteArray(re, DataFactory.getbyte(castleBean.getLevel()));
				count++;
			}else{
				log.info("id:" + friendID + " doesn't login.");
				continue;
			}
		}
		DataFactory.replace(re, 0, DataFactory.getbyte(count));
		return re;
	}
}

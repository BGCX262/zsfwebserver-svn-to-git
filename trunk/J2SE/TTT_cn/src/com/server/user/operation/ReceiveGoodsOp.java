/**
 * ReceiveGoodsOp.java
 */
package com.server.user.operation;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.management.relation.Role;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.cindy.run.connect.instance.Instance;
import com.cindy.run.util.DataFactory;
import com.database.hibernate.util.HibernateUtil;
import com.database.model.bean.UserReceiveGood;
import com.server.cache.UserMemory;

/**
 * 领取物品操作
 * 
 * @author zsf
 */
public class ReceiveGoodsOp {

	private static Log log = LogFactory.getLog(ReceiveGoodsOp.class);
	
	/**
	 * 得到 玩家的 补偿 物品列表
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static byte[] getGoods(long id) throws Exception {
		byte[] re = new byte[]{};
		Session session = null;
		session = HibernateUtil.currentSession(id);
		SQLQuery query = session.createSQLQuery("select * from userreceivegood urg where urg.masterID =:id and urg.isReceive=0 limit 10");
		query.addEntity(UserReceiveGood.class);
		query.setLong("id", id);
		List<UserReceiveGood> list = query.list();
		
		if(list!=null && list.size()>0 ){
			re = DataFactory.addByteArray(re, DataFactory.getbyte(list.size()));
			Iterator<UserReceiveGood> iterator = list.iterator();
			while (iterator.hasNext()) {
				UserReceiveGood goods = iterator.next();
				re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getId()));
				re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getGoodID()));
				re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getNum()));
				re = DataFactory.addByteArray(re, DataFactory.getbyte(goods.getIsReceive()));
			}
		}else{
			re = DataFactory.addByteArray(re, DataFactory.getbyte(0));
		}
		return re;
	}
	
	/**
	 * 更新玩家补偿物品状态
	 * @param id
	 * @param information
	 * @return
	 * @throws Exception
	 */
	public static byte[] updateReceived(long id, Instance role, byte[] information) throws Exception{
		byte[] re = new byte[]{0x00};
		Session session = null;
		Transaction transaction = null;
		try{
			int goodsId = DataFactory.getInt(DataFactory.get(information, 10, 4));
			session = HibernateUtil.currentSession(id);
			SQLQuery query = session.createSQLQuery("select * from userreceivegood urg where urg.id =?");
			query.addEntity(UserReceiveGood.class);
			query.setInteger(0,goodsId);
			UserReceiveGood receivegood = (UserReceiveGood) query.uniqueResult();
			if(receivegood==null){
				return new byte[] { 0x01 };
			}
			//更新背包
			receivegood.setRealTime(receivegood.getRealTime() == null || receivegood.getRealTime()==0 ? Long.MAX_VALUE : receivegood.getRealTime());
			StorageOp.storeGoods(id, receivegood.getGoodID(), receivegood.getNum(), receivegood.getRealTime());
			//更新补偿状态
			receivegood.setIsReceive(1);
			receivegood.setReceiveTime(new Date().getTime());
			transaction = session.beginTransaction();
			session.update(receivegood);
			transaction.commit();
			
			re = DataFactory.addByteArray(re, DataFactory.getbyte(receivegood.getGoodID()));
			re = DataFactory.addByteArray(re, DataFactory.getbyte(receivegood.getNum()));
			/*
			 * 补偿物品的类别
			 * 0手动补偿 1 怪物捕获 2 送礼
			 */
			byte[] type = DataFactory.getbyte(0);
			if(receivegood.getType()!=null){
				type = DataFactory.getbyte(receivegood.getType());
			}
			re = DataFactory.addByteArray(re,type);
			if (receivegood.getGoodID() / 10000 == 7)
				SlaverOp.sendSlaverList(id, role, information);
		}catch(Exception e){
			log.error(e);
			if(transaction!=null){
				transaction.rollback();
			}
			return new byte[] { 0x01 };
		}finally{
			if(session!=null){
				HibernateUtil.closeSession(session);
			}
		}
		return re;
	}

}

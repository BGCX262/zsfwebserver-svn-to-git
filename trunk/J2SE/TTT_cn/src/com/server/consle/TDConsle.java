package com.server.consle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cindy.run.connect.server.SocketServer;
import com.cindy.run.consle.Consle;
import com.cindy.run.consle.MyConsle;
import com.database.hibernate.util.HibernateUtil;
import com.server.cache.UserMemory;
import com.server.dispose.TDDispose;
import com.server.finance.FinanceImpl;
import com.server.goods.Goods;
import com.server.user.operation.AccountOp;
import com.server.user.operation.ActivityOp;
import com.server.user.operation.FightOp;
import com.server.user.operation.OnlineStatOp;
import com.server.user.operation.PVPListOp;
import com.server.util.LoadMemcachedUtil;

public class TDConsle implements Consle{
	private static final Log log = LogFactory.getLog(TDConsle.class);
	private static final TDConsle instance = new TDConsle();
	private static final SocketServer socketServer = new SocketServer(TDDispose.class);
	private static TDConsle getInstance(){
		if(instance != null){
			return instance;
		}else{
			log.error("initial logic server error!");
			System.exit(1);
			return instance;
		}
		
	}
	@Override
	public String getName() {
		return "TD";
	}

	@Override
	public boolean reloadConfig() {
		return false;
	}

	@Override
	public boolean restart() {
		stop();
		start();
		return true;
	}

	private void preStart() {
		HibernateUtil.closeSession(HibernateUtil.getDefaultSession());
		Goods.instance();
		PVPListOp.instance();
		UserMemory.instance();
		FightOp.instance();
		FinanceImpl.instance();
		//BroadcastOp.instance();
		OnlineStatOp.instance();
		new AccountOp();
		new ActivityOp();
		//LoadMemcachedUtil.init();
	}
	
	@Override
	public boolean start() {
		preStart();
		socketServer.start();
		return true;
	}

	@Override
	public boolean status() {
		return true;
	}

	@Override
	public boolean stop() {
		socketServer.stop();
		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			log.error(e, e);
		}
		return true;
	}
	
	public static void main(String[] args) {
		Consle consle = getInstance();
		MyConsle c = new MyConsle(800);
		c.startConsle(consle);
		consle.start();
	}
}

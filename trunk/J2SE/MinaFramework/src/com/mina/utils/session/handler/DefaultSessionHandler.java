package com.mina.utils.session.handler;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.mina.data.User;
import com.mina.framework.AbstractRoom;
import com.mina.pool.request.RequestHandlerThreadPool;
import com.mina.pool.request.RunnableRequestPacket;
import com.mina.server.room.SimpleRoom;
import com.mina.userMgr.UserFactory;
import com.mina.utils.Configuration;
import com.mina.utils.decryption.IDecryptionable;
import com.mina.utils.decryption.SimpleDecryption;

/**
 * 默认会话处理类
 * 
 * @author zsf 2011-4-11 上午09:46:02
 */
public class DefaultSessionHandler extends IoHandlerAdapter {

	public static final Logger LOG = Logger.getLogger(DefaultSessionHandler.class);

	public static final IDecryptionable DECRYPTION;

	public static final AbstractRoom ROOM;

	public static final String PROTOCOL_TYPE;

	static {
		DECRYPTION = (IDecryptionable) Configuration.getInstance("decryption", new SimpleDecryption());
		ROOM = (AbstractRoom) Configuration.getInstance("room", new SimpleRoom());
		PROTOCOL_TYPE = Configuration.get("protocol_type", "string");
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		User user = (User) session.getAttribute("user");
		Map<String, Object> args = new HashMap<String, Object>();
		args.put("user", user);
		ROOM.handleInnerEvent(args, "user_exit");
		UserFactory.freeUser(user);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		User user = UserFactory.getIdleUser();
		user.setSession(session);
		user.setRemoteAddress((InetSocketAddress) session.getRemoteAddress());
		session.setAttribute("user", user);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		session.close(true);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {

	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {

	}

	@Override
	public void messageReceived(IoSession session, Object obj) throws Exception {
		User user = (User) session.getAttribute("user");
		obj = DECRYPTION.decryption(obj);
		RequestHandlerThreadPool.putRequest(new RunnableRequestPacket(user, obj));
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		if (cause.getMessage().indexOf("远程主机强迫关闭了一个现有的连接") == -1)
			LOG.error("Exception: ", cause);
	}

}

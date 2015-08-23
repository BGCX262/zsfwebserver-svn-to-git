package com.ttt.cmd;

import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

import net.sf.cindy.Session;
import net.sf.cindy.SessionHandlerAdapter;
import net.sf.cindy.SessionType;
import net.sf.cindy.decoder.ByteArrayDecoder;
import net.sf.cindy.encoder.ByteArrayEncoder;
import net.sf.cindy.session.SessionFactory;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ttt.handlers.AbstractCallback;
import com.ttt.handlers.AbstractRequestHandler;
import com.ttt.handlers.AbstractResponseHandler;
import com.ttt.handlers.response.EnterSceneHandler;
import com.ttt.handlers.response.GetStorageHandler;
import com.ttt.handlers.response.LoginHandler;
import com.ttt.handlers.response.ReceiveGoodHandler;
import com.ttt.instance.DefaultInstance;
import com.ttt.util.ByteArrayMap;
import com.ttt.util.Cmd;
import com.ttt.util.CryptFactory;
import com.ttt.util.LoadAllClassUtil;

/**
 * 客户端模拟器
 * @author zsf 2011-12-9 下午12:26:49
 */
public abstract class SimpleClient {

	private CryptFactory crypt = new CryptFactory();
	private static final Log LOG = LogFactory.getLog(SimpleClient.class);
	public static long[] UIDS;

	private List<Session> sessionList = new LinkedList<Session>();
	protected ByteArrayMap requestInvocationMap = new ByteArrayMap();
	protected ByteArrayMap responseInvocationMap = new ByteArrayMap();
	protected ByteArrayMap callbackInvocationMap = new ByteArrayMap();
	
	public static long byte_count = 0;

	public SimpleClient(final String ipAddr, final int port, long[] uids) {
		try {
			new Thread() {
				public void run() {
					while (true) {
						LOG.info("EnterSceneHandler: " + EnterSceneHandler.outCount + "-" + EnterSceneHandler.inCount + 
								", GetStorageHandler: " + GetStorageHandler.outCount + "-" + GetStorageHandler.inCount + 
								", LoginHandler: " + LoginHandler.outCount + "-" + LoginHandler.inCount +  
								", ReceiveGoodHandler: " + ReceiveGoodHandler.outCount + "-" + ReceiveGoodHandler.inCount + "\n" + sessionList.size());
						try {
							Thread.sleep(1000 * 30);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			UIDS = uids.clone();
			LOG.info("Start connect!!");
			for (int i = 0; i < uids.length; i++) {
				final long uid = UIDS[i];
				Session session = SessionFactory.createSession(SessionType.TCP);
				session.setRemoteAddress(new InetSocketAddress(ipAddr, port));
				session.setPacketEncoder(new ByteArrayEncoder());
				session.setPacketDecoder(new ByteArrayDecoder());
				session.setSessionHandler(new SessionHandlerAdapter() {

					public void sessionStarted(Session session) throws Exception {
						DefaultInstance instance = new DefaultInstance(true);
						instance.setSession(session);
						session.setAttribute("instance", instance);
					}

					public void objectReceived(Session session, Object obj) throws Exception {
						byte[] infor = (byte[]) obj;
						DefaultInstance instance = (DefaultInstance) session.getAttribute("instance");
						if (infor != null) {
							instance.addPackages(infor);
							while (instance.hasPackage()) {
								byte[] message = instance.getPackage();
								byte_count += message.length;
								Cmd cmd = Cmd.getInstance(message);
								try {
									Class<?> class1 = responseInvocationMap.get(cmd.getResponse());
									Class<?> class2 = callbackInvocationMap.get(cmd.getResponse());
									if (class1 == null) {
										throw new RuntimeException("no response found!! "
												+ cmd.getCmdResponse(0, 4).toString());
									}
									if (class2 == null) {
										RuntimeException exception =
												new RuntimeException("no callback found!! "
														+ cmd.getCmdResponse(0, 4).toString());
										LOG.warn(exception, exception);
									}

									AbstractResponseHandler response = (AbstractResponseHandler) class1.newInstance();
									response.setClient(SimpleClient.this);
									JSONObject json = response.handlerRequest(uid, cmd);
									if (class2 != null) {
										AbstractCallback callback = (AbstractCallback) class2.newInstance();
										callback.setClient(SimpleClient.this);
										callback.callback(uid, json);
									}

								} catch (Exception e) {
									LOG.error(e, e);
								}
							}
						}
					}
				});
				session.start().complete();
				if (session.isStarted()) {
					sessionList.add(session);
				}
			}
			LOG.info("Connect success for " + sessionList.size() + "!!");
			initInvocation();
			otherInvocation();
		} catch (Exception e) {
			LOG.error(e, e);
		}
	}

	/**
	 * 不需重载
	 */
	protected void initInvocation() {
		LoadAllClassUtil.load(requestInvocationMap, "/com/ttt/handlers/request");
		LoadAllClassUtil.load(responseInvocationMap, "/com/ttt/handlers/response");
	}

	/**
	 * 重载接口
	 */
	protected abstract void otherInvocation();

	/**
	 * 发送协议
	 * @param cmd
	 * @return
	 */
	public boolean sendMsg(final byte[] cmd, Object... params) {
		boolean flag = true;
		for (long id : UIDS) {
			flag = sendMsg(id, cmd, params) && flag;
		}
		return flag;
	}

	/**
	 * 单体发送协议
	 * @param cmd
	 * @return
	 */
	public boolean sendMsg(final long id, final byte[] cmd, final Object... params) {
		try {
			int i = 0;
			for (i = 0; i < UIDS.length; i++)
				if (UIDS[i] == id)
					break;
			final Session session = sessionList.get(i);
			new Thread() {

				public void run() {
					try {
						Class<?> class1 = requestInvocationMap.get(cmd);
						if (class1 == null) {
							throw new RuntimeException("No request invocation found!! "
									+ Cmd.getInstance(cmd).toString());
						}
						final AbstractRequestHandler request = (AbstractRequestHandler) class1.newInstance();
						request.setClient(SimpleClient.this);

						new Thread() {

							public void run() {
								try {
									Cmd req = Cmd.getInstance(request.getMsg(id, params));
									req.appendLength();
									byte[] bs = encryptMessage(req.getResponse());
									session.send(bs);

								} catch (Exception e) {
									LOG.error(e, e);
								}
							}
						}.start();
					} catch (Exception e) {
						LOG.error(e, e);
					}
				}
			}.start();
		} catch (Exception e) {
			LOG.error(e, e);
		}
		return true;
	}

	private byte[] encryptMessage(byte[] obj) throws Exception {
		return crypt.blowfishEncrypt(obj);
	}

}

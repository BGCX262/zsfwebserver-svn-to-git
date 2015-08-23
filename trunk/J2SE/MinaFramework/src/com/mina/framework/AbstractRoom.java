package com.mina.framework;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import net.sf.json.JSONObject;

import com.mina.data.User;
import com.mina.pool.response.ResponseThreadPool;
import com.mina.pool.response.RunnableResponsePacket;
import com.mina.utils.Configuration;
import com.mina.utils.InvocationByteKeyMap;
import com.mina.utils.LogUtil;
import com.mina.utils.encryption.IEncryptionable;
import com.mina.utils.encryption.SimpleEncryption;


/**
 * 抽象房间，主逻辑类
 * @author zsf
 * 2011-4-12 上午09:51:02
 */
public abstract class AbstractRoom {
	
	private static final IEncryptionable encryption;
	static {
		encryption = (IEncryptionable) Configuration.getInstance("encryption", new SimpleEncryption());
	}
	
	protected Map<String, String> jsonInvocationMap = new HashMap<String, String>();
	protected Map<String, String> stringInvocationMap = new HashMap<String, String>();
	protected InvocationByteKeyMap byteInvocationMap = new InvocationByteKeyMap();
	
	/**
	 * json协议加载处理对象
	 */
	protected void initJSONInvocation() {}
	/**
	 * string协议加载处理对象
	 */
	protected void initStringInvocation() {}
	/**
	 * byte协议加载处理对象
	 */
	protected void initByteInvocation() {}
	
	public AbstractRoom() {
		initJSONInvocation();
		initStringInvocation();
		initByteInvocation();
	}
	
	/**
	 * 字符串协议处理
	 * @param command
	 * @param content
	 */
	public void handleStringRequest(String command, String content, User user) {
		try {
			String classPath = stringInvocationMap.get(command);
			
			Class<?> cls = Class.forName(classPath);
			
			IStringRequestHandler newInstance = (IStringRequestHandler) cls.newInstance();
			
			newInstance.setRoom(this);
			newInstance.onRequest(user, content);
			
		} catch (Exception e) {
			LogUtil.error("AbstractRoom.handleStringRequest() failed", e);
		}
	}
	
	/**
	 * json字符串处理
	 * @param command
	 * @param json
	 */
	public void handleJSONRequest(String command, JSONObject json, User user) {
		try {
			String classPath = stringInvocationMap.get(command);
			
			Class<?> cls = Class.forName(classPath);
			
			IJSONRequestHandler newInstance = (IJSONRequestHandler) cls.newInstance();
			
			newInstance.setRoom(this);
			newInstance.onRequest(user, json);
			
		} catch (Exception e) {
			LogUtil.error("AbstractRoom.handleJSONRequest() failed", e);
		}
	}
	
	/**
	 * byte字符串处理
	 * @param command
	 * @param json
	 */
	public void handleByteRequest(byte[] command, byte[] bytes, User user) {
		try {
			String classPath = byteInvocationMap.get(command);
			
			Class<?> cls = Class.forName(classPath);
			
			IByteRequestHandler newInstance = (IByteRequestHandler) cls.newInstance();
			
			newInstance.setRoom(this);
			newInstance.onRequest(user, bytes);
			
		} catch (Exception e) {
			LogUtil.error("AbstractRoom.handleByteRequest() failed", e);
		}
	}
	
	/**
	 * 处理内部事件
	 * @param args
	 * @param event
	 */
	public void handleInnerEvent(Map<String, Object> args, String event) {}
	
	/**
	 * 给用户集合发送json消息
	 * @param json
	 * @param users
	 */
	public void sendResponse(JSONObject json, LinkedList<User> users) {
		send(json.toString(), users);
	}
	
	/**
	 * 给单个用户发送json消息
	 * @param json
	 * @param user
	 */
	public void sendResponse(JSONObject json, User user) {
		LinkedList<User> list = new LinkedList<User>();
		list.add(user);
		send(json.toString(), list);
	}
	
	/**
	 * 给用户集合发送字符串消息
	 * @param response
	 * @param users
	 */
	public void sendResponse(String response, LinkedList<User> users) {
		send(response, users);
	}
	
	/**
	 * 给单个用户发送字符串消息
	 * @param response
	 * @param user
	 */
	public void sendResponse(String response, User user) {
		LinkedList<User> list = new LinkedList<User>();
		list.add(user);
		send(response, list);
	}
	
	private void send(Object response, LinkedList<User> users) {
		response = encryption.encryption(response);
		
		ResponseThreadPool.addResponse(new RunnableResponsePacket(users, response));
	}

}

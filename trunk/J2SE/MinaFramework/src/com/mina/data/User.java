package com.mina.data;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.session.IoSession;

/**
 * 用户实例
 * @author zsf
 * 2011-4-11 上午09:48:43
 */
public class User {
	
	private int id;
	
	private String name;
	
	private InetSocketAddress remoteAddress;
	
	private IoSession session;
	
	private Map<String, UserVar> userVars = new HashMap<String, UserVar>();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setSession(IoSession session) {
		this.session = session;
	}
	
	public IoSession getSession() {
		return session;
	}

	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(InetSocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	
	public UserVar getUserVar(String key) {
		return userVars.get(key);
	}
	
	public void clearUserVar() {
		userVars.clear();
	}
	
	public void addUserVar(String key, UserVar var) {
		userVars.put(key, var);
	}
	
	public void addAllUserVar(Map<String, UserVar> map) {
		userVars.putAll(map);
	}
	
	public User clearData() {
		name = null;
		remoteAddress = null;
		userVars.clear();
		return this;
	}
	
	public User() {}
	
	public User(int id) {
		this.id = id;
	}

}

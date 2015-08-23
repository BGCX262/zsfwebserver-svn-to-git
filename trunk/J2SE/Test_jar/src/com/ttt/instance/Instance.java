package com.ttt.instance;

import net.sf.cindy.Session;

public interface Instance{
	public enum Algorithm {Blowfish,RSA};
	
	public enum Attribute {
		UID, GID
	}
	
	public enum AttachObject {
		BEFORE_CLEAR, AFTER_CLEAR, SESSION
	}
	
	public Object getAttachObject(Object key);
	
	public void setAttachObject(Object key, Object v);

	public Object getAttribute(Object key);

	public void setAttribute(Object key, Object v);

	public byte[] encryptMessage(Algorithm algorithm, byte[] obj) throws Exception ;
	
	public byte[] decryptMessage(Algorithm algorithm, byte[] obj) throws Exception ;
	
	public boolean hasPackage();

	public byte[] getNextPackage();
	
	public void addPackages(byte[] infor);
	
	public byte[] getPackage()throws Exception ;
	
	public void send(byte[] information);
	
	public Session getSession();
	
	public void setSession(Session session);

	public Object getId();
	
	public void start();
	
	public void clear();

	public boolean close();

	public void closeWithoutSave();
}

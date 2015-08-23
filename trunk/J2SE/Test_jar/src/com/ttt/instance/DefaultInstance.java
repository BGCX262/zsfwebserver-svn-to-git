package com.ttt.instance;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.sf.cindy.Future;
import net.sf.cindy.Session;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ttt.util.CryptFactory;
import com.ttt.util.DataFactory;

public class DefaultInstance implements Instance {
	private static final Log log = LogFactory.getLog(DefaultInstance.class);
	private static final InstanceFactory instanceFactory = InstanceFactory.getFactory();
	private	Object id;
	private Session session;
	private ConcurrentHashMap<Object, Object> attribute;
	private ConcurrentHashMap<Object, Object> attachObject;
	private ConcurrentLinkedQueue<byte[]> packageList;
	private Decompose decompose;
	private CryptFactory crypt = new CryptFactory();
	private boolean save = true;
	public DefaultInstance(){
		id = new Object();
		attribute = new ConcurrentHashMap<Object, Object>();
		attachObject = new ConcurrentHashMap<Object, Object>();
		packageList = new ConcurrentLinkedQueue<byte[]>();
		decompose = new Decompose(packageList);
		instanceFactory.addInstance(this);
	}
	
	public DefaultInstance(boolean enableCrypt){
		this();
	}
	
	public byte[] encryptMessage(byte[] obj) throws Exception {
		return crypt.blowfishEncrypt(obj);
	}

	public byte[] decryptMessage(byte[] obj) throws Exception {
		return crypt.blowfishDecrypt(obj);
	}

	public byte[] getPackage() throws Exception {
		byte[] infor = decryptMessage(getNextPackage());
		if (infor == null || infor.length == 0) {
			return null;
		}
		return infor;
	}
	
	@Override
	public void send(byte[] information){
		try{
			if (session != null) {
				information = DataFactory.addLength(DataFactory.lengthOffset, encryptMessage(information));
				if (information != null && !information.equals("")) {
					session.send(information);
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
	}

	@Override
	public void clear() {
		if (attachObject != null && attachObject.get(AttachObject.BEFORE_CLEAR) != null && save) {
			Runnable beforeClear = (Runnable) attachObject.get(AttachObject.BEFORE_CLEAR);
			try {
				beforeClear.run();
			} catch (Exception e) {
				log.error(e);
			}
		}
		attribute.clear();
		if (attachObject != null && attachObject.get(AttachObject.AFTER_CLEAR) != null && save) {
			Runnable afterClear = (Runnable) attachObject.get(AttachObject.AFTER_CLEAR);
			try {
				afterClear.run();
			} catch (Exception e) {
				log.error(e);
			}
		}
		attachObject.clear();
		packageList = null;
		instanceFactory.removeInstance(this);
	}

	@Override
	public Session getSession() {
		return session;
	}
	
	public Object getAttachObject(Object key) {
		return attachObject.get(key);
	}

	public Object getAttribute(Object key) {
		return attribute.get(key);
	}

	public void setAttachObject(Object key, Object v) {
		if (v != null) {
			attachObject.put(key, v);
		} else {
			attachObject.remove(key);
		}
	}

	public void setAttribute(Object key, Object v) {
		if (v != null) {
			attribute.put(key, v);
		} else {
			attribute.remove(key);
		}
	}

	public boolean hasPackage() {
		return packageList != null ? packageList.isEmpty() ? false : true : false;
	}

	public byte[] getNextPackage() {
		byte[] infor = packageList.poll();
		return DataFactory.get(infor, DataFactory.lengthOffset + 2, infor.length - 2);
	}

	public void addPackages(byte[] infor) {
		if (decompose != null) {
			boolean status = decompose.addMessage(infor);
			if (!status && session != null) {
				session.close();
				log.warn("Instance is destroyed by package flow control.");
			}
		}
	}
	
	public ConcurrentHashMap<Object, Object> getAttachObject() {
		return attachObject;
	}

	public void setAttachObject(ConcurrentHashMap<Object, Object> attachObject) {
		this.attachObject = attachObject;
	}

	public ConcurrentHashMap<Object, Object> getAttribute() {
		return attribute;
	}

	public void setAttribute(ConcurrentHashMap<Object, Object> attribute) {
		this.attribute = attribute;
	}

	public ConcurrentLinkedQueue<byte[]> getPackageList() {
		return packageList;
	}

	public void setPackageList(ConcurrentLinkedQueue<byte[]> packageList) {
		this.packageList = packageList;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public boolean close() {
		save = true;
		Future future = session.close();
		if(future.isSucceeded()){
			return true;
		}
		return false;
	}
	
	@Override
	public void closeWithoutSave(){
		save = false;
		session.close();
	}

	@Override
	public Object getId() {
		return id;
	}

	@Override
	public void start() {
		session.start();
	}

	@Override
	public byte[] encryptMessage(Algorithm algorithm, byte[] obj) throws Exception {
		return null;
	}

	@Override
	public byte[] decryptMessage(Algorithm algorithm, byte[] obj) throws Exception {
		return null;
	}
}
package com.ttt.util;

import java.util.Arrays;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ttt.handlers.AbstractHandler;


public class ByteArrayMap extends HashMap<Integer, Class<AbstractHandler>> {

	private static final Log LOG = LogFactory.getLog(ByteArrayMap.class);
	private static final long serialVersionUID = 6578961679914231615L;

	@Override
	public Class<AbstractHandler> get(Object key) {
		return super.get(DataFactory.getInt((byte[]) key));
	}

	@Override
	public Class<AbstractHandler> put(Integer key, Class<AbstractHandler> value) {
		return super.put(key, value);
	}

	public Class<AbstractHandler> put(byte[] key, Class<AbstractHandler> value) {
		key = Arrays.copyOf(key, 4);
		return put(DataFactory.getInt(key), value);
	}
	
	public Class<AbstractHandler> put(String cls) {
		try {
			@SuppressWarnings("unchecked")
			Class<AbstractHandler> forName = (Class<AbstractHandler>) Class.forName(cls);
			byte[] command = forName.newInstance().getCommand();
			if (command == null) {
				throw new RuntimeException("no command return!! " + cls);
			}
			return put(command, forName);
		} catch (Exception e) {
			LOG.error(e, e);
		}
		return null;
	}
	
}

package com.server.cache;

import com.database.model.bean.MemoryBean;

public interface Cache {
	public void clean(MemoryBean bean);
	public void update(MemoryBean bean);
	public Object getFromDB(long id);
	public Object gerFromDB(long id, Object para);
	public void persist(MemoryBean bean);
}

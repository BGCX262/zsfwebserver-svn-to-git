package com.server.dao;

import com.server.database.IDBHelper;
import com.server.po.User;

public interface TestDao extends IDBHelper {
	
	public boolean login(User user);
	
	public void saveUser(User user);

}

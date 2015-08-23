package com.server.dao;

import com.server.database.DBHelperImpl;
import com.server.po.User;

public class TestDaoImpl extends DBHelperImpl implements TestDao {

	public boolean login(User user) {
		User result = getHibernateTemplate().load(User.class, user.getId());
		boolean flag = result != null;
		flag = flag ? result.getName().equals(user.getName()) : flag;
		flag = flag ? result.getPwd().equals(user.getPwd()) : flag;
		return flag;
	}

	public void saveUser(User user) {
		save(user);
	}

}

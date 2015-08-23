/**
 * @author zsf
 * 2011-9-20 下午05:19:00
 */
package com.server.actions.global;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.server.dao.TestDao;
import com.server.po.User;

/**
 * Default Action
 */
public class DefaultAction extends ActionSupport {
	private static final long serialVersionUID = 3358251179453674265L;

	private TestDao testDao;

	public String test() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().write("welcome~");
		response.getWriter().flush();
		testDao.saveUser(new User("admin1", "password"));
		return null;
	}

	public TestDao getTestDao() {
		return testDao;
	}

	public void setTestDao(TestDao testDao) {
		this.testDao = testDao;
	}

}

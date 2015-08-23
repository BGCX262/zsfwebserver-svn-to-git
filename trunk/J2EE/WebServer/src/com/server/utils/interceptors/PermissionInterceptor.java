/**
 * @author zsf
 * 2011-9-23 上午09:39:17
 */
package com.server.utils.interceptors;

import java.util.Map;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.server.po.User;

/**
 * Struts Permission Interceptor
 */
public class PermissionInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = -1286585131131413979L;

	/*
	 * Struts Permission Interceptor Method
	 */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		Map<String, Object> session = invocation.getInvocationContext().getSession();
		User user = (User) session.get("user");
		
		/* Is login */
		if (user != null) {
			return invocation.invoke();
		} else {
			return Action.LOGIN;
		}
	}

}

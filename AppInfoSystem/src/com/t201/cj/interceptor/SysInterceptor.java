package com.t201.cj.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.t201.cj.pojo.BackendUser;
import com.t201.cj.tools.Constants;
/**
 * 后台拦截器，拦截系统管理员
 * @author Mr_MS
 *
 */
public class SysInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = Logger.getLogger(SysInterceptor.class);
	
	public boolean preHandle(HttpServletRequest request,
						HttpServletResponse response,
						Object handler) throws Exception{
		logger.debug("================>SysInterceptor preHander!");
		HttpSession session = request.getSession();
		BackendUser backendUser = (BackendUser)session.getAttribute(Constants.USER_SESSION);
		if (null  == backendUser) {
			response.sendRedirect(request.getContextPath()+"/403.jsp");
			return false;
		}
		return true;
	}
}

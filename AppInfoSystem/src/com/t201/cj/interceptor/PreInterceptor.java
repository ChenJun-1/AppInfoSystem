package com.t201.cj.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.t201.cj.pojo.DevUser;
import com.t201.cj.tools.Constants;
/**
 * 前台拦截器，拦截开发者用户
 * @author Mr_MS
 *
 */
public class PreInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = Logger.getLogger(PreInterceptor.class);
	
	public boolean preHandle(HttpServletRequest request,
						HttpServletResponse response,
						Object handler) throws Exception{
		logger.debug("=============>PreInterceptor preHander!");
		HttpSession session = request.getSession();
		DevUser devUser = (DevUser)session.getAttribute(Constants.DEV_USER_SESSION);
		if (null  == devUser) {
			response.sendRedirect(request.getContextPath()+"/403.jsp");
			return false;
		}
		return true;
	}
}

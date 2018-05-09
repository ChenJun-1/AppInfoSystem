package com.t201.cj.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.t201.cj.service.backenduser.BackendUserService;
import com.t201.cj.tools.Constants;

@Controller
@RequestMapping("/sys/backend")
public class BackendUserController {
	private Logger logger = Logger.getLogger(BackendUserController.class);
	
	@Resource
	private BackendUserService backendUserService;
	
	@RequestMapping(value="/loginout.html")
	public String loginOut(HttpSession session){
		session.removeAttribute(Constants.USER_SESSION);
		logger.debug("BackendLoginOut========>session:" + session.getAttribute(Constants.USER_SESSION));
		return "backendlogin";
	}
	
	@RequestMapping(value="/main.html")
	public String main(){
		return "backend/main";
	}
	
	
}

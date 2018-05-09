package com.t201.cj.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.t201.cj.service.devuser.DevUserService;
import com.t201.cj.tools.Constants;

@Controller
@RequestMapping(value="/pre/devuser")
public class DevUserController {
	private Logger logger = Logger.getLogger(DevUserController.class);
	
	@Resource 
	private DevUserService devUserService;
	
	@RequestMapping(value="/loginout.html")
	public String loginOut(HttpSession session){
		session.removeAttribute(Constants.DEV_USER_SESSION);
		logger.debug("DevUserLoginOut========>session:" + session.getAttribute(Constants.DEV_USER_SESSION));
		return "devlogin";
	}
	
	@RequestMapping(value="/main.html")
	public String main(){
		return "developer/main";
	}
	
}

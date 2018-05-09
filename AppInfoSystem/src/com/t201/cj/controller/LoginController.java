package com.t201.cj.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.t201.cj.pojo.BackendUser;
import com.t201.cj.pojo.DevUser;
import com.t201.cj.service.backenduser.BackendUserService;
import com.t201.cj.service.devuser.DevUserService;
import com.t201.cj.tools.Constants;

@Controller
public class LoginController {
	private Logger logger = Logger.getLogger(LoginController.class);
	
	@Resource
	private BackendUserService backendUserService;
	@Resource
	private DevUserService devUserService;
	
	/*前台开发者登录处理*/
	@RequestMapping(value="/devlogin.html",method=RequestMethod.POST)
	public String devLogin(@RequestParam String devCode,
			   			   @RequestParam String devPassword,
			   			   HttpServletRequest request,
			   			   HttpSession session){
		logger.debug("devLogin=====>userCode:" + devCode + "userPassword:" + devPassword);
		//根据开发者账号查询开发者信息
		DevUser devUser = devUserService.getLogin(devCode);
		if (devUser != null) {
			if (devUser.getDevPassword().equals(devPassword)) {
				session.setAttribute(Constants.DEV_USER_SESSION, devUser);
				return "redirect:/pre/devuser/main.html";
			}else {
				request.setAttribute("error", "用户密码不正确！");
			}
		}else {
			request.setAttribute("error", "该用户不存在！");
		}
		return "devlogin";
	}
	
	/*后台管理员登录处理*/
	@RequestMapping(value="/backendlogin.html",method=RequestMethod.POST)
	public String backendLogin(@RequestParam String userCode,
			 				   @RequestParam String userPassword,
			 				   HttpServletRequest request,
			 				   HttpSession session){
		logger.debug("backendLogin=====>userCode:" + userCode + "userPassword:" + userPassword);
		//通过用户编码获取用户信息
		BackendUser backendUser = backendUserService.getLogin(userCode);
		if (backendUser != null) {
			if (backendUser.getUserPassword().equals(userPassword)) {
				session.setAttribute(Constants.USER_SESSION, backendUser);
				return "redirect:/sys/backend/main.html";
			}else {
				request.setAttribute("error", "用户密码不正确！");
			}
		}else {
			request.setAttribute("error", "该用户不存在！");
		}
		return "backendlogin";
	}
	
	/*后台管理系统 入口*/
	@RequestMapping(value="/gobackendlogin.html")
	public String goBackendLogin(){
		return "backendlogin";
	}
	
	/*开发者平台 入口*/
	@RequestMapping(value="/godevlogin.html")
	public String goDevLogin(){
		return "devlogin";
	}
	
}

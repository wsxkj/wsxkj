package com.vue.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.zpj.jwt.Constant;
import com.zpj.jwt.JwtUtil;
import com.zpj.materials.service.MaintainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zpj.common.BaseController;
import com.zpj.sys.entity.User;
import com.zpj.sys.service.UserService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/vue/login")
@Api(value = "/vue/login",tags="用户功能接口", description = "用户接口")
public class LoginAppController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private MaintainService maintainService;
	
	@RequestMapping("/checkLogin")
	@ResponseBody
	public void checkLogin(String username, String password){
		Map map=getRequestMap();
		User user=(User)getSession().getAttribute("jluser");
		param=new HashMap<String,Object>();
		if(!isNotNullObject(username)){
			username="";
		}
		if(!isNotNullObject(password)){
			password="";
		}
		if(null==user){
			if(!username.equalsIgnoreCase("")&&!password.equalsIgnoreCase(""))
				user=userService.checkLogin(username,password);
		}
		Map map1=new HashMap();
		if(null!=user){
			user.setIsAdmin("1");
			getSession().setAttribute("jluser", user);
			map1.put("msg", true);
			map1.put("data", user);
			map1.put("token",JwtUtil.buildJsonByUser(user));
			List list=maintainService.findUserNameList();
			map1.put("namelist",list);
		}else{
			map1.put("msg", false);
		}
		jsonWrite2(map1);
		
	}
}

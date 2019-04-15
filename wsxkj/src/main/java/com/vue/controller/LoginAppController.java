package com.vue.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zpj.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zpj.common.BaseController;
import com.zpj.sys.entity.User;
import com.zpj.sys.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping("/vue/login")
@Api(value = "/vue/login",tags="登陆功能", description = "登陆功能接口")
public class LoginAppController extends BaseController {
	@Autowired
	private UserService userService;

	@RequestMapping("/checkLogin")
	@ResponseBody
	@ApiOperation(value = "登陆", notes = "登陆", httpMethod = "POST")
	public void checkLogin(@ApiParam(required = false, name = "phone", value = "手机号码")@RequestParam("phone")String phone){
		Map map=getRequestMap();
//		User user=(User)getSession().getAttribute("jluser");
//		param=new HashMap<String,Object>();
//		if(!isNotNullObject(username)){
//			username="";
//		}
//		if(!isNotNullObject(password)){
//			password="";
//		}
//		if(null==user){
//			if(!username.equalsIgnoreCase("")&&!password.equalsIgnoreCase(""))
//				user=userService.checkLogin(username,password);
//		}
//		Map map1=new HashMap();
//		if(null!=user){
////			user.setIsAdmin("1");
//			getSession().setAttribute("jluser", user);
//			map1.put("msg", true);
//			map1.put("data", user);
//			map1.put("token",JwtUtil.buildJsonByUser(user));
//		}else{
//			map1.put("msg", false);
//		}
		jsonWrite2(map);
		
	}
	
	@RequestMapping("/sendCode")
	@ResponseBody
	@ApiOperation(value = "发送验证码", notes = "发送验证码", httpMethod = "POST")
	public void sendCode(@ApiParam(required = false, name = "phone", value = "手机号码")@RequestParam("phone")String phone){
		System.out.println(phone);
	}
}

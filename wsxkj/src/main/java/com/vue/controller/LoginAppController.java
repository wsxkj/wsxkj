package com.vue.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zpj.common.MsgUtil;
import com.zpj.common.ResultData;
import com.zpj.jwt.JwtUtil;
import com.zpj.materials.entity.IdCodeInfo;
import com.zpj.materials.service.IdCodeService;
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
@RequestMapping("/app/login")
@Api(value = "/app/login",tags="登陆功能", description = "登陆功能接口")
public class LoginAppController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private IdCodeService idCodeService;



	@RequestMapping("/sendCode")
	@ResponseBody
	@ApiOperation(value = "发送验证码", notes = "发送验证码", httpMethod = "POST")
	public void sendCode(@ApiParam(required = false, name = "phone", value = "手机号码")@RequestParam("phone")String phone){

		IdCodeInfo ici=new IdCodeInfo();
		String yzm=MsgUtil.generateYzm();
		ici.setPhone(phone);
		ici.setYzm(yzm);
		idCodeService.saveInfo(ici);
		MsgUtil.sendMsg(yzm,phone);
		ResultData rd=new ResultData<>();
		rd.setCode(200);
		rd.setMsg("验证码发送成功");
		jsonWrite2(rd);
	}


	@RequestMapping("/checkLogin")
	@ResponseBody
	@ApiOperation(value = "登陆", notes = "登陆", httpMethod = "POST",response=User.class)
	public void checkLogin(@ApiParam(required = false, name = "phone", value = "手机号码")@RequestParam("phone")String phone,
						   @ApiParam(required = false, name = "yzm", value = "验证码")@RequestParam("yzm")String yzm
						   ){
		IdCodeInfo ici=idCodeService.findInfoByPhone(phone);
		ResultData rd=new ResultData<>();
		if(ici.getYzm().equalsIgnoreCase(yzm)){
			User user=userService.findUserByPhone(phone);
			if(null!=user){
				user.setLastLoginTime(user.getUpdateTime());
				user.setUpdateTime(new Date());

			}else{
				user=new User();
				user.setUpdateTime(new Date());
				user.setLastLoginTime(new Date());
				user.setPhone(phone);
			}
			userService.saveInfo(user);
			rd.setCode(200);
			rd.setData(user);
			rd.setMsg("登陆成功");
			String token=JwtUtil.buildJsonByUser(user);
			rd.setToken(token);
		}else{
			rd.setCode(500);
			rd.setMsg("验证码错误");
		}
		jsonWrite2(rd);

	}
}

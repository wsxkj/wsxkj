package com.vue.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.entity.User;
import com.zpj.sys.service.LogInfoService;
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
	@Autowired
	private LogInfoService logService;


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
		LogInfo loginfo=new LogInfo();
        loginfo.setId(UUID.randomUUID().toString());
        loginfo.setCreatetime(new Date());
        loginfo.setType("发送手机验证码");
        loginfo.setDescription("手机验证");
        logService.saveLog(loginfo);
		jsonWrite2(rd);
	}


	@RequestMapping("/checkLogin")
	@ResponseBody
	@ApiOperation(value = "登陆", notes = "登陆", httpMethod = "POST",response=User.class)
	public void checkLogin(@ApiParam(required = false, name = "phone", value = "手机号码")@RequestParam("phone")String phone,
						   @ApiParam(required = false, name = "yzm", value = "验证码")@RequestParam("yzm")String yzm
						   ){
		IdCodeInfo ici=idCodeService.findInfoByPhone(phone);
//		ResultData rd=new ResultData<>();
		Map retMap=new HashMap();
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
			retMap.put("code", 200);
			retMap.put("msg", "登陆成功");
			String token=JwtUtil.buildJsonByUser(user);
			user.setToken(token);
			retMap.put("data", user);
			
			LogInfo loginfo=new LogInfo();
	        loginfo.setId(UUID.randomUUID().toString());
	        loginfo.setUsername(user.getId());
	        loginfo.setCreatetime(new Date());
	        loginfo.setType("登陆成功");
	        loginfo.setDescription(user.toString());
	        logService.saveLog(loginfo);
			System.out.println("phone:"+phone+";token:"+token);
		}else{
			retMap.put("code",500);
			retMap.put("msg", "验证码错误");
		}
		jsonWrite2(retMap);

	}
	
	@RequestMapping("/getNewToken")
	@ResponseBody
	@ApiOperation(value = "根据id更新token", notes = "根据id更新token", httpMethod = "POST",response=User.class)
	public void getNewToken(@ApiParam(required = true, name = "id", value = "id")@RequestParam("id")String id
						   ){
		Map retMap=new HashMap();
		User user=null;
		try{
			user=userService.findById(id);
			retMap.put("code", 200);
			retMap.put("msg", "更新token成功");
			String token=JwtUtil.buildJsonByUser(user);
			user.setToken(token);
			retMap.put("data", user);
		}catch (Exception e) {
			e.printStackTrace();
			retMap.put("code",500);
			retMap.put("msg", "该用户不存在");
		}
		jsonWrite2(retMap);
	}
}

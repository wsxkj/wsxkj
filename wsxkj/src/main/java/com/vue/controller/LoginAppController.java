package com.vue.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.zpj.common.MsgUtil;
import com.zpj.common.PropertyHelper;
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
import com.zpj.common.DateHelper;
import com.zpj.sys.entity.Level;
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.entity.User;
import com.zpj.sys.service.LevelService;
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
	@Autowired
	private LevelService levelService;
	
	

	@RequestMapping("/sendCode")
	@ResponseBody
	@ApiOperation(value = "发送验证码", notes = "发送验证码", httpMethod = "POST")
	public void sendCode(@ApiParam(required = false, name = "phone", value = "手机号码")@RequestParam("phone")String phone){

		
		IdCodeInfo ici=new IdCodeInfo();
		String yzm=MsgUtil.generateYzm();
		ici.setPhone(phone);
		ici.setYzm(yzm);
		ici.setUpdateTime(new Date());
		idCodeService.saveInfo(ici);
		MsgUtil.sendMsg(yzm,phone);
		ResultData rd=new ResultData<>();
		rd.setCode(200);
		rd.setMsg("验证码发送成功");
		LogInfo loginfo=new LogInfo();
        loginfo.setId(UUID.randomUUID().toString());
        loginfo.setCreatetime(new Date());
        loginfo.setType("发送手机验证码:"+yzm);
        loginfo.setDescription("手机验证:"+phone);
        logService.saveLog(loginfo);
		jsonWrite2(rd);
	}


	@RequestMapping("/checkLogin")
	@ResponseBody
	@ApiOperation(value = "登陆", notes = "登陆", httpMethod = "POST",response=User.class)
	public void checkLogin(@ApiParam(required = false, name = "phone", value = "手机号码")@RequestParam("phone")String phone,
						   @ApiParam(required = false, name = "yzm", value = "验证码")@RequestParam("yzm")String yzm
						   ){
		Date tempTime=null;
		Date now=new Date();
		Map retMap=new HashMap();
		//测试上架用户特殊处理
		if(phone!=null&&phone.equalsIgnoreCase("13101995003")&&yzm!=null&&yzm.equalsIgnoreCase("1234")){
			User user=userService.findUserByPhone(phone);
			if(null!=user){
				tempTime=user.getLastLoginTime();
				user.setLastLoginTime(now);
				user.setUpdateTime(now);
				Level lv=levelService.findInfoById(user.getLevel());
				user.setLevelName(lv.getName());
				if(user.getLevel()>0&&user.getLevel()<4){
					Date endtime=user.getEndTime();
					if(now.compareTo(endtime)<0){
						user.setIsExpire("0");
					}else{
						user.setIsExpire("1");
					}
				}else{
					user.setIsExpire("0");
				}
			}else{
				//第一次登陆时设置默认
				user=new User();
				user.setUpdateTime(now);
				tempTime=now;
				user.setLastLoginTime(now);
				user.setPhone(phone);
				//默认普通会员
				user.setLevelId("LEVEL_19822103696886");
				user.setLevel(0);
				user.setLevelName("普通会员");
				user.setIsExpire("0");
				user.setMaxtime(0);
				user.setStartTime(now);
			}
			user.setPassword(MsgUtil.generatePassword(6));
			userService.saveInfo(user);
			retMap.put("code", 200);
			retMap.put("msg", "登陆成功");
			
			
			//用户保存以后重新替换之前的lastlogintime字段保证接口返回的数据正确
			user.setLastLoginTime(tempTime);
			String token=JwtUtil.buildJsonByUser(user);
			user.setToken(token);
			retMap.put("data", user);
			
			LogInfo loginfo=new LogInfo();
	        loginfo.setId(UUID.randomUUID().toString());
	        loginfo.setUsername(user.getPhone());
	        loginfo.setCreatetime(new Date());
	        loginfo.setType("登陆成功");
	        loginfo.setDescription(user.toString());
	        logService.saveLog(loginfo);
			System.out.println("phone:"+phone+";token:"+token);
		}else{
			IdCodeInfo ici=idCodeService.findInfoByPhone(phone);
			Date date1=ici.getUpdateTime();
			long bt=DateHelper.getDiffMinute(now,date1);
			//判断验证码是否过期
			if(bt>0&&bt<=5){
				if(ici.getYzm().equalsIgnoreCase(yzm)){
					User user=userService.findUserByPhone(phone);
					if(null!=user){
						tempTime=user.getLastLoginTime();
						user.setLastLoginTime(now);
						user.setUpdateTime(now);
						Level lv=levelService.findInfoById(user.getLevel());
						user.setLevelName(lv.getName());
						if(user.getLevel()>0&&user.getLevel()<4){
							Date endtime=user.getEndTime();
							if(now.compareTo(endtime)<0){
								user.setIsExpire("0");
							}else{
								user.setIsExpire("1");
							}
						}else{
							user.setIsExpire("0");
						}
					}else{
						//第一次登陆时设置默认
						user=new User();
						user.setUpdateTime(now);
						tempTime=now;
						user.setLastLoginTime(now);
						user.setPhone(phone);
						//默认普通会员
						user.setLevelId("LEVEL_19822103696886");
						user.setLevel(0);
						user.setLevelName("普通会员");
						user.setIsExpire("0");
						user.setMaxtime(0);
						user.setStartTime(now);
					}
					user.setPassword(MsgUtil.generatePassword(6));
					userService.saveInfo(user);
					retMap.put("code", 200);
					retMap.put("msg", "登陆成功");
					
					
					//用户保存以后重新替换之前的lastlogintime字段保证接口返回的数据正确
					user.setLastLoginTime(tempTime);
					String token=JwtUtil.buildJsonByUser(user);
					user.setToken(token);
					retMap.put("data", user);
					
					LogInfo loginfo=new LogInfo();
			        loginfo.setId(UUID.randomUUID().toString());
			        loginfo.setUsername(user.getPhone());
			        loginfo.setCreatetime(new Date());
			        loginfo.setType("登陆成功");
			        loginfo.setDescription(user.toString());
			        logService.saveLog(loginfo);
					System.out.println("phone:"+phone+";token:"+token);
				}else{
					retMap.put("code",500);
					retMap.put("msg", "验证码错误");
				}
			}else{
				retMap.put("code",500);
				retMap.put("msg", "验证码已过期");
			}
		}
		jsonWrite3(retMap);

	}
	
	@RequestMapping("/getNewToken")
	@ResponseBody
	@ApiOperation(value = "根据id更新token", notes = "根据id更新token", httpMethod = "POST",response=User.class)
	public void getNewToken(@ApiParam(required = true, name = "phone", value = "phone")@RequestParam("phone")String phone,
			@ApiParam(required = true, name = "password", value = "password")@RequestParam("password")String password
			   ){
		Map retMap=new HashMap();
		User user=null;
		Date now=new Date();
		try{
			user=userService.findUserByPhoneAndPassword(phone,password);
			if(null!=user){
				if(user.getLevel()>0&&user.getLevel()<4){
					Date endtime=user.getEndTime();
					if(now.compareTo(endtime)<0){
						user.setIsExpire("0");
					}else{
						user.setIsExpire("1");
					}
				}
				userService.saveInfo(user);
				retMap.put("code", 200);
				retMap.put("msg", "更新token成功");
				String token=JwtUtil.buildJsonByUser(user);
				user.setToken(token);
				retMap.put("data", user);
			}else{
				retMap.put("code", 500);
				retMap.put("msg", "更新token失败，密码已过期，请重新登陆");
				retMap.put("data", null);
			}
		}catch (Exception e) {
			e.printStackTrace();
			retMap.put("code",500);
			retMap.put("msg", "该用户不存在");
		}
		jsonWrite2(retMap);
	}
	
	@RequestMapping("/getVersion")
	@ResponseBody
	@ApiOperation(value = "获取app版本信息", notes = "获取app版本信息", httpMethod = "POST",response=ResultData.class)
	public void getVersion(@ApiParam(required = true, name = "version", value = "version，填‘ios’或者‘android’")@RequestParam("version")String version
						   ){
		ResultData rd=new ResultData<>();
		Map map=new HashMap();
		try{
			Map params=PropertyHelper.getPhonePropertiesValue();
			if(version.equalsIgnoreCase("ios")){
				map.put("version", params.get("ios.latestversion"));
				map.put("downloadaddress", params.get("ios.downloadaddress"));
				map.put("forceupdate", params.get("ios.forceupdate"));
			}else if(version.equalsIgnoreCase("android")){
				map.put("version", params.get("android.latestversion"));
				map.put("downloadaddress", params.get("android.downloadaddress"));
				map.put("forceupdate", params.get("android.forceupdate"));
			}
			System.out.println(map);
			rd.setData(map);
			rd.setCode(200);
			rd.setMsg("获取版本信息成功");
		}catch (Exception e) {
			e.printStackTrace();
			rd.setCode(500);
			rd.setMsg("获取版本信息失败");
		}
		jsonWrite2(rd);
	}
	
	
}

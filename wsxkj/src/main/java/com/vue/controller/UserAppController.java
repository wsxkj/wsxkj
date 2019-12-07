package com.vue.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.common.ResultData;
import com.zpj.sys.entity.User;
import com.zpj.sys.service.UserService;

import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
@Controller
@RequestMapping("/app/user")
@Api(value = "/app/user",tags="用户功能接口", description = "用户接口")
public class UserAppController extends BaseController{
	@Autowired
	public UserService userService;
	
	@RequestMapping(value = "/findById", method = RequestMethod.GET)
	@ApiOperation(value = "根据id获取用户信息", notes = "根据id获取用户信息", httpMethod = "GET")
	@ResponseBody
	public void findById(@ApiParam(required = true, name = "id", value = "id") @RequestParam("id") String id){
		//U_20160808102715
		User u=userService.findById(id);
		jsonWrite2(u);
	}
	
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
	public void update(User u){
		userService.saveInfo(u);
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	@ResponseBody
	public void delete(String id){
		userService.deleteUser(id);
	}
	@RequestMapping(value = "/list3", method = RequestMethod.POST)
	@ResponseBody
	public void listJson3(HttpServletRequest request, String pageIndex,String pageSize,String condition){
		
		if(null==pageIndex||pageIndex.equalsIgnoreCase("")){
			pageIndex="1";
		}
		if(null==pageSize||pageSize.equalsIgnoreCase("")){
			pageSize="10";
		}
		MyPage my= userService.findPageData(condition, Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
		jsonWrite2(my);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation(value = "用户列表信息", notes = "根据参数获取用户信息", httpMethod = "GET")
	public void listJson(@RequestBody Map map){
 		String pageIndex=(String)map.get("pageIndex");
		String pageSize=(String)map.get("pageSize");
		String condition=(String)map.get("condition");
		if(null==pageIndex||pageIndex.equalsIgnoreCase("")){
			pageIndex="1";
		}
		if(null==pageSize||pageSize.equalsIgnoreCase("")){
			pageSize="10";
		}
		MyPage my= userService.findPageData(condition, Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
		jsonWrite2(my);
	}
	@RequestMapping(value = "/list1", method = RequestMethod.POST)
	@ResponseBody
	public void listJson1(){
		String pageIndex=null;
		String pageSize=null;
		String condition=null;
		if(null==pageIndex||pageIndex.equalsIgnoreCase("")){
			pageIndex="1";
		}
		if(null==pageSize||pageSize.equalsIgnoreCase("")){
			pageSize="10";
		}
		MyPage my= userService.findPageData(condition, Integer.parseInt(pageIndex), Integer.parseInt(pageSize));
		jsonWrite2(my);
	}
	
	 /**************************v1_1_0版本新接口**开始*****************************/
	@RequestMapping(value = "/v1_1_0/setShop", method = RequestMethod.GET)
	@ApiOperation(value = "设置商店名称", notes = "设置商店名称", httpMethod = "GET")
	@ResponseBody
	public void setShop_v1_1_0(
			@ApiParam(required = true, name = "token", value = "token")@RequestParam("token")String token,
			@ApiParam(required = false, name = "shopName", value = "店铺名称")@RequestParam(value="shopName",required = false)String shopName,
			@ApiParam(required = false, name = "shopUrl", value = "店铺地址")@RequestParam(value="shopUrl",required = false)String shopUrl
			){
		ResultData rd=new ResultData();
	try{	
		User user= getCurrentUser();
		User temp=userService.findById(user.getId());
		temp.setShopName(shopName);
		temp.setShopUrl(shopUrl);
		userService.saveInfo(temp);
		rd.setData(temp);
        rd.setCode(200);
        rd.setMsg("设置成功");
    }catch (JwtException e){
        e.printStackTrace();
        rd.setCode(500);
        rd.setMsg("token转码失败，token过期");
    }catch (Exception e){
        e.printStackTrace();
        rd.setCode(500);
        rd.setMsg("设置失败");
    }
    this.jsonWrite2(rd);
	}
	 /**************************v1_1_0版本新接口**结束*****************************/
}

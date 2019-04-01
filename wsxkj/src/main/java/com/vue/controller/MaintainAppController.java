package com.vue.controller;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zpj.common.BaseController;
import com.zpj.common.DateHelper;
import com.zpj.common.MyPage;
import com.zpj.materials.entity.Maintain;
import com.zpj.materials.service.MaintainService;

import io.swagger.annotations.Api;
@Controller
@RequestMapping("/vue/maintain")
@Api(value = "/vue/maintain",tags="维修功能接口", description = "维修接口")
public class MaintainAppController extends BaseController{
	
	@Autowired
	private MaintainService maintainService;
	
	@RequestMapping("/findById")
	@ResponseBody
	public void findById(String id){
		Maintain mt=maintainService.findById(id);
		Map map=new HashMap();
		map.put("msg", true);
		map.put("data", mt);
		jsonWrite2(map);
	}
	
	@RequestMapping("/saveInfo")
	@ResponseBody
	public void saveInfo(String id,String registertime,String username,String jine,String remark ,String isPayCode,String loginId,String isAdmin){
		Map map=new HashMap();
		try{
			Maintain mt =new Maintain();
			if(null==id||id.equalsIgnoreCase("")){
				mt.setId(UUID.randomUUID().toString());
				mt.setCreatetime(new Date());
			}else{
				mt.setId(id);
			}
			mt.setRegistertime(DateHelper.getDateFromString(registertime+":00", "yyyy-MM-dd HH:mm:ss"));
			mt.setUsername(username);
			mt.setJine(Double.valueOf(jine));
			mt.setRemark(remark);
			if(isPayCode.equalsIgnoreCase("undefined")||isPayCode.equalsIgnoreCase("")){
				mt.setIsPay("0");
			}else{
				mt.setIsPay(isPayCode);
			}
			maintainService.saveInfo(mt);
			map.put("msg", true);
//			map.put("data", mt);
		}catch (Exception e) {
			map.put("msg", false);
			e.printStackTrace();
		}
		
		jsonWrite2(map);
	}
	
	@RequestMapping("/findList")
	@ResponseBody
	public void findList(String datemin,String datemax,String username,String ispay,String remark,String cpage,String pagerow,String loginId,String isAdmin ){
		
		Map param=new HashMap();
		param.put("username", username);
		param.put("starttime", datemin);
		param.put("endtime", datemax);
		param.put("ispay", ispay);
		param.put("remark", remark);
		MyPage pagedata =maintainService.findPageData(param,Integer.parseInt(cpage),Integer.parseInt(pagerow));		
		Map map=new HashMap();
		if(null==pagedata.getData()){
			map.put("list", new ArrayList());
		}else{
			map.put("list", pagedata.getData());
		}
		int tot=(Integer)pagedata.getCount();
		double totalPage=Math.ceil((float)tot/Integer.parseInt(pagerow));
		map.put("totalPage", totalPage);
		map.put("count", tot);
		this.jsonWrite2(map);
	}
	
	@RequestMapping("/delInfoById")
	@ResponseBody
	public void delInfoById(String delId,String loginId,String isAdmin){
		if(null!=delId&&!delId.equalsIgnoreCase("")){
			maintainService.delete(delId);
			Map map =new HashMap();
			map.put("msg", true);
			this.jsonWrite2(map);
		}
	}


//	public UserInfo getCurrentUser(HttpServletRequest request){
//		UserInfo user = (UserInfo)request.getSession().getAttribute("jluserinfo");
//		if(user==null){
//			String id= request.getParameter("loginId");
//			user=jlUserInfoService.findById(Integer.parseInt(id));
//			String isAdmin=request.getParameter("isAdmin");
//			user.setIsAdmin(isAdmin);
//			request.getSession().setAttribute("jluserinfo",user);
//		}
//		return user;
//	}


	@RequestMapping("/findUserNameList")
	@ResponseBody
	public void findUserNameList(String name){
		List list=maintainService.findUserNameList();
		Map map =new HashMap();
		map.put("msg", true);
		map.put("data",list);
		this.jsonWrite2(map);

	}
}

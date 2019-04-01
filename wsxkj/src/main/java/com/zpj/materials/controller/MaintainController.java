package com.zpj.materials.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.materials.entity.Maintain;
import com.zpj.materials.service.MaintainService;

@Controller
@RequestMapping("/maintainInfo")
public class MaintainController extends BaseController{
	@Autowired
	private MaintainService maintainService;

	@RequestMapping("/toList")
	public String toList(){
		return "materials/maintain/list";
	}
	
	/**
	 * @Description (列表)
	 * @title userList
	 * @param param void
	 * @author zpj
	 * @Date 2018年4月6日 上午10:12:51
	 */
	@RequestMapping("/initList")
	@ResponseBody
	public void initList(String param,String starttime,String endtime,int page,int limit){
		Map params=new HashMap();
		params.put("username", param);
		params.put("starttime", starttime);
		params.put("endtime", endtime);
		params.put("ispay", "");
		MyPage pagedata =maintainService.findPageData(params,page,limit);	
		pagedata.setMsg("success");
		this.jsonWrite2(pagedata);
	}	
	/**
	 * @Description (跳转供应商添加编辑页面)
	 * @title add
	 * @param id  主键信息用于查询编辑的对象，如果为空则是新增
	 * @author zpj
	 * @Date 2018年4月6日 上午10:13:09
	 */
	@RequestMapping("/toAdd")
	public String add(String id){
		Maintain sup=new Maintain();
		if(null!=id&&!"".equalsIgnoreCase(id)){
			sup=maintainService.findById(id);
		}else{
			sup=new Maintain();
			sup.setId(UUID.randomUUID().toString());
		}
		setRequstAttribute("info", sup);
		return "materials/maintain/add";
	}
	/**
	 * @Description (保存信息)
	 * @title doAdd
	 * @param 
	 * @author zpj
	 * @Date 2018年4月6日 上午10:15:34
	 */
	@RequestMapping("/doAdd")
	@ResponseBody
	public void doAdd(Maintain sup){
		sup.setCreatetime(new Date());
		maintainService.saveInfo(sup);
		Map map=new HashMap();
		map.put("flag", true);
		jsonWrite2(map);
	}
	
	@RequestMapping("/doDel")
	@ResponseBody
	public void doDel(String ids){
		maintainService.delete(ids);
		Map map=new HashMap();
		map.put("flag", true);
		jsonWrite2(map);
	}
	
}

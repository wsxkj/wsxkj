package com.vue.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.materials.entity.Supplier;
import com.zpj.materials.service.SupplierService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/vue/supplier")
@Api(value = "/vue/supplier",tags="供货商功能接口", description = "供货商接口")
public class SupplierAppController extends BaseController{
	
	@Autowired
	private SupplierService supplierService;
	
	@RequestMapping("/findById")
	@ResponseBody
	public void findById(String id){
		Supplier mt=supplierService.findById(Integer.parseInt(id));
		Map map=new HashMap();
		map.put("msg", true);
		map.put("data", mt);
		jsonWrite2(map);
	}
	
	@RequestMapping("/saveInfo")
	@ResponseBody
	public void saveInfo(String id,String name,String address,String contactname,String remark ,String phone,String loginId,String isAdmin){
		Map map=new HashMap();
		try{
			Supplier mt =new Supplier();
			if(null==id||id.equalsIgnoreCase("")){
			}else{
				mt.setId(Integer.parseInt(id));
			}
			mt.setName(name);
			mt.setAddress(address);
			mt.setContactname(contactname);
			mt.setRemark(remark);
			mt.setPhone(phone);
			mt.setCreatetime(new Date());
			supplierService.saveInfo(mt);
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
	public void findList(String datemin,String datemax,String username,String ispay,String cpage,String pagerow,String loginId,String isAdmin ){
		
		MyPage pagedata =supplierService.findPageData(username,Integer.parseInt(cpage),Integer.parseInt(pagerow));		
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
	/**
	 * 下拉框列表数据
	 * @Title findSupplierList
	 * @author zpj
	 * @time 2018年7月14日 下午4:31:20
	 */
	@RequestMapping("/findSupplierList")
	@ResponseBody
	public void findSupplierList(){
		MyPage pagedata =supplierService.findPageData("",1,100);		
		Map map=new HashMap();
		map.put("msg", true);
		if(null==pagedata.getData()){
			map.put("list", new ArrayList());
		}else{
			map.put("list", pagedata.getData());
			
		}
		this.jsonWrite2(map);
	}
	
	
	@RequestMapping("/delInfoById")
	@ResponseBody
	public void delInfoById(String delId,String loginId,String isAdmin){
		if(null!=delId&&!delId.equalsIgnoreCase("")){
			supplierService.delete(delId);
			Map map =new HashMap();
			map.put("msg", true);
			this.jsonWrite2(map);
		}
	}
}

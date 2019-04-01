package com.zpj.materials.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.materials.service.SupplierService;
import com.zpj.materials.entity.Supplier;


@Controller
@RequestMapping("/supplierInfo")
public class SupplierController  extends BaseController{
	@Autowired
	private SupplierService supplierService;

	@RequestMapping("/toList")
	public String toList(){
		return "materials/supplier/list";
	}
	
	/**
	 * @Description (供应商列表)
	 * @title userList
	 * @param param void
	 * @author zpj
	 * @Date 2018年4月6日 上午10:12:51
	 */
	@RequestMapping("initList")
	@ResponseBody
	public void initList(String param,int page,int limit){
		MyPage pagedata =supplierService.findPageData(param,page,limit);		
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
		Supplier sup=new Supplier();
		if(null!=id&&!"".equalsIgnoreCase(id)){
			sup=supplierService.findById(Integer.parseInt(id));
		}else{
			sup=new Supplier();
		}
		setRequstAttribute("info", sup);
		return "materials/supplier/add";
	}
	/**
	 * @Description (保存供应商信息)
	 * @title doAdd
	 * @param 
	 * @author zpj
	 * @Date 2018年4月6日 上午10:15:34
	 */
	@RequestMapping("doAdd")
	@ResponseBody
	public void doAdd(Supplier sup){
		supplierService.saveInfo(sup);
		Map map=new HashMap();
		map.put("flag", true);
		jsonWrite2(map);
	}
	
	@RequestMapping("/doDel")
	@ResponseBody
	public void doDel(String ids){
		supplierService.delete(ids);
		Map map=new HashMap();
		map.put("flag", true);
		jsonWrite2(map);
	}
	/**
	 * 初始化供应商select
	 * @Title initSupplierSelect
	 * @author zpj
	 * @time 2018年4月28日 下午2:04:49
	 */
	@RequestMapping("/initSupplierSelect")
	@ResponseBody
	public void initSupplierSelect(){
		List list=supplierService.findSupplier();
		Map map=new HashMap();
		map.put("flag", true);
		map.put("list", list);
		jsonWrite2(map);
	}
	
}

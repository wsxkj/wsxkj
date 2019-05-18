package com.zpj.materials.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.common.ResourceCodeUtil;
import com.zpj.materials.service.GoodsService;
import com.zpj.sys.entity.DictionaryType;
import com.zpj.materials.entity.Goods;

@Controller
@RequestMapping("/goodsInfo")
public class GoodsController extends BaseController{
	@Autowired
	public GoodsService goodsService;
	
	@RequestMapping("/toList")
	public String toList(){
		return "materials/goods/list";
	}
	/**
	 * @Description (初始化列表)
	 * @title userList
	 * @param param void
	 * @author zpj
	 * @Date 2018年4月6日 上午10:12:51
	 */
	@RequestMapping("initList")
	@ResponseBody
	public void initList(String param,int page,int limit){
		Map param1=new HashMap();
		param1.put("name", param);
		MyPage pagedata =goodsService.findPageData(param1,page,limit);		
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
		Goods sup=new Goods();
		if(null!=id&&!"".equalsIgnoreCase(id)){
			sup=goodsService.findById(id);
		}else{
			sup=new Goods();
			sup.setId(UUID.randomUUID().toString());
		}
		setRequstAttribute("info", sup);
		return "materials/goods/add";
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
	public void doAdd(Goods sup){
		goodsService.saveInfo(sup);
		Map map=new HashMap();
		map.put("flag", true);
		jsonWrite2(map);
	}
	
	@RequestMapping("/doDel")
	@ResponseBody
	public void doDel(String ids){
//		goodsService.delete(ids);
		Map map=new HashMap();
		map.put("flag", true);
		jsonWrite2(map);
	}
	
	@RequestMapping("initFenlei")
	@ResponseBody
	public void initFenlei(String type){
		List<DictionaryType> list=ResourceCodeUtil.getTypeList(type);
		Map map=new HashMap();
		map.put("flag", true);
		if(list!=null&&list.size()>1){
			map.put("list", list);
		}else{
			map.put("list", new ArrayList<DictionaryType>());
		}
		jsonWrite2(map);
	}
	
}

package com.vue.controller;

import java.util.ArrayList;
import java.util.Date;
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
import com.zpj.materials.entity.Goods;
import com.zpj.materials.entity.Supplier;
import com.zpj.materials.service.GoodsService;
import com.zpj.sys.entity.DictionaryType;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/vue/goods")
@Api(value = "/vue/goods",tags="商品功能接口", description = "商品接口")
public class GoodsAppController extends BaseController{
	
	
	@Autowired
	public GoodsService goodsService;
	
	@RequestMapping("/findById")
	@ResponseBody
	public void findById(String id){
		Goods mt=goodsService.findById(id);
		String[] types=mt.getGoodsType().split(",");
		String tn="";
		for(int k=0;k<types.length;k++){
			if(k>0){
				tn+="-";
			}
			tn+=ResourceCodeUtil.dpspType.get(types[k]);
		}
		mt.setGoodsTypeName(tn);
		Map map=new HashMap();
		map.put("msg", true);
		map.put("data", mt);
		jsonWrite2(map);
	}
	@RequestMapping("/delById")
	@ResponseBody
	public void delById(String delId){
		goodsService.delete(delId);
		Map map=new HashMap();
		map.put("msg", true);
		jsonWrite2(map);
	}
	
	@RequestMapping("/saveInfo")
	@ResponseBody
	public void saveInfo(String id,String name,String type,String unit,String purchasePrice,String sellingPrice,String remark,String supplierName,String supplierId,String goodsType,String loginId,String isAdmin){
		Goods goods=new Goods();
		if(id.equalsIgnoreCase("")){
			goods.setId(UUID.randomUUID().toString());
		}else{
			goods.setId(id);
		}
		goods.setCreatetime(new Date());
		goods.setGoodsType(goodsType);
		goods.setName(name);
		goods.setPurchasePrice(Double.parseDouble(purchasePrice));
		goods.setSellingPrice(Double.parseDouble(sellingPrice));
		goods.setRemark(remark);
		goods.setSupplierId(supplierId);
		goods.setSupplierName(supplierName);
		goods.setType(type);
		goods.setUnit(unit);
		goodsService.saveInfo(goods);
		
		Map map=new HashMap();
		map.put("msg", true);
		jsonWrite2(map);
	}
	
	
	@RequestMapping("/findList")
	@ResponseBody
	public void findList(String goodsName,String supplierId,String goodsType,String cpage,String pagerow,String loginId,String isAdmin ){
		Map param=new HashMap();
		param.put("name", goodsName);
		param.put("supplierId", supplierId);
		param.put("goodsType", goodsType);
		MyPage pagedata =goodsService.findPageData(param,Integer.parseInt(cpage),Integer.parseInt(pagerow));		
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
	
	@RequestMapping("/initGoodsType")
	@ResponseBody
	public void initGoodsType(){
		List<Map> list=ResourceCodeUtil.dpsp;
		this.jsonWrite2(list);
	}
	
}

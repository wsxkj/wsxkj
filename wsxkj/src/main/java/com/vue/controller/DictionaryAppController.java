package com.vue.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zpj.common.BaseController;
import com.zpj.common.MyPage;
import com.zpj.common.ResourceCodeUtil;
import com.zpj.common.ResultData;
import com.zpj.materials.service.GoodsService;
import com.zpj.sys.entity.DictionaryItem;
import com.zpj.sys.entity.DictionaryType;
import com.zpj.sys.service.DictionaryService;

import io.swagger.annotations.Api;

@Controller
@RequestMapping("/vue/dictionary")
@Api(value = "/vue/dictionary",tags="字典功能接口", description = "字典接口")
public class DictionaryAppController extends BaseController{
	@Autowired
	public DictionaryService dictionaryService;
	@Autowired
	public GoodsService goodsService;
	
	@RequestMapping("/initThreeType")
	@ResponseBody
	public void initGoodsType(){
		Map temp=new HashMap();
		temp.put("first", ResourceCodeUtil.first);
		temp.put("second", ResourceCodeUtil.second);
		temp.put("third", ResourceCodeUtil.third);
		ResultData<Map> rd=new ResultData<Map>(temp,"查询成功", true);
		this.jsonWrite2(rd);
	}
	
	@RequestMapping("/getFirstType")
	@ResponseBody
	public void getFirstType(){
		Map map=new HashMap();
		map.put("first", ResourceCodeUtil.first);
		Map param=new HashMap();
		map.put("second", ResourceCodeUtil.second);
		ResultData<Map> rd=new ResultData<Map>(map,"查询成功", true);
		this.jsonWrite2(rd);
	}
	
	@RequestMapping("/getSecondType")
	@ResponseBody
	public void getSecondType(String code){
		String showName=ResourceCodeUtil.dpspType.get(code);
		Map map=new HashMap();
		List<Map> third=new ArrayList<>() ;
		StringBuilder sb=new StringBuilder(50);
		for(int k=0;k<ResourceCodeUtil.third.size();k++){
			sb=new StringBuilder(50);
			sb.append(ResourceCodeUtil.third.get(k).get("pvalue")+"");
			if(sb.toString().equalsIgnoreCase(code)){
				third.add(ResourceCodeUtil.third.get(k));
			}
		}
		sb=null;
		map.put("third", third);
		map.put("title",showName);
		Map param=new HashMap();
		MyPage pagedata =goodsService.findPageData(param,1,500);		
		if(null==pagedata.getData()){
			map.put("goodslist", new ArrayList());
		}else{
			map.put("goodslist", pagedata.getData());
		}
		int tot=(Integer)pagedata.getCount();
		ResultData<Map> rd=new ResultData<Map>(map,"查询成功", true);
		this.jsonWrite2(rd);
	}
	
	@RequestMapping("/getThirdType")
	@ResponseBody
	public void getThirdType(){
		Map map=new HashMap();
		map.put("third", ResourceCodeUtil.third);
		Map param=new HashMap();
		MyPage pagedata =goodsService.findPageData(param,1,500);		
		if(null==pagedata.getData()){
			map.put("goodslist", new ArrayList());
		}else{
			map.put("goodslist", pagedata.getData());
		}
		int tot=(Integer)pagedata.getCount();
		ResultData<Map> rd=new ResultData<Map>(map,"查询成功", true);
		this.jsonWrite2(rd);
	}
	
	
	@RequestMapping("/saveInfo")
	@ResponseBody
	public void saveInfo(String pCode,String pName,String code,String name,String bCode,String orderNum){
		DictionaryType dt=null;
		if(null!=bCode&&!"".equalsIgnoreCase(bCode)){
			dt= dictionaryService.findDicTypeByCode(bCode);
		}
		if(null!=dt){
			dt.setOrderNum(Integer.parseInt(orderNum));
			dt.setTypeCode(code);
			dt.setTypeName(name);
			DictionaryType temp=dictionaryService.findDicTypeByCode(pCode);
			dt.setParentTypeid(temp.getId());
//			dt.setParentTypeName(temp.getTypeName());
			dictionaryService.saveDictionaryTypeByPhone(dt,bCode);
		}else{
			DictionaryType cdt=new DictionaryType();
			cdt.setOrderNum(Integer.parseInt(orderNum));
			cdt.setTypeCode(code);
			cdt.setTypeName(name);
			DictionaryType temp=dictionaryService.findDicTypeByCode(pCode);
			cdt.setParentTypeid(temp.getId());
//			cdt.setParentTypeName(temp.getTypeName());
			dictionaryService.saveDictionaryTypeByPhone(cdt,bCode);
		}
		
		ResultData<Map> rd=new ResultData<Map>(null,"保存成功", true);
		this.jsonWrite2(rd);
	}
	
	
	/**
	 * @Description (根据编码删除数据)
	 * @title delByCode
	 * @param code void
	 * @author zpj
	 * @Date 2018年12月9日 上午11:49:50
	 */
	@RequestMapping("/delByCode")
	@ResponseBody
	public void delByCode(String code){
		DictionaryType dt=	dictionaryService.findDicTypeByCode(code);
		if(null!=dt){
			dictionaryService.delDictionaryType(String.valueOf(dt.getId()));
		}
		ResultData<Map> rd=new ResultData<Map>(null,"删除成功", true);
		this.jsonWrite2(rd);
	}
	/**
	 * @Description (直接查询sys_dictionary_type展示数据列表)
	 * @title findDataListInDataBaseByTypeCode
	 * @param
	 * @author zpj
	 * @Date 2018年12月9日 上午11:28:13
	 */
	@RequestMapping("/findDataListInDataBaseByTypeCode")
	@ResponseBody
	public void findDataListInDataBaseByTypeCode(String typeCode){
		List list=dictionaryService.findChlidrenDictionaryTypeByCode(typeCode);
		ResultData<List> rd=new ResultData<List>(list,"查询成功", true);
		this.jsonWrite2(rd);
	}
	
	@RequestMapping("/findInfoByTypeCode")
	@ResponseBody
	public void findInfoByTypeCode(String typeCode){
		DictionaryType dt=dictionaryService.findDicTypeByCode(typeCode);
		ResultData<DictionaryType> rd=new ResultData<DictionaryType>(dt,"查询成功", true);
		this.jsonWrite2(rd);
	}
	
	/**
	 * 更新缓存
	 * @Title updateCache
	 * @author zpj
	 * @time 2018年12月11日 下午4:27:35
	 */
	@RequestMapping("/updateCache")
	@ResponseBody
	public void updateCache(){
		try{
			dictionaryService.updateDictionaryCache();
			ResultData rd=new ResultData<>(null,"更新缓存成功", true);
			this.jsonWrite2(rd);
		}catch (Exception e) {
			e.printStackTrace();
			ResultData rd=new ResultData<>(null,"更新缓存失败", true);
			this.jsonWrite2(rd);
		}
		
		
	}
}

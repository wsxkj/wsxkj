package com.zpj.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.chainsaw.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.mysql.fabric.xmlrpc.base.Array;
import com.zpj.sys.entity.DictionaryItem;
import com.zpj.sys.entity.DictionaryType;
import com.zpj.sys.service.DictionaryService;

import net.sf.ehcache.store.disk.ods.AATreeSet;
import redis.clients.jedis.Jedis;

/**
    * @ClassName: 字典资源初始化
    * @Description: TODO(这里用一句话描述这个类的作用)
    * @author zpj
    * @date 2018年7月6日
    *
    */
    
public class ResourceCodeUtil implements ApplicationListener<ContextRefreshedEvent>{
	/**
	 * 根据code查询子节点下面的信息列表List<DictionaryType>
	 */
	public static Map<String,List<DictionaryType>> typeMap=new HashMap();
	public static Map<String,List<DictionaryItem>> itemMap=new HashMap();
	
	
	    /**
	    * @Fields dpspType : 可以根据code获取中文值
	    */
	    
	public static Map<String,String> dpspType=new HashMap();
	
	
	    /**
	    * @Fields dpsp : 手机上三级下拉框的对象数据
	    */
	    
	public static List<Map> dpsp=new ArrayList<>();

	//jedis对象
	public Jedis jedis=null;
	
	    /**
	    * @Fields fst : 分别获取三层级的数据
	    */
	    
	public static List<Map> first=new ArrayList<>() ;
	public static List<Map> second=new ArrayList<>() ;
	public static List<Map> third=new ArrayList<>() ;
	
	@Autowired
	public DictionaryService dictionaryService;
	
	
	
	public static List<DictionaryType> findSon(DictionaryType cdt,List<DictionaryType> list){
		List<DictionaryType> temp=new ArrayList<DictionaryType>();
//		System.out.println(cdt.getId()+">>>>");
		for(int k=0;k<list.size();k++){
			if(list.get(k).getParentTypeid().intValue()==cdt.getId().intValue()){
				temp.add(list.get(k));
				typeMap.put(list.get(k).getTypeCode(),findSon(list.get(k),list));
			}
		}
		return temp;
	}
	
	public void initDictionary() {
		jedis=RedisUtil.getJedis();
		System.out.println("----初始化字典数据-----");
		itemMap.clear();
		typeMap.clear();
		dpspType.clear();
		dpsp.clear();
		first.clear();
		second.clear();
		third.clear();
		
		/********初始化所有type信息**开始*********/
		List<DictionaryType> typeList=dictionaryService.findAllDictionaryType();
		List<DictionaryType> topList=new ArrayList<DictionaryType>();
		if(null==typeList||typeList.size()==0){
			return ;
		}
		for(int i=0;i<typeList.size();i++){
			if(typeList.get(i).getParentTypeid()==1){
				topList.add(typeList.get(i));
				typeMap.put(typeList.get(i).getTypeCode(),findSon(typeList.get(i),typeList));
			}
		}
		typeMap.put("ROOT", topList);
		/********初始化所有type信息**结束*********/
		
		
		/********初始化商品分类信息**开始*********/
		initGoodsType("");
		for(int q=0;q<typeList.size();q++){
			dpspType.put(typeList.get(q).getTypeCode(),typeList.get(q).getTypeName());
		}
		jedis.hmset("dpspType",dpspType);
		/********初始化商品分类信息**结束*********/
		
		/********初始化字典值信息**开始**************************/
		DictionaryType dt;
		for(int j=0;j<typeList.size();j++){
			dt=typeList.get(j);
			itemMap.put(dt.getTypeCode(), new ArrayList<DictionaryItem>());
		}
		List<DictionaryItem> itemList=dictionaryService.findAllDictionaryItem();
		List<DictionaryItem> temp;
		if(null!=itemList&&itemList.size()>0){
			for(int m=0;m<itemList.size();m++){
				temp=itemMap.get(itemList.get(m).getTypeCode());
				temp.add(itemList.get(m));
				itemMap.put(itemList.get(m).getTypeCode(), temp);
			}
		}
		/********初始化字典值信息**结束**************************/
		
		
		
	}
	
	
	
	public static Map<String,String> getTypeListByTypeCode(String typeCode){
		List<DictionaryType> list=typeMap.get(typeCode);
		Map<String,String> retMap=new HashMap();
		for(int k=0;k<list.size();k++){
			retMap.put(list.get(k).getTypeCode(),list.get(k).getTypeName());
		}
		return retMap;
	}
	/**
	 * 返回 type 的list
	 * @Title getTypeList
	 * @param typeCode
	 * @return
	 * @author zpj
	 * @time 2018年7月19日 下午5:11:48
	 */
	public static List<DictionaryType>  getTypeList(String typeCode){
		return typeMap.get(typeCode);
	}
	
	
	/**
	 * 手机上三级下拉框需要用到的数据店铺商品的数据
	 * @Title initGoodsType
	 * @param type
	 * @author zpj
	 * @time 2018年8月1日 上午9:57:55
	 */
	public static void initGoodsType(String type){
		if(type.equalsIgnoreCase("")){
			type="DPSP";
		}
		List<DictionaryType> list=getTypeList(type);
		List<DictionaryType> temp1;
		List<Map> retList=new ArrayList<>();
		Map<String,Object> map1=new HashMap();
		Map fMap;
		for(int i=0;i<list.size();i++){
			map1=new HashMap();
			map1.put("value",list.get(i).getTypeCode());
			map1.put("text", list.get(i).getTypeName());
			//设置1层的数据
			fMap=new HashMap();
			fMap.put("value", list.get(i).getTypeCode());
			fMap.put("text", list.get(i).getTypeName());
			fMap.put("pvalue", "DPSP");
			fMap.put("ptext", "店铺商品");
			first.add(fMap);
			
			
			temp1=getTypeList(list.get(i).getTypeCode());
			Map<String,Object> map2=new HashMap();
			List<Map> children1=new ArrayList<Map>();
			List<DictionaryType> temp2;
			Map sMap;
			for(int j=0;j<temp1.size();j++){
				map2=new HashMap();
				List<Map> children2=new ArrayList();
				map2.put("value",temp1.get(j).getTypeCode());
				map2.put("text", temp1.get(j).getTypeName());
				//设置2层的数据
				sMap=new HashMap();
				sMap.put("value", temp1.get(j).getTypeCode());
				sMap.put("text", temp1.get(j).getTypeName());
				sMap.put("pvalue", list.get(i).getTypeCode());
				sMap.put("ptext", list.get(i).getTypeName());
				second.add(sMap);
				
				
				temp2=getTypeList(temp1.get(j).getTypeCode());
				Map<String,String> map3=new HashMap();
				Map tMap;
				for(int k=0;k<temp2.size();k++){
					map3=new HashMap(); 
					map3.put("value",temp2.get(k).getTypeCode());
					map3.put("text", temp2.get(k).getTypeName());
					children2.add(map3);
					//设置3层的数据
					tMap=new HashMap();
					tMap.put("value", temp2.get(k).getTypeCode());
					tMap.put("text", temp2.get(k).getTypeName());
					tMap.put("pvalue", temp1.get(j).getTypeCode());
					tMap.put("ptext", temp1.get(j).getTypeName());
					third.add(tMap);
					
				}
				map2.put("children", children2);
				children1.add(map2);
			}
			map1.put("children", children1);
			dpsp.add(map1);
		}
//		System.out.println(dpsp);
//		System.out.println(first);
//		System.out.println(second);
//		System.out.println(third);
		/*****************/
//		System.out.println(typeMap);
	}
	
	

	public static Map<String,String> getItemByTypeCode(String typeCode){
		List<DictionaryItem> list=itemMap.get(typeCode);
		Map<String,String> retMap=new HashMap();
		for(int k=0;k<list.size();k++){
			retMap.put(list.get(k).getItemCode(),list.get(k).getItemName());
		}
		return retMap;
	}

@Override
public void onApplicationEvent(ContextRefreshedEvent event) {
	
	if(event.getApplicationContext().getParent() == null){//root application context 没有parent，他就是老大.  
        //需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。  
		initDictionary();
	}  
}
}

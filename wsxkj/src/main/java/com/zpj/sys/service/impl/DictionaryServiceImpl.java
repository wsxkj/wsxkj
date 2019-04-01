package com.zpj.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.common.PingyinTool;
import com.zpj.common.ResourceCodeUtil;
import com.zpj.materials.entity.Goods;
import com.zpj.sys.entity.DictionaryItem;
import com.zpj.sys.entity.DictionaryType;
import com.zpj.sys.service.DictionaryService;
@Service
public class DictionaryServiceImpl implements DictionaryService {
	
	@Autowired
	private BaseDao<DictionaryType> dtDao;
	@Autowired
	private BaseDao<DictionaryItem> diDao;
	
	private String tablename_item="sys_dictionary_item";
	
	
	public String findJson(Map<String, String> params) {
		StringBuffer s=new StringBuffer();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from sys_dictionary_type a where  parentTypeid ='"+params.get("id")+"' order by orderNum asc   ");
		List<Map> list=dtDao.findMapObjBySql(sql.toString(), null, 1, 100);
		if(list!=null&&list.size()>0){
			s.append("[");
			for(int i=0;i<list.size();i++){
				if(i>0){
					s.append(",");
				}
				s.append("{\"id\":\""+list.get(i).get("id")+"\",\"text\":\""+list.get(i).get("typeName")+"("+list.get(i).get("typeCode")+")\",\"attributes\":{\"pk\":\""+list.get(i).get("id")+"\"},");
				String id=String.valueOf((list.get(i).get("id")));
				Map temp= new HashMap();
				temp.put("id", id);
				List tlist=findChildMenuByPId(temp);
				if(tlist.size()>0){
					s.append("\"state\":\"closed\"}");
				}else{
					s.append("\"state\":\"open\"}");
				}
			}
			s.append("]");
			return s.toString();
		}else{
		  return "[]";
		}
	}
	
	public List<DictionaryType> findAllDictionaryType(){
		Map<String,Object> condition=new HashMap();
		condition.put("parentTypeid-self", "parentTypeid<>0");
		Map<String,Object> px=new HashMap();
		px.put("orderNum", "asc");
		MyPage mp= dtDao.findPageDateSqlT("sys_dictionary_type", condition, px, 1, 500, DictionaryType.class);
		return (List<DictionaryType>)mp.getData();
	}
	
	@Override
	public String findTopJson() {
		StringBuffer s=new StringBuffer();
		//s.append("[{\"id\":1,\"text\":\"My Documents\",\"children\":[{\"id\":11,\"text\":\"Photos\",\"state\":\"closed\",\"children\":[{\"id\":111,\"text\":\"Friend\"},{\"id\":112,\"text\":\"Wife\"},{\"id\":113,\"text\":\"Company\"}]},{\"id\":12,\"text\":\"Program Files\",\"children\":[{\"id\":121,\"text\":\"Intel\"},{\"id\":122,\"text\":\"Java\",\"attributes\":{\"p1\":\"Custom Attribute1\",\"p2\":\"Custom Attribute2\"}},{\"id\":123,\"text\":\"Microsoft Office\"},{\"id\":124,\"text\":\"Games\",\"checked\":true}]},{\"id\":13,\"text\":\"index.html\"},{\"id\":14,\"text\":\"about.html\"},{\"id\":15,\"text\":\"welcome.html\"}]}]");
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from sys_dictionary_type a where  parentTypeid =0 order by orderNum asc   ");
		List<Map> list=dtDao.findMapObjBySql(sql.toString(), null, 1, 100);
		s.append("[");
		for(int i=0;i<list.size();i++){
			if(i>0){
				s.append(",");
			}
			s.append("{\"id\":\""+list.get(i).get("id")+"\",\"text\":\""+list.get(i).get("typeName")+"\",\"attributes\":{\"pk\":\""+list.get(i).get("id")+"\"},\"state\":\"closed\"}");
		}
		s.append("]");
		return s.toString();
	}
	
	public List<Map> findChildMenuByPId(Map params) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from sys_dictionary_type a where  parentTypeid ='"+params.get("id")+"' order by orderNum asc   ");
		List<Map> list=dtDao.findMapObjBySql(sql.toString(), null, 1, 100);
		return list;
	}
	
	public DictionaryType findDicTypeId(String id){
		if(null!=id&&!id.equalsIgnoreCase("")){
			return dtDao.get(Integer.parseInt(id),DictionaryType.class);
		}else{
			return null;
		}
	}
	
	
	/**
	 * 根据code查询对象
	 * @Title findDicTypeByCode
	 * @param code
	 * @return
	 * @author zpj
	 * @time 2018年7月16日 下午4:42:00
	 */
	public DictionaryType findDicTypeByCode(String code){
		List<DictionaryType> list=dtDao.findBySqlT("select * from sys_dictionary_type where typeCode='"+code+"'", DictionaryType.class);
		if(null!=list&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
public void saveDictionaryTypeByPhone(DictionaryType dt,String oldType){
		try{
			if(null==dt.getId()||dt.getId()==0){
				dtDao.add(dt);
			}else{
				dtDao.merge(dt, String.valueOf(dt.getId()));
//				if(!oldType.equalsIgnoreCase("")&&!oldType.equalsIgnoreCase(dt.getTypeCode())){
//					dtDao.executeSql(" update "+tablename_item+" set typeCode='"+dt.getTypeCode()+"' where typeCode='"+oldType+"'");
//				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}
	public void saveDictionaryType(DictionaryType dt){
		
		try{
			if(null==dt.getId()){
				dtDao.add(dt);
			}else{
				DictionaryType temp=findDicTypeId(String.valueOf(dt.getId()));
				if(null!=temp){
					String oldType=temp.getTypeCode();
					dtDao.merge(dt, String.valueOf(dt.getId()));
					if(!oldType.equalsIgnoreCase(dt.getTypeCode())){
						dtDao.executeSql(" update "+tablename_item+" set typeCode='"+dt.getTypeCode()+"' where typeCode='"+oldType+"'");
					}
				}else{
					dtDao.add(dt);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
	}
	
	public void delDictionaryType(String id){
		if(null!=id){
			DictionaryType temp=findDicTypeId(String.valueOf(id));
			dtDao.delete(temp);
			dtDao.executeSql(" delete from sys_dictionary_type where parentTypeid='"+id+"'");
		}
	}
	
	
	/***************item******************/
	
	public MyPage findPageData(String typeCode, Integer page, Integer limit){
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("typeCode-eq", typeCode);
		Map px=new HashMap();
	    px.put("itemOrder", "asc");
		return diDao.findPageDateSqlT(tablename_item, param,px , page, limit, DictionaryItem.class);
	}
	
	public List<DictionaryItem> findAllDictionaryItem(){
		Map<String,Object> param=new HashMap<String,Object>();
		Map px=new HashMap();
	    px.put("itemOrder", "asc");
	    MyPage mp=diDao.findPageDateSqlT(tablename_item, param,px , 1, 1000, DictionaryItem.class);
		return (List<DictionaryItem>)mp.getData();
	}
	
	public void saveItem(DictionaryItem di){
		if(null!=di.getId()&&di.getId()!=1){
			diDao.merge(di, String.valueOf(di.getId()));
		}else{
			diDao.add(di);
		}
	}
	
	public DictionaryItem findItem(String id){
		return diDao.get(Integer.parseInt(id), DictionaryItem.class);
	}
	
	public void delItem(String id){
		String[] ids=id.split(",");
		StringBuffer sb=new StringBuffer(500);
		for (int m=0;m<ids.length;m++) {
			if(m>0){
				sb.append(",");
			}
			sb.append("'"+ids[m]+"'");
		}
		diDao.executeSql(" delete from "+tablename_item+" where id in ("+sb+")");
	}

	@Override
	public List findChlidrenDictionaryTypeByCode(String code) {
		List<Map> list1=dtDao.findMapObjBySqlNoPage("select t.*,s.typeName as parentTypeName from sys_dictionary_type t INNER JOIN sys_dictionary_type s on t.parentTypeid=s.id where s.typeCode='"+code+"' ORDER BY t.orderNum asc ");
//		List<DictionaryType> list=dtDao.findBySqlT("select t.*,s.typeName as parentTypeName from sys_dictionary_type t INNER JOIN sys_dictionary_type s on t.parentTypeid=s.id where s.typeCode='"+code+"'", DictionaryType.class);
		return list1;
	}
	
	public void updateDictionaryCache(){
		System.out.println("----初始化字典数据-----");
		ResourceCodeUtil.itemMap.clear();
		ResourceCodeUtil.typeMap.clear();
		ResourceCodeUtil.dpsp.clear();
		ResourceCodeUtil.dpspType.clear();
		ResourceCodeUtil.first.clear();
		ResourceCodeUtil.second.clear();
		ResourceCodeUtil.third.clear();
		/********初始化所有type信息**开始*********/
		List<DictionaryType> typeList=this.findAllDictionaryType();
		List<DictionaryType> topList=new ArrayList<DictionaryType>();
		if(null==typeList||typeList.size()==0){
			return ;
		}
		for(int i=0;i<typeList.size();i++){
			if(typeList.get(i).getParentTypeid()==1){
				topList.add(typeList.get(i));
				ResourceCodeUtil.typeMap.put(typeList.get(i).getTypeCode(),ResourceCodeUtil.findSon(typeList.get(i),typeList));
			}
		}
		ResourceCodeUtil.typeMap.put("ROOT", topList);
		
		/********初始化所有type信息**结束*********/
		
		
		/********初始化商品分类信息**开始*********/
		ResourceCodeUtil.initGoodsType("");
		for(int q=0;q<typeList.size();q++){
			ResourceCodeUtil.dpspType.put(typeList.get(q).getTypeCode(),typeList.get(q).getTypeName());
		}
		/********初始化商品分类信息**结束*********/
		
		/********初始化字典值信息**开始**************************/
		DictionaryType dt;
		for(int j=0;j<typeList.size();j++){
			dt=typeList.get(j);
			ResourceCodeUtil.itemMap.put(dt.getTypeCode(), new ArrayList<DictionaryItem>());
		}
		List<DictionaryItem> itemList=this.findAllDictionaryItem();
		List<DictionaryItem> temp;
		if(null!=itemList&&itemList.size()>0){
			for(int m=0;m<itemList.size();m++){
				temp=ResourceCodeUtil.itemMap.get(itemList.get(m).getTypeCode());
				temp.add(itemList.get(m));
				ResourceCodeUtil.itemMap.put(itemList.get(m).getTypeCode(), temp);
			}
		}
		/********初始化字典值信息**结束**************************/
	}
	
}

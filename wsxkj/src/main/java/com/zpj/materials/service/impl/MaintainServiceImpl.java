package com.zpj.materials.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zpj.common.BaseDao;
import com.zpj.common.DateHelper;
import com.zpj.common.MyPage;
import com.zpj.common.aop.Log;
import com.zpj.materials.entity.Maintain;
import com.zpj.materials.service.MaintainService;
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.service.LogInfoService;
@Service
public class MaintainServiceImpl implements MaintainService {

	@Autowired
	private BaseDao<Maintain> maintainDao;
	
	private String tablename="jl_material_maintain_info";
	@Autowired
	private BaseDao<LogInfo> logDao;
	
	@Override
	public MyPage findPageData(Map params, Integer page, Integer limit) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("username-like", params.get("username"));
		param.put("remark-like", params.get("remark"));
		if(null!=params.get("starttime")&&!"".equalsIgnoreCase((String)params.get("starttime"))){
			param.put("starttime-self", " registertime >='"+params.get("starttime")+"' ");
		}
		if(null!=params.get("endtime")&&!"".equalsIgnoreCase((String)params.get("endtime"))){
			param.put("endtime-self", " registertime <='"+params.get("endtime")+"' ");
		}
		if(null!=params.get("ispay")&&!"".equalsIgnoreCase((String)params.get("ispay"))){
			param.put("isPay-eq", params.get("ispay"));
		}
		
		Map px=new HashMap();
	    px.put("registertime", "desc");
//	    px.put("createtime", "desc");
		return maintainDao.findPageDateSqlT(tablename, param,px , page, limit, Maintain.class);
	}

//	@Log(type="保存",remark="保存修理信息")
	public void saveInfo(Maintain info) {
		if(null!=info.getId()&&!"".equalsIgnoreCase(info.getId())){
			Maintain user=this.findById(info.getId());
			if(null!=user){
				info.setCreatetime(user.getCreatetime());
				maintainDao.merge(info,info.getId());
			}else{
				maintainDao.add(info);
			}
		}else{
			maintainDao.add(info);
		}
		LogInfo loginfo=new LogInfo();
		loginfo.setId(UUID.randomUUID().toString());
    	loginfo.setUsername("朱培军");
    	loginfo.setCreatetime(new Date());
    	loginfo.setType("保存维修记录");
    	loginfo.setDescription(info.toString());
    	logDao.add(loginfo);
	}

//	@Log(type="删除",remark="删除维修信息")
	public void delete(String deleteID) {
		String[] ids=deleteID.split(",");
		StringBuffer sb=new StringBuffer(500);
		for (int m=0;m<ids.length;m++) {
			if(m>0){
				sb.append(",");
			}
			sb.append("'"+ids[m]+"'");
		}
		List list=maintainDao.findBySqlT(" select *  from "+tablename+" where id in ("+sb+")", Maintain.class);
		Maintain mt=null;
		if(null!=list&&list.size()>0){
			for(int m=0;m<list.size();m++){
				mt=(Maintain)list.get(m);
				LogInfo loginfo=new LogInfo();
				loginfo.setId(UUID.randomUUID().toString());
				loginfo.setUsername("朱培军");
				loginfo.setCreatetime(new Date());
				loginfo.setType("删除维修记录");
				loginfo.setDescription(mt.toString());
				logDao.add(loginfo);
			}
			maintainDao.executeSql(" delete from "+tablename+" where id in ("+sb+")");
		}
	}
	public List findUserNameList(){
		List<Map> list=maintainDao.findMapObjBySqlNoPage("select DISTINCT(username) as name from "+tablename+" GROUP BY username ");
		List list1=new ArrayList();
		for (int i = 0; i <list.size() ; i++) {
			list1.add(list.get(i).get("name"));
		}
		return list1;
	}
	public Maintain findById(String id) {
		return maintainDao.get(id,Maintain.class);
	}	
}

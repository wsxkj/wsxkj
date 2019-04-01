package com.zpj.materials.service.impl;

import java.util.*;

import com.zpj.materials.entity.Goods;
import com.zpj.sys.entity.LogInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.common.aop.Log;
import com.zpj.materials.entity.Supplier;
import com.zpj.materials.service.SupplierService;
@Service
public class SupplierServiceImpl implements SupplierService {

	@Autowired
	private BaseDao<Supplier> supplierDao;
	@Autowired
	private BaseDao<LogInfo> logDao;

	private String tablename="jl_material_supplier_info";

	@Override
	public MyPage findPageData(String username, Integer page, Integer limit) {
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("name-like", username);
		Map px=new HashMap();
	    px.put("createtime", "desc");
		return supplierDao.findPageDateSqlT(tablename, param,px , page, limit, Supplier.class);
	}

	@Log(type="保存",remark="保存供货商信息")
	public void saveInfo(Supplier info) {
		if(info.getId()!=0){
			Supplier user=this.findById(info.getId());
			if(null!=user){
				supplierDao.merge(info, String.valueOf(info.getId()));
			}else{
				supplierDao.add(info);
			}
		}else{
			supplierDao.add(info);
		}
		LogInfo loginfo=new LogInfo();
		loginfo.setId(UUID.randomUUID().toString());
		loginfo.setUsername("朱培军");
		loginfo.setCreatetime(new Date());
		loginfo.setType("保存供货商记录");
		loginfo.setDescription(info.toString());
		logDao.add(loginfo);
	}

	@Log(type="删除",remark="删除供货商信息")
	public void delete(String deleteID) {
		String[] ids=deleteID.split(",");
		StringBuffer sb=new StringBuffer(500);
		for (int m=0;m<ids.length;m++) {
			if(m>0){
				sb.append(",");
			}
			sb.append("'"+ids[m]+"'");
		}
		List list=supplierDao.findBySqlT(" select *  from "+tablename+" where id in ("+sb+")", Supplier.class);
		Supplier info=null;
		if(null!=list&&list.size()>0) {
			for (int m = 0; m < list.size(); m++) {
				info = (Supplier) list.get(m);
				LogInfo loginfo = new LogInfo();
				loginfo.setId(UUID.randomUUID().toString());
				loginfo.setUsername("朱培军");
				loginfo.setCreatetime(new Date());
				loginfo.setType("删除商品记录");
				loginfo.setDescription(info.toString());
				logDao.add(loginfo);
			}
		}


		supplierDao.executeSql(" delete from "+tablename+" where id in ("+sb+")");
	}

	public Supplier findById(int id) {
		return supplierDao.get(id,Supplier.class);
	}	
	
	
	public List findSupplier(){
		return supplierDao.findMapObjBySqlNoPage("select id,name from "+tablename);
	}
	
}

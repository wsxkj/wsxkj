package com.zpj.materials.service.impl;

import com.zpj.common.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zpj.materials.entity.IdCodeInfo;
import com.zpj.materials.service.IdCodeService;

import java.util.List;

@Service
public class IdCodeSerivceImpl implements IdCodeService{
	@Autowired
	private BaseDao<IdCodeInfo> iciDao;

	private String tableName="jl_material_idcode_info";

	@Override
	public void saveInfo(IdCodeInfo ici) {
		iciDao.executeSql("delete from "+tableName +" where phone='"+ici.getPhone()+"'");
		iciDao.add(ici);
	}

	public IdCodeInfo findInfoByPhone(String phone){
		List<IdCodeInfo> list=iciDao.findBySqlT("select * from "+tableName+" where phone='"+phone+"'",IdCodeInfo.class);
		if(null!=list&&list.size()>0){
			return list.get(0);
		}
		return null;
	}

}

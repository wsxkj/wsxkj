package com.zpj.materials.service.impl;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.common.aop.Log;
import com.zpj.materials.entity.Store;
import com.zpj.materials.service.StoreService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private BaseDao<Store> storeBaseDao;
    
    private String tablename="jl_material_store_info";
    
    
    
    public MyPage findPageData(Map canshu, Integer page, Integer limit){
    	Map param=new HashMap();
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            param.put("userId-eq", canshu.get("userId"));
        }
        if(null!=canshu.get("goodsId")&&!"".equalsIgnoreCase((String)canshu.get("goodsId"))){
            param.put("goodsId-eq", canshu.get("goodsId"));
        }
        Map px=new HashMap();
        px.put("updateTime", "desc");
        return storeBaseDao.findPageDateSqlT(tablename,"", param,px , page, limit, Store.class);
    }
    
    
    @Log(type="保存",remark="保存库存信息")
    public void saveInfo(Store store){
        storeBaseDao.add(store);
    }
    
    public Store findById(String id){
    	return storeBaseDao.get(id,Store.class);
    }
    
    public int  findStoreInCount(Map canshu){
        Map param=new HashMap();
        StringBuilder sql=new StringBuilder(100);
        sql.append("select sum(innum) as sn from "+tablename +" where 1=1 ");
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            sql.append(" and userId='"+canshu.get("userId")+"'") ;
        }
        if(null!=canshu.get("startTime")&&!"".equalsIgnoreCase((String)canshu.get("startTime"))){
            sql.append(" and updateTime>='"+canshu.get("startTime")+"'") ;
        }
        if(null!=canshu.get("endTime")&&!"".equalsIgnoreCase((String)canshu.get("endTime"))){
            sql.append(" and updateTime<='"+canshu.get("endTime")+"'") ;
        }
        List list=storeBaseDao.findMapObjBySqlNoPage(sql.toString());
        return 0;
    }
}

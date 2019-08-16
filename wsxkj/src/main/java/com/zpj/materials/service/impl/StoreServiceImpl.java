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

    public Map findStatisticsData(Map canshu){
        StringBuilder sql=new StringBuilder(100);
        sql.append("select sum(inNum) as totalInNum ,sum(inPrice) as totalInPrice from "+tablename +" s left join jl_material_goods_info g on s.goodsId=g.id   where 1=1 ");
        if(null!=canshu.get("mdate")&&!"".equalsIgnoreCase((String)canshu.get("mdate"))){
            sql.append(" and s.inDate like '"+canshu.get("mdate")+"%' " );
        }
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            sql.append(" and s.userId = '"+canshu.get("userId")+"' ");
        }
        List list=storeBaseDao.findMapObjBySqlNoPage(sql.toString());
        if(null!=list&&list.size()>0){
           return  (Map)list.get(0);
        }
        Map retMap=new HashMap();
        retMap.put("totalInNum","0");
        retMap.put("totalInPrice","0");
        return retMap;
    }
    public List findMultiData(Map canshu, Integer page, Integer limit){

        StringBuilder sql=new StringBuilder(100);
        sql.append("select g.picture,g.name,s.inDate ,s.inNum ,s.inPrice from "+tablename +" s left join jl_material_goods_info g on s.goodsId=g.id   where 1=1 ");
        if(null!=canshu.get("mdate")&&!"".equalsIgnoreCase((String)canshu.get("mdate"))){
            sql.append(" and s.inDate like '"+canshu.get("mdate")+"%' " );
        }
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            sql.append(" and s.userId = '"+canshu.get("userId")+"' ");
        }
        sql.append(" order by s.updateTime desc");
        List list=storeBaseDao.findMapObjBySql(sql.toString(),null,page,limit);
        return list;
    }
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

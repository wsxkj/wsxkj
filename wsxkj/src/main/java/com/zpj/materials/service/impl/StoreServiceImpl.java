package com.zpj.materials.service.impl;

import com.zpj.common.BaseDao;
import com.zpj.common.aop.Log;
import com.zpj.materials.entity.Store;
import com.zpj.materials.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private BaseDao<Store> storeBaseDao;
    
    
    @Log(type="保存",remark="保存库存信息")
    public void saveInfo(Store store){
        storeBaseDao.add(store);
    }
    
    public Store findById(String id){
    	return storeBaseDao.get(id,Store.class);
    }
}

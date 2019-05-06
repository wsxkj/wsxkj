package com.zpj.materials.service.impl;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.common.aop.Log;
import com.zpj.materials.entity.Customer;
import com.zpj.materials.entity.Goods;
import com.zpj.materials.service.CustomerService;
import com.zpj.sys.entity.LogInfo;
import com.zpj.sys.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * @ClassName: CustomerServiceImpl
 * @Description: TODO(客户)
 * @author zpj
 * @date 2019/5/5 11:32
*/
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private BaseDao<Customer> customerBaseDao;
    private String tablename="jl_material_customer_info";
    @Autowired
    private BaseDao<LogInfo> logDao;

    @Override
    public MyPage findPageData(Map canshu, Integer page, Integer limit) {
        Map param=new HashMap();
        if(null!=canshu.get("name")&&!"".equalsIgnoreCase((String)canshu.get("name"))){
            param.put("nickname-like", canshu.get("name"));
        }
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            param.put("userId-eq", canshu.get("userId"));
        }
        Map px=new HashMap();
        px.put("updateTime", "desc");
        return customerBaseDao.findPageDateSqlT(tablename, param,px , page, limit, Customer.class);
    }
    @Log(type="保存",remark="保存客户信息")
    public void saveInfo(Customer info) {
        Customer temp=this.findById(info.getId());
        if(null!=temp){
            customerBaseDao.merge(info, String.valueOf(info.getId()));
        }else{
            customerBaseDao.add(info);
        }
        LogInfo loginfo=new LogInfo();
        loginfo.setId(UUID.randomUUID().toString());
        loginfo.setUsername(info.getUserId());
        loginfo.setCreatetime(new Date());
        loginfo.setType("保存客户信息记录");
        loginfo.setDescription(info.toString());
        logDao.add(loginfo);
    }
    public Customer findById(String id) {
        return customerBaseDao.get(id,Customer.class);
    }
    
    public void delInfo(String id,User user){
    	Customer temp=this.findById(id);
    	if(null!=temp){
    		customerBaseDao.delete(temp);
    	}
    	LogInfo loginfo=new LogInfo();
        loginfo.setId(UUID.randomUUID().toString());
        loginfo.setUsername(user.getId());
        loginfo.setCreatetime(new Date());
        loginfo.setType("保存客户信息记录");
        loginfo.setDescription(temp.toString());
        logDao.add(loginfo);
    }
}

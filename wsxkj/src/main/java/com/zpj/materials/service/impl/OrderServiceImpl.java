package com.zpj.materials.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.materials.entity.OrderInfo;
import com.zpj.materials.service.OrderService;
import com.zpj.sys.entity.LogInfo;
@Service
public class OrderServiceImpl implements OrderService{
	@Autowired
	private BaseDao<OrderInfo> orderDao;
	private String tablename="jl_material_order_info";
    @Autowired
    private BaseDao<LogInfo> logDao;
	
    public MyPage findPageData(Map canshu, Integer page, Integer limit) {
        Map param=new HashMap();
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            param.put("userId-eq", canshu.get("userId"));
        }
        Map px=new HashMap();
        px.put("updateTime", "desc");
        return orderDao.findPageDateSqlT(tablename, param,px , page, limit, OrderInfo.class);
    }
    
    public void saveInfo(OrderInfo info){
    	orderDao.add(info);
    }

    public OrderInfo findById(String id){
        return orderDao.get(id,OrderInfo.class);
    }

    public void deleteInfo(String id){
        OrderInfo oi=orderDao.get(id,OrderInfo.class);
        if(null!=oi)
            orderDao.delete(oi);
    }
}

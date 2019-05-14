package com.zpj.materials.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zpj.common.BaseDao;
import com.zpj.common.MyPage;
import com.zpj.materials.entity.OrderGoodsInfo;
import com.zpj.materials.service.OrderGoodsService;
import com.zpj.sys.entity.LogInfo;
@Service
public class OrderGoodsServiceImpl implements OrderGoodsService {
	@Autowired
	private BaseDao<OrderGoodsInfo> orderGoodsDao;
	private String tablename="jl_material_order_goods_info";
    @Autowired
    private BaseDao<LogInfo> logDao;
	@Override
	public MyPage findPageData(Map canshu, Integer page, Integer limit) {
		Map param=new HashMap();
        if(null!=canshu.get("orderId")&&!"".equalsIgnoreCase((String)canshu.get("orderId"))){
            param.put("orderId-eq", canshu.get("orderId"));
        }
        if(null!=canshu.get("userId")&&!"".equalsIgnoreCase((String)canshu.get("userId"))){
            param.put("userId-eq", canshu.get("userId"));
        }
        Map px=new HashMap();
        px.put("updateTime", "desc");
        return orderGoodsDao.findPageDateSqlT(tablename, param,px , page, limit, OrderGoodsInfo.class);
	}
    public void saveInfo(OrderGoodsInfo orderGoodsInfo){
        orderGoodsDao.add(orderGoodsInfo);
    }
    public void deleteInfoByOrderGoodsId(String orderId){
        orderGoodsDao.executeSql("delete from "+tablename+" where orderId='"+orderId+"'");
    }
    public void deleteInfo(String id){
	    OrderGoodsInfo ogi=orderGoodsDao.get(id,OrderGoodsInfo.class);
	    if(null!=ogi){
            orderGoodsDao.delete(ogi);
        }
    }
}

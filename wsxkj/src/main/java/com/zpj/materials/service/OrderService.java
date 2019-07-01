package com.zpj.materials.service;

import java.util.List;
import java.util.Map;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.OrderInfo;
import com.zpj.sys.entity.User;

public interface OrderService {
	MyPage findPageData(Map param, Integer page, Integer limit);
	public MyPage findPageDataMuti(Map canshu, Integer page, Integer limit);
	public void saveInfo(OrderInfo info);

	public OrderInfo findById(String id);

	public void deleteInfo(String id);
	
	public void saveOrderMultiInfo(String id,String customerid,String state,String trackingNo,String orderGoods,String postage,User user);

}

package com.zpj.materials.service;

import java.util.Map;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.OrderInfo;

public interface OrderService {
	MyPage findPageData(Map param, Integer page, Integer limit);
	
	public void saveInfo(OrderInfo info);

	public OrderInfo findById(String id);

	public void deleteInfo(String id);

}

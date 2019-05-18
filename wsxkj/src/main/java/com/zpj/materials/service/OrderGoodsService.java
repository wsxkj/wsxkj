package com.zpj.materials.service;

import java.util.List;
import java.util.Map;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.OrderGoodsInfo;

public interface OrderGoodsService {
	MyPage findPageData(Map param, Integer page, Integer limit);

	public void saveInfo(OrderGoodsInfo orderGoodsInfo);
	/*
	 * @MethodName: deleteOrderGoodsInfoByOrderId
	 * @Description: TODO(根据订单id删除订单商品信息)
	 * @params [orderId]
	 * @return void
	 * @author zpj
	 * @date 2019/5/18 14:17
	*/
	public void deleteOrderGoodsInfoByOrderId(String orderId);


	public void deleteInfo(String id);
}

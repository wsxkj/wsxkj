package com.zpj.materials.service;

import java.util.Map;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.OrderGoodsInfo;

public interface OrderGoodsService {
	MyPage findPageData(Map param, Integer page, Integer limit);

	public void saveInfo(OrderGoodsInfo orderGoodsInfo);
	public void deleteInfoByOrderGoodsId(String orderId);
	public void deleteInfo(String id);
}

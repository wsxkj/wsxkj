package com.zpj.materials.service;

import java.util.List;
import java.util.Map;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.OrderGoodsInfo;
import com.zpj.sys.entity.User;

public interface OrderGoodsService {
	
	MyPage findPageData(Map param, Integer page, Integer limit);
	/**
	 * 多表关联查
	 * @Title findPageDataMuti
	 * @param canshu
	 * @param page
	 * @param limit
	 * @return
	 * @author zpj
	 * @time 2019年6月4日 上午8:39:13
	 */
	public MyPage findPageDataMuti(Map canshu, Integer page, Integer limit);
	
	/**
	 * 查询order list 根据goodsId
	 * @Title findOrderListByGoodsId
	 * @param param
	 * @param page
	 * @param limit
	 * @return
	 * @author zpj
	 * @time 2019年5月28日 上午11:25:15
	 */
	List findOrderListByGoodsId(Map param, Integer page, Integer limit);
	
	/*
	 * @MethodName: findOrderGoodsOutCount
	 * @Description: TODO(这查找出货量数据和)
	 * @params [param]
	 * @return int
	 * @author zpj
	 * @date 2019/5/21 16:15
	*/
	int  findOrderGoodsOutCount(Map param);

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

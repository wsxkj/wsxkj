package com.zpj.materials.service;

import java.util.List;
import java.util.Map;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.OrderInfo;
import com.zpj.sys.entity.User;

public interface OrderService {
	MyPage findPageData(Map param, Integer page, Integer limit);
	public MyPage findPageDataMuti(Map canshu, Integer page, Integer limit);

	
	/**
	 * 某个月每天的销售额，净利润，进货件数，出售件数
	 * @Title findPageDataMutiGroupByDate
	 * @param canshu
	 * @param page
	 * @param limit
	 * @return
	 * @author zpj
	 * @time 2019年8月21日 下午2:53:23
	 */
	public List findPageDataMutiGroupByMonthDay(Map canshu);
	
	
	/**
	 * 查询某月销售额和净利润
	 * @Title findPageDataMutiGroupByMonth
	 * @param canshu
	 * @return
	 * @author zpj
	 * @time 2019年8月21日 下午2:05:50
	 */
	public Map findPageDataMutiGroupByMonth(Map canshu);
	
	
	/**
	 * 获取总销售额、总净利润、总出售件数，总进货件数
	 * @Title findMutiSumDataAll
	 * @param canshu
	 * @return
	 * @author zpj
	 * @time 2019年8月21日 下午3:48:33
	 */
	public Map findMutiSumDataAll(Map canshu);
	
	public void saveInfo(OrderInfo info);

	public OrderInfo findById(String id);

	public void deleteInfo(String id);
	
	public void saveOrderMultiInfo(String id,String customerid,String state,String trackingNo,String orderGoods,String postage,User user);

}

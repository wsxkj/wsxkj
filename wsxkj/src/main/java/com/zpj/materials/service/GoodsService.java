package com.zpj.materials.service;

import java.util.List;
import java.util.Map;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.Goods;

public interface GoodsService {
	
	MyPage findPageData(Map param, Integer page, Integer limit);
	
	
	/**
	 * 查询商品返回特定的字段信息多表关联查询
	 * @Title findMultiGoods
	 * @param param
	 * @param page
	 * @param limit
	 * @return
	 * @author zpj
	 * @time 2019年5月27日 下午3:14:46
	 */
//	List findMultiGoods(Map param, Integer page, Integer limit);
	
	
	
	
	void saveInfo(Goods info);

	void deleteInfo(String deleteID);
	
	public Goods findById(String id);
	
	
	/**
	 * v1_1_0版本查询详细信息，去掉不必要的字段
	 * @Title findById_v1_1_0
	 * @param id
	 * @return
	 * @author zpj
	 * @time 2019年12月16日 上午11:26:01
	 */
	public Goods findById_v1_1_0(String id);
	
	/**
	 * v1_1_0版本增加是否发布字段查询，同时返回售价字段和是否发布字段
	 * @Title findMultiGoods_v1_1_0
	 * @param param
	 * @param page
	 * @param limit
	 * @return
	 * @author zpj
	 * @time 2019年12月16日 上午10:57:16
	 */
	List findMultiGoods_v1_1_0(Map param, Integer page, Integer limit);
	
	
	/**
	 * @Description (根据条形码查询商品信息)
	 * @title findByQrcode
	 * @param qrcode
	 * @return Goods
	 * @author zpj
	 * @Date 2020年2月5日 下午4:03:35
	 */
	public Goods findByQrcode(String qrcode,String userId);
}

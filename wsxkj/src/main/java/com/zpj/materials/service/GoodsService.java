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
	List findMultiGoods(Map param, Integer page, Integer limit);
	
	void saveInfo(Goods info);

	void deleteInfo(String deleteID);
	
	public Goods findById(String id);
}

package com.zpj.materials.service;

import java.util.Map;

import com.zpj.common.MyPage;
import com.zpj.materials.entity.Goods;

public interface GoodsService {
	
	MyPage findPageData(Map param, Integer page, Integer limit);
	
	void saveInfo(Goods info);

	void delete(String deleteID);
	
	public Goods findById(String id);
}

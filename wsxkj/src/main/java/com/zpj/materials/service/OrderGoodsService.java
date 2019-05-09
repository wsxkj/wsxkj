package com.zpj.materials.service;

import java.util.Map;

import com.zpj.common.MyPage;

public interface OrderGoodsService {
	MyPage findPageData(Map param, Integer page, Integer limit);

}

package com.zpj.materials.service;

import com.zpj.common.MyPage;

import java.util.Map;

public interface GoodsBrandService {

    public MyPage findPageData(Map canshu, Integer page, Integer limit);
}